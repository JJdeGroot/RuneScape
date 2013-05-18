package scripts.jjsgoptokenexchanger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {

	private final String[] items = {"Air talisman", "Mind Talisman",
			"Water talisman", "Earth talisman", "Fire talisman",
			"Body talisman", "Cosmic talisman", "Chaos talisman",
			"Nature talisman", "Law talisman"};
	
	private final Talisman[] talismans = {Talisman.AIR, Talisman.MIND,
			Talisman.WATER, Talisman.EARTH, Talisman.FIRE,
			Talisman.BODY, Talisman.COSMIC, Talisman.CHAOS,
			Talisman.NATURE, Talisman.LAW};

	private JFrame frame;
	private JLabel itemLabel;
	private JComboBox<String> itemBox;
	private JButton startButton;
	
	private String chosenTalisman = items[0];
	private boolean finished = false;
	
	public void create(){
		frame = new JFrame("JJ's Token Exchanger GUI");
		frame.setSize(300, 125);
		frame.setLayout(new GridLayout(2, 1));
		
		// TEAM PANEL
		JPanel itemPanel = new JPanel(new GridLayout(1, 2));
		itemLabel = new JLabel("Talisman to buy:");
		itemPanel.add(itemLabel);
	
		itemBox = new JComboBox<String>(items);
		itemPanel.add(itemBox);
		
		// START PANEL
		JPanel startPanel = new JPanel();
		startButton = new JButton("Start Exchanging!");
		startButton.addActionListener(new StartListener());
		startPanel.add(startButton);
		
		// FINISHING
		frame.add(itemPanel);
		frame.add(startPanel);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}
	
	private String getName(String fullName){
		int index = chosenTalisman.indexOf(" ");
		String name = chosenTalisman.substring(0, index);
		System.out.println("Talisman name: " + name);
		return name;
	}
	
	public Talisman getChosenTalisman(){
		String talismanName = getName(chosenTalisman).toLowerCase();
		
		for(Talisman talisman : talismans){
			String talismanEnum = talisman.toString().toLowerCase();
			if(talismanEnum.contains(talismanName)){
				return talisman;
			}
		}
		
		return Talisman.WATER;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	private class StartListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Exchanger -> Start button has been clicked!");
			chosenTalisman = items[itemBox.getSelectedIndex()];
			finished = true;
			frame.dispose();
		}
	}
	
	public static void main(String[] args){
		GUI g = new GUI();
		g.create();
	}
	
}