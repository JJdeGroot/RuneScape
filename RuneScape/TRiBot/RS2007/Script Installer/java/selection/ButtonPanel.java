package org.obduro.java.selection;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.obduro.java.ScriptFolder;

public class ButtonPanel extends JPanel {

	private JButton installSelectedButton, installAllButton, removeSelectedButton, removeAllButton;
	private ClickListener listener;
	private SelectionBoxPanel boxPanel;
	private ScriptFolder scriptFolder;
	
	public ButtonPanel(SelectionBoxPanel boxPanel, ScriptFolder scriptFolder){
		this.boxPanel = boxPanel;
		this.scriptFolder = scriptFolder;
		setLayout(new GridLayout(1, 4));
		listener = new ClickListener();

		installAllButton = new JButton("Install all");
		installAllButton.addActionListener(listener);
		add(installAllButton);
		
		installSelectedButton = new JButton("Install selected");
		installSelectedButton.addActionListener(listener);
		add(installSelectedButton);
		
		removeSelectedButton = new JButton("Remove selected");
		removeSelectedButton.addActionListener(listener);
		add(removeSelectedButton);
		
		removeAllButton = new JButton("Remove all");
		removeAllButton.addActionListener(listener);
		add(removeAllButton);
	}
	
	private class ClickListener implements ActionListener {

		private String getExtension(File file){
			String fileName = file.getName();
			String ext = null;
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length()-1) {
			    ext = fileName.substring(i+1);
			}
			return ext;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Button clicked!");

			JButton button = (JButton) e.getSource();
			if(button.equals(installAllButton)){
				System.out.println("Install all button has been clicked");
				ArrayList<File> files = boxPanel.getAllFiles();
				for(File file : files){
					String filePath = file.getAbsolutePath();
					System.out.println("File path: " + filePath);
					
					scriptFolder.getLocation();
					
					
				}
				
				//StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			    //Iterable compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);
			    // compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
			    
			    System.out.println("Done");
			
	
				
				/*
				for(File file : files){
					String ext = getExtension(file);
					System.out.println("Extension: " + ext);
					
					if(ext.equals("java")){
						System.out.println("Compiling file: " + file);
					
					    
							
					
					    // Compile source file.
					    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
					    int result = compiler.run(null, null, null, file.getPath());
					    System.out.println("Result: " + result);
					
					}
					
				}
				*/
			
			    
				
			}else if(button.equals(installSelectedButton)){
				System.out.println("Install selected button has been clicked");
				
				
				
			}else if(button.equals(removeSelectedButton)){
				System.out.println("Remove selected button has been clicked");
				boxPanel.removeSelectedFiles();
			}else if(button.equals(removeAllButton)){
				System.out.println("Remove all button has been clicked");
				boxPanel.removeAllFiles();
			}
		}
	}

	
	
	
}
