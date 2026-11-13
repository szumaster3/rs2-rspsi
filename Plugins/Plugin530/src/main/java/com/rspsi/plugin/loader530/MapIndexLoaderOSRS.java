package com.rspsi.plugin.loader530;

import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;
import com.jagex.cache.loader.map.MapIndexLoader;
import com.jagex.cache.loader.map.MapType;
import com.jagex.io.Buffer;
import com.rspsi.misc.RegionData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapIndexLoaderOSRS extends MapIndexLoader {

	private int[] mapHashes;
	private int[] objects;
	private int[] landscapes;

	@Override
	public void init(Archive archive) {
		if (archive == null) {
			throw new IllegalArgumentException("Map index archive is missing");
		}
		File file = archive.file("map_index");
		if (file == null || file.getData() == null) {
			throw new IllegalArgumentException("The map_index file is missing from the config archive");
		}
		init(new Buffer(file.getData()));
	}

	@Override
	public void init(Buffer buffer) {
		int count = buffer.readUShort();
		if (buffer.getPayload() == null || buffer.getPosition() + count * 6 > buffer.getPayload().length) {
			throw new IllegalArgumentException("Invalid map_index: expected " + count + " region entries");
		}
		mapHashes = new int[count];
		landscapes = new int[count];
		objects = new int[count];
		for (int region = 0; region < count; region++) {
			mapHashes[region] = buffer.readUShort();
			landscapes[region] = readArchiveId(buffer);
			objects[region] = readArchiveId(buffer);
		}
	}

	private int readArchiveId(Buffer buffer) {
		int id = buffer.readUShort();
		return id == 0xFFFF ? -1 : id;
	}

	@Override
	public int getFileId(int hash, MapType type) {
		if (mapHashes == null || type == null) {
			return -1;
		}
		for (int i = 0; i < mapHashes.length; i++) {
			if (hash == mapHashes[i]) {
				return type == MapType.LANDSCAPE ? landscapes[i] : objects[i];
			}
		}
		return -1;
	}

	@Override
	public boolean landscapePresent(int id) {
		if (id < 0 || landscapes == null) {
			return false;
		}
		for (int landscape : landscapes) {
			if (id == landscape) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean objectPresent(int id) {
		if (id < 0 || objects == null) {
			return false;
		}
		for (int object : objects) {
			if (id == object) {
				return true;
			}
		}
		return false;
	}

	@Override
	public byte[] encode() {
		if (mapHashes == null) {
			return new byte[] { 0, 0 };
		}
		ByteBuffer buffer = ByteBuffer.allocate((mapHashes.length * 6) + 2);
		buffer.putShort((short) mapHashes.length);
		for (int index = 0; index < mapHashes.length; index++) {
			buffer.putShort((short) mapHashes[index]);
			buffer.putShort((short) landscapes[index]);
			buffer.putShort((short) objects[index]);
		}
		return buffer.array();
	}

	@Override
	public void set(int regionX, int regionY, int landscapeId, int objectsId) {
		if (regionX < 0 || regionX > 255 || regionY < 0 || regionY > 255) {
			throw new IllegalArgumentException("Region coordinates must be between 0 and 255");
		}
		if (mapHashes == null) {
			mapHashes = new int[0];
			landscapes = new int[0];
			objects = new int[0];
		}
		int hash = (regionX << 8) | regionY;
		for (int i = 0; i < mapHashes.length; i++) {
			if (hash == mapHashes[i]) {
				landscapes[i] = landscapeId;
				objects[i] = objectsId;
				return;
			}
		}
		int newSize = mapHashes.length + 1;
		mapHashes = Arrays.copyOf(mapHashes, newSize);
		landscapes = Arrays.copyOf(landscapes, newSize);
		objects = Arrays.copyOf(objects, newSize);
		mapHashes[newSize - 1] = hash;
		landscapes[newSize - 1] = landscapeId;
		objects[newSize - 1] = objectsId;
	}

	/**
	 * Rebuilds the legacy map_index table from the named region archives in
	 * cache index 5. Either half of a region pair may be absent; -1 is encoded
	 * as 0xFFFF, matching the client's unsigned-short sentinel.
	 */
	public void init(Index mapIndex) {
		if (mapIndex == null) {
			throw new IllegalArgumentException("Map index 5 is missing");
		}
		List<RegionData> regionData = new ArrayList<>();
		Map<Integer, Integer> archiveIds = new HashMap<>();
		for (Archive archive : mapIndex.archives()) {
			archiveIds.put(archive.getHashName(), archive.getId());
		}
		for (int region = 0; region <= 0xFFFF; region++) {
			int x = region >> 8;
			int y = region & 0xFF;
			int landscapeId = archiveIds.getOrDefault(mapIndex.toHash("m" + x + "_" + y), -1);
			int objectId = archiveIds.getOrDefault(mapIndex.toHash("l" + x + "_" + y), -1);
			if (landscapeId != -1 || objectId != -1) {
				regionData.add(new RegionData(region, landscapeId, objectId));
			}
		}
		mapHashes = new int[regionData.size()];
		landscapes = new int[regionData.size()];
		objects = new int[regionData.size()];
		for (int i = 0; i < regionData.size(); i++) {
			RegionData data = regionData.get(i);
			mapHashes[i] = data.getHash();
			landscapes[i] = data.getLoc();
			objects[i] = data.getObj();
		}
	}
}
