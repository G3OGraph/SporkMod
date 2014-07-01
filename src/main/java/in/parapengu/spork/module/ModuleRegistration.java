package in.parapengu.spork.module;

import in.parapengu.spork.exception.module.ModuleBuildException;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderInfo;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ModuleRegistration {

	private List<Class<? extends Module>> registered;

	public ModuleRegistration() {
		this.registered = new ArrayList<>();
	}

	public void register(Class<? extends Module> register) {
		this.registered.add(register);
	}

	public List<Builder> getBuilders() throws ModuleBuildException {
		List<Builder> builders = new ArrayList<>();
		for(Class<? extends Module> module : registered) {
			builders.add(getBuilder(module));
		}

		return builders;
	}

	public Builder getBuilder(Class<? extends Module> module) throws ModuleBuildException {
		ModuleInfo info = module.getAnnotation(ModuleInfo.class);
		if(info == null) {
			throw new ModuleBuildException(module, "ModuleInfo was not present");
		}

		Class<?> clazz = info.builder();
		Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
		constructor.setAccessible(true);

		if(clazz.getAnnotation(BuilderInfo.class) == null) {
			throw new ModuleBuildException(module, "BuilderInfo was not present in " + clazz.getSimpleName());
		}

		Builder builder;
		try {
			builder = (Builder) constructor.newInstance();
		} catch(Exception ex) {
			throw new ModuleBuildException(module, ex);
		}

		return builder;
	}

}
