package org.obduro.runecrafter.methods;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.obduro.runecrafter.Timer;
import org.obduro.runecrafter.types.Altar;
import org.obduro.runecrafter.types.BankLoc;

import bot.script.methods.Keyboard;
import bot.script.methods.Methods;
import bot.script.methods.Objects;
import bot.script.methods.Players;
import bot.script.methods.Walking;
import bot.script.methods.Widgets;
import bot.script.util.Random;
import bot.script.wrappers.Component;
import bot.script.wrappers.GameObject;
import bot.script.wrappers.Tile;

public class Walk {

	private final int DITCH_ID = 23271,
					  WARNING_SCREEN_ID = 382,
					  ENTER_WILDY_ID = 18,
					  DONT_ASK_ID = 27;

	private Altar chosenAltar;
	private BankLoc chosenBank;
	private Tile altarTile, portalTile;
	private Tile[] path;
	
	public Walk(Altar altar, BankLoc bank){
		chosenAltar = altar;
		chosenBank = bank;
		
		switch(chosenAltar){
				
			case AIR:
				altarTile = new Tile(2843, 4832);
				portalTile = new Tile(2841, 4828);
				
				switch(chosenBank){
					case FALADOR_EAST:
						Tile[] fallyEastAirPath = {new Tile(3012, 3356), new Tile(3012, 3358), new Tile(3010, 3359), new Tile(3007, 3358),  new Tile(3006, 3355), new Tile(3006, 3352), new Tile(3007, 3349), new Tile(3007, 3346), new Tile(3007, 3344), new Tile(3007, 3341), new Tile(3007, 3338), new Tile(3007, 3335), new Tile(3007, 3332), new Tile(3006, 3329), new Tile(3006, 3326), new Tile(3006, 3323), new Tile(3005, 3320), new Tile(3002, 3318), new Tile(3000, 3315), new Tile(2999, 3312), new Tile(2997, 3309), new Tile(2995, 3307), new Tile(2993, 3304), new Tile(2991, 3302), new Tile(2991, 3299), new Tile(2991, 3295),new Tile(2989, 3292), new Tile(2987, 3292)};
						path = fallyEastAirPath;
						break;
				}
			
				break;
				
			case BODY:
				altarTile = new Tile(2523, 4842);
				portalTile = new Tile(2521, 4834);
				
				switch(chosenBank){
					case EDGEVILLE:
						Tile[] edgeBodyPath = {new Tile(3093, 3491),new Tile(3090, 3491),new Tile(3088, 3489),new Tile(3086, 3486),new Tile(3084, 3485),new Tile(3081, 3482),new Tile(3081, 3478),new Tile(3081, 3474),new Tile(3081, 3471),new Tile(3081, 3467),new Tile(3083, 3465),new Tile(3087, 3463),new Tile(3085, 3460),new Tile(3083, 3457),new Tile(3081, 3455),new Tile(3081, 3450),new Tile(3079, 3448),new Tile(3075, 3447),new Tile(3073, 3444),new Tile(3073, 3440),new Tile(3070, 3441),new Tile(3067, 3440),new Tile(3063, 3439),new Tile(3059, 3440),new Tile(3055, 3443)};
						path = edgeBodyPath;
						break;
				}
				
				break;
				
			case EARTH:
				altarTile = new Tile(2657, 4839);
				portalTile = new Tile(2655, 4830);
				
				switch(chosenBank){
					case VARROCK_EAST:
						Tile[] varrockEastEarthPath = {new Tile(3254, 3420),new Tile(3254, 3423),new Tile(3255, 3427),new Tile(3260, 3428),new Tile(3265, 3428),new Tile(3271, 3428),new Tile(3277, 3428),new Tile(3283, 3429),new Tile(3287, 3433),new Tile(3290, 3437),new Tile(3291, 3442),new Tile(3291, 3447),new Tile(3292, 3452),new Tile(3294, 3458),new Tile(3295, 3462),new Tile(3297, 3466),new Tile(3300, 3469),new Tile(3304, 3472)};
						path = varrockEastEarthPath;
						break;
				}
		
				break;
				
			case FIRE:
				altarTile = new Tile(2583, 4840);
				portalTile = new Tile(2574, 4850);
				
				switch(chosenBank){
					case AL_KHARID:
						Tile[] alKharidFirePath = {new Tile(3269, 3167),new Tile(3272, 3167),new Tile(3276, 3168),new Tile(3276, 3172),new Tile(3278, 3176),new Tile(3279, 3179),new Tile(3284, 3180),new Tile(3289, 3183),new Tile(3292, 3176),new Tile(3294, 3190),new Tile(3297, 3196),new Tile(3301, 3199),new Tile(3301, 3204),new Tile(3302, 3210),new Tile(3302, 3215),new Tile(3302, 3219),new Tile(3302, 3223),new Tile(3302, 3227),new Tile(3304, 3233),new Tile(3306, 3239),new Tile(3307, 3244),new Tile(3309, 3249),new Tile(3312, 3253)};
						path = alKharidFirePath;
						break;
				}
				
				
				break;
				
			case MIND:
				altarTile = new Tile(2787, 4840);
				portalTile = new Tile(2793, 4827);
				
				System.out.println("Chosen bank: " + chosenBank);
				
				switch(chosenBank){
					case EDGEVILLE:
						Tile[] edgeMindPath = {new Tile(3096, 3494),new Tile(3093, 3497),new Tile(3090, 3502),new Tile(3087, 3504),new Tile(3087, 3508),new Tile(3086, 3512),new Tile(3085, 3515), new Tile(3080, 3516),new Tile(3074, 3517),new Tile(3067, 3518),new Tile(3061, 3518),new Tile(3056, 3518),new Tile(3050, 3518),new Tile(3045, 3520),new Tile(3041, 3526),new Tile(3037, 3529),new Tile(3034, 3531),new Tile(3032, 3534),new Tile(3031, 3537),new Tile(3029, 3541),new Tile(3026, 3544),new Tile(3021, 3547),new Tile(3015, 3548),new Tile(3010, 3548),new Tile(3005, 3547),new Tile(3002, 3543),new Tile(2999, 3539),new Tile(2996, 3536),new Tile(2993, 3534),new Tile(2990, 3531),new Tile(2988, 3529),new Tile(2987, 3526),new Tile(2986, 3523),new Tile(2986, 3520),new Tile(2984, 3516)};
						path = edgeMindPath;
						break;
					
					case FALADOR_WEST:
						Tile[] fallyWestMindPath = {new Tile(2946, 3369), new Tile(2946, 3370), new Tile(2946, 3373), new Tile(2948, 3376),	new Tile(2952, 3379), new Tile(2956, 3382),	new Tile(2960, 3382), new Tile(2964, 3385),	new Tile(2966, 3389), new Tile(2965, 3393),	new Tile(2967, 3398), new Tile(2968, 3402),	new Tile(2971, 3407), new Tile(2974, 3410),	new Tile(2977, 3413), new Tile(2981, 3416),	new Tile(2984, 3418), new Tile(2987, 3420),	new Tile(2988, 3424), new Tile(2988, 3429),	new Tile(2986, 3433), new Tile(2983, 3436),	new Tile(2981, 3441),new Tile(2980, 3445), new Tile(2977, 3449),	new Tile(2976, 3453), new Tile(2973, 3457),	new Tile(2972, 3462),new Tile(2973, 3467),new Tile(2974, 3470),new Tile(2972, 3475),	new Tile(2973, 3478),new Tile(2975, 3481),new Tile(2976, 3485),new Tile(2978, 3487),new Tile(2982, 3491), new Tile(2980, 3500), new Tile(2980, 3503),	new Tile(2979, 3508), new Tile(2980, 3513)};
						path = fallyWestMindPath;
						break;
				}
	
				break;
				
			case WATER:
				altarTile = new Tile(2718, 4835);
				portalTile = new Tile(2726, 4832);
				
				switch(chosenBank){
					case DRAYNOR:
						Tile[] draynorWaterPath = {new Tile(3093, 3243),new Tile(3093, 3246),new Tile(3095, 3248),new Tile(3099, 3245),new Tile(3101, 3342),new Tile(3101, 3239),new Tile(3102, 3236),new Tile(3104, 3234),new Tile(3108, 3233),new Tile(3111, 3230),new Tile(3114, 3228),new Tile(3119, 3227),new Tile(3122, 3224),new Tile(3125, 3221),new Tile(3129, 3220),new Tile(3132, 3219),new Tile(3136, 3217),new Tile(3140, 3215),new Tile(3141, 3211),new Tile(3142, 3208),new Tile(3141, 3199),new Tile(3145, 3195),new Tile(3147, 3191),new Tile(3151, 3186),new Tile(3153, 3182),new Tile(3157, 3178),new Tile(3160, 3175),new Tile(3162, 3171),new Tile(3166, 3168),new Tile(3170, 3166),new Tile(3175, 3165),new Tile(3179, 3165),new Tile(3183, 3165)};
						path = draynorWaterPath;
						break;
				}
				
				break;
			
			case LAW:
				altarTile = new Tile(2462, 4830);
				portalTile = new Tile(2464, 4817);
				
				switch(chosenBank){
					case DRAYNOR:
						Tile[] draynorLawPath = {};
						path = draynorLawPath;
						break;
				}
				
				break;
		
		}
	}
	
