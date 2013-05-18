package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.EventQueue;

import org.tribot.api.Banking;
import org.tribot.api.ChooseOption;
import org.tribot.api.DTMs;
import org.tribot.api.GameTab;
import org.tribot.api.Inventory;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.BankItem;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.DTM;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Magic", name = "JJ's Superheater V3.1")
public class JJsSuperheater3_2 extends Script implements Painting{
	SuperheatDTMs dtms = new SuperheatDTMs();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* Declaring variables */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ID's/DTM's etc
	final int oreID = 125105,
			  barID = 261751,
			  natureID = 81512,
			  coalbag = 388266;
	final DTM superheatDTM = dtms.superheatDTM(),
			  natureDTM = dtms.natureDTM();
	final DTM[] oreDTMs = dtms.oreDTMs();
	final long startTime = System.currentTimeMillis();
	
	// Other info
	int ores = 2,
		superheats = 0;
	long superheatStart = System.currentTimeMillis(),
		 superheatTime = System.currentTimeMillis() - superheatStart;

	// From the GUI
	final boolean  useCoalbag = true,
			       useGauntlets = true; // true or false
	final String bankMode = "Chest",  // "Banker" , "Bankbooth", "Chest"
	             bar = "Iron",        // "Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune"
	             style = "Fast";	   // "Random", "Fast"

	boolean guiDone = false;
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* On-screen paint */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void onPaint(Graphics g){
        g.setColor(Color.YELLOW);
        g.drawString("JJ's AIO Superheater", 5, 160);
        g.drawString("Running for: " + Timing.msToString(System.currentTimeMillis() - startTime), 5, 175);
        g.drawString("Made " + superheats + " " + bar.toLowerCase() + " bars at " + toHour(superheats) + " per hour", 5, 190);
        g.drawString("Gained " + superheats*53 + " magic experience at " + toHour(superheats*53) + " per hour", 5, 205);
        g.drawString("Gained " + superheats*smithingXp(bar) + " smithing experience at " + toHour(superheats*smithingXp(bar)) + " per hour", 5, 220);

    }
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* GUI */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//SuperheatGUI GUI = new SuperheatGUI();
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* General stuff */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Converts to xp per hour
	private int toHour(double detail){
		long difference = System.currentTimeMillis() - startTime;
		double xpH = (3600000 * detail) / difference;

		return (int)xpH;
	}
	
	private double smithingXp(String bar){
		int number = defineBarNumber(bar);
		double[] xp = {6.5, 12.5, 13.7, 17.5, 22.5, 30, 37.5, 50}; 
		if (useGauntlets){
			if (number == 4){
				return 56.2;
			}
		}
		return xp[number];
	}
	
	// Generates a random number including negative.
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	// Defines the ore by assigning a number
	private int defineOreNumber(String choice){
		String[] oreNames = {"Tin", "Copper", "Iron", "Silver", "Coal", "Gold", "Mithril", "Adamant", "Rune"};
		for (int i = 0; i < oreNames.length; i++){
			if (oreNames[i].contains(choice)){
				return i;
			}
		}
		return -1;
	}
		
	// Defines the bar by assigning a number
	private int defineBarNumber(String choice){
		String[] barNames = {"Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune"};
		for (int i = 0; i < barNames.length; i++){
			if (barNames[i].contains(choice)){
				return i;
			}
		}
		return -1;
	}
	
	private Point[] orePoints(String choice){
		int number = defineOreNumber(choice);
		InventoryItem[] all = Inventory.getAll();
		ArrayList<Point> coordList = new ArrayList<Point>();
		
		if (all.length > 0){
			for (int i = 0; i < all.length; i++){
				Point[] points = DTMs.find_simple(oreDTMs[number], all[i].x-5, all[i].y-5, all[i].x+35, all[i].y+35);
				if (points.length > 0){
					coordList.add(new Point(all[i].x + 15, all[i].y + 15));
				}
			}
			
			if (!coordList.isEmpty()){
				Point[] coords = coordList.toArray(new Point[coordList.size()]); 
				return coords;
			}
		}

		return null;
	}
	
	
	// Returns the amounts of the chosen ore in the inventory.
	private int countOre(String choice){
		Point[] pts = orePoints(choice);
		if (pts != null){
			return pts.length;
		}
		
		return 0;
	}
	
