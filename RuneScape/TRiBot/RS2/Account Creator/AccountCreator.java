package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.tribot.api.Login;
import org.tribot.api.EGW;
import org.tribot.api.ScreenModels;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Private", name = "JJ's Account Creator")
public class AccountCreator extends Script implements Painting{
	int characterNumber = 66;
	String status = "Starting";

	public void onPaint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(3, 54, 250, 70);
		
		g.setColor(Color.WHITE);
		g.drawString("Account number: #" + characterNumber, 10, 70);
		g.drawString("Status: " + status, 10, 90);
		Point p = EGW.getPosition();
		g.drawString("Position: (" + p.x + ", " + p.y + ")", 10, 110);
	}
	
	private boolean selectStyleScreen(){
		return Textures.find(17200).length > 0;
	}
	
	private boolean customiseCharacterScreen(){
		return Textures.find(5171).length > 0;
	}
	
	private boolean characterNameScreen(){
		return Textures.find(21191).length > 0;
	}
	
	private void typeName(){
		status = "Generating name";
		String name = "yu6 7jp " + characterNumber;
		Mouse.clickBox(159, 237, 326, 251, 1);
		sleep(500, 1000);
		status = "Typing name";
		Keyboard.typeString(name);
		sleep(500, 1000);
	}

	private boolean handleSetup(){
		status = "Handling setup of character";
		long t = System.currentTimeMillis();
		
		while(Timing.timeFromMark(t) < 60000){
			if(selectStyleScreen() || customiseCharacterScreen()){
				status = "Confirming";
				// Clicking on confirm
				Mouse.clickBox(627, 473, 717, 491, 1);
				sleep(2000, 3000);
			}
			
			if(characterNameScreen()){
				// Typing a random generated name
				typeName();
				// Clicking on confirm
				status = "Clicking on a name";
				Mouse.clickBox(164, 370, 248, 385, 1);
				sleep(2000, 3000);
				return true;
			}
			
			sleep(2000, 3000);
		}

		return false;
	}
	
	private boolean skipCutscene(){
		status = "Trying to skip cutscene";
		long t = System.currentTimeMillis();
		
		while(Timing.timeFromMark(t) < 15000){
			if(Textures.find(2886).length > 0){
				// Pressing 'Esc' to skip
				Keyboard.pressKey((char) 27);
				sleep(100, 200);
				Keyboard.pressKey((char) 27);
				
				// Waiting a while
				sleep(2000, 3000);
				
				return true;
			}
			
			sleep(2000, 3000);
		}
		
		return false;
	}
	
	private boolean atTutorial(){
		return ScreenModels.find(4390934).length > 0;
	}
	
	private boolean openNoticeboardTab(){
		status = "Clicking on noticeboard tab";
		Mouse.moveBox(562, 223, 577, 247);
		if(Timing.waitUptext("Noticeboard", 1000)){
			Mouse.click(1);
			sleep(1000, 2000);
			return true;
		}
		return false;
	}
	
	private boolean openNoticeboard(){
		if(openNoticeboardTab()){
			status = "Opening noticeboard";
			Mouse.moveBox(565, 260, 680, 274);
			if(Timing.waitUptext("Open Noticeboard", 1000)){
				Mouse.click(1);
				sleep(3000, 5000);
				return true;
			}
		}
		
		return false;
	}
	
	private boolean noticeboardScreen(){
		return Textures.find(28797).length > 0;
	}
	
	private boolean backButtonExists(){
		return Textures.find(40254).length > 0;
	}
	
	private boolean clickOnBack(){
		status = "Clicking on back";
		if(backButtonExists()){
			Mouse.clickBox(685, 217, 737, 231, 1);
			sleep(2000, 3000);
			return true;
		}
		return false;
	}
	
	private boolean optionsButtonExists(){
		return Textures.find(45675).length > 0;
	}
	
	private boolean clickOnOptions(){
		status = "Clicking on a options";
		if(optionsButtonExists()){
			Mouse.clickBox(686, 131, 731, 141, 1);
			sleep(2000, 3000);
			return true;
		}
		return false;
	}
	
	private boolean tutorialOffExists(){
		return Textures.find(9193).length > 0;
	}
	
	private boolean setTutorialOff(){
		status = "Putting tutorial off";
		if(tutorialOffExists()){
			Mouse.clickBox(332, 370, 430, 380, 1);
			sleep(1000, 2000);
			return true;
		}
		return false;
	}
	
	private boolean deadWarriorsScreen(){
		long t = System.currentTimeMillis();
		
		while(Timing.timeFromMark(t) < 2000){
			if(Textures.find(30122).length > 0){
				return true;
			}
			
			sleep(100, 200);
		}
		
		return false;
	}
	
	private boolean confirmOff(){
		status = "Confirming \"Off\"";
		if(deadWarriorsScreen()){
			Mouse.clickBox(227, 453, 298, 462, 1);
			sleep(2000, 3000);
			return true;
		}
		
		return false;
	}

	private boolean skipTutorial(){
		if(atTutorial()){
			openNoticeboard();
			
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < 30000){
				status = "Trying to skip tutorial";
				
				if(backButtonExists()){
					clickOnBack();
				}
				
				if(optionsButtonExists()){
					clickOnOptions();
				}
				
				if(tutorialOffExists()){
					setTutorialOff();
				}
				
				if(!noticeboardScreen()){
					return confirmOff();
				}

				sleep(2000, 3000);
			}
		}
		
		return false;
	}
	
	public boolean mainloop(){
		if(Login.login("rsdeals914+" + characterNumber + "@outlook.com", "spamming", 53)){
			if(handleSetup()){
				if(skipCutscene()){
					if(skipTutorial()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
    public void run() {	
		final AccountWalker walking = new AccountWalker();
		while(characterNumber <= 100){
			if(mainloop()){
				status = "Walking to the Grand Exchange";
				walking.mainloop();
			}
			characterNumber++;
		}
    }
}