package in.parapengu.spork.module.builder;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.module.ModuleBuildException;
import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.module.Module;

import java.util.ArrayList;
import java.util.List;

public class Builder<M extends Module> {

	protected Class<M> module;

	public boolean isPhase(BuilderContext context) {
		BuilderInfo info = getClass().getAnnotation(BuilderInfo.class);
		return context.getPhase() == info.value();
	}

	public M single(BuilderContext context) throws ModuleLoadException {
		return null;
	}

	public M[] array(BuilderContext context) throws ModuleLoadException {
		return null;
	}

	public List<? extends M> list(BuilderContext context) throws ModuleLoadException {
		return null;
	}

	public List<M> build(BuilderContext context) throws ModuleLoadException {
		List<M> list = new ArrayList<>();
		if(isPhase(context)) {
			return list;
		}

		M single = single(context);
		if(single != null) {
			list = Lists.newArrayList(single);
			return list;
		}

		M[] array = array(context);
		if(array != null) {
			list = Lists.newArrayList(array);
			return list;
		}

		List<? extends M> modules = list(context);
		if(modules != null && modules.size() > 0) {
			list = Lists.newArrayList(modules);
			return list;
		}

		return list;
	}

	public Class<M> getModule() {
		return module;
	}

}
