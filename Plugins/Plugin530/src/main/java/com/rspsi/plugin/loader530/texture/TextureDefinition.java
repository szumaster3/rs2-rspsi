package com.rspsi.plugin.loader530.texture;

import com.jagex.cache.graphics.Sprite;
import com.jagex.draw.textures.SpriteTexture;
import com.jagex.draw.textures.Texture;
import com.jagex.io.Buffer;

public class TextureDefinition {
    private NewTexture texture;

    public TextureDefinition(Buffer buffer) {
        this.texture = new NewTexture(buffer);
        for (int i = 0; i < 6 && buffer.getPosition() < buffer.getPayload().length; i++) {
            buffer.readUByte();
        }
    }

    public Texture convertToTexture(boolean lowDetail) {
        if (!texture.spriteTextureExists()) return null;

        int[] groups = texture.getSpriteGroup();
        int[] frames = texture.getSpriteFrames();

        if (groups.length == 0) return null;

        int group = groups[0];
        int frame = frames.length > 0 ? frames[0] : 0;

        Sprite sprite = SpriteTextureOperation.loadSprite(group, frame);

        if (sprite == null) return null;

        return new SpriteTexture(sprite);
    }
}