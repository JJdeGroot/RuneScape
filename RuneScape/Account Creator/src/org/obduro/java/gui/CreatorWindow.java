package org.obduro.java.gui;

import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CreatorWindow extends JDialog {
	JLabel label;
	
	public CreatorWindow(JFrame frame){
		super(frame, "Creator Window", true);
		setLayout(new FlowLayout());
		
		label = new JLabel("Creator....");
		add(label);
	}
	
}