	// Gets the coordinate of the chosen ore in the bank.
	private Point getOrePoint(String choice){
		int number = defineOreNumber(choice);
		Point P = null;
		
		if (number != -1){
			BankItem[] all = Banking.getAll();
			for (int i = 0; i < all.length; i++){
				Point[] match = DTMs.find_simple(oreDTMs[number], all[i].x - 5, all[i].y - 5, all[i].x + 35, all[i].y + 35);
				if (match.length > 0){
					if (match[0].y > 135){
						//println("Found DTM");
						return match[0];
					}else{
						continue;
					}
				}
			}
		}

		return P;
	}
	
	// Gets the nearest point from ids
	private Point getNearestPoint(long id){
		Point MSc = new Point(259, 220);
        ScreenModel[] find = ScreenModels.find(id);
        ArrayList<Integer> distList = new ArrayList<Integer>();
        ArrayList<Point> coordList = new ArrayList<Point>();
        
        if (find.length > 0){
	        for (int i = 0; i < find.length; i++){
                distList.add((int) Math.sqrt(Math.pow(Math.abs(MSc.x - find[i].base_point.x), 2) + Math.pow(Math.abs(MSc.y - find[i].base_point.y), 2)));
                coordList.add(new Point(find[i].base_point.getLocation()));
	        }
	        
	        if (!distList.isEmpty() && !coordList.isEmpty()){
	            int nearest = 1000;
	            int t = 0;
	            
	            for (int i = 0; i < distList.size(); i++){
	                if (distList.get(i) < nearest){
	                    nearest = distList.get(i);
	                    t = i;
	                }
	            }
	
	            return coordList.get(t);
	        }
        }
        
        return null;
    }
		
	// Gets the nearest ID from a list of ids
	private long getNearestID(long[] ids){
		Point MSc = new Point(259, 220);
        ScreenModel[] find = ScreenModels.find(ids);
        ArrayList<Long> idList = new ArrayList<Long>();
        ArrayList<Integer> distList = new ArrayList<Integer>();
        
        if (find.length > 0){
	        for (int i = 0; i < find.length; i++){
                distList.add((int) Math.sqrt(Math.pow(Math.abs(MSc.x - find[i].base_point.x), 2) + Math.pow(Math.abs(MSc.y - find[i].base_point.y), 2)));
                idList.add(find[i].id);
	        }
	        
	        if (!idList.isEmpty() && !distList.isEmpty()){
	            int nearest = 1000;
	            int t = 0;
	            
	            for (int i = 0; i < distList.size(); i++){
	                if (distList.get(i) < nearest){
	                    nearest = distList.get(i);
	                    t = i;
	                }
	            }
	
	            return idList.get(t);
	        }
        }
        
        return -1;
    }

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* Switching tabs */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Switches to the desired Tab
	private boolean FTab(TABS Tab){
		char FKey;
		
		if (GameTab.getOpen() != Tab){
			switch(Tab){
				case INVENTORY: FKey = (char)KeyEvent.VK_F1; break;
				case EQUIPMENT: FKey = (char)KeyEvent.VK_F2; break;
				case PRAYERS: FKey = (char)KeyEvent.VK_F3; break;
				case MAGIC: FKey = (char)KeyEvent.VK_F4; break;
				case COMBAT: FKey = (char)KeyEvent.VK_F5; break;
				default: return false;
			}
			
			if (Character.isDefined(FKey)){
				Keyboard.pressKey(FKey);
			 	sleep(50, 100);
			 	Keyboard.releaseKey(FKey);
			 	waitTab(Tab, 500);
			 	return true;
			}
		}
		
		return false;
	}
	
