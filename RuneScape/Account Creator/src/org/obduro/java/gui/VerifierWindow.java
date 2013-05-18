package org.obduro.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.obduro.java.verifier.AccountVerifier;

public class VerifierWindow extends JDialog implements ActionListener {
	private JLabel infoLabel, usernameLabel, passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton startButton;

	public VerifierWindow(JFrame frame){
		super(frame, "Verifier Window", true);
		setSize(300, 300); // 170 old
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(null);
	
		// INFO
		infoLabel = new JLabel();
		infoLabel.setText("<html><body>Fill in the details of your Gmail account.<br>Hit the start button to start!</body></html>");
		infoLabel.setBounds(5, -5, 300, 50);
		add(infoLabel);
		
		// USERNAME
		usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(5, 40, 75, 25);
		add(usernameLabel);
		
		usernameField = new JTextField("example@gmail.com");
		usernameField.setBounds(75, 40, 200, 25);
		add(usernameField);
		
		// PASSWORD
		passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(5, 70, 75, 25);
		add(passwordLabel);
		
		passwordField = new JPasswordField("password");
		passwordField.setBounds(75, 70, 200, 25);
		add(passwordField);
		
		// START
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setBounds(5, 100, 70, 25);
		add(startButton);
	}
	
	private String getUsername(){
		return usernameField.getText();
	}
	
	private String getPassword(){
		char[] chars = passwordField.getPassword();
		StringBuilder sb = new StringBuilder(chars.length);
		for(char c : chars){
			sb.append(c);
		}
		return sb.toString();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startButton){
			System.out.println("Account Verifier has been started");
			//AccountVerifier verifier = new AccountVerifier(getUsername(), getPassword());
			//verifier.start();
		}
	}
}