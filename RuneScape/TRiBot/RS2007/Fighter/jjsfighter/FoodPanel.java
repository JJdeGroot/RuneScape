package scripts.jjsfighter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.tribot.api2007.Constants.IDs.Items;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

public class FoodPanel extends JPanel {
	
	private JPanel addPanel;
	private ClickListener clickListener;
	
	private JLabel addText;
	private JTextField enterField;
	private JButton addButton, removeButton;
	
	private ArrayList<Integer> foodIDs;
	private JList<String> foodList;
	private JScrollPane foodScroll;
	
	public FoodPanel(){
		setLayout(new BorderLayout());
		
		// panels
		addPanel = new JPanel(new BorderLayout());
		
		// listeners
		clickListener = new ClickListener();
		
		// addpanel
		addText = new JLabel("Food ID:");
		addPanel.add(addText, BorderLayout.WEST);
		enterField = new JTextField();
		addPanel.add(enterField, BorderLayout.CENTER);
		addButton = new JButton("Add to list");
		addButton.addActionListener(clickListener);
		addPanel.add(addButton, BorderLayout.EAST);
		add(addPanel, BorderLayout.NORTH);
		
		// list 
		foodIDs = new ArrayList<Integer>();
		foodList = new JList<String>();
		foodScroll = new JScrollPane(foodList);
		updateFoodModel();
		add(foodScroll, BorderLayout.CENTER);

		// remove
		removeButton = new JButton("Remove selected item(s)");
		removeButton.addActionListener(clickListener);
		add(removeButton, BorderLayout.SOUTH);
	
		
	}

	/** Updates the food jlist */
	private void updateFoodModel(){
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(int foodID : foodIDs)
			model.addElement("Item with ID: " + foodID);
		foodList.setModel(model);
	}
	
	/** Handles the clicking events on buttons */
	private class ClickListener implements ActionListener {

		/** Checking what button has been clicked */
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			
			if(button.equals(addButton)){
				System.out.println("Add food button has been clicked!");
				// Adding entered id to the list
				String text = enterField.getText();
				try {
					int id = Integer.parseInt(text);
					foodIDs.add(id);
					updateFoodModel();
					if(enterField.getBackground().equals(Color.RED)){
						enterField.setBackground(Color.WHITE);
					}
					enterField.setText("");
				} catch (NumberFormatException nfe) {
					System.out.println("You can only enter numbers!");
					enterField.setText("Numbers only!");
					enterField.setBackground(Color.RED);
				}
			}else if(button.equals(removeButton)){
				// Removing all selected indexes from the food list
				System.out.println("Remove food button has been clicked!");
				int[] selected = foodList.getSelectedIndices();
				
				if(selected != null && selected.length > 0){
					for(int i = 0; i < selected.length; i++)
						foodIDs.remove(selected[i]-i);
					updateFoodModel();
				}
			}
		}
		
	}

}
