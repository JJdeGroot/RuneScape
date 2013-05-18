package scripts.jjsfighter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame {
	
	private TargetPanel targetPanel;
	private FoodPanel foodPanel;
	
	public GUI(){
		super("JJ's Fighter GUI");
		setSize(600, 450);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(2, 1));
		
		targetPanel = new TargetPanel();
		add(targetPanel);
		
		foodPanel = new FoodPanel();
		add(foodPanel);

	}
	
	public static void main(String[] args){
		GUI g = new GUI();
		g.setVisible(true);
	}
	
	
	
	
	
	
}