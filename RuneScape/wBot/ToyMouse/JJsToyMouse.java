package org.obduro.toymouse;

import java.awt.Color;
import java.awt.Graphics;

import bot.script.BotScript;
import bot.script.methods.Inventory;
import bot.script.methods.Npcs;
import bot.script.methods.Players;
import bot.script.util.Random;
import bot.script.wrappers.Item;
import bot.script.wrappers.NPC;

public class JJsToyMouse extends BotScript {

	// Drunken dwarf: 956
	// Niles 2537
	private final int UNWINDED_MOUSE = 7768,
					  WINDED_MOUSE = 7770,
					  MOUSE_ON_FLOOR = 3597;
	private State scriptState;

	@Override
	public boolean onStart() {
		System.out.println("JJ's Toy Mouse started!");
		scriptState = getState();
		return true;
	}
	
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.drawString("JJ's Toy Mouse", 10, 95);
		g.drawString("State: " + scriptState, 10, 115);
		g.drawString("Mouses on floor: " + mousesOnFloor(), 10, 135);
		g.drawString("All mouses winded: " + allMousesWinded(), 10, 155);
	}
	
	private enum State{
		WIND_MOUSES, RELEASE_MOUSES, PICK_UP_MOUSES;
	}
	
	private State getState(){
		if(mousesOnFloor()){
			return State.PICK_UP_MOUSES;
		}else{
			if(allMousesWinded()){
				return State.RELEASE_MOUSES;
			}else{
				return State.WIND_MOUSES;
			}
		}
	}
	
	private boolean mousesOnFloor(){
		return Npcs.getNearest(MOUSE_ON_FLOOR) != null;
	}
	
	private boolean allMousesWinded(){
		return Inventory.getCount(WINDED_MOUSE, true) == (Inventory.getCount(UNWINDED_MOUSE, true) + Inventory.getCount(WINDED_MOUSE, true));
	}
	
	private boolean lootMouses(){
		NPC toyMouse = Npcs.getNearest(MOUSE_ON_FLOOR);
		if(toyMouse != null && toyMouse.isVisible()){
			if(toyMouse.interact("Toy Mouse")){
				sleep(200, 400);
				while(Players.getLocal().isMoving()){
					sleep(100, 200);
				}
				return true;
			}
		}

		return false;
	}
	
	private boolean windMouses(){
		Item[] items = Inventory.getItems();
		for(Item item : items){
			if(item.getId() == UNWINDED_MOUSE){
				if(item.interact("Wind")){
					int count = Inventory.getCount(UNWINDED_MOUSE, true);
					Timer timer = new Timer(Random.nextInt(1000, 2000));
					while(timer.isRunning()){
						if(Inventory.getCount(UNWINDED_MOUSE, true) != count){
							return true;
						}
						sleep(50, 100);
					}
					
				}
			}
		}
		return allMousesWinded();
	}
	
	private boolean releaseMouses(){
		Item[] items = Inventory.getItems();
		for(Item item : items){
			if(item.getId() == WINDED_MOUSE){
				if(item.interact("Release")){
					sleep(50, 100);
				}
			}
		}
		return Inventory.getCount(UNWINDED_MOUSE, true) + Inventory.getCount(WINDED_MOUSE, true) == 0;
	}
	
	
	@Override
	public int loop() {
		System.out.println("Inside loop");
				
		scriptState = getState();
		switch(scriptState){
			case PICK_UP_MOUSES:
				if(lootMouses()){
					sleep(50, 100);
				}
				break;
				
			case RELEASE_MOUSES:
				releaseMouses();
				break;
				
			case WIND_MOUSES:
				windMouses();
				break;
		
		}
		
		return Random.nextInt(50, 100);
	}

	

}
