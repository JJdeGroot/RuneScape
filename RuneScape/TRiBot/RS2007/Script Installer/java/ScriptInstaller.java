package org.obduro.java;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.obduro.java.mainpanels.FolderPanel;
import org.obduro.java.mainpanels.SelectionPanel;

public class ScriptInstaller extends JFrame {

	private ScriptFolder scriptFolder;
	private SelectionPanel managePanel;
	private FolderPanel folderPanel;
	
	public ScriptInstaller(){
		super("JJ's Script Installer");
		setLayout(new BorderLayout());
		setSize(550, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		managePanel = new SelectionPanel(scriptFolder);
		add(managePanel, BorderLayout.CENTER);
		
		scriptFolder = new ScriptFolder();
		folderPanel = new FolderPanel(scriptFolder);
		add(folderPanel, BorderLayout.SOUTH);
	
		setVisible(true);
	}
	
	public static void main(String[] args){
		ScriptInstaller si = new ScriptInstaller();
		si.setVisible(true);
		
	
		
		
	}
	
	
}
