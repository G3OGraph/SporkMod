package in.parapengu.spork.module.builder;

import com.google.common.collect.Lists;
import in.parapengu.spork.module.Module;

import java.util.ArrayList;
import java.util.List;

public class Builder<M extends Module> {

	public boolean isPhase(BuilderContext context) {
		BuilderInfo info = getClass().getAnnotation(BuilderInfo.class);
		return context.getPhase() == info.phase();
	}

	public M single(BuilderContext context) {
		return null;
	}

	public M[] array(BuilderContext context) {
		return null;
	}

	public List<M> list(BuilderContext context) {
		return null;
	}

	public List<M> build(BuilderContext context) {
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

		List<M> modules = list(context);
		if(modules != null && modules.size() > 0) {
			list = Lists.newArrayList(modules);
			return list;
		}

		return list;
	}

}
