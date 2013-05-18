package org.obduro.runecrafter.paths;

import bot.script.wrappers.Tile;

public class BodyPath extends RcPath {

	private Tile altarTile, portalTile;
	private Tile[] path;

	public BodyPath(){
		altarTile = new Tile(2843, 4832);
		portalTile = new Tile(2841, 4828);
		
		Tile[] bodyPath = {new Tile(3093, 3491),new Tile(3090, 3491),new Tile(3088, 3489),new Tile(3086, 3486),new Tile(3084, 3485),new Tile(3081, 3482),new Tile(3081, 3478),new Tile(3081, 3474),new Tile(3081, 3471),new Tile(3081, 3467),new Tile(3083, 3465),new Tile(3087, 3463),new Tile(3085, 3460),new Tile(3083, 3457),new Tile(3081, 3455),new Tile(3081, 3450),new Tile(3079, 3448),new Tile(3075, 3447),new Tile(3073, 3444),new Tile(3073, 3440),new Tile(3070, 3441),new Tile(3067, 3440),new Tile(3063, 3439),new Tile(3059, 3440),new Tile(3055, 3443)};
		path = bodyPath;
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
