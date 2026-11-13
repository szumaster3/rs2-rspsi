package com.rspsi.plugin.loader530;

import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;
import com.jagex.cache.config.VariableBits;
import com.jagex.cache.loader.config.VariableBitLoader;
import com.jagex.io.Buffer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class VarbitLoader extends VariableBitLoader {

    /**
     * Varbit mapping (ID -> definition).
     */
    private final Map<Integer, VariableBits> bitsMap = new HashMap<>();

    private int count;

    @Override
    public int count() {
        return count;
    }

    @Override
    public VariableBits forId(int id) {
        return bitsMap.get(id);
    }

    @Override
    public void init(Archive archive)
    {}

    /**
     * Main method for loading varbits.
     */
    public void decodeVarbits(Index index) {
        bitsMap.clear();

        for (int archiveId = 0; archiveId < index.archives().length; archiveId++) {
            Archive archive = index.archive(archiveId);
            if (archive == null) continue;

            for (File file : archive.getFiles().values()) {
                if (file == null || file.getData() == null) continue;

                int id = (archiveId << 10) | file.getId();

                VariableBits varbit = decodeVarbit(file.getData());
                if (varbit != null) {
                    bitsMap.put(id, varbit);
                }
            }
        }

        count = bitsMap.size();

        System.out.println("Loaded varbits: " + count);
    }

    /**
     * Decoding a single varbit.
     */
    private VariableBits decodeVarbit(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        VariableBits varbit = new VariableBits();

        while (true) {
            int opcode = buffer.get() & 0xFF;

            if (opcode == 0)
                break;

            if (opcode == 1) {
                int varpId = buffer.getShort() & 0xFFFF;
                int startBit = buffer.get() & 0xFF;
                int endBit = buffer.get() & 0xFF;

                varbit.setSetting(varpId);
                varbit.setLow(startBit);
                varbit.setHigh(endBit);
            } else {
                skipUnknown(buffer, opcode);
            }
        }

        return varbit;
    }

    /**
     * Handling unknown opcodes.
     */
    private void skipUnknown(ByteBuffer buffer, int opcode) {
        switch (opcode) {
            case 2:
                buffer.get();
                break;
            default:
                break;
        }
    }

    @Override
    public void init(Buffer arg0, Buffer arg1)
    {}
}