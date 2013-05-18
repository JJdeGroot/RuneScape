package org.obduro.fighter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import bot.script.BotScript;
import bot.script.methods.GroundItems;
import bot.script.methods.Inventory;
import bot.script.methods.Npcs;
import bot.script.methods.Players;
import bot.script.util.Random;
import bot.script.wrappers.Item;
import bot.script.wrappers.NPC;

public class JJsFighter extends BotScript {

	private final int AL_KHARID_WARRIOR_ID = 18,
					  FULL_CAKE_ID = 1892,
					  TWO_THIRDS_CAKE_ID = 1894,
					  ONE_THIRDS_CAKE_ID = 1896,
					  HERB_IDS = 1999;
	private final int[] CAKE_IDS = {FULL_CAKE_ID, TWO_THIRDS_CAKE_ID, ONE_THIRDS_CAKE_ID};
	
	private State state;
	
	
	@Override
	public boolean onStart() {
		System.out.println("JJ's Fighter has been started!");
		return true;
	}
	
	@Override
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.drawString("JJ's Fighter", 10, 95);
		g.drawString("State: " + state, 10, 115);
		g.drawString("Current health: " + Players.getLocal().getCurrentHealth(), 10, 135);
	}
	
	private State getState(){
		if(Players.getLocal().getCurrentHealth() < Random.nextInt(10, 15)){
			return State.EAT_FOOD;
		}else{
			if(Players.getLocal().inCombat()){
				return State.IN_COMBAT;
			}else{
				if(GroundItems.getNearest(HERB_IDS) != null){
					return State.LOOT_HERB;
				}else{
					return State.ATTACK_WARRIOR;
				}
			}
		}
	}
	
	private boolean attackWarrior(){
		NPC[] npcs = Npcs.getLoaded();
		for(NPC npc : npcs){
			if(npc.getId() == AL_KHARID_WARRIOR_ID && npc.getCurrentHealth() > 0 && npc.isVisible()){
				if(npc.interact("Attack")){
					Timer timer = new Timer(Random.nextInt(1500, 2500));
					while(timer.isRunning()){
						if(Players.getLocal().inCombat()){
							return true;
						}
						sleep(100, 200);
						if(Players.getLocal().isMoving()){
							timer.reset();
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean eatFood(){
		Item[] items = Inventory.getItems();
		ArrayList<Item> foodList = new ArrayList<Item>();
		
		for(Item item : items){
			for(int id : CAKE_IDS){
				if(item.getId() == id){
					foodList.add(item);
					break;
				}
			}
		}
		
		if(!foodList.isEmpty()){
			Timer timer = new Timer(Random.nextInt(3000, 4000));
			
			while(timer.isRunning()){
				int r = Random.nextInt(0, foodList.size()-1);
				Item food = foodList.get(r);
				if(food.interact("Eat")){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	@Override
	public int loop() {
		System.out.println("Inside loop");

		state = getState();
		switch(state){
			case ATTACK_WARRIOR:
				attackWarrior();
				break;
				
			case BANK_FOR_FOOD:
				break;
				
			case EAT_FOOD:
				eatFood();
				break;
				
			case IN_COMBAT:
				sleep(200, 400);
				break;
				
			case LOOT_HERB:
				break;
				
			case WALK_TO_WARRIORS:
				break;
		}
		
		
		return Random.nextInt(100, 200);
	}
	
}
