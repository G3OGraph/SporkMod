package io.sporkpgm.module.modules.maxheight;

import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.builder.BuilderResult;
import io.sporkpgm.module.exceptions.ModuleBuildException;
import io.sporkpgm.util.StringUtil;
import org.jdom2.Element;

@BuilderInfo(result = BuilderResult.SINGLE)
public class MaxHeightBuilder extends Builder {

	@Override
	public MaxHeightModule single(BuilderContext context) throws ModuleBuildException {
		if(!context.only("document", "loader", "map", "match")) {
			return null;
		}

		Element root = context.getDocument().getRootElement();
		Element max = root.getChild("maxbuildheight");

		if(max != null) {
			int height;

			try {
				height = StringUtil.convertStringToInteger(max.getText());
			} catch(NumberFormatException e) {
				throw new ModuleBuildException(max, "The height supplied was not valid");
			}

			return new MaxHeightModule(height);
		}

		return null;
	}

}
