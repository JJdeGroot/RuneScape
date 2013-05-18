package scripts.jjsrunecrafter.methods;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.jjsrunecrafter.types.Altars;
import scripts.jjsrunecrafter.types.Banks;
import scripts.jjsrunecrafter.types.Runes;

public class Walk {

	private Runes chosenRunes;
	private Altars chosenAltar;
	private Banks chosenBank;
	private RSTile altarRSTile, portalRSTile;
	private RSTile[] path;
	
	public Walk(Runes runes, Altars altar, Banks bank){
		chosenRunes = runes;
		chosenAltar = altar;
		chosenBank = bank;
		
		switch(chosenRunes){
				
			case AIR:
				altarRSTile = new RSTile(2843, 4832);
				portalRSTile = new RSTile(2841, 4828);
				
				RSTile[] fallyEastAirPath = {new RSTile(3012, 3357), new RSTile(3012, 3358), new RSTile(3010, 3359), new RSTile(3007, 3358),  new RSTile(3006, 3355), new RSTile(3006, 3352), new RSTile(3007, 3349), new RSTile(3007, 3346), new RSTile(3007, 3344), new RSTile(3007, 3341), new RSTile(3007, 3338), new RSTile(3007, 3335), new RSTile(3007, 3332), new RSTile(3006, 3329), new RSTile(3006, 3326), new RSTile(3006, 3323), new RSTile(3005, 3320), new RSTile(3002, 3318), new RSTile(3000, 3315), new RSTile(2999, 3312), new RSTile(2997, 3309), new RSTile(2995, 3307), new RSTile(2993, 3304), new RSTile(2991, 3302), new RSTile(2991, 3299), new RSTile(2991, 3295),new RSTile(2989, 3292), new RSTile(2987, 3292)};
				path = fallyEastAirPath;
				
				break;
				
			case BODY:
				altarRSTile = new RSTile(2523, 4842);
				portalRSTile = new RSTile(2521, 4834);
				
				RSTile[] edgeBodyPath = {new RSTile(3093, 3491),new RSTile(3090, 3491),new RSTile(3088, 3489),new RSTile(3086, 3486),new RSTile(3084, 3485),new RSTile(3081, 3482),new RSTile(3081, 3478),new RSTile(3081, 3474),new RSTile(3081, 3471),new RSTile(3081, 3467),new RSTile(3083, 3465),new RSTile(3087, 3463),new RSTile(3085, 3460),new RSTile(3083, 3457),new RSTile(3081, 3455),new RSTile(3081, 3450),new RSTile(3079, 3448),new RSTile(3075, 3447),new RSTile(3073, 3444),new RSTile(3073, 3440),new RSTile(3070, 3441),new RSTile(3067, 3440),new RSTile(3063, 3439),new RSTile(3059, 3440),new RSTile(3055, 3443)};
				path = edgeBodyPath;
					
				break;
				
			case EARTH:
				altarRSTile = new RSTile(2657, 4839);
				portalRSTile = new RSTile(2655, 4830);
				
				RSTile[] varrockEastEarthPath = {new RSTile(3254, 3420),new RSTile(3254, 3423),new RSTile(3255, 3427),new RSTile(3260, 3428),new RSTile(3265, 3428),new RSTile(3271, 3428),new RSTile(3277, 3428),new RSTile(3283, 3429),new RSTile(3287, 3433),new RSTile(3290, 3437),new RSTile(3291, 3442),new RSTile(3291, 3447),new RSTile(3292, 3452),new RSTile(3294, 3458),new RSTile(3295, 3462),new RSTile(3297, 3466),new RSTile(3300, 3469),new RSTile(3304, 3472)};
				path = varrockEastEarthPath;
					
				break;
				
			case FIRE:
				altarRSTile = new RSTile(2584, 4840);
				portalRSTile = new RSTile(2575, 4850);
				
				RSTile[] alKharidFirePath = {new RSTile(3269, 3167),new RSTile(3272, 3167),new RSTile(3276, 3168),new RSTile(3276, 3172),new RSTile(3278, 3176),new RSTile(3279, 3179),new RSTile(3284, 3180),new RSTile(3289, 3183),new RSTile(3292, 3186),new RSTile(3294, 3190),new RSTile(3297, 3196),new RSTile(3301, 3199),new RSTile(3301, 3204),new RSTile(3302, 3210),new RSTile(3302, 3215),new RSTile(3302, 3219),new RSTile(3302, 3223),new RSTile(3302, 3227),new RSTile(3304, 3233),new RSTile(3306, 3239),new RSTile(3307, 3244),new RSTile(3309, 3249),new RSTile(3312, 3253)};
				path = alKharidFirePath;
	
				break;
				
			case MIND:
				altarRSTile = new RSTile(2787, 4840);
				portalRSTile = new RSTile(2793, 4827);
				
				RSTile[] fallyWestMindPath = {new RSTile(2946, 3369), new RSTile(2946, 3370), new RSTile(2946, 3373), new RSTile(2948, 3376),	new RSTile(2952, 3379), new RSTile(2956, 3382),	new RSTile(2960, 3382), new RSTile(2964, 3385),	new RSTile(2966, 3389), new RSTile(2965, 3393),	new RSTile(2967, 3398), new RSTile(2968, 3402),	new RSTile(2971, 3407), new RSTile(2974, 3410),	new RSTile(2977, 3413), new RSTile(2981, 3416),	new RSTile(2984, 3418), new RSTile(2987, 3420),	new RSTile(2988, 3424), new RSTile(2988, 3429),	new RSTile(2986, 3433), new RSTile(2983, 3436),	new RSTile(2981, 3441),new RSTile(2980, 3445), new RSTile(2977, 3449),	new RSTile(2976, 3453), new RSTile(2973, 3457),	new RSTile(2972, 3462),new RSTile(2973, 3467),new RSTile(2974, 3470),new RSTile(2972, 3475),	new RSTile(2973, 3478),new RSTile(2975, 3481),new RSTile(2976, 3485),new RSTile(2978, 3487),new RSTile(2982, 3491), new RSTile(2980, 3500), new RSTile(2980, 3503),	new RSTile(2979, 3508), new RSTile(2980, 3513)};
				path = fallyWestMindPath;
		
				break;
				
			case WATER:
				altarRSTile = new RSTile(2718, 4835);
				portalRSTile = new RSTile(2726, 4832);
				
				RSTile[] draynorWaterPath = {new RSTile(3093, 3244),new RSTile(3093, 3248),new RSTile(3096, 3247),new RSTile(3099, 3245),new RSTile(3100, 3243),new RSTile(3101, 3340),new RSTile(3101, 3237),new RSTile(3102, 3236),new RSTile(3104, 3234),new RSTile(3108, 3233),new RSTile(3111, 3230),new RSTile(3114, 3228),new RSTile(3119, 3227),new RSTile(3122, 3224),new RSTile(3125, 3221),new RSTile(3129, 3220),new RSTile(3132, 3219),new RSTile(3136, 3217),new RSTile(3140, 3215),new RSTile(3141, 3211),new RSTile(3142, 3208),new RSTile(3141, 3199),new RSTile(3145, 3195),new RSTile(3147, 3191),new RSTile(3151, 3186),new RSTile(3153, 3182),new RSTile(3157, 3178),new RSTile(3160, 3175),new RSTile(3162, 3171),new RSTile(3166, 3168),new RSTile(3170, 3166),new RSTile(3175, 3165),new RSTile(3179, 3165),new RSTile(3183, 3165)};
				path = draynorWaterPath;
	
				break;
		}
	}
	
