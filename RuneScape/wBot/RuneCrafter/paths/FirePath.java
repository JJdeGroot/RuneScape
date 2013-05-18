package org.obduro.runecrafter.paths;

import bot.script.wrappers.Tile;

public class FirePath extends RcPath {

	private Tile altarTile, portalTile;
	private Tile[] path;

	public FirePath(){
		altarTile = new Tile(2843, 4832);
		portalTile = new Tile(2841, 4828);
		
		Tile[] firePath = {new Tile(3269, 3167),new Tile(3272, 3167),new Tile(3276, 3168),new Tile(3276, 3172),new Tile(3278, 3176),new Tile(3279, 3179),new Tile(3284, 3180),new Tile(3289, 3183),new Tile(3292, 3176),new Tile(3294, 3190),new Tile(3297, 3196),new Tile(3301, 3199),new Tile(3301, 3204),new Tile(3302, 3210),new Tile(3302, 3215),new Tile(3302, 3219),new Tile(3302, 3223),new Tile(3302, 3227),new Tile(3304, 3233),new Tile(3306, 3239),new Tile(3307, 3244),new Tile(3309, 3249),new Tile(3312, 3253)};
		path = firePath;
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
