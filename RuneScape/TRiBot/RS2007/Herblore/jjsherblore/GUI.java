package scripts.jjsherblore;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/** GUI for JJ's Herblore */
public class GUI extends JFrame {
	
	private static final long serialVersionUID = -7630015234944014767L;
	private JComboBox<Method> methodBox;
	private JLabel item1Label, item2Label;
	private JTextField item1Field, item2Field;
	private JButton startButton;
	
	private int item1ID, item2ID;
	private Method method;
	private boolean done = false;

	/** Creates the GUI for JJ's herblore */
	public GUI(){
		super("JJ's Herblore GUI");
		setSize(250, 175);
		setLayout(new GridLayout(7, 1));

		// Method interface
		JLabel methodLabel = new JLabel("Method:");
		add(methodLabel);
		
		methodBox = new JComboBox<Method>(Method.values());
		methodBox.addActionListener(new MethodListener());
		add(methodBox);
		
		// Interface for item #1
		item1Label = new JLabel("Item #1 ID:");
		add(item1Label);
		
		item1Field = new JTextField();
		add(item1Field);
		
		// Interface for item #2
		item2Label = new JLabel("Item #2 ID:");
		add(item2Label);
		
		item2Field = new JTextField();
		add(item2Field);
		
		// Start button
		startButton = new JButton("Start script!");
		startButton.addActionListener(new StartListener());
		add(startButton);
		
		setLocationRelativeTo(null);
	}
	
	/** Returns the method */
	public Method getMethod(){
		return method;
	}

	/** Returns the ID of item #1 */
	public int getItem1ID(){
		return item1ID;
	}
	
	/** Returns the ID of item #2 */
	public int getItem2ID(){
		return item2ID;
	}
	
	public boolean isDone(){
		return done;
	}
	
	/** Checks if a JTextField contains a number */
	private boolean containsNumber(JTextField field){
		String text = field.getText();
		if(text.length() == 0 || !text.matches("\\d+")){
			field.setForeground(Color.RED);
			field.setText("Numbers only!");
			return false;
		}
		return true;
	}
	
	/** Returns the value of the field as an integer */
	private int getNumber(JTextField field){
		return Integer.parseInt(field.getText());
	}
	
	/** Listens to clicks on the start button */
	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Start button clicked!");
			method = Method.values()[methodBox.getSelectedIndex()];
			done = true;
			
			switch(method){
				case GRINDING:
					if(containsNumber(item1Field)){
						item1ID = getNumber(item1Field);
						dispose();
					}
					break;
				
				case MIXING:
					if(containsNumber(item1Field) && containsNumber(item2Field)){
						item1ID = getNumber(item1Field);
						item2ID = getNumber(item2Field);
						dispose();
					}
					break;
					
				case CLEANING:
					if(containsNumber(item1Field)){
						item1ID = getNumber(item1Field);
						dispose();
					}
					break;
					
				case DECANTING:
					if(containsNumber(item1Field)){
						item1ID = getNumber(item1Field);
						dispose();
					}
					break;
			}
		}
		
	}
	
	/** Listens to changes on the JComboBox */
	private class MethodListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Method changed!");
			
			Method method = Method.values()[methodBox.getSelectedIndex()];
			switch(method){
				case CLEANING:
					item1Label.setText("Herb ID:");
					item2Label.setVisible(false);
					item2Field.setVisible(false);
					break;
					
				case GRINDING:
					item1Label.setText("Item ID:");
					item2Label.setVisible(false);
					item2Field.setVisible(false);
					break;
					
				case MIXING:
					item1Label.setText("Item #1 ID:");
					item2Label.setText("Item #2 ID:");
					item2Label.setVisible(true);
					item2Field.setVisible(true);
					break;
					
				case DECANTING:
					item1Label.setText("Potion ID:");
					item2Label.setVisible(false);
					item2Field.setVisible(false);
					break;
			}

			revalidate();
		}
		
	}
	
	
}
