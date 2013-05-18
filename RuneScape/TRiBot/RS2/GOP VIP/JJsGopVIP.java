package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tribot.api.Camera;
import org.tribot.api.Chat;
import org.tribot.api.Game;
import org.tribot.api.Login;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.TranslatableString;
import org.tribot.api.util.Restarter;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Painting;

import scripts.jjsgopvip.enums.Location;
import scripts.jjsgopvip.enums.State;
import scripts.jjsgopvip.enums.Wizard;
import scripts.jjsgopvip.gui.GUI;
import scripts.jjsgopvip.methods.Attract;
import scripts.jjsgopvip.methods.Lobby;
import scripts.jjsgopvip.methods.Portal;
import scripts.jjsgopvip.methods.RuneCraft;
import scripts.jjsgopvip.methods.Walking;
import scripts.jjsgopvip.utils.General;
import scripts.jjsgopvip.utils.Task;

@ScriptManifest(authors = { "J J" }, category = "Minigames", name = "JJ's GOP *VIP*")
public class JJsGopVIP extends EnumScript<State> implements Painting, Arguments {
	
	private final TranslatableString tokenString = new TranslatableString("tokens", "Marken", "Jetons", "fichas");
	private final GUI gui = new GUI();
	
	private long startTime = System.currentTimeMillis();
	private long lastTokensGainedTime = 0;
	private int tokensGained = 0;
	private Wizard chosenWizard = Wizard.WIZARD;
	private boolean needSetup = true;
	private boolean guiFinished = false;

	private State scriptState = State.GUI;
	private Location myLoc = Location.LOBBY;

	@Override
	public void passArguments(HashMap<String, String> arguments) {
		println("Pass arguments has been activated!");
		
		if(arguments.containsKey("autostart")){
			String s = arguments.get("autostart");
			println("Restarter arguments: " + s);
			
			// Getting original start time
			Pattern startTimePattern = Pattern.compile("StartTime=((\\d+))");
			Matcher startTimeMatcher = startTimePattern.matcher(s);
			if(startTimeMatcher.find()){
				this.startTime = Long.parseLong(startTimeMatcher.group(1));
			}
			
			// Getting tokens gained so far
			Pattern tokenPattern = Pattern.compile("TokensGained=((\\d+))");
			Matcher tokenMatcher = tokenPattern.matcher(s);
			if(tokenMatcher.find()){
				this.tokensGained = Integer.parseInt(tokenMatcher.group(1));
			}
			
			// Getting chosen team
			Pattern wizardPattern = Pattern.compile("ChosenWizard=(.*?)_");
			Matcher wizardMatcher = wizardPattern.matcher(s);
			if(wizardMatcher.find()){
				String chosenTeam = wizardMatcher.group(1).toLowerCase();
				if(chosenTeam.contains("vief")){
					this.chosenWizard = Wizard.VIEF;
				}else if(chosenTeam.contains("acantha")){
					this.chosenWizard = Wizard.ACANTHA;
				}else{
					this.chosenWizard = Wizard.WIZARD;
				}
			}
			
			// Setting needSetup to true
			this.needSetup = true;
			this.guiFinished = true;
			
			// Printing out results
			println("StartTime: " + this.startTime);
			println("TokensGained: " + this.tokensGained);
		}
	}
	
	@Override
	public void onPaint(Graphics g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(5, 80, 170, 105);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Gop *VIP*", 10, 95);
		g.drawString("Running for: " + Timing.msToString(System.currentTimeMillis()-this.startTime), 10, 115);
		g.drawString("State: " + this.scriptState, 10, 135);
		g.drawString("Location: " + this.myLoc, 10, 155);
		g.drawString("Tokens gained: " + this.tokensGained, 10, 175);
	}
	
	@Override
	public State getInitialState() {
		Mouse.setSpeed(150);
		return State.GUI;
	}
	
