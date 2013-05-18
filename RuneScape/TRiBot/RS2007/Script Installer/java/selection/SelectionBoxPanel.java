package org.obduro.java.selection;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SelectionBoxPanel extends JPanel {

	private JLabel fileLabel;
	private JScrollPane scroll;
	private JList<File> list;
	private ArrayList<File> fileList;

	public SelectionBoxPanel(){
		setLayout(new BorderLayout());
		fileList = new ArrayList<File>();
		
		fileLabel = new JLabel("Selected files:");
		add(fileLabel, BorderLayout.NORTH);

		list = new JList<File>();
		scroll = new JScrollPane(list);
		add(scroll, BorderLayout.CENTER);
	}
	
	private void updateModel(){
		DefaultListModel<File> model = new DefaultListModel<File>();
		for(File file : fileList){
			model.addElement(file);
		}

		list.setModel(model);
	}
	
	public void addFiles(File... files){
		for(File file : files){
			fileList.add(file);
			System.out.println("Added file to list: " + file);
		}
		updateModel();
	}
	
	public boolean removeFiles(File... files){
		for(File file : files){
			for(int i = 0; i < fileList.size(); i++){
				if(fileList.get(i).equals(file)){
					System.out.println("Removed file: " + file);
					fileList.remove(i);
					updateModel();
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ArrayList<File> getAllFiles(){
		return fileList;
	}
	
	public void removeAllFiles(){
		fileList.clear();
		updateModel();
	}
	
	public ArrayList<File> getSelectedFiles(){
		ArrayList<File> files = new ArrayList<File>();
		int[] indices = list.getSelectedIndices();
		for(int index : indices){
			files.add(fileList.get(index));
		}
		return files;
	}
	
	public void removeSelectedFiles(){
		ArrayList<File> files = getSelectedFiles();
		for(File file : files){
			removeFiles(file);
		}
	}

	
}
