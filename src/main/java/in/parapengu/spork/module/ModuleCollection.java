package in.parapengu.spork.module;

import java.util.ArrayList;
import java.util.List;

public class ModuleCollection extends ArrayList<Module> implements Cloneable {

	private static final long serialVersionUID = -98770778801387020L;

	public <T extends Module> T getModule(Class<T> clazz) {
		for(Module module : this) {
			if(module.getClass().equals(clazz)) {
				return (T) module;
			}
		}

		return null;
	}

	public <T extends Module> List<T> getModules(Class<T> clazz) {
		List<T> modules = new ArrayList<>();

		for(Module module : this) {
			if(module.getClass().equals(clazz)) {
				modules.add((T) module);
			}
		}

		return modules;
	}

	@Override
	public ModuleCollection clone() {
		ModuleCollection cloned = new ModuleCollection();
		cloned.addAll(this);
		return cloned;
	}


}
