package org.obduro.runecrafter.paths;

import bot.script.wrappers.Tile;

public class WaterPath extends RcPath {

	private Tile altarTile, portalTile;
	private Tile[] path;

	public WaterPath(){
		altarTile = new Tile(2843, 4832);
		portalTile = new Tile(2841, 4828);
		
		Tile[] waterPath = {new Tile(3093, 3243),new Tile(3093, 3246),new Tile(3095, 3248),new Tile(3099, 3245),new Tile(3101, 3342),new Tile(3101, 3239),new Tile(3102, 3236),new Tile(3104, 3234),new Tile(3108, 3233),new Tile(3111, 3230),new Tile(3114, 3228),new Tile(3119, 3227),new Tile(3122, 3224),new Tile(3125, 3221),new Tile(3129, 3220),new Tile(3132, 3219),new Tile(3136, 3217),new Tile(3140, 3215),new Tile(3141, 3211),new Tile(3142, 3208),new Tile(3141, 3199),new Tile(3145, 3195),new Tile(3147, 3191),new Tile(3151, 3186),new Tile(3153, 3182),new Tile(3157, 3178),new Tile(3160, 3175),new Tile(3162, 3171),new Tile(3166, 3168),new Tile(3170, 3166),new Tile(3175, 3165),new Tile(3179, 3165),new Tile(3183, 3165)};
		path = waterPath;
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
