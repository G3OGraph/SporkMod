package in.parapengu.spork.module.modules.region;

import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.builder.ModuleBuilder;
import in.parapengu.spork.module.modules.region.types.BlockRegion;
import org.bukkit.Location;

@ModuleBuilder(RegionBuilder.class)
public abstract class RegionModule extends Module {

	public boolean isInside(Location location) {
		return isInside(new BlockRegion(location.getX(), location.getY(), location.getZ()));
	}

	public abstract boolean isInside(BlockRegion region);

}
