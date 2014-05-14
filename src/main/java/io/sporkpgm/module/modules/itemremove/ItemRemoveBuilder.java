package io.sporkpgm.module.modules.itemremove;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.builder.BuilderResult;
import io.sporkpgm.module.exceptions.ModuleBuildException;
import io.sporkpgm.util.StringUtil;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(result = BuilderResult.SINGLE)
public class ItemRemoveBuilder extends Builder {

	@Override
	public ItemRemoveModule single(BuilderContext context) throws ModuleBuildException {
		if(!context.only("document", "loader", "map", "match")) {
			return null;
		}

		SporkMap map = context.getMap();
		Element root = context.getDocument().getRootElement();

		if(root.getChild("itemremove") != null) {
			Element element = root.getChild("itemremove");
			List<Material> materials = new ArrayList<>();

			for(Element item : element.getChildren("item")) {
				Material material = StringUtil.convertStringToMaterial(item.getText());
				if(material == null) {
					throw new ModuleBuildException("Unsupported Material: " + item.getText());
				}

				materials.add(material);
			}

			return new ItemRemoveModule(map, materials);
		}

		return null;
	}

}
