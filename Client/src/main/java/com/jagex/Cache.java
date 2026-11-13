package com.jagex;

import com.displee.cache.CacheLibrary;
import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.ArchiveSector;
import com.displee.cache.index.archive.file.File;
import com.displee.compress.CompressionExtKt;
import com.displee.compress.CompressionType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;

import com.jagex.cache.graphics.Sprite;
import com.jagex.cache.loader.map.MapIndexLoader;
import com.jagex.cache.loader.map.MapType;
import com.google.common.io.ByteStreams;
import com.jagex.net.ResourceProvider;
import com.rspsi.cache.CacheFileType;
import com.rspsi.misc.FixedIntegerKeyMap;
import com.rspsi.misc.XTEAManager;
import org.displee.util.Miscellaneous;
import com.displee.util.OtherExtKt;

@Slf4j
public class Cache {

	@Setter
	private BiFunction<CacheFileType, Integer, Optional<byte[]>> fileRetrieverOverride;

	@Setter
	private BiFunction<Integer, Integer, Optional<byte[]>> mapRetrieverOverride;

	@Getter
	private final CacheLibrary cacheLibrary;

	private final Index modelArchive, mapArchive, configArchive, frameArchive, baseArchive,
			spriteIndex, textureIndex, spotAnimIndex, varbitIndex, locIndex;

	public ResourceProvider resourceProvider;

	private final FixedIntegerKeyMap<Sprite> spriteCache = new FixedIntegerKeyMap<>(100);

	public Cache(String path) throws IOException {
		log.info("Loading cache at {}", path);
		cacheLibrary = CacheLibrary.create(path, true, null);

		modelArchive = cacheLibrary.index(7);
		mapArchive = cacheLibrary.index(5);
		configArchive = cacheLibrary.index(2);
		frameArchive = cacheLibrary.index(0);
		baseArchive = cacheLibrary.index(1);
		spriteIndex = cacheLibrary.index(8);
		textureIndex = cacheLibrary.index(9);
		locIndex = cacheLibrary.index(16);
		spotAnimIndex = cacheLibrary.index(21);
		varbitIndex = cacheLibrary.index(22);

		log.info("Loaded cache in RS2 format!");

		resourceProvider = new ResourceProvider(this);
		Thread t = new Thread(resourceProvider);
		t.start();
	}

	public Sprite getSprite(int id) {
		if (id < 0) return null;
		if (spriteCache.contains(id)) return spriteCache.get(id);
		if (spriteIndex == null) return null;

		Archive archive = spriteIndex.archive(id);
		if (archive == null) return null;

		try {
			byte[] data = archive.file(0).getData();
			if (data == null) return null;
			Sprite sprite = Sprite.decode(ByteBuffer.wrap(data));
			if (sprite != null) spriteCache.put(id, sprite);
			return sprite;
		} catch (Exception ex) {
			log.debug("Failed to decode sprite {}", id, ex);
		}
		return null;
	}

	public Index readFile(CacheFileType type) {
		switch (type) {
			case CONFIG: return configArchive;
			case MODEL: return modelArchive;
			case ANIMATION: return frameArchive;
			case SKELETON: return baseArchive;
			case MAP: return mapArchive;
			case SPRITE: return spriteIndex;
			case TEXTURE: return textureIndex;
			case SPOT: return spotAnimIndex;
			case VARBIT: return varbitIndex;
			case LOC: return locIndex;
			default: return null;
		}
	}

	public synchronized byte[] readMap(int fileId, int regionId) {
		if (fileId < 0) return null;
		if (mapRetrieverOverride != null) {
			Optional<byte[]> data = mapRetrieverOverride.apply(fileId, regionId);
			if (data.isPresent()) return data.get();
		}

		boolean objects = MapIndexLoader.instance != null
				&& MapIndexLoader.lookup(regionId, MapType.OBJECT) == fileId;
		if (objects) {
			byte[] plain = readStoredMap(fileId, null);
			if (plain != null) return plain;
			int[] key = XTEAManager.lookupMap(regionId);
			return hasKey(key) ? readStoredMap(fileId, key) : null;
		}
		return readStoredMap(fileId, null);
	}

