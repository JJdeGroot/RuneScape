package org.obduro.runecrafter.paths;

import org.obduro.runecrafter.Timer;

import bot.script.methods.Methods;
import bot.script.methods.Objects;
import bot.script.methods.Players;
import bot.script.methods.Widgets;
import bot.script.util.Random;
import bot.script.wrappers.Component;
import bot.script.wrappers.GameObject;
import bot.script.wrappers.Tile;

public class MindPathEdge extends RcPath {

	private final int DITCH_ID = 23271,
					  WARNING_SCREEN_ID = 382,
					  ENTER_WILDY_ID = 18,
					  DONT_ASK_ID = 27;
	private final int DITCH_AT_ALTAR_Y = 3522;

	private Tile altarTile, portalTile;
	private Tile[] bankToDitchPath, ditchToDitchPath, ditchToAltarPath;

	public MindPathEdge(){
		altarTile = new Tile(2843, 4832);
		portalTile = new Tile(2841, 4828);
		
		Tile[] path1 = {new Tile(3096, 3494),new Tile(3093, 3497),new Tile(3090, 3502),new Tile(3087, 3504),new Tile(3087, 3508),new Tile(3086, 3512),new Tile(3085, 3515), new Tile(3080, 3516),new Tile(3074, 3517),new Tile(3067, 3518),new Tile(3061, 3518),new Tile(3056, 3518),new Tile(3050, 3518),new Tile(3045, 3520)};
		bankToDitchPath = path1;
		
		Tile[] path2 = {new Tile(3041, 3526),new Tile(3037, 3529),new Tile(3034, 3531),new Tile(3032, 3534),new Tile(3031, 3537),new Tile(3029, 3541),new Tile(3026, 3544),new Tile(3021, 3547),new Tile(3015, 3548),new Tile(3010, 3548),new Tile(3005, 3547),new Tile(3002, 3543),new Tile(2999, 3539),new Tile(2996, 3536),new Tile(2993, 3534),new Tile(2990, 3531),new Tile(2988, 3529),new Tile(2987, 3526),new Tile(2986, 3523)};
		ditchToDitchPath = path2;
		
		Tile[] path3 = {new Tile(2986, 3520),new Tile(2984, 3516)};
		ditchToAltarPath = path3;
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
	
	private boolean handleDitch(){
		GameObject ditch = Objects.getNearest(DITCH_ID);
		if(ditch != null && ditch.isVisible()){
			if(ditch.interact("Cross")){
				Timer timer = new Timer(Random.nextInt(2000, 3000));
				while(timer.isRunning()){
					if(!Players.getLocal().isMoving() && Players.getLocal().getAnimation() != -1){
						return true;
					}
					
					Methods.sleep(50, 100);
					
					if(wildyWarningScreen()){
						if(handleWarningScreen()){
							timer.reset();
						}
					}
					
					Methods.sleep(50, 100);
			
					if(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1){
						timer.reset();
					}
				}
			}
		}
		
		return false;
	}
	
	private double distance(Tile tile1, Tile tile2){
		return Math.sqrt(Math.pow(Math.abs(tile1.getX()-tile2.getX()), 2) + Math.pow(Math.abs(tile1.getY() - tile2.getY()), 2));
	}
	
	private int getNearestPath(){
		Tile myPos = Players.getLocal().getLocation();
		int index = 0;
		double nearest = 10000;
		
		Tile[][] paths = {bankToDitchPath, ditchToDitchPath, ditchToAltarPath};
		for(int i = 0; i < paths.length; i++){
			for(Tile tile : paths[i]){
				double dist = distance(myPos, tile);
				if(dist < nearest){
					nearest = dist;
					index = i;
				}
			}
		}
		
		return index;
	}
	
	private boolean bankToDitch(){
		Tile myPos = Players.getLocal().getLocation();
		if(myPos.getY() < DITCH_AT_ALTAR_Y){
			handleDitch();
		}
		return super.walkPath(bankToDitchPath, false);
	}
	
	private boolean ditchToBank(){
		Tile myPos = Players.getLocal().getLocation();
		if(myPos.getY() > DITCH_AT_ALTAR_Y){
			handleDitch();
		}
		return super.walkPath(bankToDitchPath, true);
	}
	
	private boolean ditchToAltar(){
		Tile myPos = Players.getLocal().getLocation();
		if(myPos.getY() > DITCH_AT_ALTAR_Y){
			handleDitch();
		}
		return super.walkPath(ditchToAltarPath, false);
	}
	
	private boolean altarToDitch(){
		Tile myPos = Players.getLocal().getLocation();
		if(myPos.getY() < DITCH_AT_ALTAR_Y){
			handleDitch();
		}
		return super.walkPath(ditchToAltarPath, true);
	}
	
	private boolean ditchToAltarDitch(){
		Tile myPos = Players.getLocal().getLocation();
		if(myPos.getY() > DITCH_AT_ALTAR_Y){
			handleDitch();
		}
		return super.walkPath(ditchToDitchPath, false);
	}
	
	private boolean ditchToEdgeDitch(){
		Tile myPos = Players.getLocal().getLocation();
		if(myPos.getY() < DITCH_AT_ALTAR_Y){
			handleDitch();
		}
		return super.walkPath(ditchToDitchPath, true);
	}
	
	@Override
	public boolean toRuins() {
		int nearestPath = getNearestPath();
		
		//return super.walkPath(path, false);
		return false;
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
		
		//return super.walkPath(path, true);
		return false;
	}
	
}
