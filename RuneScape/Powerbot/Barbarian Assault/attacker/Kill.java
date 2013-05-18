package org.obduro.barbassault.attacker;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class Kill extends Node {
	
	private final int[] rangerIDs = {5231, 5232, 5233, 5234, 5235, 5236, 5237},
						fighterIDs = {5214, 5215, 5216, 5217, 5218, 5219},
						monsterIDs = {5231, 5232, 5234, 5235, 5236, 5237, // Ranger
								      5214, 5215, 5217, 5218, 5219}; // Fighter
	private final int queenSpawnID = 5248, 
					  queenID = 5247;
	
	
	@Override
	public boolean activate() {
		System.out.println("Kill -> Attempting to activate");
		return onScreen();
	}
	
	public boolean onScreen(){
		NPC monster = NPCs.getNearest(monsterIDs);
		return monster != null && monster.isOnScreen();
	}
	
	public boolean navigate(){
		NPC[] monsters = NPCs.getLoaded(monsterIDs);
		if(monsters != null && monsters.length > 0){
			for(NPC monster : monsters){
				if(monster.getLocation().clickOnMap()){
					System.out.println("Clicked monster location on map");
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean attack(){
		/*
		NPC monster = NPCs.getNearest(monsterIDs);
		if(monster != null && monster.isOnScreen()){
			monster.interact("Attack");
			return true;
		}
		*/
		NPC[] monsters = NPCs.getLoaded(monsterIDs);
		if(monsters != null){
			for(NPC monster : monsters){
				if(monster.getHealthPercent() > 0 && monster.isOnScreen()){
					if(monster.interact("Attack")){
						Timer timer = new Timer(1000);
						while(timer.isRunning() && monster != null && monster.getHealthPercent() > 0 && monster.isOnScreen()){
							if(Players.getLocal().isMoving()){
								timer.reset();
							}
							Task.sleep(50, 100);
						}
						return true;
					}
				}
			}
		}

		return false;
	}
	
	@Override
	public void execute() {
		System.out.println("Kill node activated!");
		
		if(onScreen()){
			attack();
		}
	}

	
	
	
}