	private static boolean hasKey(int[] key) {
		return key != null && key.length == 4 && !Arrays.equals(key, XTEAManager.NULL_KEYS);
	}

	private byte[] readStoredMap(int id, int[] key) {
		try {
			ArchiveSector sector = mapArchive.readArchiveSector(id);
			if (sector == null || sector.getData().length < 5) return null;

			// The disk trailer is outside the JS5 container and the client's XTEA range.
			byte[] bytes = sector.getData();
			int compression = bytes[0] & 0xFF;
			int payloadLength = ByteBuffer.wrap(bytes, 1, 4).getInt();
			int headerLength = compression == 0 ? 5 : 9;
			if (compression > 2 || payloadLength < 0 || payloadLength > bytes.length - headerLength) {
				return null;
			}
			byte[] container = Arrays.copyOf(bytes, headerLength + payloadLength);
			if (key != null) {
				cryptMapContainer(container, container.length, key, false);
			}
			sector.setData(container);
			if (compression == 2) {
				int expectedLength = ByteBuffer.wrap(container, 5, 4).getInt();
				// Surface groups are much smaller than this, including terrain metadata.
				if (expectedLength <= 0 || expectedLength > 1024 * 1024) return null;
				try (GZIPInputStream stream = new GZIPInputStream(
						new ByteArrayInputStream(container, headerLength, payloadLength))) {
					byte[] data = ByteStreams.toByteArray(ByteStreams.limit(stream, expectedLength + 1L));
					return data.length == expectedLength ? data : null;
				}
			}
			byte[] data = CompressionExtKt.decompress(sector, null);
			return data.length == 0 ? null : data;
		} catch (IOException | RuntimeException ex) {
			return null;
		}
	}

	// The cache library's XTEA implementation uses an incompatible round-key mask.
	private static void cryptMapContainer(byte[] container, int end, int[] key, boolean encrypt) {
		ByteBuffer bytes = ByteBuffer.wrap(container);
		int delta = 0x9E3779B9;
		for (int offset = 5; offset + 8 <= end; offset += 8) {
			int left = bytes.getInt(offset);
			int right = bytes.getInt(offset + 4);
			int sum = encrypt ? 0 : delta * 32;
			for (int round = 0; round < 32; round++) {
				if (encrypt) {
					left += ((right << 4 ^ right >>> 5) + right) ^ (sum + key[sum & 3]);
					sum += delta;
					right += ((left << 4 ^ left >>> 5) + left) ^ (sum + key[sum >>> 11 & 3]);
				} else {
					right -= ((left << 4 ^ left >>> 5) + left) ^ (sum + key[sum >>> 11 & 3]);
					sum -= delta;
					left -= ((right << 4 ^ right >>> 5) + right) ^ (sum + key[sum & 3]);
				}
			}
			bytes.putInt(offset, left);
			bytes.putInt(offset + 4, right);
		}
	}

	public synchronized void saveMap(int region, byte[] terrain, byte[] objects) throws IOException {
		int x = region >>> 8;
		int y = region & 0xFF;
		int terrainId = mapArchive.archiveId("m" + x + "_" + y);
		int objectsId = mapArchive.archiveId("l" + x + "_" + y);
		if (terrainId < 0 || objectsId < 0 || readStoredMap(terrainId, null) == null) {
			throw new IOException("Region " + x + "," + y + " has no complete surface map in this cache.");
		}

		int[] key = null;
		if (readStoredMap(objectsId, null) == null) {
			key = XTEAManager.lookupMap(region);
			if (!hasKey(key) || readStoredMap(objectsId, key) == null) {
				throw new IOException("Load the matching XTEA keys for region " + region + " before saving.");
			}
		}

		Archive terrainArchive = mapArchive.archive(terrainId, true);
		Archive objectsArchive = mapArchive.archive(objectsId, true);
		byte[] oldTerrain = mapArchive.readArchiveSector(terrainId).getData();
		byte[] oldObjects = mapArchive.readArchiveSector(objectsId).getData();
		byte[] newTerrain = packMap(terrainArchive, terrain, null);
		byte[] newObjects = packMap(objectsArchive, objects, key);
		try {
			writeMapArchive(terrainArchive, newTerrain);
			writeMapArchive(objectsArchive, newObjects);
			if (!Arrays.equals(terrain, readStoredMap(terrainId, null))
					|| !Arrays.equals(objects, readStoredMap(objectsId, key))) {
				throw new IOException("The saved archives for region " + region + " failed disk verification.");
			}
			mapArchive.update();
		} catch (IOException | RuntimeException ex) {
			try {
				writeMapArchive(terrainArchive, oldTerrain);
				writeMapArchive(objectsArchive, oldObjects);
				mapArchive.update();
			} catch (IOException | RuntimeException restoreError) {
				ex.addSuppressed(restoreError);
			}
			throw ex;
		}
	}

