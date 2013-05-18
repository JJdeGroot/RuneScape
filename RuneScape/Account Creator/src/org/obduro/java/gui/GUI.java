package org.obduro.java.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI implements ActionListener {
	private JFrame frame;
	private JPanel panel;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JButton startButton;
	
	private boolean finished = false;
	private String chosenName = "JJ";
	private String chosenPassword = "JJ";
	
	private void createGUI(){
		frame = new JFrame("Gmail Details");
		panel = new JPanel();
		
		nameField = new JTextField("Username here");
		nameField.setPreferredSize(new Dimension(200, 30));

		passwordField = new JPasswordField("Password here");
		passwordField.setPreferredSize(new Dimension(200, 30));

		startButton = new JButton("Start");
		startButton.addActionListener(this);
		
		panel.add(nameField);
		panel.add(passwordField);
		panel.add(startButton);

		frame.add(panel);
		frame.setSize(250, 150);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void create(){
		createGUI();
	}
	
	public boolean isFinished(){
		return this.finished;
	}
	
	public String getChosenName(){
		return this.chosenName;
	}
	
	public String getChosenPassword(){
		return this.chosenPassword;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startButton){
			System.out.println("Clicked");
			
			chosenName = nameField.getText();
			
			char[] chars = passwordField.getPassword();
			chosenPassword = "";
			for(char c : chars){
				chosenPassword = chosenPassword + c;
			}

			frame.dispose();
			finished = true;
		}
	}
}