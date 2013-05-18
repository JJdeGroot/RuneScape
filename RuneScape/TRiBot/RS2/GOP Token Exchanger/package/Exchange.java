package scripts.jjsgoptokenexchanger;

import java.awt.Point;
import java.awt.Rectangle;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.NPCChat;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.Texture;

public class Exchange {

	private static final Point elrissLoc = new Point(1692, 5465);
	private final static long elrissID = 903918791;
	
	private static final  Talisman[][] layout = 
		{{Talisman.AIR, Talisman.EARTH, Talisman.COSMIC, Talisman.LAW},
		 {Talisman.MIND, Talisman.FIRE, Talisman.CHAOS},
		 {Talisman.WATER, Talisman.BODY, Talisman.NATURE}};

	private static final int startX = 43, startY = 140, 
							 width = 34, height = 28,
							 horizontal = 141, vertical = 42;
	
	private static final int shopScreenID = 62615,
							 exitShopID = 15553;
	private static final Rectangle confirmRectangle = new Rectangle(176, 334, 165, 40);

	public static boolean walkToElriss(){
		return Generic.walkTo(elrissLoc);
	}
	
	private static Rectangle getRectangle(Talisman talisman){
		for(int x = 0; x < layout.length; x++)
			for(int y = 0; y < layout[x].length; y++)
				if(talisman.equals(layout[x][y])){
					System.out.println("Talis man is on spot: [" + x + "[]" + y + "]");
					return new Rectangle(startX+x*horizontal, startY+y*vertical, width, height);
				}
	
		return null;
	}
	
	private static boolean shopScreen(){
		return Textures.find(shopScreenID).length > 0;
	}
	
	private static boolean waitShopScreen(int time){
		long t = System.currentTimeMillis();
		
		while(Timing.timeFromMark(t) < time){
			if(shopScreen()){
				return true;
			}
			General.sleep(50, 100);
		}
		
		return false;
	}
	
	public static boolean interactElriss(){
		if(DynamicClicking.clickScreenModel(Generic.getScreenModel(elrissID), "Exchange")){
			Generic.waitUntilNotMoving();
			return true;
		}
		return false;
	}
	
	private static boolean selectTalisman(Talisman talisman){
		if(shopScreen()){
			Rectangle r = getRectangle(talisman);
			System.out.println("Rectangle: " + r);
			Mouse.clickBox(r.x, r.y, r.x+r.width, r.y+r.height, 3);
			if(Timing.waitChooseOption("Buy X", 1000)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean enterAmount(int amount){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < 3000){
			String msg = NPCChat.getMessage();
			if(msg.contains("Enter amount")){
				Keyboard.typeSend(String.valueOf(amount));
				return true;
			}
			General.sleep(50, 100);
		}
		return false;
	}

	private static int getTokens(){
		return Generic.stringToInt(Generic.getText(210, 351, 100, 9));
	}
	
	private static String getTalismanName(){
		return Generic.getText(210, 364, 100, 9);
	}
	
	private static int getTalismanAmount(){
		return Generic.stringToInt(getTalismanName());
	}
	
	public static void getDetails(){
		System.out.println("Tokens: " + getTokens());
		System.out.println("Talisman name: " + getTalismanName());
		System.out.println("Amount: " + getTalismanAmount());
	}
	
	private static boolean clickConfirm(){
		if(shopScreen()){
			Mouse.clickBox(confirmRectangle.x, confirmRectangle.y, confirmRectangle.x+confirmRectangle.width, confirmRectangle.y+confirmRectangle.height, 1);
			return true;
		}
		return false;
	}
	
	private static boolean getShopScreen(){
		int attempt = 0;
		while(attempt < 4){
			if(shopScreen()){
				return true;
			}else{
				if(interactElriss()){
					Generic.waitUntilNotMoving();
					if(waitShopScreen(3000)){
						System.out.println("Shop screen!");
						return true;
					}
				}
			}
			attempt++;
		}
	
		return false;
	}
	
	private static boolean closeShop(){
		Texture[] exit = Textures.find(exitShopID);
		if(exit.length > 0){
			return exit[0].click("Close");
		}
		return false;
	}
	
	public static boolean buyTalismans(Talisman talisman){
		if(getShopScreen()){
			if(selectTalisman(talisman)){
				int tokens = getTokens();
				System.out.println("Tokens left: " + tokens);
				enterAmount(27);
				
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < 3000){
					if(getTalismanName().contains("talisman")){
						clickConfirm();
						closeShop();
						return true;
					}
				}
			}
		}
		return false;
	}

}
