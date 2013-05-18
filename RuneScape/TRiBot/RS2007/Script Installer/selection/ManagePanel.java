package org.obduro.java.selection;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ManagePanel extends JPanel {

	private JLabel chooseLabel;
	private JButton openButton;
	private JFileChooser chooser;
	private SelectionBoxPanel boxPanel;
	
	public ManagePanel(SelectionBoxPanel boxPanel){
		this.boxPanel = boxPanel;
		setLayout(new GridLayout(1, 2));
		
		chooseLabel = new JLabel("Click the button to start selecting files:");
		add(chooseLabel, BorderLayout.CENTER);
		
		openButton = new JButton("Open file chooser");
		openButton.addActionListener(new OpenListener());
		add(openButton, BorderLayout.EAST);
		
		chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
	}

	private class OpenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Starting JFileChooser");
			chooser.showDialog(null, "Select");
			File[] files = chooser.getSelectedFiles();
			boxPanel.addFiles(files);
		}
		
	}
	
	
	
}
