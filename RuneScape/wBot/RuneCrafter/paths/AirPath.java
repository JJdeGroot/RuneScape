package org.obduro.runecrafter.paths;

import bot.script.wrappers.Tile;

public class AirPath extends RcPath {

	private Tile altarTile, portalTile;
	private Tile[] path;

	public AirPath(){
		altarTile = new Tile(2843, 4832);
		portalTile = new Tile(2841, 4828);
		
		Tile[] airPath = {new Tile(3012, 3356), new Tile(3012, 3358), new Tile(3010, 3359), new Tile(3007, 3358),  new Tile(3006, 3355), new Tile(3006, 3352), new Tile(3007, 3349), new Tile(3007, 3346), new Tile(3007, 3344), new Tile(3007, 3341), new Tile(3007, 3338), new Tile(3007, 3335), new Tile(3007, 3332), new Tile(3006, 3329), new Tile(3006, 3326), new Tile(3006, 3323), new Tile(3005, 3320), new Tile(3002, 3318), new Tile(3000, 3315), new Tile(2999, 3312), new Tile(2997, 3309), new Tile(2995, 3307), new Tile(2993, 3304), new Tile(2991, 3302), new Tile(2991, 3299), new Tile(2991, 3295),new Tile(2989, 3292), new Tile(2987, 3292)};
		path = airPath;
	}

	@Override
	public boolean toRuins() {
		return super.walkPath(path, false);
	}

	@Override
	public boolean toAltar() {
		return super.walkTo(altarTile);
	}

	@Override
	public boolean toPortal() {
		return super.walkTo(portalTile);
	}

	@Override
	public boolean toBank() {
		return super.walkPath(path, true);
	}
	
}
