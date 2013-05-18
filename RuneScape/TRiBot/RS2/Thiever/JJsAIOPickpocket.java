package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.tribot.api.ChooseOption;
import org.tribot.api.Constants;
import org.tribot.api.EGW;
import org.tribot.api.Game;
import org.tribot.api.GameTab;
import org.tribot.api.Inventory;
import org.tribot.api.NPCChat;
import org.tribot.api.ScreenModels;
import org.tribot.api.Skills;
import org.tribot.api.Timing;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.Skills.SKILLS;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Thieving", name = "JJ's AIO Pickpocket")
public class JJsAIOPickpocket extends Script implements Painting {
	final Point MSc = new Point(Constants.MSCX, Constants.MSCY);
	
	final long birdID = 300114155L;
	
	final int thievingLvl = Skills.getBaseLevel(SKILLS.THIEVING),
			  constitutionLvl = Skills.getBaseLevel(SKILLS.CONSTITUTION),
			  maxLifepoints = constitutionLvl * 10;
	
	int currentThievingLvl = thievingLvl,
		goal = 99,
	    lifepoints = Game.getHitpoints(),
	    xpGained = 0,
	    xpPer = 10,
	    foodID = 323884,
		hpPercentage = 30,
		knockouts = 0;
	
	long thiefID = 923674497L,
		 startTime = System.currentTimeMillis();
	
	String status = "Fill the GUI in",
		   uptext = "Man";
	
	boolean guiDone = false;
	
