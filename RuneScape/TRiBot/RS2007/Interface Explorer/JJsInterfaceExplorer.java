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
		
		System.out.println("Gathering interfaces v4 started");
		RSInterfaceMaster[] masterInterfaces = Interfaces.getAll();
		for(RSInterfaceMaster master : masterInterfaces){
			if(master.getComponentStack() == 1337){
				System.out.println("Master interface #" + master.getIndex() + " is 1337!");
				System.out.println("Master #" + master.getIndex());
			}
			RSItem[] items = master.getItems();
			if(items != null && items.length > 0){
				for(RSItem item : items){
					System.out.println("Master interface #" + master.getIndex() + " item id: " + item.getID());
				}
			}
			RSInterfaceChild[] childs = master.getChildren();
			if(childs != null && childs.length > 0){
				for(RSInterfaceChild child : childs){
					if(child.getComponentStack() == 1337){
						System.out.println("Child interface #" + child.getIndex() + " is 1337!");
						System.out.println("Master #" + master.getIndex() + ", Child #" + child.getIndex());
					}
					RSItem[] items2 = child.getItems();
					if(items != null && items.length > 0){
						for(RSItem item2 : items2){
							System.out.println("Child interface #" + child.getIndex() + " item id: " + item2.getID());
						}
					}
					RSInterfaceComponent[] comps = child.getChildren();
					if(comps != null && comps.length > 0){
						for(RSInterfaceComponent comp : comps){
							if(comp.getComponentStack() == 1337){
								System.out.println("Component #" + comp.getIndex() + " is 1337!");
								System.out.println("Master #" + master.getIndex() + ", Child #" + child.getIndex() + ", Comp #" + comp.getIndex());
							}
							RSItem[] items3 = comp.getItems();
							if(items3 != null && items3.length > 0){
								for(RSItem item3 : items3){
									System.out.println("Component interface #" + comp.getIndex() + " item id: " + item3.getID());
								}
							}
							
						}
						
					}
				}
			}
		}
		System.out.println("Gathering interfaces v4 ended");
		
		while(true){
			sleep(50, 100);
			if(guiIntialized && !gui.isVisible()){
				break;
			}
		}
		
	}

	
	
}
