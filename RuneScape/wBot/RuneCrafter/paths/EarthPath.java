package org.obduro.runecrafter.paths;

import bot.script.wrappers.Tile;

public class EarthPath extends RcPath {

	private Tile altarTile, portalTile;
	private Tile[] path;

	public EarthPath(){
		altarTile = new Tile(2843, 4832);
		portalTile = new Tile(2841, 4828);
		
		Tile[] earthPath = {new Tile(3254, 3420),new Tile(3254, 3423),new Tile(3255, 3427),new Tile(3260, 3428),new Tile(3265, 3428),new Tile(3271, 3428),new Tile(3277, 3428),new Tile(3283, 3429),new Tile(3287, 3433),new Tile(3290, 3437),new Tile(3291, 3442),new Tile(3291, 3447),new Tile(3292, 3452),new Tile(3294, 3458),new Tile(3295, 3462),new Tile(3297, 3466),new Tile(3300, 3469),new Tile(3304, 3472)};
		path = earthPath;
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
