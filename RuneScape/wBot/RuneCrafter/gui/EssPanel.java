package org.obduro.runecrafter.gui;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.obduro.runecrafter.types.Essence;

public class EssPanel extends JPanel {

	private JLabel essenceLabel;
	private JComboBox<Essence> essenceList;

	public EssPanel(){
		setLayout(new GridLayout(1, 2));
		
		essenceLabel = new JLabel("Essence:");
		add(essenceLabel);
		
		essenceList = new JComboBox<Essence>(Essence.values());
		add(essenceList);
	}

	public Essence getSelectedEssence(){
		return Essence.values()[essenceList.getSelectedIndex()];
	}
	
}
