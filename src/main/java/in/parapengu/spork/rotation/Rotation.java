package in.parapengu.spork.rotation;

import com.google.common.base.Charsets;
import in.parapengu.commons.utils.file.TextFile;
import in.parapengu.spork.Spork;
import in.parapengu.spork.exception.rotation.RotationLoadException;
import in.parapengu.spork.map.MapLoader;
import in.parapengu.spork.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Rotation {

	private static int MAX_SIZE = 30;
	private static boolean OVERFLOW = true;

	private File file;
	private int index;
	private List<MapLoader> loaders;
	private List<RotationSlot> slots;

	public Rotation(File file, List<MapLoader> loaders, int index) {
		this.file = file;
		this.index = index;
		this.loaders = loaders;
		this.slots = new ArrayList<>();

		if(loaders.size() > MAX_SIZE && OVERFLOW) {
			for(MapLoader loader : loaders) {
				slots.add(new RotationSlot(loader));
			}
		} else {
			while(slots.size() < MAX_SIZE) {
				for(int i = 0; i < loaders.size() && slots.size() < MAX_SIZE; i++) {
					slots.add(new RotationSlot(loaders.get(i)));
				}

				if(slots.size() == 0) {
					Log.warning("No slots were loaded");
					break;
				}
			}
		}
	}

	public Rotation(File file, List<MapLoader> loaders) {
		this(file, loaders, 0);
	}

	public File getFile() {
		return file;
	}

	public int getIndex() {
		return index;
	}

	public List<RotationSlot> getSlots() {
		return slots;
	}

	public boolean save() {
		TextFile file;
		try {
			file = new TextFile(this.file);
		} catch(IOException ex) {
			Log.exception(ex);
			return false;
		}

		file.line(index + "");
		for(MapLoader loader : loaders) {
			file.line(loader.getName());
		}

		file.save();
		return true;
	}

	public static Rotation parse(File file) throws RotationLoadException {
		if(!file.exists()) {
			throw new RotationLoadException(file, file.getName() + " does not exist");
		}

		if(file.isDirectory()) {
			throw new RotationLoadException(file, file.getName() + " was a directory");
		}

		List<String> lines;
		try {
			lines = Files.readAllLines(file.toPath(), Charsets.UTF_8);
		} catch(IOException ex) {
			throw new RotationLoadException(file, ex);
		}

		String first = lines.get(0);
		int index;
		try {
			index = Integer.parseInt(first);
		} catch(NumberFormatException ex) {
			throw new RotationLoadException(file, "The rotation index was not a valid number");
		}

		lines.remove(0);
		List<MapLoader> maps = new ArrayList<>();
		for(String line : lines) {
			MapLoader loader = Spork.getMaps().getMap(line);
			if(loader == null) {
				throw new RotationLoadException(file, "Could not find a map matching \"" + line + "\"");
			}

			maps.add(loader);
		}

		return new Rotation(file, maps, index);
	}

}