	// Waits until the tabs are visible again
	private boolean waitTabs(long time){
		long maxTime = System.currentTimeMillis() + time;
		while (System.currentTimeMillis() < maxTime){
			Color yellow = new Color(200, 200, 100);
			Tolerance tol = new Tolerance(75, 75, 75);
			ColourPoint[] count = Screen.findColours(yellow, 522, 219, 763, 254, tol);
			if (count.length > 10){
				return true;
			}
			sleep(50, 100);
		}

		return false;
	}
	
	private void waitTab(TABS Tab, long time){
		long maxTime = System.currentTimeMillis() + time;
		int MIX1 = 555, MIY1 = 255, MIX2 = 720, MIY2 = 480;
		while (System.currentTimeMillis() < maxTime){
			if (Tab == TABS.INVENTORY){
				Point[] findNature = DTMs.find_simple(natureDTM, MIX1, MIY1, MIX2, MIY2);
				if (findNature.length > 0){
					//println("Found nature -> inv tab");
					sleep(200, 300);
					return;
				}
			}
			
			if (Tab == TABS.MAGIC){
				Point[] findSuperheat = DTMs.find_simple(superheatDTM, MIX1, MIY1, MIX2, MIY2);
				if (findSuperheat.length > 0){
					//println("Found superheat -> magic tab");
					sleep(200, 300);
					return;
				}
			}
			
			sleep(10, 20);
		}

		FTab(Tab);
	}
		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* Banking */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	// Opens the bank by using the NPC
	private boolean openBankNPC(){
		long[] NPCs = {964016324L, 186323308L, 2791056588L, 372895788L, 3790880451L, 
					   4001282980L, 1919077414L, 558125369L, 666845606L, 4050299616L, 
					   4222475077L, 901358828L, 3365752498L, 3774985671L, 2129181768L,
					   666845606L, 4222475077L, 4222475077L, 1676840816L};
		
		long ID = getNearestID(NPCs);
		if (ID != -1){
			Point P = getNearestPoint(ID);
			if (P != null){
				Point BP = new Point(P.x + randomRange(-3, 3), P.y - randomRange(0, 6));
				Mouse.move(BP);
				if (Timing.waitUptext("Banker", 500)){
					Mouse.click(3);
					if (Timing.waitChooseOption("Bank ", 500)){
						return true;
					}else{
						Mouse.click(3);
						if (Timing.waitChooseOption("Bank", 500)){
							return true;
						}
					}
				}
				
			}
		}

		return false;
	}
	
	// Opens the bank chest by using the chest
	private boolean openBankChest(){
		long[] chests = {2550191772L, 3702316490L, 1356502326L, 934762440L};

		long ID = getNearestID(chests);
		if (ID != -1){
			Point P = getNearestPoint(ID);
			if (P != null){
				Point CP = new Point(P.x + randomRange(-3, 3), P.y + randomRange(-3, 3));
				Mouse.move(CP);
				if (Timing.waitUptext("chest", 500)){
					Mouse.click(1);
					return true;
				}else{
					Mouse.click(3);
					if (Timing.waitChooseOption("Bank", 500)){
						return true;
					}
				}
			}
		}

		return false;
	}
	
