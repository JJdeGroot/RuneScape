package org.obduro.java.mainpanels;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.obduro.java.ScriptFolder;

public class FolderPanel extends JPanel {

	private ScriptFolder scriptFolder;
	private Desktop desktop;
	private JLabel folderLabel;
	private JButton folderButton;
	
	public FolderPanel(ScriptFolder scriptFolder){
		this.scriptFolder = scriptFolder;
		setLayout(new GridLayout(1, 2));
		desktop = Desktop.getDesktop();
		
		folderLabel = new JLabel("Click the button to show your script folder:");
		add(folderLabel);
		
		folderButton = new JButton("Show script folder");
		folderButton.addActionListener(new FolderListener());
		add(folderButton);
	}
	
	private class FolderListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			System.out.println("Script folder: " + scriptFolder.getLocation());
			
			try {
				desktop.open(new File(scriptFolder.getLocation()));
			} catch (IOException ioe) {
				System.out.println("Failed to open script folder");
				ioe.printStackTrace();
			}
		}
		
	}
	
	
	
}