	private void waitPath(Tile tile){
		Timer timer = new Timer(Random.nextInt(2000, 3000));
		while(timer.isRunning()){
			if(tile.distance() <= Random.nextInt(3, 6)){
				break;
			}
			Methods.sleep(100, 200);
			if(Players.getLocal().isMoving()){
				timer.reset();
			}
		}
	}
	
	private boolean wildyWarningScreen(){
		return Widgets.getComponent(WARNING_SCREEN_ID, ENTER_WILDY_ID) != null;
	}
	
	private boolean handleWarningScreen(){
		Component enterComp = Widgets.getComponent(WARNING_SCREEN_ID, ENTER_WILDY_ID);
		
		if(enterComp != null){
			Component dontAskComp = Widgets.getComponent(WARNING_SCREEN_ID, DONT_ASK_ID);
			if(dontAskComp != null){
				dontAskComp.click();
			}
			if(enterComp != null){
				enterComp.click();
				return true;
			}
		}
		
		return false;
	}

	private boolean checkDitch(Tile tile){
		GameObject ditch = Objects.getNearest(DITCH_ID);
		if(ditch != null && ditch.isVisible() && ditch.distance() <= 8){
			// Filter out the vertical ditch near mind altar
			int ditchX = ditch.getLocation().getX();
			if(ditchX < 2993 || ditchX > 3036){
				int myY = Players.getLocal().getLocation().getY();
				int tileY = tile.getY();
				int ditchY = ditch.getLocation().getY();
				
				System.out.println("MyY: " + myY + ", tileY: " + tileY + ", ditchY: " + ditchY);
				if((ditchY <= tileY && ditchY >= myY) || (ditchY >= tileY && ditchY <= myY)){
					System.out.println("We need to jump over the ditch!");
					if(ditch.interact("Cross")){
						Timer timer = new Timer(Random.nextInt(3000, 4000));
						while(timer.isRunning()){
							if(wildyWarningScreen()){
								if(handleWarningScreen()){
									timer.reset();
								}
							}
							
							if(!Players.getLocal().isMoving() && Players.getLocal().getLocation().getY() != myY){
								return true;
							}
							
							Methods.sleep(50, 100);
							if(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1){
								timer.reset();
							}
						}
					}
				}
			}
		}
		
		return false;
	}

