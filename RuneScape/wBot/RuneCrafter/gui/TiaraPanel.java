package org.obduro.runecrafter.gui;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TiaraPanel extends JPanel {

	private JLabel tiaraLabel;
	private JCheckBox tiaraCheck;
	
	public TiaraPanel(){
		setLayout(new GridLayout(1, 2));
		
		tiaraLabel = new JLabel("Using tiara?");
		add(tiaraLabel);
		
		tiaraCheck = new JCheckBox("Tick to use tiara");
		add(tiaraCheck);
	}
	
	public boolean getSelectedTiaraValue(){
		return tiaraCheck.isSelected();
	}
	
}
