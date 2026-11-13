package com.rspsi.plugin.loader530.texture;

import com.displee.cache.index.Index;
import com.jagex.cache.graphics.Sprite;

import java.nio.ByteBuffer;

public class SpriteTextureOperation {

	public static Index spriteIndex;

	public static Sprite loadSprite(int group, int frame) {
		try {
			if (spriteIndex.archive(group) == null)
				return null;

			return Sprite.decode(ByteBuffer.wrap(spriteIndex.archive(group).file(frame).getData()));
		} catch (Exception e) {
			return null;
		}
	}
}