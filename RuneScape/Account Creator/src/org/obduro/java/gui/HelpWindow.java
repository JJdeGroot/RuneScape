package org.obduro.java.gui;

import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HelpWindow extends JDialog {
	JLabel label;
	
	public HelpWindow(JFrame frame){
		super(frame, "Help Window", true);
		setLayout(new FlowLayout());
		
		label = new JLabel("This is help");
		add(label);
	}
}
