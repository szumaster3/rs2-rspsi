package com.jagex.util;

import com.displee.cache.CacheLibrary;
import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;
import com.jagex.chunk.Chunk;
import com.jagex.map.SceneGraph;
import com.jagex.map.tile.SceneTile;
import com.jagex.map.object.DefaultWorldObject;
import com.jagex.map.object.GameObject;
import com.jagex.cache.def.ObjectDefinition;
import com.jagex.cache.loader.object.ObjectDefinitionLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class WorldMapCacheSynchronizer {
    public static final int WORLD_MAP_INDEX = 23;

    /*
     *   archive 0: RuneScape surface data
     *   archive 1: details (contains the "main" map definition)
     *   archive 2: RuneScape surface labels
     */
    private static final int REV530_SURFACE_ARCHIVE_ID = 0;
    private static final int REV530_DETAILS_ARCHIVE_ID = 1;
    private static final int REV530_SURFACE_LABELS_ARCHIVE_ID = 2;

    private WorldMapCacheSynchronizer() { }

    public static Result synchronize(CacheLibrary cache, Chunk chunk) throws IOException {
        if (cache == null || chunk == null || chunk.mapRegion == null || chunk.scenegraph == null) {
            throw new IllegalArgumentException("Cache and a fully loaded map chunk are required.");
        }
        if (chunk.offsetX < 0 || chunk.offsetY < 0
                || chunk.offsetX + 64 > chunk.mapRegion.underlays[0].length
                || chunk.offsetY + 64 > chunk.mapRegion.underlays[0][0].length) {
            throw new IOException("The edited chunk is outside the loaded map region bounds.");
        }
        if (!cache.exists(WORLD_MAP_INDEX)) {
            throw new IOException("Cache index 23 (World Map) is missing.");
        }

        int regionX = chunk.regionHash >>> 8 & 0xFF;
        int regionY = chunk.regionHash & 0xFF;
        Index index = cache.index(WORLD_MAP_INDEX);
        Archive details = index.archive("details");
        if (details == null) {
            details = index.archive(REV530_DETAILS_ARCHIVE_ID);
        }
        if (details == null) {
            throw new IOException("World Map does not contain the 'details' archive.");
        }

        MapDefinition definition = null;
        // Match MapList.getContainingSource: the 530 client selects the first
        // valid details entry whose display chunks contain this source coordinate.
        for (int fileId : details.fileIds()) {
            File file = details.file(fileId);
            if (file == null || file.getData() == null) continue;
            MapDefinition candidate = MapDefinition.decode(file.getData());
            if (candidate != null && candidate.valid && candidate.contains(regionX, regionY)) {
                definition = candidate;
                break;
            }
        }
        if (definition == null) {
            throw new IOException("No World Map definition covers region " + regionX + "," + regionY + ".");
        }
        Archive group = index.archive(definition.group);
        if (group == null && isRevision530SurfaceGroup(definition.group)) {
            // "RuneScape surface" is the map's display/group name in details.
            group = index.archive(REV530_SURFACE_ARCHIVE_ID);
        }
        if (group == null) {
            throw new IOException("World Map group '" + definition.group
                    + "' could not be resolved in index 23 (revision-530 surface archive ID "
                    + REV530_SURFACE_ARCHIVE_ID + " was also checked).");
        }
        Archive labelsArchive = index.archive(definition.group + "_labels");
        if (labelsArchive == null && isRevision530SurfaceGroup(definition.group)) {
            labelsArchive = index.archive(REV530_SURFACE_LABELS_ARCHIVE_ID);
        }

        int[] mapFunctions = encodeMapFunctions(chunk.scenegraph, chunk.offsetX, chunk.offsetY);
        byte[] oldLocs = groupFile(group, "loc");
        byte[] mergedLocs = mergeMapFunctions(oldLocs, regionX, regionY, mapFunctions);
        boolean locationDataUpdated = !Arrays.equals(oldLocs, mergedLocs);
        if (locationDataUpdated) {
            replaceNamedFile(group, "loc", mergedLocs);
        }

        return new Result(definition.group, regionX, regionY,
                locationDataUpdated, labelsArchive != null);
    }

    private static boolean isRevision530SurfaceGroup(String group) {
        return group != null && "runescape surface".equalsIgnoreCase(group.trim());
    }

    private static byte[] groupFile(Archive group, String name) throws IOException {
        File file = group.file(name);
        if (file == null || file.getData() == null) {
            throw new IOException("World Map group is missing required file '" + name + "'.");
        }
        return file.getData();
    }

    private static void replaceNamedFile(Archive group, String name, byte[] data) {
        File file = group.file(name);
        if (file == null) group.add(name, data);
        else group.add(file.getId(), data);
    }

    private static int[] encodeMapFunctions(SceneGraph scene, int offsetX, int offsetY) {
        int[] mapFunctions = new int[4096];
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 64; y++) {
                int worldX = offsetX + x;
                int worldY = offsetY + y;
                int mapFunction = 0;
                for (int plane = 0; plane < 4 && mapFunction == 0; plane++) {
                    SceneTile tile = scene.tiles[plane][worldX][worldY];
                    if (tile == null) continue;
                    for (DefaultWorldObject object : tile.getExistingObjects()) {
                        if (object instanceof GameObject
                                && (object.getX() != worldX || object.getY() != worldY)) continue;
                        ObjectDefinition definition = ObjectDefinitionLoader.lookup(object.getKey().getId());
                        if (definition != null && definition.getMinimapFunction() >= 0) {
                            // WorldMap.readLocs resolves this value as LocTypeList.get(value - 1).
                            mapFunction = object.getKey().getId() + 1;
                            break;
                        }
                    }
                }
                mapFunctions[x * 64 + y] = mapFunction;
            }
        }
        return mapFunctions;
    }

    private static byte[] mergeMapFunctions(byte[] source, int regionX, int regionY,
                                            int[] generatedMapFunctions) throws IOException {
        if (generatedMapFunctions.length != 4096) {
            throw new IOException("Invalid generated World Map function array.");
        }
        byte[] walls = new byte[4096];
        int[] mapScenes = new int[4096];
        int[] mapFunctions = new int[4096];
        ByteArrayOutputStream out = new ByteArrayOutputStream(source.length + 4096);
        int offset = 0;
        boolean foundRegion = false;
        boolean mapFunctionsChanged = false;

        while (offset < source.length) {
            int recordStart = offset;
            require(source, offset, 1);
            int recordFlags = source[offset++] & 0xFF;
            boolean partial = recordFlags == 1;
            int chunkX = 0;
            int chunkY = 0;
            if (partial) {
                require(source, offset, 2);
                chunkX = source[offset++] & 0xFF;
                chunkY = source[offset++] & 0xFF;
                if (chunkX > 7 || chunkY > 7) {
                    throw new IOException("Invalid World Map loc 8x8 chunk coordinates.");
                }
            } else if (recordFlags != 0) {
                throw new IOException("Unsupported World Map loc record flag " + recordFlags + ".");
            }
            require(source, offset, 2);
            int x = source[offset++] & 0xFF;
            int y = source[offset++] & 0xFF;
            int tileCount = partial ? 64 : 4096;
            boolean target = x == regionX && y == regionY;

            int partialIndex = 0;
            for (int tileX = 0; tileX < 64; tileX++) {
                for (int tileY = 0; tileY < 64; tileY++) {
                    if (partial && (tileX < chunkX * 8 || tileX >= chunkX * 8 + 8
                            || tileY < chunkY * 8 || tileY >= chunkY * 8 + 8)) {
                        continue;
                    }
                    require(source, offset, 1);
                    int tileFlags = source[offset++] & 0xFF;
                    int wall = 0;
                    int mapScene = 0;
                    int mapFunction = 0;
                    if ((tileFlags & 1) != 0) {
                        require(source, offset, 1);
                        wall = source[offset++] & 0xFF;
                    }
                    if ((tileFlags & 2) != 0) {
                        require(source, offset, 3);
                        mapScene = (source[offset] & 0xFF) << 16
                                | (source[offset + 1] & 0xFF) << 8
                                | (source[offset + 2] & 0xFF);
                        offset += 3;
                    }
                    if ((tileFlags & 4) != 0) {
                        require(source, offset, 3);
                        mapFunction = (source[offset] & 0xFF) << 16
                                | (source[offset + 1] & 0xFF) << 8
                                | (source[offset + 2] & 0xFF);
                        offset += 3;
                    }
                    if (target) {
                        int index = tileX * 64 + tileY;
                        walls[index] = (byte) wall;
                        mapScenes[index] = mapScene;
                        mapFunctions[index] = mapFunction;
                        if (generatedMapFunctions[index] != 0
                                && generatedMapFunctions[index] != mapFunction) {
                            mapFunctionsChanged = true;
                        }
                    }
                    partialIndex++;
                }
            }
            if (partialIndex != tileCount) {
                throw new IOException("Invalid World Map loc record tile count.");
            }
            if (target) {
                foundRegion = true;
            } else {
                out.write(source, recordStart, offset - recordStart);
            }
        }

        if (!foundRegion) {
            for (int generated : generatedMapFunctions) {
                if (generated != 0) {
                    mapFunctionsChanged = true;
                    break;
                }
            }
        }
        if (!mapFunctionsChanged) {
            return source;
        }

        boolean hasAnyData = false;
        ByteArrayOutputStream region = new ByteArrayOutputStream(12291);
        for (int index = 0; index < 4096; index++) {
            if (generatedMapFunctions[index] != 0) {
                mapFunctions[index] = generatedMapFunctions[index];
            }
            int flags = (walls[index] != 0 ? 1 : 0)
                    | (mapScenes[index] != 0 ? 2 : 0)
                    | (mapFunctions[index] != 0 ? 4 : 0);
            region.write(flags);
            if ((flags & 1) != 0) region.write(walls[index] & 0xFF);
            if ((flags & 2) != 0) writeMedium(region, mapScenes[index]);
            if ((flags & 4) != 0) writeMedium(region, mapFunctions[index]);
            hasAnyData |= flags != 0;
        }

        if (hasAnyData || foundRegion) {
            out.write(0); // full-region record
            out.write(regionX);
            out.write(regionY);
            region.writeTo(out);
        }
        return out.toByteArray();
    }

    private static void writeMedium(ByteArrayOutputStream out, int value) {
        out.write(value >>> 16 & 0xFF);
        out.write(value >>> 8 & 0xFF);
        out.write(value & 0xFF);
    }

    private static void require(byte[] data, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset + length > data.length) {
            throw new IOException("Truncated World Map archive data at offset " + offset + ".");
        }
    }

    private static final class MapDefinition {
        final String group;
        final int[][] chunks;
        final boolean valid;

        MapDefinition(String group, int[][] chunks, boolean valid) {
            this.group = group;
            this.chunks = chunks;
            this.valid = valid;
        }

        boolean contains(int regionX, int regionY) {
            int x = regionX * 64 + 32;
            int y = regionY * 64 + 32;
            for (int[] c : chunks) {
                // Map.create reads MapChunk fields in this order:
                // displayMinX, displayMaxX, displayMaxZ, displayMinZ.
                // MapList.getContainingSource(x, y) calls Map.method664(y, x),
                // which checks X against [displayMaxX, displayMinZ] and
                // Y against [displayMinX, displayMaxZ].
                if (x >= c[1] && x <= c[3] && y >= c[0] && y <= c[2]) return true;
            }
            return false;
        }

        static MapDefinition decode(byte[] data) throws IOException {
            Cursor in = new Cursor(data);
            in.readString(); // map display name
            String group = in.readString();
            in.readUnsignedShort(); // origin X
            in.readUnsignedShort(); // origin Z
            in.readInt(); // background colour
            boolean valid = in.readUnsignedByte() == 1;
            in.readUnsignedByte(); // default zoom
            int count = in.readUnsignedByte();
            if (count > 128) throw new IOException("Invalid World Map details chunk count: " + count);
            int[][] chunks = new int[count][4];
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < 4; j++) chunks[i][j] = in.readUnsignedShort();
            }
            if (group.isEmpty()) return null;
            return new MapDefinition(group, chunks, valid);
        }
    }

    private static final class Cursor {
        final byte[] data;
        int offset;
        Cursor(byte[] data) { this.data = data; }
        int readUnsignedByte() throws IOException {
            require(data, offset, 1);
            return data[offset++] & 0xFF;
        }
        int readUnsignedShort() throws IOException {
            return readUnsignedByte() << 8 | readUnsignedByte();
        }
        int readInt() throws IOException {
            return readUnsignedByte() << 24 | readUnsignedByte() << 16 | readUnsignedByte() << 8 | readUnsignedByte();
        }
        String readString() throws IOException {
            int start = offset;
            while (readUnsignedByte() != 0) { }
            return new String(data, start, offset - start - 1, StandardCharsets.ISO_8859_1);
        }
    }

    public static final class Result {
        public final String mapGroup;
        public final int regionX;
        public final int regionY;
        public final boolean locationDataUpdated;
        public final boolean labelsPreserved;
        Result(String mapGroup, int regionX, int regionY, boolean locationDataUpdated, boolean labelsPreserved) {
            this.mapGroup = mapGroup;
            this.regionX = regionX;
            this.regionY = regionY;
            this.locationDataUpdated = locationDataUpdated;
            this.labelsPreserved = labelsPreserved;
        }
    }
}