	private boolean walkPath(Tile[] tiles, boolean reverse){
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		for(Tile tile : tiles){
			tileList.add(tile);
		}
	
		Timer timer = new Timer(Random.nextInt(1500, 2000));
		while(!tileList.isEmpty() && timer.isRunning()){
			if(!reverse){
				// NORMAL PATH
				for(int i = tileList.size()-1; i >= 0; i--){
					Tile tile;
					if(i > tileList.size()-5){
						tile = new Tile(tileList.get(i).getX() + Random.nextInt(-1, 1), tileList.get(i).getY() + Random.nextInt(-1, 1));
					}else{
						tile = new Tile(tileList.get(i).getX() + Random.nextInt(-2, 2), tileList.get(i).getY() + Random.nextInt(-2, 2));
					}

					if(tile.onMinimap() && tile.distance() <= 15){
						checkDitch(tile);
						tile.clickMinimap();
						waitPath(tile);
						if(i == tileList.size()-1){
							System.out.println("Succesfully walked path!");
							while(Players.getLocal().isMoving()){
								Methods.sleep(100, 200);
							}
							return true;
						}else{
							tileList.remove(i);
							timer.reset();
							break;
						}
					}
				}
			}else{
				// REVERSED PATH
				for(int i = 0; i < tileList.size(); i++){
					Tile tile;
					if(i < 5){
						tile = new Tile(tileList.get(i).getX() + Random.nextInt(-1, 1), tileList.get(i).getY() + Random.nextInt(-1, 1));
					}else{
						tile = new Tile(tileList.get(i).getX() + Random.nextInt(-2, 2), tileList.get(i).getY() + Random.nextInt(-2, 2));
					}
					
					if(tile.onMinimap() && tile.distance() <= 15){
						checkDitch(tile);
			
						// Temp running fix
						Keyboard.pressKey((char)KeyEvent.VK_CONTROL);
						tile.clickMinimap();
						Keyboard.releaseKey((char)KeyEvent.VK_CONTROL);
						
						waitPath(tile);
						if(i == 0){
							System.out.println("Succesfully walked path!");
							while(Players.getLocal().isMoving()){
								Methods.sleep(100, 200);
							}
							return true;
						}else{
							tileList.remove(i);
							timer.reset();
							break;
						}
					}
				}
			}
		}

		return false;
	}
		
