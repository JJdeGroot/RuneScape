package org.obduro.barbassault.attacker;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import utils.ActionBar.SlotState;

/*
 * ActionBar class for handling abilities on the action bar.
 * Credits to AddictiveScripts his API!
 */
public class ActionBar {
	
	private static final int ID_SETTINGS_ITEM_BASE = 811;
    private static final int ID_SETTINGS_ABILITY_BASE = 727;
    private static final int ID_WIDGET_ACTION_BAR = 640;

    public static boolean isOpen() {
		WidgetChild wc = Widgets.get(640, 4);
		return wc.validate() && wc.visible();
	}

	public static boolean open() {
		if(!isOpen()) {
			WidgetChild wc = Widgets.get(640, 3);
			if (wc.validate() && wc.click(true)){
				Timer timer = new Timer(Random.nextInt(2000, 3000));
				while (timer.isRunning() && !isOpen()){
					Task.sleep(50, 100);
				}
			}
		}

		return isOpen();
	}
	
	public static boolean close() {
		if(isOpen()) {
			WidgetChild wc = Widgets.get(640, 30);
			if (wc.validate() && wc.click(true)) {
				Timer timer = new Timer(Random.nextInt(2000, 3000));
				while (timer.isRunning() && isOpen()){
					Task.sleep(50, 100);
				}
			}
		}

		return !isOpen();
	}

	public enum Ability {
		BALANCED(68),
		DEFENSIVE(84),
		RECKLESS(52),
		AGRESSIVE(100);
		
		private final int onBarValue;
		
		private Ability(int onBarValue){
			this.onBarValue = onBarValue;
		}

		public Slot getSlot() {
			for(int i = 0; i < 12; i++){
				int value = Settings.get(ID_SETTINGS_ABILITY_BASE + i);
				if(value == this.onBarValue){
					return Slot.values()[i];
				}
			}
			return null;
		}
	}

	public enum Slot {
        ONE(0, 32, 36, 70),
        TWO(1, 72, 73, 75),
        THREE(2, 76, 77, 79),
        FOUR(3, 80, 81, 83),
        FIVE(4, 84, 85, 87),
        SIX(5, 88, 89, 91),
        SEVEN(6, 92, 93, 95),
        EIGHT(7, 96, 97, 99),
        NINE(8, 100, 101, 103),
        TEN(9, 104, 105, 107),
        ELEVEN(10, 108, 109, 111),
        TWELVE(11, 112, 113, 115);

		private int index;
		private int widgetChildAvailable;
		private int widgetChildCoolDown;
		private int widgetChildText;

		Slot(final int index, final int widgetChildAvailable, final int widgetChildCoolDown, final int widgetChildText) {
			this.index = index;
			this.widgetChildAvailable = widgetChildAvailable;
			this.widgetChildCoolDown = widgetChildCoolDown;
			this.widgetChildText = widgetChildText;
		}

		public int getIndex() {
			return this.index;
		}

		public int getWidgetChildAvailable() {
			return this.widgetChildAvailable;
		}

		public int getWidgetChildCoolDown() {
			return this.widgetChildCoolDown;
		}

		public int getWidgetChildText() {
			return this.widgetChildText;
		}

		public WidgetChild getAvailableWidget() {
			return Widgets.get(ID_WIDGET_ACTION_BAR, widgetChildAvailable);
		}

		public WidgetChild getCooldownWidget() {
			return Widgets.get(ID_WIDGET_ACTION_BAR, widgetChildCoolDown);
		}

		public boolean isAvailable() {
			final WidgetChild available = getAvailableWidget();
			return available != null && available.validate() && available.getTextColor() == 16777215;
		}

		public SlotState getSlotState() {
			final int item = Settings.get(ID_SETTINGS_ITEM_BASE + index);
			if (item > -1)
				return SlotState.ITEM;
			return Settings.get(ID_SETTINGS_ABILITY_BASE + index) > -1 ? SlotState.ABILITY : SlotState.EMPTY;
		}

		public boolean activate(boolean sendKey) {
			final WidgetChild widgetChild = Widgets.get(ID_WIDGET_ACTION_BAR,
					widgetChildAvailable);

			if (!widgetChild.validate())
				return false;

			if (sendKey) {
				Keyboard.sendKey(Widgets.get(ID_WIDGET_ACTION_BAR, widgetChildText).getText().charAt(0));
				return true;
			} else {
				return widgetChild.click(true);
			}
		}
    }

	
	
	
	
	
	
	
}
