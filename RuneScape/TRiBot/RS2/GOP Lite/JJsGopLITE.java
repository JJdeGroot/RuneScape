package scripts;

import java.awt.Color;
import java.awt.Graphics;

import org.tribot.api.Camera;
import org.tribot.api.Chat;
import org.tribot.api.Game;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.jjsgoplite.enums.Location;
import scripts.jjsgoplite.enums.State;
import scripts.jjsgoplite.enums.Wizard;
import scripts.jjsgoplite.methods.Attract;
import scripts.jjsgoplite.methods.Lobby;
import scripts.jjsgoplite.methods.Portal;
import scripts.jjsgoplite.methods.RuneCraft;
import scripts.jjsgoplite.methods.Walking;
import scripts.jjsgoplite.utils.General;
import scripts.jjsgoplite.utils.Task;

@ScriptManifest(authors = { "J J" }, category = "Minigames", name = "JJ's GOP -LITE-")
public class JJsGopLITE extends EnumScript<State> implements Painting {
	private final long startTime = System.currentTimeMillis();
	
	private long lastTokensGainedTime = 0;
	private int tokensGained = 0;
	private boolean needSetup = true;
	
	private State scriptState = State.JOIN_TEAM;
	private Location myLoc = Location.LOBBY;

	@Override
	public void onPaint(Graphics g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(5, 80, 170, 105);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Gop -LITE-", 10, 95);
		g.drawString("Running for: " + Timing.msToString(System.currentTimeMillis()-this.startTime), 10, 115);
		g.drawString("State: " + this.scriptState, 10, 135);
		g.drawString("Location: " + this.myLoc, 10, 155);
		g.drawString("Tokens gained: " + this.tokensGained, 10, 175);
	}
	
	@Override
	public State getInitialState() {
		Mouse.setSpeed(150);
		return State.JOIN_TEAM;
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
						if(chatLine.contains("tokens")){
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

	@Override
	public State handleState(State state) {
		// For the paint
		this.scriptState = state;
		this.myLoc = Task.getLocation();

		// Checking if we need to set the pitch up
		if(this.needSetup){
			Camera.setPitch(true);
			Game.closeSOFWidget();
			this.needSetup = false;
		}

		// Handling all the states
		switch(state){

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
					Lobby.joinTeam(Wizard.WIZARD);
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
				
			case LOGGED_OUT:
				println("You are not logged in anymore...");
				println("The LITE version does not support restarting or logging in.");
				Task.sleep(30000, 60000);
				break;
				
		}
		
		General.handleRun();

		Task.sleep(100, 200);

		return Task.getState();
	}
}