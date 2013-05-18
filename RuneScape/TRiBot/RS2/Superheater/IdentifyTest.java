package scripts;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.tribot.api.Banking;
import org.tribot.api.ChooseOption;
import org.tribot.api.Inventory;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.BankItem;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "Identify")
public class IdentifyTest extends Script{
	boolean useCoalbag = true;
	
	// Generates a random number including negative.
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	// Picks the colors of the ores at the same spots as they will be searched
	private void pickColors(){
		
		for (int i = 0; i < 9; i++){
			int X = 56 + i * 44;
			int Y = 157;
			
			Color color = Screen.getColourAt(new Point(X, Y));
			println("Red @ " + i + ": " + color.getRed());
			println("Green @ " + i + ": " + color.getGreen());
			println("Blue @ " + i + ": " + color.getBlue());
			
		}
	}
	
	public int defineOreNumber(String choice){
		String[] oreNames = {"Tin", "Copper", "Iron", "Silver", "Coal", "Gold", "Mithril", "Adamant", "Rune"};
		for (int i = 0; i < oreNames.length; i++){
			if (oreNames[i].contains(choice)){
				return i;
			}
		}
		return -1;
	}
	
	public int defineBarNumber(String choice){
		String[] barNames = {"Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune"};
		for (int i = 0; i < barNames.length; i++){
			if (barNames[i].contains(choice)){
				return i;
			}
		}
		return -1;
	}

	// Gets all the points where the chosen ore is found in the inventory.
	public Point[] orePoints(String choice){
		int[][] oreRGB = {{31, 26, 23}, {41, 28, 18}, {27, 20, 15}, {24, 20, 15}, {27, 22, 21}, {28, 25, 22}};
		int oreID = 125105,
		    number = defineOreNumber(choice);
		
		ArrayList<Point> coordList = new ArrayList<Point>();
		InventoryItem[] ore = Inventory.find(oreID);
		if (ore.length > 0){
			for (int i = 0; i < ore.length; i++){
				if (ore[i].avg_r == oreRGB[number][0]){
					if (ore[i].avg_g == oreRGB[number][1]){
						if (ore[i].avg_b == oreRGB[number][2]){
							println("Ore has been found");
							coordList.add(new Point(ore[i].x, ore[i].y));
						}
					}
					
				}
			}
		}
		
		if (coordList.size() > 0){
			Point[] coords = coordList.toArray(new Point[coordList.size()]); 
			return coords;
		}
		
		return null;
	}
	
	// Returns the amounts of the chosen ore in the inventory.
	public int countOre(String choice){
		Point[] pts = orePoints(choice);
		if (pts != null){
			return pts.length;
		}
		
		return 0;
	}
	
	// Gets the coordinate of the chosen ore in the bank.
	public Point getOrePoint(String choice){
		int[][] oreColor = {{99, 95, 95}, {184, 105, 56}, {52, 31, 23}, 
							{133, 145, 140}, {35, 35, 27}, {174, 138, 24},
				            {54, 55, 82}, {56, 65, 56}, {68, 82, 88}};
		int number = defineOreNumber(choice);
		println("Number: " + number);
		double best = 1000;
		Point P = null;
		
		BankItem[] all = Banking.getAll();
		for (int i = 0; i < all.length; i++){
			//println(all[i].x + ", "+ all[i].y);
			if (all[i].y > 135){
				Point grab = new Point(all[i].x + 24, all[i].y + 21);
				Color color = Screen.getColourAt(grab);
				//println(color);
				double difference = Math.abs(color.getRed() - oreColor[number][0]) +
									Math.abs(color.getGreen() - oreColor[number][1]) +
									Math.abs(color.getBlue() - oreColor[number][2]);
				//println(difference);
				if (difference < best){
					best = difference;
					P = grab;
				}
			}
		}
		
		//println("-----------------------------");

		return P;
	}
	
	// Withdraws the chosen amount of the chosen ore in the bank.
	public boolean withdrawOre(String choice, int amount){
		Point ore = getOrePoint(choice);
		if (ore != null){
			Mouse.move(new Point(ore.x + randomRange(-7, 7), ore.y + randomRange(-7, 7)));
			Mouse.click(3);
			
			String option;
			if (amount >= 28){
				option = "All";
			}else{
				option = Integer.toString(amount);
			}

			if (!Timing.waitChooseOption(option, 500)){
				println("Select-X");
				ChooseOption.select("X");
				sleep(1000, 1500);
				Keyboard.typeSend(Integer.toString(amount));
				return true;
			}else{
				return true;
			}
		}
		
		return false;
	}
	