	private static byte[] packMap(Archive archive, byte[] data, int[] key) {
		byte[] packed = CompressionExtKt.compress(data, CompressionType.GZIP, null,
				(archive.getRevision() + 1) & 0xFFFF);
		if (key != null) cryptMapContainer(packed, packed.length - 2, key, true);
		return packed;
	}

	private void writeMapArchive(Archive archive, byte[] packed) throws IOException {
		if (!mapArchive.writeArchiveSector(archive.getId(), packed)) {
			throw new IOException("Could not write map archive " + archive.getId());
		}
		int length = packed.length - 2;
		archive.setRevision(ByteBuffer.wrap(packed).getShort(length) & 0xFFFF);
		archive.setCrc(OtherExtKt.generateCrc(packed, 0, length));
		if (mapArchive.hasWhirlpool()) {
			archive.setWhirlpool(OtherExtKt.generateWhirlpool(packed, 0, length));
		}
		archive.restore();
		mapArchive.flag();
	}

	public byte[] readFile(CacheFileType type, int file) {
		if (fileRetrieverOverride != null) {
			Optional<byte[]> data = fileRetrieverOverride.apply(type, file);
			if (data.isPresent()) return data.get();
		}

		try {
			switch (type) {
				case CONFIG: return cacheLibrary.data(configArchive.getId(), file, 0);
				case MODEL: return cacheLibrary.data(modelArchive.getId(), file, 0);
				case ANIMATION: return cacheLibrary.data(frameArchive.getId(), file, 0);
				case SKELETON: return cacheLibrary.data(baseArchive.getId(), file, 0);
				case MAP: return cacheLibrary.data(mapArchive.getId(), file, 0);
				case SPOT: return cacheLibrary.data(spotAnimIndex.getId(), file >>> 8, file & 0xFF);
				case VARBIT: return cacheLibrary.data(varbitIndex.getId(), file >>> 10, file & 0x3FF);
				case LOC:
					int archive = Miscellaneous.getConfigArchive(file, 8);
					int subFile = Miscellaneous.getConfigFile(file, 8);
					return cacheLibrary.data(locIndex.getId(), archive, subFile);
				default: return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public File writeFile(CacheFileType type, String name, int file, byte[] data, int[] xteas) {
		try {
			switch (type) {
				case CONFIG: return cacheLibrary.put(configArchive.getId(), file, 0, data, xteas);
				case MODEL: return cacheLibrary.put(modelArchive.getId(), file, 0, data, xteas);
				case ANIMATION: return cacheLibrary.put(frameArchive.getId(), file, 0, data, xteas);
				case MAP: return cacheLibrary.put(mapArchive.getId(), file, 0, data, xteas);
				case SPOT: return cacheLibrary.put(spotAnimIndex.getId(), file >>> 8, file & 0xFF, data, xteas);
				case VARBIT: return cacheLibrary.put(varbitIndex.getId(), file >>> 10, file & 0x3FF, data, xteas);
				case LOC:
					int archive = Miscellaneous.getConfigArchive(file, 8);
					int subFile = Miscellaneous.getConfigFile(file, 8);
					return cacheLibrary.put(locIndex.getId(), archive, subFile, data, xteas);
				default: return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public final Archive createArchive(int file, String name) {
		return configArchive.archive(file);
	}

	public void close() throws IOException {
		cacheLibrary.close();
	}

	public ResourceProvider getProvider() {
		return resourceProvider;
	}
}
