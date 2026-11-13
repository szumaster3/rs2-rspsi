package com.rspsi.plugin.loader530.texture;

import com.displee.cache.index.Index;
import com.jagex.io.Buffer;

import java.util.ArrayList;
import java.util.List;

public class NewTexture {

	public static Index spriteIndex;

	private int[] spriteGroup;
	private int[] spriteFrames;

	public NewTexture(Buffer buffer) {

		List<Integer> groups = new ArrayList<>();
		List<Integer> frames = new ArrayList<>();

		byte[] data = buffer.getPayload();

		for (int i = 0; i < data.length - 3; i++) {

			int group = ((data[i] & 0xFF) << 8) | (data[i + 1] & 0xFF);
			int frame = ((data[i + 2] & 0xFF) << 8) | (data[i + 3] & 0xFF);

			if (group > 0 && group < 10000) {
				if (spriteIndex.archive(group) != null) {
					groups.add(group);
					frames.add(frame);
				}
			}
		}

		if (groups.isEmpty()) {
			spriteGroup = new int[0];
			spriteFrames = new int[0];
			return;
		}

		spriteGroup = groups.stream().mapToInt(i -> i).toArray();
		spriteFrames = frames.stream().mapToInt(i -> i).toArray();
	}

	public boolean spriteTextureExists() {
		return spriteGroup != null && spriteGroup.length > 0;
	}

	public int[] getSpriteGroup() {
		return spriteGroup;
	}

	public int[] getSpriteFrames() {
		return spriteFrames;
	}
}