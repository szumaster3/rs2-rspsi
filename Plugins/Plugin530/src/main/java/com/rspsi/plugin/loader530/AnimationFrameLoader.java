package com.rspsi.plugin.loader530;

import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;
import com.jagex.cache.anim.Frame;
import com.jagex.cache.anim.FrameBase;
import com.jagex.cache.loader.anim.FrameBaseLoader;
import com.jagex.cache.loader.anim.FrameLoader;
import com.jagex.io.Buffer;
import org.major.cache.anim.FrameConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnimationFrameLoader extends FrameLoader {
	private final Map<Integer, Frame> frames = new HashMap<>();
	private final Map<Integer, FrameBase> bases = new HashMap<>();
	private Index frameIndex;
	private Index baseIndex;

	public void init(Index frameIndex, Index baseIndex) {
		this.frameIndex = frameIndex;
		this.baseIndex = baseIndex;
		frames.clear();
		bases.clear();
	}

	@Override
	protected Frame forId(int id) {
		if (id == -1) return null;
		return frames.computeIfAbsent(id, this::readFrame);
	}

	private Frame readFrame(int id) {
		Archive group = frameIndex.archive(id >>> 16);
		File file = group == null ? null : group.file(id & 0xFFFF);
		if (file == null || file.getData() == null) return null;

		byte[] data = file.getData();
		Buffer flags = new Buffer(data);
		int baseId = flags.readUShort();
		FrameBase base = bases.computeIfAbsent(baseId, this::readBase);
		if (base == null) return null;
		int count = flags.readUByte();
		Buffer values = new Buffer(data);
		values.setPosition(3 + count);

		int[] indices = new int[count];
		int[] x = new int[count];
		int[] y = new int[count];
		int[] z = new int[count];
		int length = 0;
		int lastOrigin = -1;
		int appliedOrigin = -1;
		Frame frame = new Frame();
		frame.setBase(base);

		for (int index = 0; index < count; index++) {
			int type = base.getTransformationType(index);
			if (type == FrameConstants.CENTROID_TRANSFORMATION) lastOrigin = index;
			int attributes = flags.readUByte();
			if (attributes == 0) continue;

			if (type >= 1 && type <= 3 && lastOrigin > appliedOrigin) {
				indices[length++] = lastOrigin;
				appliedOrigin = lastOrigin;
			}
			if (type == FrameConstants.CENTROID_TRANSFORMATION) appliedOrigin = index;

			int initial = type == FrameConstants.SCALE_TRANSFORMATION ? 128 : 0;
			indices[length] = index;
			x[length] = (attributes & 1) == 0 ? initial : values.readSmart();
			y[length] = (attributes & 2) == 0 ? initial : values.readSmart();
			z[length] = (attributes & 4) == 0 ? initial : values.readSmart();
			if (type == FrameConstants.ALPHA_TRANSFORMATION) frame.setOpaque(false);
			length++;
		}

		frame.setTransformationCount(length);
		frame.setTransformationIndices(Arrays.copyOf(indices, length));
		frame.setTransformX(Arrays.copyOf(x, length));
		frame.setTransformY(Arrays.copyOf(y, length));
		frame.setTransformZ(Arrays.copyOf(z, length));
		return frame;
	}

	private FrameBase readBase(int id) {
		Archive group = baseIndex.archive(id);
		File file = group == null ? null : group.file(0);
		return file == null || file.getData() == null ? null
				: FrameBaseLoader.instance.decode(new Buffer(file.getData()));
	}

	@Override
	public void load(int file, byte[] data) {
		// Frames are loaded with their file IDs directly from the JS5 group.
	}
}
