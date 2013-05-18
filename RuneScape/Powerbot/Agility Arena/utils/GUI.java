package org.obduro.agilityarena.utils;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame {

	/*
	// Obstacle list
	private final Obstacle[] obstacleList = {Obstacle.PLANK, Obstacle.LOW_WALL, 
			Obstacle.BALANCING_ROPE, Obstacle.LOG_BALANCE, Obstacle.MONKEY_BARS, 
			Obstacle.BALANCING_LEDGE, Obstacle.PILLARS, Obstacle.ROPE_SWING, 
			Obstacle.BLADE, Obstacle.FLOOR_SPIKES, Obstacle.PRESSURE_PAD, 
			Obstacle.HAND_HOLDS, Obstacle.SPINNING_BLADE, Obstacle.STAT_DARTS};
	*/
	// GUI components
	private JPanel panel;
	private GridLayout layout;
	private JLabel statusLabel, healLabel/*, obstacleLabel*/;
	private JTextField healPercentageField;
	//private JCheckBox bankFoodBox;
	private JButton startButton;
	//private JCheckBox[] obstacleBoxes;
	
	// Variables to pass
	private int healPercentage = 15;
	//private boolean bankForFood = false;
	//private boolean[] obstaclePreferences = new boolean[obstacleList.length];
	
	public GUI(){
		super("JJ's Agility Arena Preferences");
		panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		
		// Status
		statusLabel = new JLabel("STATUS: Fill in!");
		panel.add(statusLabel);

		// Eat percentage
		healLabel = new JLabel("Eat percentage");
		panel.add(healLabel);
		
		healPercentageField = new JTextField("15");
		panel.add(healPercentageField);
		
		/*
		// Bank for food
		bankFoodBox = new JCheckBox("Bank for food");
		panel.add(bankFoodBox);
		*/
		
		/*
		// Obstacles
		obstacleLabel = new JLabel("Obstacles to traverse:");
		panel.add(obstacleLabel);
		obstacleBoxes = new JCheckBox[obstacleList.length];
		for(int i = 0; i < obstacleList.length; i++){
			obstacleBoxes[i] = new JCheckBox(obstacleList[i].toString());
			obstacleBoxes[i].setSelected(true);
			panel.add(obstacleBoxes[i]);
		}
		*/
		
		// Start button		
		startButton = new JButton("Start");
		startButton.addActionListener(new MyActionListener());
		panel.add(startButton);

		// Finishing frame
		add(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(200, 200);
		setResizable(true);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
	}
	
	private class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == startButton){
				System.out.println("Start button has been clicked");
				
				// Setting heal percentage
				String percentage = healPercentageField.getText().replaceAll("\\D", "");
				if(percentage.length() > 0){
					int healPercent = Integer.parseInt(percentage);
					System.out.println("Heal percent: " + healPercent);
					if(healPercent > 0 && healPercent < 100){
						System.out.println("Edited healPercentage!");
						healPercentage = healPercent;
						General.setHealPercentage(healPercentage);
					}else{
						statusLabel.setText("STATUS: Wrong %");
						return;
					}
				}else{
					statusLabel.setText("STATUS: Wrong %");
					return;
				}
				
				/*
				// Settings bank preference
				bankForFood = bankFoodBox.isSelected();
				*/
				
				/*
				// Setting obstacle preferences
				for(int i = 0; i < obstacleList.length; i++){
					obstaclePreferences[i] = obstacleBoxes[i].isSelected();
				}
				*/
				
				System.out.println("All fields have been filled in correctly");
				dispose();
			}
		}
	}

	/*
	public boolean[] getObstacleChoices(){
		return this.obstaclePreferences;
	}
	*/
	
	public int getHealPercentage(){
		return healPercentage;
	}
	/*	
	public boolean isBankingEnabled(){
		return this.bankForFood;
	}
	*/

	public static void main(String[] args){
		GUI gui = new GUI();
		gui.setVisible(true);
	}
}

