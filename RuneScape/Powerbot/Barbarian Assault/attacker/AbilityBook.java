package org.obduro.barbassault.attacker;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/*
 * AbilityBook class for handling abilities in the Ability book
 * Credits to Strikeskids his API!
 */
public class AbilityBook {

	/*
	private final Widget spellbookWidget = new Widget(275);
	private final WidgetChild defenceWidget = new WidgetChild(spellbookWidget, 51),
							  constitutionAbilities = new WidgetChild(spellbookWidget, 55);
	private final WidgetChild abilitiesWidget = new WidgetChild(spellbookWidget, 16);
	private final WidgetChild aggressiveAbility = new WidgetChild(spellbookWidget, abilitiesWidget, 3),
							  balancedAbility = new WidgetChild(spellbookWidget, abilitiesWidget, 4),
							  defensiveAbility = new WidgetChild(spellbookWidget, abilitiesWidget, 5),
							  recklessAbility = new WidgetChild(spellbookWidget, abilitiesWidget, 6);
	*/
	
	public enum AbilityTab {
		OTHER_TAB(51);
		
		private final int component;
		
		private final int spellbook = 275, settingID = 682;
		private final int[] openIDs = {2359909, 2359910};

		private AbilityTab(int c) {
			this.component = c;
		}
		
		public boolean isSettingOpen() {
			int tabSetting = Settings.get(settingID);
			for(int i : openIDs)
				if(tabSetting == i)
					return true;
			return false;
		}

		public boolean isOpen() {
			return Tabs.ABILITY_BOOK.isOpen() && isSettingOpen();
		}

		public boolean open() {
			if(!isOpen()){
				System.out.println("Not open!");

				if(!Tabs.ABILITY_BOOK.isOpen()){
					if(Tabs.ABILITY_BOOK.open()){
						Timer timer = new Timer(Random.nextInt(500, 1000));
						while(timer.isRunning() && !Tabs.ABILITY_BOOK.isOpen()){
							Task.sleep(50, 100);
						}
					}
				}
				
				if(!isSettingOpen()){
					WidgetChild wc = Widgets.get(spellbook, this.component);
					if(wc.validate() && wc.visible() && wc.click(true)){
						Timer timer = new Timer(Random.nextInt(500, 1000));
						while(timer.isRunning() && !AbilityTab.OTHER_TAB.isOpen()){
							Task.sleep(50, 100);
						}
					}
				}
			}

			return isOpen();
		}

	}
	
	public enum InnerAbilityTab {
		CONSTITUTION(60);
		
		private final int spellbook = 275, settingID = 682;
		private final int openID = 2359910;
		private final int component;

		private InnerAbilityTab(int c) {
			this.component = c;
		}
		
		public boolean isSettingOpen() {
			return Settings.get(settingID) == openID;
		}

		public boolean isOpen() {
			return AbilityTab.OTHER_TAB.isOpen() && isSettingOpen();
		}

		public boolean open() {
			if(!isOpen()){
				System.out.println("Not open!");

				if(!AbilityTab.OTHER_TAB.isOpen()){
					if(AbilityTab.OTHER_TAB.open()){
						Timer timer = new Timer(Random.nextInt(500, 1000));
						while(timer.isRunning() && !AbilityTab.OTHER_TAB.isOpen()){
							Task.sleep(50, 100);
						}
					}
				}
				
				if(!isSettingOpen()){
					WidgetChild wc = Widgets.get(spellbook, this.component);
					if(wc.validate() && wc.visible() && wc.click(true)){
						Timer timer = new Timer(Random.nextInt(500, 1000));
						while(timer.isRunning() && !AbilityTab.OTHER_TAB.isOpen()){
							Task.sleep(50, 100);
						}
					}
				}
			}

			return this.isOpen();
		}

	}
	
	public enum SpellBookAbility {
		MOMENTUM(7);
		
		private final Widget spellbook = Widgets.get(275);
		private final WidgetChild abilities = new WidgetChild(spellbook, 16);
		private final int component;

		private SpellBookAbility(int c) {
			this.component = c;
		}
		
		public boolean isVisible(){
			WidgetChild ability = new WidgetChild(spellbook, abilities, this.component);
			return InnerAbilityTab.CONSTITUTION.isOpen() && ability.validate() && ability.visible();
		
		}

		public boolean isSettingOpen() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean open() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
