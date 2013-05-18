package scripts.jjsrunecrafter.methods;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import scripts.jjsrunecrafter.types.Altars;
import scripts.jjsrunecrafter.types.Banks;
import scripts.jjsrunecrafter.types.Pouches;
import scripts.jjsrunecrafter.types.Runes;
import scripts.jjsrunecrafter.types.Essences;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1337;
	
	private final Banks[] airBanks = {Banks.FALADOR_EAST},
						  mindBanks = {Banks.FALADOR_WEST},
						  fireBanks = {Banks.AL_KHARID},
						  waterBanks = {Banks.DRAYNOR},
						  earthBanks = {Banks.VARROCK_EAST},
						  bodyBanks = {Banks.EDGEVILLE};
	private final Banks[][] allBanks = {airBanks, mindBanks, waterBanks, earthBanks, fireBanks, bodyBanks};
	
	private JPanel panel, pouchPanel;
	private JLabel runesLabel, bankLabel, essenceLabel;
	private JComboBox<Runes> runesList;
	private JComboBox<Essences> essenceList;
	private JComboBox<Banks> bankList;
	private JRadioButton smallPouchButton, mediumPouchButton, largePouchButton, giantPouchButton;
	private JButton startButton;
	
	private Runes chosenRunes;
	private Banks chosenBank;
	private Essences chosenEssence;
	private Pouches[] chosenPouches;
	
	public GUI(){
		super("JJ's RuneCrafter GUI");
		setSize(450, 175);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// MAIN PANEL
		panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));

		runesLabel = new JLabel("Runes:");
		panel.add(runesLabel);
		
		runesList = new JComboBox<Runes>(Runes.values());
		runesList.addActionListener(new SelectionListener());
		panel.add(runesList);
		
		bankLabel = new JLabel("Bank:");
		panel.add(bankLabel);
		
		bankList = new JComboBox<Banks>(airBanks);
		panel.add(bankList);
		
		essenceLabel = new JLabel("Essence:");
		panel.add(essenceLabel);
		
		essenceList = new JComboBox<Essences>(Essences.values());
		essenceList.setSelectedItem(Essences.PURE);
		panel.add(essenceList);
		
		// POUCH PANEL
		pouchPanel = new JPanel();
		pouchPanel.setLayout(new GridLayout(1, 4));
		
		smallPouchButton = new JRadioButton("Small Pouch");
		pouchPanel.add(smallPouchButton);
		
		mediumPouchButton = new JRadioButton("Medium Pouch");
		pouchPanel.add(mediumPouchButton);
				
		largePouchButton = new JRadioButton("Large Pouch");
		pouchPanel.add(largePouchButton);
				
		giantPouchButton = new JRadioButton("Giant Pouch");
		pouchPanel.add(giantPouchButton);
		
		// FINISHING		
		startButton = new JButton("Start");
		startButton.addActionListener(new StartListener());
		
		//add(pouchPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(startButton, BorderLayout.SOUTH);
	}
	
	public Runes getChosenRunes(){
		return chosenRunes;
	}
	
	public Altars getChosenAltar(){
		return chosenRunes.getAltar();
	}
	
	public Banks getChosenBank(){
		return chosenBank;
	}
	
	public Essences getChosenEssence(){
		return chosenEssence;
	}
	
	public Pouches[] getChosenPouches(){
		return chosenPouches;
	}

	private Pouches[] getSelectedPouches(){
		ArrayList<Pouches> pouchList = new ArrayList<Pouches>();
		JRadioButton[] pouchButtons = {smallPouchButton, mediumPouchButton, largePouchButton, giantPouchButton};
		
		for(int i = 0; i < pouchButtons.length; i++)
			if(pouchButtons[i].isSelected()){
				System.out.println("Added: " + Pouches.values()[i]);
				pouchList.add(Pouches.values()[i]);
			}
				
		
		return pouchList.toArray(new Pouches[pouchList.size()]);
	}
	
	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Start clicked");
			chosenRunes = Runes.values()[runesList.getSelectedIndex()];
			chosenBank = allBanks[runesList.getSelectedIndex()][bankList.getSelectedIndex()];
			chosenEssence = Essences.values()[essenceList.getSelectedIndex()];
			chosenPouches = getSelectedPouches();
			dispose();
		}
		
	}
	
	private class SelectionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Selection made");
	
			bankList.removeAllItems();
			Banks[] banks = allBanks[runesList.getSelectedIndex()];
			for(Banks bank : banks){
				bankList.addItem(bank);
			}

			repaint();
		}
		
	}

}
