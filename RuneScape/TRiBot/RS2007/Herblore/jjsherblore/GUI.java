package scripts.jjsherblore;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/** GUI for JJ's Herblore */
public class GUI extends JFrame {
	
	private static final long serialVersionUID = -7630015234944014767L;
	private JTextField item1Field, item2Field;
	private JButton startButton;
	private int item1ID, item2ID;

	/** Creates the GUI for JJ's herblore */
	public GUI(){
		super("JJ's Herblore GUI");
		setSize(225, 150);
		setLayout(new GridLayout(5, 1));
		
		// Interface for item #1
		JLabel item1Label = new JLabel("Item #1 ID:");
		add(item1Label);
		
		item1Field = new JTextField();
		add(item1Field);
		
		// Interface for item #2
		JLabel item2Label = new JLabel("Item #2 ID:");
		add(item2Label);
		
		item2Field = new JTextField();
		add(item2Field);
		
		// Start button
		startButton = new JButton("Start script!");
		startButton.addActionListener(new StartListener());
		add(startButton);
	}

	/** Returns the ID of item #1 */
	public int getItem1ID(){
		return item1ID;
	}
	
	/** Returns the ID of item #2 */
	public int getItem2ID(){
		return item2ID;
	}
	
	/** Listens to clicks on the start button */
	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Checking item #1 text field
			String item1 = item1Field.getText();
			if(item1.length() > 0 && item1.matches("\\d+")){
				item1ID = Integer.parseInt(item1);
				
				// Checking item #2 text field
				String item2 = item2Field.getText();
				if(item2.length() > 0 && item2.matches("\\d+")){
					item2ID = Integer.parseInt(item2);
					dispose();
				}else{
					item2Field.setForeground(Color.RED);
					item2Field.setText("Numbers only!");
				}
			}else{
				item1Field.setForeground(Color.RED);
				item1Field.setText("Numbers only!");
			}
		}
		
	}
	
	
}