	// Creates the GUI
	public void createGUI(){
		// The frame
		final JFrame frame = new JFrame("GUI");
		frame.setSize(155, 235);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// The panel
		JPanel panel = new JPanel();
		
		JLabel idInfo = new JLabel("NPC ID");
		final JTextField idChoice = new JTextField("923674497");
		
		JLabel xpInfo = new JLabel("Xp for thieving");
		final JTextField xpChoice = new JTextField("8");
		
		JLabel uptextInfo = new JLabel("NPC Uptext");
		final JTextField uptextChoice = new JTextField("Man");
	
		JLabel foodInfo = new JLabel("Food ID");
		final JTextField foodChoice = new JTextField("323884");
		
		JLabel hpInfo = new JLabel("Eat percentage");
		final JTextField hpChoice = new JTextField("30");
		
		JLabel goalInfo = new JLabel("Goal Level");
		final JTextField goalChoice = new JTextField("99");
		
		JButton start = new JButton("Start!");

		// Adding action listener to start
		class ClickListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				thiefID = Long.parseLong(idChoice.getText());
				xpPer = Integer.parseInt(xpChoice.getText());
				uptext = uptextChoice.getText();
				foodID = Integer.parseInt(foodChoice.getText());
				hpPercentage = Integer.parseInt(hpChoice.getText());
				goal = Integer.parseInt(goalChoice.getText());
				startTime = System.currentTimeMillis();
						
				guiDone = true;
				
				frame.setVisible(false);
				frame.dispose();
			}
		}
		
		ActionListener listener = new ClickListener();
		start.addActionListener(listener);

        // Adding them to the panel
        panel.add(idInfo);
        panel.add(idChoice);
        panel.add(xpInfo);
        panel.add(xpChoice);
        panel.add(uptextInfo);
        panel.add(uptextChoice);
        panel.add(foodInfo);
        panel.add(foodChoice);
        panel.add(hpInfo);
        panel.add(hpChoice);
        panel.add(goalInfo);
        panel.add(goalChoice);
        panel.add(start);

        // Adding them to the frame
		frame.add(panel);
		
		// Calculating the position
		Window[] windows = Window.getWindows();
		for (int i = 0; i < windows.length; i++){
			String name = windows[i].getName();
			if(name.contains("frame")){
				Point loc = windows[i].getLocation();
				Point P = new Point(loc.x + 190, loc.y + 200);
				frame.setLocation(P);
				break;
			}
		}

		frame.setVisible(true);
	}

	// Draws the progress report on the screen
	public void onPaint(Graphics g){
		// DRAWING GENERAL STUFF
		// Filling the chatbox
		g.setColor(new Color(44, 38, 32));
		g.fillRect(7, 395, 506, 128);
		
		// Drawing JJ's AIO Pickpocketer
		g.setColor(new Color(209, 0, 0));
		g.setFont(new Font("Segoe Print", Font.BOLD, 26));
		g.drawString("JJ's AIO Pickpocketer", 229, 417);
		
		// Drawing time running & status
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", Font.PLAIN, 12));
		g.drawString("Running for " + Timing.msToString(Timing.timeFromMark(startTime)), 10, 412);
		g.drawString("Status: " + status, 10, 432);

		
		// THIEVING RELATED STUFF
		// Drawing a red bar
		g.setColor(new Color(209, 0, 0));
		g.fillRect(195, 409, 15, 100);
		
		// Drawing a green bar to show the thieving progress
		g.setColor(new Color(21, 181, 0));
		double thiefPercent = (double) currentThievingLvl / goal;
		int y = (int) (thiefPercent * 100);
		g.fillRect(195, 509-y, 15, y);
		
		// Drawing the levels
		g.setColor(Color.WHITE);
		g.drawString("Starting thieving level: " + thievingLvl, 10, 452);
		g.drawString("Current thieving level: " + currentThievingLvl, 10, 472);
		g.drawString("Levels gained: " + (currentThievingLvl-thievingLvl), 10, 492);
		int xpHr = (int) (xpGained * (3600000 / Timing.timeFromMark(startTime)));
		g.drawString("Xp gained: " + xpGained + " (" +  xpHr + "/h)" , 10, 512);

		// Drawing the goal levels
		g.drawString(Integer.toString(goal), 194, 405);
		g.drawString(Integer.toString(currentThievingLvl), 194, 521);
		
		// Drawing the percentage VERTICAL
		Graphics2D g2D = (Graphics2D)g;
	    AffineTransform fontAT = new AffineTransform();
	    Font theFont = g2D.getFont();
	    fontAT.rotate(270 * Math.PI/180);
	    Font theDerivedFont = theFont.deriveFont(fontAT);
	    g2D.setFont(theDerivedFont);
	    int thiefPercentage = (int) (thiefPercent * 100);
	    g2D.drawString(String.valueOf(thiefPercentage) + "%", 206, 472);
	    g2D.setFont(theFont);

	    
		// HEALTH RELATED STUFF
		// Drawing a green bar
		g.setColor(new Color(21, 181, 0));
		g.fillRect(355, 504, 150, 15);
		
		// Drawing the red parts to display our health
		g.setColor(new Color(209, 0, 0));
		double hpPercent = (double) lifepoints / maxLifepoints;
		int x = (int) (hpPercent * 150);
		g.fillRect(355+x, 504, 150-x, 15);
		
		// Drawing health related details
		g.setColor(Color.WHITE);
		g.drawString("Constitution level: " + constitutionLvl, 355, 458);
		g.drawString("Maximum lifepoints: " + maxLifepoints, 355, 478);
		g.drawString("Current lifepoints: " + lifepoints, 355, 498);
		int hpPercentage = (int) (hpPercent * 100);
		g.drawString(String.valueOf(hpPercentage) + "%", 420, 516);
	}

	// Waits until we are not moving anymore based on our positions.
	public void waitUntilNotMoving(){
		status = "Moving...?";
		
		sleep(500, 1000);
		int matches = 0;
		while(matches <= 5){
			Point oldPos = EGW.getPosition();
			sleep(100, 200);
			Point newPos = EGW.getPosition();

			double difference = Math.abs(oldPos.x-newPos.x) + Math.abs(oldPos.y-newPos.y);
			if(difference > 0){
				matches = 0;
			}else{
				matches++;
			}
		}
	}
	
	// Calculates the difference between two points using the Pythagorean theorem.
	public double getDifference(Point P1, Point P2){
		return Math.sqrt(Math.pow(Math.abs(P1.x-P2.x), 2) + Math.pow(Math.abs(P1.y-P2.y), 2));
	}
	
	// Returns a random number
	private int randomRange(int aFrom, int aTo){
		int number = (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    	return number;
    }
	
	// Removes the choose option menu if visible
	private void fixOption(){
    	while (ChooseOption.getPosition() != null){
    		Point MP = new Point(Mouse.getPos());
    		Mouse.move(MP.x + randomRange(-50, 50), MP.y + randomRange(-50, 50));
    	}
	}
	
	// Checks if we have been knocked out
	private boolean handleKnockout(){
		status = "Checking knockout";
		
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < randomRange(750, 1250)){
			ScreenModel[] birds = ScreenModels.findNearest(birdID);
			if(birds.length > 0){
				Point P = birds[0].base_point;
				double d = getDifference(MSc, P);
				if(d <= 15){
					status = "We are knockout";
					sleep(2000, 3000);
					return true;
				}
			}
			sleep(10, 20);
		}
		return false;
	}
	
	// Checks if we need to eat
	private boolean needFood(){
		int lifepoints = Game.getHitpoints();
		double percent = (double) lifepoints / maxLifepoints,
			   r = (double) hpPercentage / 100;
	
		return percent < r;
	}
	
	// Eats food from your inventory
	private boolean eatFood(){
		status = "Eating food";
		
		if (GameTab.getOpen() != TABS.INVENTORY){
			GameTab.open(TABS.INVENTORY);
			sleep(400, 600);
		}
	
		InventoryItem[] food = Inventory.find(foodID);
		int L = food.length;
		
		if(L > 0){
			int r = randomRange(0, L-1);
			Point P = new Point(food[r].x+15+randomRange(-15, 15), food[r].y+15+randomRange(-15, 15));
			Mouse.move(P);
			Mouse.click(1);
			sleep(1500, 2000);
			return true;
		}
		
		return L > 0;
	}
	
	// Updates our thieving level based on the chat
	private void updateThievingLvl(){
		String[] chatbox = NPCChat.getOptions();
		for(int i = chatbox.length-1; i >= 0; i--){
			if(chatbox[i].contains("You've just advanced a Thieving level! You have reached level ")){
				String lvlString = chatbox[i].replaceAll("\\D+","");
				if(lvlString.length() > 0){
					int lvl = Integer.parseInt(lvlString);
					if(lvl > currentThievingLvl){
						currentThievingLvl = lvl;
					}
					break;
				}
				
			}
		}
	}
	
	// Updates our lifepoints
	private void updateLifepoints(){
		lifepoints = Game.getHitpoints();
	}

	// Pickpockets the chosen NPC
	private boolean doPickpocket(){
		status = "Pickpocketing";
		
		fixOption();
		
		ScreenModel[] target = ScreenModels.findNearest(thiefID);
		if(target.length> 0){
			for(int i = 0; i < target.length; i++){
				int L = target[i].points_x.length;
				int r = randomRange(0, L-1);
				
				Point P = new Point(target[i].points_x[r], target[i].points_y[r]);
				Mouse.move(P);
				if(Timing.waitUptext(uptext, 250)){
					Mouse.click(3);
					if(Timing.waitChooseOption("Pickpocket", 500)){
						waitUntilNotMoving();
						if(!handleKnockout()){
							xpGained += xpPer;
						}else{
							knockouts++;
						}
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	// Message when you stop the script
	public void onStop(){
		println("Thanks for using JJ's AIO Pickpocketer");
		println("Script ran for " + Timing.msToString(Timing.timeFromMark(startTime)));
		int xpHr = (int) (xpGained * (3600000 / Timing.timeFromMark(startTime)));
		println("You gained " + xpGained + " xp at " + xpHr + " per hour");
		println("You pickpocketed " + xpGained/xpPer + " times");
		println("You went knockout " + knockouts + " times");
	}

	// Mainloop
	@Override
	public void run() {
		java.awt.EventQueue.invokeLater(new Runnable(){
   			@Override
			public void run() {
				createGUI();
			}
	   	});
		
		while(!guiDone){
			sleep(50, 100);
		}
		
		boolean loop = true;
		Mouse.setSpeed(125);
		
		while(loop){
			updateThievingLvl();
			updateLifepoints();
			
			if(currentThievingLvl >= goal){
				println("You have reached your goal");
				loop = false;
			}
			
			if(needFood()){
				if(!eatFood()){
					println("You ran out of food");
					loop = false;
				}
			}
			
			doPickpocket();
		}
		
		onStop();
	}
}