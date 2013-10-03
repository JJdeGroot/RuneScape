package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.FileWriter;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.jjsinterfaceexplorer.GUI;

@ScriptManifest(authors = { "J J" }, category = "Tools", name = "JJ's Interface Explorer")
public class JJsInterfaceExplorer extends Script implements Painting {

	private GUI gui;
	private boolean guiIntialized = false;
	
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("JJ's Interface Explorer running!", 10, 20);
	
		if(guiIntialized){
			Rectangle rect = gui.getRSRect();
			if(rect != null && rect.x >= 0 && rect.x+rect.width <= 765 && rect.y >= 0 && rect.y+rect.height <= 503){
				g.setColor(Color.YELLOW);
				g.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
		}
		
	}

	@Override
	public void run() {
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				gui = new GUI();
				gui.setVisible(true);
				guiIntialized = true;
			}
    	});

		while(true){
			sleep(50, 100);
			if(guiIntialized && !gui.isVisible()){
				break;
			}
		}
		
	}

	
	
}
