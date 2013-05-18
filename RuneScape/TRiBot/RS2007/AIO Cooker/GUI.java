package scripts.jjscooker07;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI extends JFrame {

	private JLabel idLabel;
	private JTextField idField;
	private JButton startButton;
	
	private boolean done = false;
	private int id = 335;

	public GUI(){
		super("JJ's Cooker GUI");
		setSize(200, 80);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setResizable(false);
		setLayout(new BorderLayout());
		
		idLabel = new JLabel("Raw Food ID:");
		add(idLabel, BorderLayout.WEST);
		
		idField = new JTextField();
		add(idField, BorderLayout.CENTER);
		
		startButton = new JButton("Start");
		startButton.addActionListener(new StartListener());
		add(startButton, BorderLayout.SOUTH);
	}
	
	public int getID(){
		return id;
	}
	
	public boolean isStartPressed(){
		return done;
	}
	
	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Start pressed!");
			
			String text = idField.getText();
			try {
				int typedID = Integer.parseInt(text);
				id = typedID;
				done = true;
				dispose();
			} catch (NumberFormatException nfe) {
				idField.setText("Only enter numbers, please");
			}
		}
		
	}
	
	
}
