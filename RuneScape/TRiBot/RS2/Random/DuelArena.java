package scripts;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import org.tribot.api.Screen;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Duel Arena", name = "Duel Arena script")
public class DuelArena extends Script{
	public int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	private void typeText(){
		switch(randomRange(0, 4)){
			case 0: Keyboard.typeSend("Boxing 10-17m maxed x2"); break;
			case 1: Keyboard.typeSend("Boxing 10-17m when maxed x2"); break;
			case 2: Keyboard.typeSend("Box 10-17m maxed x2"); break;
			case 3: Keyboard.typeSend("Box 10-17m if maxed x2"); break;
			case 4: Keyboard.typeSend("Boxing 10-17m maxed x2 clean me"); break;
		}
	}
	
	private boolean findChallenge(){
		ColourPoint[] duel = Screen.findColours(new Color(126, 50, 0), 10, 494, 350, 501, new Tolerance(5));
		if (duel.length > 50){
			int i = randomRange(0, duel.length);
			Point DP = new Point(duel[i].point);
			Mouse.move(DP);
			if (Timing.waitUptext("challenge", 1000)){
				Mouse.click(1);
				return true;
				
			}
		}
		return false;
	}
	
	@Override
    public void run() {
    	while (!findChallenge()){
    		typeText();
    	}
    }
}