package com.rspsi.plugin.loader530;

import com.displee.cache.index.archive.Archive;
import com.jagex.cache.loader.textures.TextureLoader;
import com.jagex.draw.raster.GameRasterizer;
import com.jagex.draw.textures.Texture;
import com.jagex.io.Buffer;

import java.util.Arrays;

public class TextureLoaderOSRS extends TextureLoader {
	private Texture[] materials = new Texture[0];

	@Override
	public void init(Archive archive) {
		Buffer buffer = new Buffer(archive.file(0).getData());
		int count = buffer.readUShort();
		boolean[] present = new boolean[count];
		int presentCount = 0;
		for (int id = 0; id < count; id++) {
			present[id] = buffer.readUByte() == 1;
			if (present[id]) presentCount++;
		}
		// The four flags and four material properties are stored column by column.
		buffer.skip(presentCount * 8);
		materials = new Texture[count];
		for (int id = 0; id < count; id++) {
			if (present[id]) materials[id] = new MaterialColour(buffer.readUShort());
		}
	}

	@Override
	public Texture forId(int id) {
		if (id < 0 || id >= materials.length || materials[id] == null) return null;
		return materials[id];
	}

	@Override
	public int[] getPixels(int id) {
		return null;
	}

	@Override
	public boolean isTransparent(int id) {
		return false;
	}

	@Override
	public void setBrightness(double brightness) {
		// Material colours are read from the rasterizer's current HSL palette.
	}

	@Override
	public int count() {
		return materials.length;
	}

	public static final class MaterialColour extends Texture {
		private final int hsl;

		private MaterialColour(int hsl) {
			super(16, 16);
			this.hsl = hsl;
		}

		private void refresh() {
			int rgb = averageTextureColour();
			if (pixels[0] != rgb) Arrays.fill(pixels, rgb);
		}

		@Override
		public javafx.scene.image.WritableImage getAsFXImage() {
			refresh();
			return super.getAsFXImage();
		}

		@Override
		public int averageTextureColour() {
			return GameRasterizer.getInstance().colourPalette[hsl];
		}

		@Override
		public boolean supportsAlpha() {
			return false;
		}
	}
}
