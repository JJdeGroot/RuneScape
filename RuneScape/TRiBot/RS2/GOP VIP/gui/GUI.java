package scripts.jjsgopvip.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {

	private final String[] teams = {"Green team", "Yellow team", "Random team"};

	private JFrame frame;
	private JLabel teamLabel;
	private JComboBox<String> teamBox;
	private JButton startButton;
	
	private String chosenTeam = teams[0];
	private boolean finished = false;
	
	public void create(){
		frame = new JFrame("JJ's Gop GUI");
		frame.setSize(250, 125);
		frame.setLayout(new GridLayout(2, 1));
		
		// TEAM PANEL
		JPanel teamPanel = new JPanel(new GridLayout(1, 2));
		teamLabel = new JLabel("Team:");
		teamPanel.add(teamLabel);
	
		teamBox = new JComboBox<String>(teams);
		teamPanel.add(teamBox);
		
		// START PANEL
		JPanel startPanel = new JPanel();
		startButton = new JButton("Start playing GOP");
		startButton.addActionListener(new StartListener());
		startPanel.add(startButton);
		
		// FINISHING
		frame.add(teamPanel);
		frame.add(startPanel);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}
	
	public String getChosenTeam(){
		return chosenTeam;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	private class StartListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("TeamFrame -> Start button has been clicked!");
			chosenTeam = teams[teamBox.getSelectedIndex()];
			finished = true;
			frame.dispose();
		}
	}
	
	public static void main(String[] args){
		GUI g = new GUI();
		g.create();
	}
	
}