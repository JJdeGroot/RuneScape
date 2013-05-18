package org.obduro.runecrafter.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.obduro.runecrafter.types.Pouch;

public class PouchPanel extends JPanel {

	private JLabel pouchLabel;
	private JRadioButton smallPouchButton, mediumPouchButton, largePouchButton, giantPouchButton;

	public PouchPanel(){
		setLayout(new GridLayout(1, 5));
		setPreferredSize(new Dimension(300, 50));
		
		pouchLabel = new JLabel("Pouches:");
		add(pouchLabel);
		
		smallPouchButton = new JRadioButton("Small");
		add(smallPouchButton);
		
		mediumPouchButton = new JRadioButton("Medium");
		add(mediumPouchButton);
				
		largePouchButton = new JRadioButton("Large");
		add(largePouchButton);
				
		giantPouchButton = new JRadioButton("Giant");
		add(giantPouchButton);
	}
	
	public Pouch[] getSelectedPouches(){
		ArrayList<Pouch> pouchList = new ArrayList<Pouch>();
		
		JRadioButton[] pouchButtons = {smallPouchButton, mediumPouchButton, largePouchButton, giantPouchButton};
		for(int i = 0; i < pouchButtons.length; i++){
			if(pouchButtons[i].isSelected()){
				pouchList.add(Pouch.values()[i]);
			}
		}
		
		return pouchList.toArray(new Pouch[pouchList.size()]);
	}
	
}
