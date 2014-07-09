package in.parapengu.spork.module.modules.spawn;

import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.modules.region.types.BlockRegion;

public class SpawnModule extends Module {

	private RegionModule region;
	// private KitModule kit;

	public SpawnModule(RegionModule region) {
		this.region = region;
	}

	public RegionModule getRegion() {
		return region;
	}

	public BlockRegion getSpawn() {
		return region.getRandom();
	}

	/*
	public KitModule getKit() {
		return kit;
	}
	 */

}