	// Updates the tokens, only if the last token update time was more than 10 mins ago
	private void updateTokens(){
		if((System.currentTimeMillis() - this.lastTokensGainedTime) > 600000){
			if(Walking.getLocation().equals(Location.LOBBY)){
				String[] chatLines = Chat.getChatLines();
				if(chatLines.length > 0){
					// Looping from bottom to top to find the latest gained tokens
					for(int i = chatLines.length-1; i >= 0; i--){
						String chatLine = chatLines[i];
						// Checking if it contains the token string
						if(chatLine.contains(tokenString.getTranslation())){
							try {
								int tokens = Integer.parseInt(chatLine.replaceAll("\\D", ""));
								if(tokens > 0 && tokens <= 2000){
									println("We have gained: " + tokens + " tokens this round!");
									this.tokensGained += tokens;
									this.lastTokensGainedTime = System.currentTimeMillis();
									println("Total tokens gained: " + this.tokensGained);
									break;
								}
							} catch (final NumberFormatException ignored) {}
						}
					}
				}
			}
		}
	}
	
	// Handles the GUI
	private void handleGUI(){
		if(!guiFinished){
			// Setting up GUI
			java.awt.EventQueue.invokeLater(new Runnable(){
				@Override
				public void run() {
					gui.create();
				}
	    	});
			
			while(!gui.isFinished()){
				Task.sleep(250, 750);
			}
			
			String chosenTeam = gui.getChosenTeam();
			if(chosenTeam.contains("Green")){
				chosenWizard = Wizard.ACANTHA;
			}else if(chosenTeam.contains("Yellow")){
				chosenWizard = Wizard.VIEF;
			}else{
				chosenWizard = Wizard.WIZARD;
			}
	
			println("Chosen wizard: " + chosenWizard);
			guiFinished = true;
		}
	}

	@Override
	public State handleState(State state) {
		// For the paint
		this.scriptState = state;
		this.myLoc = Task.getLocation();

		// Checking if we need to set the pitch up
		if(this.needSetup && Login.getLoginState() == Login.LOGINSTATE.IN_GAME){
			Camera.setPitch(true);
			Game.closeSOFWidget();
			this.needSetup = false;
		}

		// Handling all the states
		switch(state){
		
			case GUI:
				handleGUI();
				break;

			case LOGIN: 
				if(!Login.login() && !Login.login()){
					println("Failed to login, restarting TRiBot in 3 seconds!");
					Task.sleep(2500, 3500);
					Restarter.restart("_StartTime=" + this.startTime + 
									  "_TokensGained=" + this.tokensGained +
									  "_ChosenWizard=" + this.chosenWizard + "_");
				}else{
					println("Succesfully logged in and started the script!");
					println("Resuming script in 3 seconds!");
					Task.sleep(2500, 3500);
				}
				break;

			case ATTRACT_ORBS:
				int attracts = 0;
				while(attracts < 3 && !Walking.atAltar()){
					if(Attract.attractOrb2()){
						Walking.walkTowardsAltar();
					}
					attracts++;
				}
				Attract.attractOrb2();
				Walking.walkToAltar();
				break;

			case CRAFT_RUNES:
				RuneCraft.craftRunes();
				break;

			case ENTER_PORTAL:
				if(!Portal.isVisible() || !Walking.atPortal()){
					Walking.walkToPortal();
				}
				Portal.enterPortal();
				break;

			case JOIN_TEAM:
				int attempts = 0;
				while(attempts < 3 && !General.inTeam()){
					Lobby.joinTeam(chosenWizard);
					attempts++;
				}
				break;

			case WAIT_FOR_PORTAL:
				if(!Walking.atPortal()){
					println("Not at portal..");
					Walking.walkToPortal();
				}
				updateTokens();
				Task.sleep(500, 1500);
				break;

			case WALK_TO_ORBS:
				Walking.walkToOrbs();
				break;
				
		}
		
		// Checking if gui is finished
		while(!guiFinished){
			Task.sleep(250, 750);
		}
		
		General.handleRun();
		
		Task.sleep(100, 200);

		return Task.getState();
	}
}