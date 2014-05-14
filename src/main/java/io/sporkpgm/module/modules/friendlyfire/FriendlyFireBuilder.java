package io.sporkpgm.module.modules.friendlyfire;

import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.builder.BuilderResult;
import io.sporkpgm.util.StringUtil;

@BuilderInfo(result = BuilderResult.SINGLE)
public class FriendlyFireBuilder extends Builder {

	@Override
	public FriendlyFireModule single(BuilderContext context) {
		if(!context.only("document", "loader", "map", "match")) {
			return null;
		}

		return new FriendlyFireModule(StringUtil.convertStringToBoolean(context.getDocument().getRootElement().getChildText("friendlyfire"), false));
	}

}
