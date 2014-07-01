package in.parapengu.spork.module;

import in.parapengu.spork.exception.module.ModuleBuildException;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;

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

	public List<Module> build(BuilderContext context) {
		List<Module> modules = new ArrayList<>();
		for(Builder builder : builders) {
			modules.addAll(builder.build(context));
		}
		return modules;
	}

}
