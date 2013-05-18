package org.obduro.mta;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bot.script.BotScript;
import bot.script.methods.Game;
import bot.script.methods.Methods;

public class JJsMageTrainArena extends BotScript {

	private final int[] settings = Game.getSettings();
	
	@Override
	public boolean onStart() {
		System.out.println("JJ's Mage training arena started!");
		saveSettings();
		return true;
	}
	
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.drawString("JJ's Mage Training Arena", 10, 95);
	}
	
	private void saveSettings(){
		System.out.println("SAVED SETTINGS!");
		try {
			FileWriter fw = new FileWriter(new File("Settings" + System.currentTimeMillis() + ".txt"));
			for(int i = 0; i < settings.length; i++){
				if(settings[i] == -1){
					continue;
				}
				System.out.println("Setting != -1!");
				fw.write("Setting #" + i + ": " + settings[i] + "\r\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while writing...");
			e.printStackTrace();
		}
		
	}
	
	private void compareSettings(){
		int[] newSettings = Game.getSettings();

		System.out.println("Comparing settings");
		for(int i = 0; i < newSettings.length; i++){
			if(newSettings[i] == settings[i]){
				continue;
			}
			System.out.println("Setting #" + i + " changed from " + settings[i] + " to " + newSettings[i]);
		}
				
	}
	
	@Override
	public int loop() {
		System.out.println("Inside loop");
		
		System.out.println("Setting #8: " + settings[8]);
		compareSettings();
		
		return 100;
	}

	

}
