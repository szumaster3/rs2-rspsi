package com.rspsi.plugin.loader530;

import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;
import com.google.common.collect.Maps;
import com.jagex.cache.def.Floor;
import com.jagex.cache.loader.floor.FloorDefinitionLoader;
import com.jagex.cache.loader.floor.FloorType;
import com.jagex.util.ByteBufferUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Map;

@Slf4j
public class FloorDefLoader extends FloorDefinitionLoader {

	private final Map<Integer, Floor> overlays = Maps.newConcurrentMap();
	private final Map<Integer, Floor> underlays = Maps.newConcurrentMap();

	@Override
	public void init(Archive archive) {
		decodeUnderlays(archive);
		decodeOverlays(archive);
	}

	@Override
	public void init(byte[] data) {

	}

	public void decodeUnderlays(Archive archive) {
		for (int id : archive.fileIds()) {
			File file = archive.file(id);
			if (file != null && file.getData() != null) {
				try {
					Floor floor = decodeUnderlay(ByteBuffer.wrap(file.getData()));
					underlays.put(id, floor);
				} catch (Exception ex) {
					log.warn("Failed to load underlay {}", id, ex);
				}
			}
		}

		log.info("Loaded {} underlays | max id: {}",
				underlays.size(),
				underlays.keySet().stream().max(Integer::compareTo).orElse(0));
	}

	public Floor decodeUnderlay(ByteBuffer buffer) {
		Floor floor = new Floor();

		int rgb = 0;

		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0)
				break;

			switch (opcode) {
				case 1:
					rgb = ByteBufferUtils.readU24Int(buffer);
					floor.setRgb(rgb);
					break;

				case 2:
					int texture = buffer.getShort() & 0xFFFF;
					if (texture == 65535)
						texture = -1;
					floor.setTexture(texture);
					break;

				case 3:
					buffer.getShort(); // textureSize
					break;

				case 4:
					floor.setShadowed(false);
					break;

				default:
					System.out.println("Unknown underlay opcode: " + opcode);
					break;
			}
		}
		rgb2hsl(floor, rgb);
		return floor;
	}

	public void decodeOverlays(Archive archive) {
		for (int id : archive.fileIds()) {
			File file = archive.file(id);
			if (file != null && file.getData() != null) {
				try {
					Floor floor = decodeOverlay(ByteBuffer.wrap(file.getData()));
					overlays.put(id, floor);
				} catch (Exception ex) {
					log.warn("Failed to load overlay {}", id, ex);
				}
			}
		}

		log.info("Loaded {} overlays | max id: {}",
				overlays.size(),
				overlays.keySet().stream().max(Integer::compareTo).orElse(0));
	}

	public Floor decodeOverlay(ByteBuffer buffer) {
		Floor floor = new Floor();

		int texture = -1;

		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0)
				break;

			switch (opcode) {

				case 1:
					floor.setRgb(ByteBufferUtils.readU24Int(buffer));
					break;

				case 2:
					texture = buffer.get() & 0xFF;
					floor.setTexture(texture);
					break;

				case 3:
					texture = buffer.getShort() & 0xFFFF;
					if (texture == 65535)
						texture = -1;
					floor.setTexture(texture);
					break;

				case 5:
					// hideUnderlay = false
					break;

				case 7:
					int secondaryRgb = ByteBufferUtils.readU24Int(buffer);
					floor.setAnotherRgb(secondaryRgb);
					break;

				case 8:
					break;

				case 9:
					buffer.getShort(); // scale
					break;

				case 10:
					// blockShadow
					break;

				case 11:
					buffer.get(); // brightness
					break;

				case 12:
					// blendTexture
					break;

				case 13:
					ByteBufferUtils.readU24Int(buffer); // waterColor
					break;

				case 14:
					buffer.get(); // waterScale
					break;

				case 15:
					int i = buffer.getShort() & 0xFFFF;
					if (i == 65535)
						i = -1;
					break;

				case 16:
					buffer.get(); // waterIntensity
					break;

				default:
					System.out.println("Unknown overlay opcode: " + opcode);
					break;
			}
		}

		floor.generateHsl();

		return floor;
	}

	private void rgb2hsl(Floor floor, int rgb) {
		double r = (rgb >> 16 & 0xff) / 256.0;
		double g = (rgb >> 8 & 0xff) / 256.0;
		double b = (rgb & 0xff) / 256.0;

		double min = Math.min(r, Math.min(g, b));
		double max = Math.max(r, Math.max(g, b));

		double h = 0.0;
		double s = 0.0;
		double l = (min + max) / 2.0;

		if (min != max) {
			if (l < 0.5)
				s = (max - min) / (max + min);
			else
				s = (max - min) / (2.0 - max - min);

			if (r == max)
				h = (g - b) / (max - min);
			else if (g == max)
				h = 2.0 + (b - r) / (max - min);
			else
				h = 4.0 + (r - g) / (max - min);
		}

		h /= 6.0;

		int hue = (int) (h * 256.0);
		int sat = (int) (s * 256.0);
		int lum = (int) (l * 256.0);

		sat = Math.max(0, Math.min(255, sat));
		lum = Math.max(0, Math.min(255, lum));

		int chroma = (l > 0.5)
				? (int) ((1.0 - l) * s * 512.0)
				: (int) (l * s * 512.0);

		if (chroma < 1) chroma = 1;

		int weightedHue = (int) (h * chroma);

		floor.setHue(hue);
		floor.setSaturation(sat);
		floor.setLuminance(lum);
		floor.setChroma(chroma);
		floor.setWeightedHue(weightedHue);
		floor.setColour(encodeHsl(hue, sat, lum));
	}

	private int encodeHsl(int h, int s, int l) {
		if (l > 179) s /= 2;
		if (l > 192) s /= 2;
		if (l > 217) s /= 2;
		if (l > 243) s /= 2;

		return (h / 4 << 10) + (s / 32 << 7) + l / 2;
	}

	@Override
	public Floor getFloor(int id, FloorType type) {
		if (type == FloorType.OVERLAY)
			return overlays.computeIfAbsent(id, FloorDefLoader::emptyFloor);
		else
			return underlays.computeIfAbsent(id, FloorDefLoader::emptyFloor);
	}

	@Override
	public int getSize(FloorType type) {
		if (type == FloorType.OVERLAY)
			return overlays.keySet().stream().max(Integer::compareTo).orElse(0);
		else
			return underlays.keySet().stream().max(Integer::compareTo).orElse(0);
	}

	@Override
	public int count() {
		return overlays.size() + underlays.size();
	}

	private static Floor emptyFloor(int id) {
		Floor floor = new Floor();
		floor.generateHsl();
		return floor;
	}

	@Override
	public Floor forId(int id) {
		return overlays.getOrDefault(id, underlays.get(id));
	}
}