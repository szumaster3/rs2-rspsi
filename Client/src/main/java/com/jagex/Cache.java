package com.jagex;

import com.displee.cache.CacheLibrary;
import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.BiFunction;

import com.jagex.cache.graphics.Sprite;
import com.jagex.net.ResourceProvider;
import com.rspsi.cache.CacheFileType;
import com.rspsi.misc.FixedIntegerKeyMap;
import com.rspsi.misc.XTEAManager;
import org.displee.util.Miscellaneous;

@Slf4j
public class Cache {

	@Setter
	private BiFunction<CacheFileType, Integer, Optional<byte[]>> fileRetrieverOverride;

	@Setter
	private BiFunction<Integer, Integer, Optional<byte[]>> mapRetrieverOverride;

	@Getter
	private final CacheLibrary cacheLibrary;

	private final Index modelArchive, mapArchive, configArchive, skeletonArchive, skinArchive,
			spriteIndex, textureIndex, spotAnimIndex, varbitIndex, locIndex;

	public ResourceProvider resourceProvider;

	private final FixedIntegerKeyMap<Sprite> spriteCache = new FixedIntegerKeyMap<>(100);

	public Cache(String path) throws IOException {
		log.info("Loading cache at {}", path);
		cacheLibrary = CacheLibrary.create(path, true, null);

		modelArchive = cacheLibrary.index(7);
		mapArchive = cacheLibrary.index(5);
		configArchive = cacheLibrary.index(2);
		skeletonArchive = cacheLibrary.index(0);
		skinArchive = cacheLibrary.index(1);
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
			case ANIMATION: return skinArchive;
			case SKELETON: return skeletonArchive;
			case MAP: return mapArchive;
			case SPRITE: return spriteIndex;
			case TEXTURE: return textureIndex;
			case SPOT: return spotAnimIndex;
			case VARBIT: return varbitIndex;
			case LOC: return locIndex;
			default: return null;
		}
	}

	public byte[] readMap(int fileId, int regionId) {
		if (mapRetrieverOverride != null) {
			Optional<byte[]> data = mapRetrieverOverride.apply(fileId, regionId);
			if (data.isPresent()) return data.get();
		}

		try {
			int[] xtea = XTEAManager.lookupMap(regionId); // int[]
			return cacheLibrary.data(mapArchive.getId(), fileId, 0, xtea);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
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
				case ANIMATION: return cacheLibrary.data(skinArchive.getId(), file, 0);
				case SKELETON: return cacheLibrary.data(skeletonArchive.getId(), file, 0);
				case MAP: return cacheLibrary.data(mapArchive.getId(), file, 0);
				case SPOT: return cacheLibrary.data(spotAnimIndex.getId(), file >>> 8, file & 0xFF);
				case VARBIT: return cacheLibrary.data(varbitIndex.getId(), file >>> 18, file & 0x3FFFF);
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
				case ANIMATION: return cacheLibrary.put(skinArchive.getId(), file, 0, data, xteas);
				case MAP: return cacheLibrary.put(mapArchive.getId(), file, 0, data, xteas);
				case SPOT: return cacheLibrary.put(spotAnimIndex.getId(), file >>> 8, file & 0xFF, data, xteas);
				case VARBIT: return cacheLibrary.put(varbitIndex.getId(), file >>> 18, file & 0x3FFFF, data, xteas);
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