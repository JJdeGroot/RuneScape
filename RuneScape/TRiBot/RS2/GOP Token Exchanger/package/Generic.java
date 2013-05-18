package scripts.jjsgoptokenexchanger;

import java.awt.Point;
import java.util.Random;

import org.tribot.api.EGW;
import org.tribot.api.General;
import org.tribot.api.Player;
import org.tribot.api.ScreenModels;
import org.tribot.api.Text;
import org.tribot.api.Textures;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.TextChar;
import org.tribot.api.types.Texture;
import org.tribot.api.types.generic.CustomRet_0P;

public class Generic {

	private static final int closeID = 15553;

	// Returns a CustomRet_0P of the nearest ScreenModel for DynamicClicking
	public static CustomRet_0P<ScreenModel> getScreenModel(final long... ids){
		return new CustomRet_0P<ScreenModel>() {
            @Override
            public ScreenModel ret() {
                ScreenModel[] models = ScreenModels.findNearest(ids);
                if (models.length > 0){
                	return models[0];
                }else{
                	return null;
                }
            }
        };
	}

	public static void waitUntilNotMoving() {
		General.sleep(1000, 1500);
		while(Player.isMoving() || Player.getAnimation() != -1){
			General.sleep(400, 600);
		}
	}
	
	public static int stringToInt(String s){
		try {
			return Integer.parseInt(s.replaceAll("\\D", ""));
		} catch (final NumberFormatException ignored) {}
		return -1;
	}
	
	public static String getText(int x1, int y1, int width, int height){
		TextChar[] text = Text.findCharsInArea(x1, y1, width, height, true);
		StringBuilder sb = new StringBuilder();
		for(TextChar tc : text){
			sb.append(tc.character);
		}
		return sb.toString();
	}
	
	public static int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	public static boolean walkTo(Point p){
		int x = p.x + Generic.randomRange(-2, 2);
		int y = p.y + Generic.randomRange(-2, 2);
		if(EGW.walkTo(x, y)){
			Generic.waitUntilNotMoving();
			return true;
		}
		return false;
	}
	
	public static boolean closeInterfaces(){
		Texture[] exit = Textures.find(closeID);
		if(exit.length > 0){
			return exit[0].click("Close");
		}
		return false;
	}
	
}
