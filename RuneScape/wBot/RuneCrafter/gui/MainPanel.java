package org.obduro.runecrafter.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.obduro.runecrafter.types.Altar;
import org.obduro.runecrafter.types.BankLoc;

public class MainPanel extends JPanel {

	private final BankLoc[] airBanks = {BankLoc.FALADOR_EAST},
						    mindBanks = {BankLoc.FALADOR_WEST, BankLoc.EDGEVILLE},
						    waterBanks = {BankLoc.DRAYNOR},
						    earthBanks = {BankLoc.VARROCK_EAST},
						    fireBanks = {BankLoc.AL_KHARID},
						    bodyBanks = {BankLoc.EDGEVILLE},
						    lawBanks = {BankLoc.DRAYNOR};
	private final BankLoc[][] allBanks = {airBanks, mindBanks, waterBanks, earthBanks, fireBanks, bodyBanks, lawBanks};

	private JLabel runesLabel, bankLabel;
	private JComboBox<Altar> altarList;
	private JComboBox<BankLoc> bankLocList;

	public MainPanel(){
		setLayout(new GridLayout(2, 2));
		setPreferredSize(new Dimension(300, 150));

		runesLabel = new JLabel("Runes:");
		add(runesLabel);
		
		altarList = new JComboBox<Altar>(Altar.values());
		altarList.addActionListener(new SelectionListener());
		add(altarList);
		
		bankLabel = new JLabel("Bank:");
		add(bankLabel);
		
		bankLocList = new JComboBox<BankLoc>(airBanks);
		add(bankLocList);
	}
	
	public Altar getSelectedAltar(){
		return Altar.values()[altarList.getSelectedIndex()];
	}

	public BankLoc getSelectedBank(){
		return allBanks[altarList.getSelectedIndex()][bankLocList.getSelectedIndex()];
	}

	private class SelectionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Selection made");
	
			bankLocList.removeAllItems();
			BankLoc[] banks = allBanks[altarList.getSelectedIndex()];
			for(BankLoc bank : banks){
				bankLocList.addItem(bank);
			}

			repaint();
		}
		
	}
	
	
	
}
