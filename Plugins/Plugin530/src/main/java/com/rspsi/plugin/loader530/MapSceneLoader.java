package com.rspsi.plugin.loader530;

import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;
import com.jagex.Client;
import com.jagex.cache.graphics.Sprite;
import com.jagex.io.Buffer;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

public class MapSceneLoader {

    private Sprite[] mapScenes;

    public void init(Client client, Archive archive, Index spriteIndex) {
        if (archive == null || spriteIndex == null) {
            System.out.println("MapSceneLoader: archive or spriteIndex is null!");
            return;
        }

        mapScenes = new Sprite[archive.getFiles().lastKey() + 1];

        for (File file : archive.files()) {
            if (file == null || file.getData() == null)
                continue;

            MapScene mapScene = decode(file.getId(), new Buffer(file.getData()));

            if (mapScene.spriteId == -1)
                continue;

            int archiveId = mapScene.spriteId;
            int fileId = 0;

            Archive spriteArchive = spriteIndex.archive(archiveId);
            if (spriteArchive == null) {
                System.out.println("Missing sprite archive: " + archiveId + " (spriteId=" + mapScene.spriteId + ")");
                continue;
            }

            File spriteFile = spriteArchive.file(fileId);
            if (spriteFile == null || spriteFile.getData() == null) {
                System.out.println("Missing sprite file: " + fileId + " in archive: " + archiveId);
                continue;
            }

            try {
                mapScenes[mapScene.id] = Sprite.decode(
                        ByteBuffer.wrap(spriteFile.getData())
                );
            } catch (Exception e) {
                System.out.println("Failed to decode spriteId=" + mapScene.spriteId +
                        " archive=" + archiveId + " file=" + fileId);
                e.printStackTrace();
            }
        }

        client.mapScenes = this.mapScenes;
    }

    private MapScene decode(int id, Buffer buffer) {
        MapScene area = new MapScene(id);

        while (true) {
            int opcode = buffer.readUByte();

            if (opcode == 0)
                break;

            if (opcode == 1) {
                area.spriteId = buffer.readUShort();
            } else if (opcode == 2) {
                area.colourSomething = buffer.readUTriByte();
            } else if (opcode == 3) {
                area.someBoolean = true;
            } else if (opcode == 4) {
                area.spriteId = -1;
            } else {
            }
        }

        return area;
    }

    @RequiredArgsConstructor
    public static class MapScene {
        private final int id;
        int spriteId = -1;
        int colourSomething;
        boolean someBoolean;
    }
}
