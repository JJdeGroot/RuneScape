package org.obduro.barbassault.healer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Heal extends Node {
	
	// Heal players
	private final int healingPoolID = 20150,
					  unfilledVialID = 10546,
					  filledVialID = 10547;
	
	public enum TeamMate {
		ONE(488, 6, 5),
		TWO(488, 11, 10),
		THREE(488, 16, 15),
		FOUR(488, 21, 20);
		
		private final int widgetID, healthIndex, nameIndex;
		
		private TeamMate(int widgetID, int healthIndex, int nameIndex){
			this.widgetID = widgetID;
			this.healthIndex = healthIndex;
			this.nameIndex = nameIndex;
		}

		private int getHealth(String text, int index){
			System.out.println("Text: " + text);
			
			Pattern p = Pattern.compile("(((\\d+)) / ((\\d+)))");
			Matcher matcher = p.matcher(text);
			if(matcher.find()){
				try {
					return Integer.parseInt(matcher.group(index));
				} catch (NumberFormatException ignored) {
					System.out.println("Heal -> getHealth(" + text + ", " + index + ") -> NumberFormatException");
				}
			}
			
			return -1;
		}
		
		public int getCurrentHealth(){
			WidgetChild hp = Widgets.get(widgetID, healthIndex);
			if(hp != null && hp.validate()){
				return getHealth(hp.getText(), 3);
			}
			return -1;
		}
		
		public int getMaximumHealth(){
			WidgetChild hp = Widgets.get(widgetID, healthIndex);
			if(hp != null && hp.validate()){
				return getHealth(hp.getText(), 4);
			}
			return -1;
		}
		
		public String getName(){
			WidgetChild name = Widgets.get(widgetID, nameIndex);
			if(name != null && name.validate()){
				return name.getText();
			}
			return null;
		}
		
	}

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
	
}