	// Opens the bank by using the bank booth
	private boolean openNormalBank(){
		long[] banks = {4216896244L, 1644377173L, 1570830985L,
					    1644377173L, 3469489886L, 959726307L};
		
		long[] NPCs = {964016324L, 186323308L, 2791056588L, 372895788L, 3790880451L, 
				   4001282980L, 1919077414L, 558125369L, 666845606L, 4050299616L, 
				   4222475077L, 901358828L, 3365752498L, 3774985671L, 2129181768L,
				   666845606L, 4222475077L, 4222475077L, 1676840816L};
	
		long NPCID = getNearestID(NPCs);
		if (NPCID != -1){
			Point PP = getNearestPoint(NPCID);
			if (PP != null){
				long ID = getNearestID(banks);
				if (ID != -1){
					Point P = getNearestPoint(ID);
					if (P != null){
						double xDist = Math.abs(PP.x-P.x);
						double yDist = Math.abs(PP.y - P.y);
						Point BP = null;
						
						if (xDist > yDist){
							BP = new Point(P.x + randomRange(-5, 5), PP.y + randomRange(-3, 3));
						}else{
							BP = new Point(PP.x + randomRange(-3, 3), P.y + randomRange(-5, 5));
						}
						
						if (BP != null){
							Mouse.move(BP);
							if (Timing.waitUptext("Bank", 500)){
								Mouse.click(3);
								if (Timing.waitChooseOption("Bank", 500)){
									return true;
								}else{
									Mouse.click(3);
									if (Timing.waitChooseOption("Bank", 500)){
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}
	
	// Opens the bank by using the chosen bank method
	private boolean openBank(){
		String[] options = {"Bankbooth", "Chest", "Banker"};
		int choice = -1;
		
		for (int i = 0; i < options.length; i++){
			if (options[i].contains(bankMode)){
				choice = i;
				break;
			}
		}
		
		switch(choice){
			case 0: if (openNormalBank()){
				    	return true;
					}
					break;
			case 1: if (openBankChest()){
				    	return true;
					}
					break;
			case 2: if (openBankNPC()){
				    	return true;
					}
					break;
		}
		
		if (waitBankscreen(2000)){
			return true;
		}

		return false;
	}
	
	// Waits until the bankscreen is visible
	private boolean waitBankscreen(long time){
		long maxTime = System.currentTimeMillis() + time;
		while (System.currentTimeMillis() < maxTime){
			if (Banking.isBankScreenOpen()){
				sleep(200, 400);
				return true;
			}
			sleep(10, 50);
		}
		return false;
	}
	
	// Closes the bank
	private boolean closeBank(){
		if (Banking.isBankScreenOpen()){
			if (Banking.closeBankScreen()){
				return true;
			}
		}
		return false;
	}
	
	// Deposits the bars
	private void depositBars(){
		InventoryItem[] bars = Inventory.find(barID);
		if (bars.length > 0){
			int j = randomRange(0, bars.length - 1);
			Point BP = new Point(bars[j].x + 15 + randomRange(-8, 8), bars[j].y + 15 + randomRange(-8, 8));
			Mouse.move(BP);
			if (Timing.waitUptext("bar", 500)){
				Mouse.click(3);
				if (!Timing.waitChooseOption("All", 500)){
					ChooseOption.select("Deposit");
				}
			}
		}
	}
	
	// Deposits the ores
	private void depositOre(String ore){
		Point[] coords = orePoints(ore);
		if (coords != null){
			if (coords.length > 0){
				int r = 0;
				if (coords.length > 1){
					r = randomRange(0, coords.length-1);
				}
				Point P = new Point(coords[r].x + randomRange(-8, 8), coords[r].y + randomRange(-8, 8));
				Mouse.move(P);
				if (Timing.waitUptext("ore", 500)){
					Mouse.click(3);
					if (!Timing.waitChooseOption("All", 500)){
						ChooseOption.select("Deposit");
					}
				}
			}
		}
	}
	
	// Deposits leftover ores that isn't coal
	private boolean depositOres(){
		int number = defineBarNumber(bar);
		//println("Number: " + number);
		if (number != -1){
			switch(number){
				case 0: int tinCount = countOre("Tin"),
						    copperCount = countOre("Copper");
						if (tinCount > 0){
							depositOre("Tin");
						}
						if (copperCount > 0){
							depositOre("Copper");
						}
						break;
				case 3: int ironCount2 = countOre("Iron");
						if (ironCount2 > 0){
							depositOre("Iron");
						}
						break;
				case 5: int mithrilCount = countOre("Mithril");
							if (mithrilCount > 0){
								depositOre("Mithril");
							}
						break;
				case 6: int adamantCount = countOre("Adamant");
						if (adamantCount > 0){
							depositOre("Adamant");
						}
						break;
				case 7: int runeCount = countOre("Rune");
						if (runeCount > 0){
							depositOre("Rune");
						}
						break;
			}
		}
		
		if (number != -1){
			return true;
		}
		
		return false;
	}
	
	
	// Deposits everything that isn't needed	
	private boolean deposit(){
		depositBars();
		depositOres();

		InventoryItem[] all = Inventory.getAll();
		long[] goodIDs = {oreID, barID, natureID, coalbag};
		
		for (int i = 0; i < all.length; i++){
			int correct = 0;
			for (int j = 0; j < goodIDs.length; j++){
				if (all[i].id == goodIDs[j]){
					correct++;
				}
			}
			if (correct == 0){
				Point[] pts =  DTMs.find_simple(natureDTM, 544, 244, 745, 525);
				if (pts == null){
					Point P = new Point(all[i].x + 15 + randomRange(-8, 8), all[i].y + 15 + randomRange(-8, 8));
					Mouse.move(P);
					Mouse.click(1);
					sleep(250, 500);
				}
			}
		}
		
		return true;
	}

	// Withdraws the chosen amount of the chosen ore in the bank.
	private boolean withdrawOre(String choice, int amount){
		Point ore = getOrePoint(choice);
		if (ore != null){
			Mouse.move(new Point(ore.x + randomRange(-10, 10), ore.y + randomRange(-10, 10)));
			Mouse.click(3);
			
			String option;
			if (amount >= 28){
				option = "All";
			}else{
				option = Integer.toString(amount);
			}

			if (!Timing.waitChooseOption(option, 500)){
				//println("We need to Select-X");
				ChooseOption.select("Withdraw-X");
				sleep(1000, 1500);
				Keyboard.typeSend(Integer.toString(amount));
				return true;
			}else{
				//println("Succesfully withdrawed ores");
				return true;
			}
		}
		
		return false;
	}
	
	// Puts 28 coal in the coalbag
	private boolean fillCoalbag(){
		InventoryItem[] bag = Inventory.find(coalbag);
		if (bag.length > 0){
			withdrawOre("Coal", 28);
			
			Point P = new Point(bag[0].x + 15 + randomRange(-10, 10), bag[0].y + 15 + randomRange(-10, 10));
			Mouse.move(P);
			if (Timing.waitUptext("Coal bag", 500)){
				Mouse.click(3);
				if (Timing.waitChooseOption("Fill", 500)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Withdraws ores for bronze bars
	private void withdrawBronze(){
		switch(randomRange(0, 1)){
			case 0: if (withdrawOre("Tin", 13)){
						withdrawOre("Copper", 13);
					}
			        break;
			case 1: if (withdrawOre("Copper", 13)){
						withdrawOre("Tin", 13);
					}
	        		break;
		}
	}
	
	// Withdraws ores for steel bars
	private void withdrawSteel(){
		if (useCoalbag){
			if (fillCoalbag()){
				if (withdrawOre("Iron", 17)){
					int ores = countOre("Coal");
					if (ores > 0){
						depositOre("Coal");
						depositOre("Iron");
						withdrawOre("Iron", 17);
					}
					withdrawOre("Coal", 28);
				}
			}
		}else{
			if (withdrawOre("Iron", 9)){
				  withdrawOre("Coal", 28);
			 }
		}
	}
	
	// Withdraws ores for mith bars
	private void withdrawMithril(){
		if (useCoalbag){
			if (fillCoalbag()){
				if (withdrawOre("Mithril", 10)){
					int ores = countOre("Coal");
					if (ores > 0){
						depositOre("Coal");
						depositOre("Mithril");
						withdrawOre("Mithril", 10);
					}
					withdrawOre("Coal", 28);
				}
			}
		}else{
			 if (withdrawOre("Mithril", 5)){
				 withdrawOre("Coal", 28);
			}
		}
	}
	
	// Withdraws ores for addy bars
	private void withdrawAdamant(){
		if (useCoalbag){
			if (fillCoalbag()){
				if (withdrawOre("Adamant", 7)){
					int ores = countOre("Coal");
					if (ores > 0){
						depositOre("Coal");
						depositOre("Adamant");
						withdrawOre("Adamant", 7);
					}
					withdrawOre("Coal", 28);
				}
			}
		}else{
			if (withdrawOre("Adamant", 3)){
				withdrawOre("Coal", 28);
			}
		}
	}
	
	// Withdraws ores for rune bars
	private void withdrawRune(){
		if (useCoalbag){
			if (fillCoalbag()){
				if (withdrawOre("Rune", 5)){
					int ores = countOre("Coal");
					if (ores > 0){
						depositOre("Coal");
						depositOre("Rune");
						withdrawOre("Rune", 5);
					}
					withdrawOre("Coal", 28);
				}
			}
		}else{
			if (withdrawOre("Rune", 3)){
				 withdrawOre("Coal", 28);
			 }
		}
	}
	
	// Withdraws ores of choice
	private boolean withdrawOres(String choice){
		int number = defineBarNumber(choice);

		switch(number){
			case 0: withdrawBronze(); break;
			case 1: withdrawOre("Iron", 28); break;
			case 2: withdrawOre("Silver", 28); break;
			case 3: withdrawSteel(); break;
			case 4: withdrawOre("Gold", 28); break;
			case 5: withdrawMithril(); break;
			case 6: withdrawAdamant(); break;
			case 7: withdrawRune(); break;
		} 
		
		if (number != -1){
			return true;
		}

		return false;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* Superheating */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Clicks on the superheat icon
	private boolean clickSuperheat(){
		waitTab(TABS.MAGIC, 500);
		int x1 = 555, y1 = 255, x2 = 720, y2 = 480;
		
		Point[] icon = DTMs.find_simple(superheatDTM, x1, y1, x2, y2);
		if (icon.length > 0){
			if (!Timing.waitUptext("Cast superheat", 100)){
				Point SP = new Point(icon[0].x + randomRange(-10, 10), icon[0].y + randomRange(-10, 10));
				Mouse.move(SP);
				Mouse.click(1);
			}else{
				Mouse.click(1);
			}
			waitTab(TABS.INVENTORY, 500);
			return true;
		}
		
		return false;
	}

	// Checks if we have enough ores to superheat
	private int canSuperheat(){
		//String[] barNames = {"Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune"};
		int number = defineBarNumber(bar);

		if (number != -1){
			switch(number){
				case 0: int tinCount = countOre("Tin"),
						    copperCount = countOre("Copper");
						if (tinCount > 0 && copperCount > 0){
							return ((tinCount + copperCount)/2);
						}
						break;
				case 1: int ironCount = countOre("Iron");
						if (ironCount > 0){
							return ironCount;
						}
						break;
				case 2: int silverCount = countOre("Silver");
						if (silverCount > 0){
							return silverCount;
						}
						break;
				case 3: int ironCount2 = countOre("Iron"),
						    coalCount = countOre("Coal");
						if (ironCount2 > 0 && coalCount > 1){
							return ironCount2;
						}
						break;
				case 4: int goldCount = countOre("Gold");
						if (goldCount > 0){
							return goldCount;
						}
						break;
				case 5: int mithrilCount = countOre("Mithril"),
						    coalCount2 = countOre("Coal");
						if (mithrilCount > 0 && coalCount2 > 3){
							return mithrilCount;
						}
						break;
				case 6: int adamantCount = countOre("Adamant"),
						    coalCount3 = countOre("Coal");
						if (adamantCount > 0 && coalCount3 > 5){
							return adamantCount;
						}
						break;
				case 7: int runeCount = countOre("Rune"),
						    coalCount4 = countOre("Coal");
						if (runeCount > 0 && coalCount4 > 7){
							return runeCount;
						}
						break;
			}
		}
		
		//println("Count failed");
		
		return 0;
	}

	// Clicks on the ore which we should superheat
	private boolean doSuperheat(){
		String[] ores = {"Copper&Tin", "Iron", "Silver", "Iron", "Gold", "Mithril", "Adamant", "Rune"};
		int barNumber = defineBarNumber(bar);
		if (barNumber != -1){
			String ore = "None";
			if (ores[barNumber].contains("&")){
				switch(randomRange(0, 1)){
					case 0: ore = "Tin"; break;
					case 1: ore = "Copper"; break;
				}
			}else{
				ore = ores[barNumber];
			}
			
			Point[] coords = orePoints(ore);
			Point coord = null;
			if (coords != null){
				if (style.contains("Random")){
					int j = randomRange(0, coords.length -1);
					coord = coords[j];
				}else{
					Point mouse = Mouse.getPos();
					
					double smallest = 10000;
					int best = -1;
					
					for (int i = 0; i < coords.length; i++){
						double xDist = Math.abs(coords[i].x - mouse.x),
							   yDist = Math.abs(coords[i].y - mouse.y),
							   dist = Math.pow(xDist, 2) + Math.pow(yDist, 2);

						if (dist < smallest){
							smallest = dist;
							best = i;
						}
					}
					
					if (best != -1){
						coord = coords[best];
					}
				}
				
				if (coord != null){
					if (!Timing.waitUptext(" ore", 50)){
						Point OP = new Point(coord.x + randomRange(-15, 15), coord.y + randomRange(-15, 15));
						Mouse.move(OP);
						while((System.currentTimeMillis() - superheatStart < randomRange(1300, 1400))){
							sleep(10, 50);
						}
						Mouse.click(1);
						superheatStart = System.currentTimeMillis();
					}else{
						while((System.currentTimeMillis() - superheatStart) < randomRange(1300, 1400)){
							sleep(10, 50);
						}
						Mouse.click(1);
						superheatStart = System.currentTimeMillis();
					}
					superheats++;
					waitTab(TABS.MAGIC, 500);
					return true;
				}
			}
		}

		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* Mainloop */
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
    public void run() {
		println("GUI Test");

		java.awt.EventQueue.invokeLater(new Runnable(){
   
			@Override
			public void run() {
				SuperheatGUI2 g = new SuperheatGUI2();
			}
    	});
      	
      	/*
      	SuperheatGUI2 Gui = new SuperheatGUI2();
      	println(Gui.completedGUI);
      	
      	/*
      	while(!Gui.completedGUI){
			sleep(1000, 2000);
		}
		*/
		
      	while (!dtms.guiCompleted){
      		println(dtms.guiCompleted);
      		println("Not completed");
      		sleep(5000);
      	}
      	
      	println("Clicked on start");
    		

    	/*
		while (!GUI.completedGUI){
			sleep(1000, 2000);
		}
		*/
		
		/*
		Mouse.setSpeed(100);
		// BANKING
		while(openBank()){
			waitBankscreen(2000);
			if (deposit()){
				withdrawOres(bar);
				closeBank();
				waitTabs(2000);
				FTab(TABS.MAGIC);
				ores = 2;
			}
	
			// SUPERHEATING
			while (ores > 1){
				clickSuperheat();
				ores = canSuperheat();
				doSuperheat();
			}
			println("Out of ores");
		}
		
		*/
		
    }
}