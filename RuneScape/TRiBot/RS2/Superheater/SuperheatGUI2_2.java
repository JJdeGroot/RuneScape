package scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SuperheatGUI2_2 extends JFrame{
	SuperheatDTMs dtms = new SuperheatDTMs();
	JJsSuperheater3 main = new JJsSuperheater3();

	public SuperheatGUI2_2(){
		createGUI();
	}
	
	public void createGUI(){
		String[] bars = {"Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune"},
				 bankTypes = {"Chest", "Banker", "Bankbooth"};
		
		// The frame
		final JFrame frame = new JFrame("JJ's Superheater");
		frame.setSize(310, 165);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// The panel
		JPanel panel = new JPanel();
		JLabel barInfo = new JLabel("Select the bar of choice");
    	JComboBox<String> barChoice = new JComboBox<String>(bars);
    	JCheckBox checkCoalbag = new JCheckBox("Use coalbag");
    	JCheckBox wearGauntlets = new JCheckBox("Wearing Goldsmith gauntlets");
		JLabel bankInfo = new JLabel("Select the banktype of choice");
		JComboBox<String> bankChoice = new JComboBox<String>(bankTypes);
		JButton start = new JButton("Click me to start");
		
		// Adding action listener to start
		start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add the selected options to our data
            	 //coalbag = checkCoalbag.getModel().isSelected();
            	 //gauntlets = wearGauntlets.getModel().isSelected(); 
                 //selectedBar = (String) bar.getSelectedItem();
                 //selectedBank = (String) bankType.getSelectedItem();

                // Start script and hide GUI
                //GO = true;
                //START_TIME = System.currentTimeMillis();
                frame.setVisible(false);
                dtms.guiCompleted = true;
               // main.guiDone = true;
            }
        });
		
        // Adding them to the panel
        panel.add(barInfo);
        panel.add(barChoice);
        panel.add(checkCoalbag);
        panel.add(wearGauntlets);
        panel.add(bankInfo);
        panel.add(bankChoice);
        panel.add(start);

        // Adding them to the frame
		frame.add(panel);
		frame.setVisible(true);
	}
	
	/*
	public static void main(String args[]){
    	final SuperheatGUI2 Gui = new SuperheatGUI2();
    	Gui.createGUI();
     }
     */
}
