package com.jagex.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jagex.chunk.Chunk;
import com.rspsi.misc.Vector2;

public class MultiMapEncoder {

	/**
	 * Encodes only loaded chunks and writes the number of records actually
	 * emitted. The old implementation wrote chunks.size() before skipping
	 * unloaded chunks, which made the resulting stream truncated/corrupt.
	 */
	public static byte[] encode(List<Chunk> chunks) {
		List<Chunk> loadedChunks = new ArrayList<>();
		for (Chunk chunk : chunks) {
			if (chunk != null && chunk.hasLoaded()) {
				loadedChunks.add(chunk);
			}
		}
		return encodeChunks(loadedChunks, true);
	}

	/**
	 * Encodes the cached source bytes without requiring the chunk to be loaded.
	 */
	public static byte[] encodeShallow(List<Chunk> chunks) {
		List<Chunk> availableChunks = new ArrayList<>();
		for (Chunk chunk : chunks) {
			if (chunk != null && chunk.objectMapData != null && chunk.tileMapData != null) {
				availableChunks.add(chunk);
			}
		}
		return encodeChunks(availableChunks, false);
	}

	private static byte[] encodeChunks(List<Chunk> chunks, boolean encodeCurrentState) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(bytes);
			output.writeInt(chunks.size());
			for (Chunk chunk : chunks) {
				byte[] objectMap = encodeCurrentState
						? chunk.scenegraph.saveObjects(chunk)
						: chunk.objectMapData;
				byte[] tileMap = encodeCurrentState
						? chunk.mapRegion.save_terrain_block(chunk)
						: chunk.tileMapData;
				if (objectMap == null || tileMap == null) {
					throw new IllegalStateException("Chunk " + chunk.regionHash + " has no encoded map data");
				}
				output.writeInt(chunk.objectMapId);
				output.writeInt(chunk.tileMapId);
				output.writeInt(chunk.offsetX / 64);
				output.writeInt(chunk.offsetY / 64);
				output.writeInt(objectMap.length);
				output.write(objectMap);
				output.writeInt(tileMap.length);
				output.write(tileMap);
			}
			output.flush();
			return bytes.toByteArray();
		} catch (IOException ex) {
			// ByteArrayOutputStream does not throw during normal writes.
			throw new IllegalStateException("Unable to encode map chunks", ex);
		}
	}

	public static Vector2 getSize(byte[] encoded) {
		ByteBuffer buffer = ByteBuffer.wrap(encoded);
		int size = readCount(buffer);
		int maximumX = 0;
		int maximumY = 0;
		for (int i = 0; i < size; i++) {
			buffer.getInt();
			buffer.getInt();
			int positionX = buffer.getInt();
			int positionY = buffer.getInt();
			maximumX = Math.max(maximumX, positionX);
			maximumY = Math.max(maximumY, positionY);
			skipBytes(buffer);
			skipBytes(buffer);
		}
		return new Vector2(maximumX, maximumY);
	}

	public static List<Chunk> decode(byte[] encoded) {
		List<Chunk> chunks = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(encoded);
		int size = readCount(buffer);
		for (int i = 0; i < size; i++) {
			int objectMapId = buffer.getInt();
			int tileMapId = buffer.getInt();
			int positionX = buffer.getInt();
			int positionY = buffer.getInt();
			byte[] objectData = readBytes(buffer);
			byte[] landscapeData = readBytes(buffer);
			int chunkX = positionX * 64;
			int chunkY = positionY * 64;
			int hash = (positionX << 8) | positionY;
			Chunk chunk = new Chunk(hash);
			chunk.offsetX = chunkX;
			chunk.offsetY = chunkY;
			chunk.objectMapData = objectData;
			chunk.tileMapData = landscapeData;
			chunk.objectMapId = objectMapId;
			chunk.tileMapId = tileMapId;
			chunks.add(chunk);
		}
		if (buffer.hasRemaining()) {
			throw new IllegalArgumentException("Unexpected trailing bytes in map pack: " + buffer.remaining());
		}
		return chunks;
	}

	private static int readCount(ByteBuffer buffer) {
		if (buffer.remaining() < 4) {
			throw new IllegalArgumentException("Invalid map pack: missing chunk count");
		}
		int count = buffer.getInt();
		if (count < 0 || count > buffer.remaining() / 24) {
			throw new IllegalArgumentException("Invalid map pack chunk count: " + count);
		}
		return count;
	}

	private static byte[] readBytes(ByteBuffer buffer) {
		if (buffer.remaining() < 4) {
			throw new IllegalArgumentException("Invalid map pack: missing data length");
		}
		int length = buffer.getInt();
		if (length < 0 || length > buffer.remaining()) {
			throw new IllegalArgumentException("Invalid map pack data length: " + length);
		}
		byte[] data = new byte[length];
		buffer.get(data);
		return data;
	}

	private static void skipBytes(ByteBuffer buffer) {
		if (buffer.remaining() < 4) {
			throw new IllegalArgumentException("Invalid map pack: missing data length");
		}
		int length = buffer.getInt();
		if (length < 0 || length > buffer.remaining()) {
			throw new IllegalArgumentException("Invalid map pack data length: " + length);
		}
		buffer.position(buffer.position() + length);
	}
}
