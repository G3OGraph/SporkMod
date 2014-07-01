package in.parapengu.spork.rotation;

import in.parapengu.commons.utils.file.TextFile;
import in.parapengu.spork.map.MapLoader;
import in.parapengu.spork.util.Log;

import java.io.File;
import java.io.IOException;
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

}
