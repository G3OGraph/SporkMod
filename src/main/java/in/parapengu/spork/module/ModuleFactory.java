package in.parapengu.spork.module;

import in.parapengu.spork.exception.map.MapLoadException;
import in.parapengu.spork.exception.module.ModuleBuildException;
import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.builder.BuilderInfo;
import in.parapengu.spork.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ModuleFactory {

	private ModuleRegistration registration;
	private List<Builder> builders;

	public ModuleFactory(ModuleRegistration registration) throws ModuleBuildException {
		this.registration = registration;
		this.builders = registration.getBuilders();
	}

	public ModuleRegistration getRegistration() {
		return registration;
	}

	public List<Module> build(BuilderContext context) throws MapLoadException {
		List<Module> modules = new ArrayList<>();
		for(Builder builder : builders) {
			try {
				modules.addAll(builder.build(context.register(modules)));
			} catch(ModuleLoadException ex) {
				Log.exception(ex);
				if(builder.getClass().getAnnotation(BuilderInfo.class).required()) {
					throw new MapLoadException(context.getMap(), "Failed to load a required module, " + builder.getModule().getSimpleName());
				}
			}
		}
		return modules;
	}

}