	public boolean nearRuins(){
		GameObject ruins = Objects.getNearest(chosenAltar.getRuinsID());
		return ruins != null && ruins.isVisible() && ruins.distance() <= 5;
	}
	
	public boolean insideAltar(){
		GameObject[] loaded = Objects.getLoaded();
		int altarID = chosenAltar.getAltarID();
		int portalID = chosenAltar.getPortalID();
		
		for(GameObject obj : loaded){
			int id = obj.getId();
			if(id == altarID || id == portalID){
				return true;
			}
		}

		return false;
	}
	
	public boolean nearAltar(){
		GameObject altar = Objects.getNearest(chosenAltar.getAltarID());
		return altar != null && altar.isVisible() && altar.distance() <= 5;
	}
	
	public boolean nearPortal(){
		GameObject portal = Objects.getNearest(chosenAltar.getPortalID());
		return portal != null && portal.isVisible() && portal.distance() <= 5;
	}
	
	public boolean toBank(){
		return walkPath(path, true);
		//return walkPath(generatePath(true));
	}
	
	public boolean toMysteriousRuins(){
		return walkPath(path, false);
		//return walkPath(generatePath(false));
	}
	
	private void walkTo(Tile tile){
		tile = new Tile(tile.getX() + Random.nextInt(-2, 2), tile.getY() + Random.nextInt(-2, 2));
		Walking.walkTo(tile);
		Methods.sleep(400, 800);
		while(Players.getLocal().isMoving()){
			Methods.sleep(100, 200);
		}
	}
	
	public void toAltar(){
		walkTo(altarTile);
	}
	
	public void toPortal(){
		walkTo(portalTile);
	}
	
	public void toBankFailsafe(){
		walkTo(path[0]);
	}
		
}
