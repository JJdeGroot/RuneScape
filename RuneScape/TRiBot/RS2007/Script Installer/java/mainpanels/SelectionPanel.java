package org.obduro.java.mainpanels;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.obduro.java.ScriptFolder;
import org.obduro.java.selection.ButtonPanel;
import org.obduro.java.selection.ManagePanel;
import org.obduro.java.selection.SelectionBoxPanel;

public class SelectionPanel extends JPanel {

	private ScriptFolder scriptFolder;
	private ButtonPanel buttonPanel;
	private ManagePanel managePanel;
	private SelectionBoxPanel boxPanel;

	public SelectionPanel(ScriptFolder scriptFolder){
		this.scriptFolder = scriptFolder;
		setLayout(new BorderLayout());
		
		boxPanel = new SelectionBoxPanel();
		add(boxPanel, BorderLayout.CENTER);
		
		managePanel = new ManagePanel(boxPanel);
		add(managePanel, BorderLayout.NORTH);
	
		buttonPanel = new ButtonPanel(boxPanel, scriptFolder);
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	
	
	
}
