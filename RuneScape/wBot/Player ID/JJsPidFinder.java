package org.obduro.pid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import bot.script.BotScript;
import bot.script.methods.Players;
import bot.script.util.Random;
import bot.script.wrappers.Player;

public class JJsPidFinder extends BotScript {

	private HashMap<String, Integer> pidList;
	private String filterName;
	private boolean draw = false;
	
	public void paint(Graphics g){
		if(draw && pidList.size() > 0){
			g.setColor(Color.YELLOW);
			
			Player[] loaded = Players.getLoaded();
			Set<String> keys = pidList.keySet();
			for(String key : keys){
				int pid = pidList.get(key);
				for(Player player : loaded){
					if(player.getName().equals(key)){
						Point p = player.getPoint();
						g.drawString("PID: " + pid, p.x-20, p.y);
					}
				}
			}
		}
	}
	
	@Override
	public int loop() {
		Player[] loaded = Players.getLoaded();
		for(Player player : loaded){
			String name = player.getName();
			if(!name.equals(filterName) && !pidList.containsKey(name)){
				int pid = Random.nextInt(1, 2000);
				Collection<Integer> pids = pidList.values();
				while(pids.contains(pid)){
					pid = Random.nextInt(1, 2000);
				}
				pidList.put(name, pid);
			}
		}
		
		System.out.println(pidList);
		
		return Random.nextInt(100, 200);
	}

	@Override
	public boolean onStart() {
		pidList = new HashMap<String, Integer>();
		filterName = Players.getLocal().getName();
		draw = true;
		return true;
	}

	
}