	private void waitPath(RSTile tile){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(500, 1000)){
			if(tile.distanceTo(Player.getPosition()) <= General.random(1, 3)){
				break;
			}
			General.sleep(100, 200);
			if(Player.isMoving()){
				t = System.currentTimeMillis();
			}
		}
	}

	private boolean walkPath(RSTile[] tiles, boolean reverse){
		if(reverse){
			RSTile[] reversed = new RSTile[tiles.length];
			for(int i = tiles.length-1; i >= 0; i--){
				reversed[tiles.length-1-i] = tiles[i];
			}
			tiles = reversed;
		}
		
		if(Walking.walkPath(tiles)){
			waitPath(tiles[tiles.length-1]);
		}
	
		return false;
	}

	public boolean nearBank(){
		RSObject[] bank = Objects.findNearest(25, chosenBank.getBoothID());
		return bank != null && bank.length > 0 && bank[0].isOnScreen() && bank[0].getPosition().distanceTo(Player.getPosition()) <= 5;
	}
	
	public boolean nearRuins(){
		RSObject[] ruins = Objects.findNearest(25, chosenAltar.getRuinsID());
		return ruins != null && ruins.length > 0 && ruins[0].isOnScreen() && ruins[0].getPosition().distanceTo(Player.getPosition()) <= 5;
	}
	
	public boolean insideAltar(){
		RSObject[] loaded = Objects.find(25, chosenAltar.getAltarID(), chosenAltar.getPortalID());
		return loaded != null && loaded.length > 0;
	}
	
	public boolean nearAltar(){
		RSObject[] altar = Objects.findNearest(25, chosenAltar.getAltarID());
		return altar != null && altar.length > 0 && altar[0].isOnScreen() && altar[0].getPosition().distanceTo(Player.getPosition()) <= 5;
	}
	
	public boolean nearPortal(){
		RSObject[] portal = Objects.findNearest(25, chosenAltar.getPortalID());
		return portal != null && portal.length > 0 && portal[0].isOnScreen() && portal[0].getPosition().distanceTo(Player.getPosition()) <= 5;
	}
	
	public boolean toBank(){
		return walkPath(path, true);
	}
	
	public boolean toMysteriousRuins(){
		return walkPath(path, false);
	}
	
	private void walkTo(RSTile RSTile){
		RSTile = new RSTile(RSTile.getX() + General.random(-3, 3), RSTile.getY() + General.random(-3, 3));
		Walking.walkTo(RSTile);
		General.sleep(400, 800);
		while(Player.isMoving()){
			General.sleep(100, 200);
		}
	}
	
	public void toAltar(){
		walkTo(altarRSTile);
	}
	
	public void toPortal(){
		walkTo(portalRSTile);
	}
	
	public void toBankFailsafe(){
		walkTo(path[0]);
	}
		
}

