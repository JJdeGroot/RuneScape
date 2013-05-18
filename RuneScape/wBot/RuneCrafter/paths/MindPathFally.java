package org.obduro.runecrafter.paths;

import bot.script.wrappers.Tile;

public class MindPathFally extends RcPath {

	private Tile altarTile, portalTile;
	private Tile[] path;

	public MindPathFally(){
		altarTile = new Tile(2843, 4832);
		portalTile = new Tile(2841, 4828);
		
		Tile[] mindPath = {new Tile(2946, 3369), new Tile(2946, 3370), new Tile(2946, 3373), new Tile(2948, 3376),	new Tile(2952, 3379), new Tile(2956, 3382),	new Tile(2960, 3382), new Tile(2964, 3385),	new Tile(2966, 3389), new Tile(2965, 3393),	new Tile(2967, 3398), new Tile(2968, 3402),	new Tile(2971, 3407), new Tile(2974, 3410),	new Tile(2977, 3413), new Tile(2981, 3416),	new Tile(2984, 3418), new Tile(2987, 3420),	new Tile(2988, 3424), new Tile(2988, 3429),	new Tile(2986, 3433), new Tile(2983, 3436),	new Tile(2981, 3441),new Tile(2980, 3445), new Tile(2977, 3449),	new Tile(2976, 3453), new Tile(2973, 3457),	new Tile(2972, 3462),new Tile(2973, 3467),new Tile(2974, 3470),new Tile(2972, 3475),	new Tile(2973, 3478),new Tile(2975, 3481),new Tile(2976, 3485),new Tile(2978, 3487),new Tile(2982, 3491), new Tile(2980, 3500), new Tile(2980, 3503),	new Tile(2979, 3508), new Tile(2980, 3513)};
		path = mindPath;
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
