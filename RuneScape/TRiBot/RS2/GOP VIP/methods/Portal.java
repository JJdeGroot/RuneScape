package scripts.jjsgopvip.methods;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.tribot.api.DynamicClicking;
import org.tribot.api.ScreenModels;
import org.tribot.api.Text;
import org.tribot.api.Timing;
import org.tribot.api.types.TextChar;
import org.tribot.api.types.generic.TranslatableString;

import scripts.jjsgopvip.utils.General;
import scripts.jjsgopvip.utils.Task;


public class Portal {
	private final static long portalID = 2067124836L;
	private final static TranslatableString portalString = new TranslatableString("Enter Portal", "Betreten: Portal", "Entrer Portail", "Entrar Portal"),
											waitForPortalString = new TranslatableString("portal", "Portal", "Portail", "portal"),
											waitForOrbsString = new TranslatableString("orbs", "Kugeln", "Orbes", "orbes"),
											waitForPlayerString = new TranslatableString("players", "Mitspieler", "Participants", "jogadores"),
											portalIsOpenString = new TranslatableString("portal", "Portal", "Portail", "Portal");
	
	// Makes an arraylist of characters from a string
	private static ArrayList<Character> stringToCharList(String s){
		ArrayList<Character> result = new ArrayList<Character>();
		for(int i = 0; i < s.length(); i++){
			result.add(s.charAt(i));
		}
		return result;
	}
	
	// Attempts to find certain text in an area
	private static boolean findText(Rectangle r, String text){
		TextChar[] detectedChars = Text.findCharsInArea(r.x, r.y, r.width, r.height, true);
		ArrayList<Character> textChars = stringToCharList(text);
		int matches = 0;
		
		for(TextChar tc : detectedChars){
			for(int i = 0; i < textChars.size(); i++){
				if(textChars.get(i).equals(tc.character)){
					//System.out.println("Detected " + tc.character);
					matches++;
					textChars.remove(i);
					break;
				}
			}
		}
		return matches >= text.length();
	}
	
	// Waits 100 ms for flashing text to appear
	private static boolean waitForText(Rectangle r, String text){
		long t = System.currentTimeMillis();
		
		while(Timing.timeFromMark(t) < 200){
			if(findText(r, text)){
				return true;
			}
			Task.sleep(5, 10);
		}
		
		return false;
	}
	
	// Reads the waiting for portal text
	public static boolean waitForPortalText(){
		return waitForText(new Rectangle(175, 329, 200, 15), waitForPortalString.getTranslation());
	}

	// Reads the waiting for orbs text
	public static boolean waitForOrbsText(){
		return waitForText(new Rectangle(175, 329, 200, 15), waitForOrbsString.getTranslation());
	}
	
	// Reads the waiting for players text
	public static boolean waitForPlayersText(){
		return waitForText(new Rectangle(175, 329, 200, 15), waitForPlayerString.getTranslation());
	}

	// Reads the is the portal open text
	public static boolean isOpen(){
		return waitForText(new Rectangle(208, 259, 140, 16), portalIsOpenString.getTranslation());
	}
	
	// Returns true if the portal is on screen
	public static boolean isVisible(){
		return ScreenModels.find(portalID).length > 0;
	}
	
	// Enters the portal and waits for new area to load
	public static boolean enterPortal(){
		int count = 0;
		while(count < 3){
			if(DynamicClicking.clickScreenModel(General.getScreenModel(portalID), portalString.getTranslation())){
				General.waitUntilNotMoving();
				Task.sleep(6000, 8000);
				return true;
			}
			Task.sleep(50, 100);
			count++;
		}
		return false;
	}

}