	// Puts 28 coal in the coalbag
	public boolean fillCoalbag(){
		int coalbag = 388266;
		InventoryItem[] bag = Inventory.find(coalbag);
		if (bag.length > 0){
			withdrawOre("Coal", 28);
			
			Point P = new Point(bag[0].x + 15 + randomRange(-5, 5), bag[0].y + 15 + randomRange(-5, 5));
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
	
	public void withdrawBronze(){
		switch(randomRange(0, 3)){
			case 0: if (withdrawOre("Tin", 13)){
						withdrawOre("Copper", 13);
					}
			        break;
			case 1: if (withdrawOre("Copper", 13)){
						withdrawOre("Tin", 13);
					}
	        		break;
			case 2: if (withdrawOre("Tin", 13)){
						withdrawOre("Copper", 28);
					}
	        		break;
			case 3: if (withdrawOre("Copper", 13)){
						withdrawOre("Tin", 28);
					}
    			break;
		}
	}
	
	public void withdrawSteel(){
		if (useCoalbag){
			if (fillCoalbag()){
				switch(randomRange(0, 29)){
					case 0: if (withdrawOre("Coal", 9)){
								withdrawOre("Iron", 28);
							}
					        break;
					case 1: if (withdrawOre("Coal", 8)){
								withdrawOre("Iron", 28);
							}
					        break;
					case 2: if (withdrawOre("Coal", 7)){
								withdrawOre("Iron", 28);
							}
					        break;
					default: if (withdrawOre("Iron", 17)){
								  withdrawOre("Coal", 28);
							 }
							 break;
				}
			}
		}else{
			switch(randomRange(0, 9)){
				case 0: if (withdrawOre("Coal", 18)){
							withdrawOre("Iron", 9);
						}
				        break;
				default: if (withdrawOre("Iron", 9)){
							  withdrawOre("Coal", 18);
						 }
						 break;
			}
		}
	}
	
	public void withdrawMithril(){
		if (useCoalbag){
			if (fillCoalbag()){
				switch(randomRange(0, 39)){
					case 0: if (withdrawOre("Coal", 16)){
								withdrawOre("Mithril", 28);
							}
					        break;
					case 1: if (withdrawOre("Coal", 15)){
								withdrawOre("Mithril", 28);
							}
					        break;
					case 2: if (withdrawOre("Coal", 14)){
								withdrawOre("Mithril", 28);
							}
					        break;
					case 3: if (withdrawOre("Coal", 13)){
								withdrawOre("Mithril", 28);
							}
					        break;
					default: if (withdrawOre("Mithril", 10)){
								 withdrawOre("Coal", 28);
							 }
							 break;
				}
			}
		}else{
			switch(randomRange(0, 29)){
				case 0: if (withdrawOre("Coal", 22)){
							withdrawOre("Mithril", 5);
						}
				        break;
				case 1: if (withdrawOre("Coal", 21)){
							withdrawOre("Mithril", 5);
						}
				        break;
				case 2: if (withdrawOre("Coal", 20)){
							withdrawOre("Mithril", 5);
						}
				        break;
				default: if (withdrawOre("Mithril", 5)){
							 withdrawOre("Coal", 28);
						 }
						 break;
			}
		}
	}
	
	public void withdrawAdamant(){
		if (useCoalbag){
			if (fillCoalbag()){
				switch(randomRange(0, 49)){
					case 0: if (withdrawOre("Coal", 19)){
								withdrawOre("Adamant", 28);
							}
					        break;
					case 1: if (withdrawOre("Coal", 18)){
								withdrawOre("Adamant", 28);
							}
					        break;
					case 2: if (withdrawOre("Coal", 17)){
								withdrawOre("Adamant", 28);
							}
					        break;
					case 3: if (withdrawOre("Coal", 16)){
								withdrawOre("Adamant", 28);
							}
					        break;
					case 4: if (withdrawOre("Coal", 15)){
								withdrawOre("Adamant", 28);
							}
					        break;
					default: if (withdrawOre("Adamant", 7)){
								 withdrawOre("Coal", 28);
							 }
							 break;
				}
			}
		}else{
			switch(randomRange(0, 69)){
				case 0: if (withdrawOre("Coal", 24)){
							withdrawOre("Adamant", 28);
						}
				        break;
				case 1: if (withdrawOre("Coal", 23)){
							withdrawOre("Adamant", 28);
						}
				        break;
				case 2: if (withdrawOre("Coal", 22)){
							withdrawOre("Adamant", 28);
						}
				        break;
				case 3: if (withdrawOre("Coal", 21)){
							withdrawOre("Adamant", 28);
						}
				        break;
				case 4: if (withdrawOre("Coal", 20)){
							withdrawOre("Adamant", 28);
						}
				        break;
				case 5: if (withdrawOre("Coal", 19)){
							withdrawOre("Adamant", 28);
						}
		        		break;
				case 6: if (withdrawOre("Coal", 18)){
							withdrawOre("Adamant", 28);
						}
				        break;
				default: if (withdrawOre("Adamant", 3)){
							 withdrawOre("Coal", 28);
						 }
						 break;
			}
		}
	}
	
	public void withdrawRune(){
		if (useCoalbag){
			if (fillCoalbag()){
				switch(randomRange(0, 89)){
					case 0: if (withdrawOre("Coal", 21)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 1: if (withdrawOre("Coal", 20)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 2: if (withdrawOre("Coal", 19)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 3: if (withdrawOre("Coal", 18)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 4: if (withdrawOre("Coal", 17)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 5: if (withdrawOre("Coal", 16)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 6: if (withdrawOre("Coal", 15)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 7: if (withdrawOre("Coal", 14)){
								withdrawOre("Rune", 28);
							}
					        break;
					case 8: if (withdrawOre("Coal", 13)){
								withdrawOre("Rune", 28);
							}
					        break;
					default: if (withdrawOre("Rune", 5)){
								 withdrawOre("Coal", 28);
							 }
							 break;
				}
			}
		}else{
			if (withdrawOre("Rune", 3)){
				 withdrawOre("Coal", 28);
			 }
		}
	}
	
	public boolean withdrawOres(String choice){
		int number = defineBarNumber(choice);
		println(number);
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
		
		InventoryItem[] check = Inventory.getAll();
		if (check.length >= 24){
			return true;
		}
		
		return false;
	}
	
	
	@Override
    public void run() {
    	//detectOres("Tin");
    	//clickOre("Tin");
		//withdrawOre("Rune", 2);
		//pickColors();
		
		//fillCoalbag();

		//withdrawOre("Rune", 28);
		//withdrawOre("Copper", 28);
		
		//withdrawOres("Bronze");
		//withdrawOres("Iron");
		//withdrawOres("Steel");
		withdrawOres("Mithril");
		//withdrawOres("Adamant");
		//withdrawOres("Rune");
		
		
		
		
    	
    }
}