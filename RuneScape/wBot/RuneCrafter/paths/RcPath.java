package org.obduro.runecrafter.paths;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.obduro.runecrafter.Timer;

import bot.script.methods.Keyboard;
import bot.script.methods.Methods;
import bot.script.methods.Players;
import bot.script.methods.Walking;
import bot.script.util.Random;
import bot.script.wrappers.Tile;

public class RcPath {

	public boolean toRuins(){
		return false;
	}
	
	public boolean toAltar(){
		return false;
	}
	
	public boolean toPortal(){
		return false;
	}
	
	public boolean toBank(){
		return false;
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
	
	public boolean walkPath(Tile[] tiles, boolean reverse){
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
						tile = new Tile(tileList.get(i).getX() + Random.nextInt(-3, 3), tileList.get(i).getY() + Random.nextInt(-3, 3));
					}

					if(tile.onMinimap()){
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
						tile = new Tile(tileList.get(i).getX() + Random.nextInt(-3, 3), tileList.get(i).getY() + Random.nextInt(-3, 3));
					}
					
					if(tile.onMinimap()){
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
	
	public boolean walkTo(Tile tile){
		tile = new Tile(tile.getX() + Random.nextInt(-2, 2), tile.getY() + Random.nextInt(-2, 2));
		Walking.walkTo(tile);
		Methods.sleep(400, 800);
		while(Players.getLocal().isMoving()){
			Methods.sleep(100, 200);
		}
		return tile.distance() <= 5;
	}
	
	
}
