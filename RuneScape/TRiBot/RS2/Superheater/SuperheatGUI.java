package scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
 
public class SuperheatGUI extends JFrame{
	final String[] bars = {"Bronze", "Iron", "Silver", "Steel",
			               "Gold", "Mithril", "Adamant", "Rune"},
			       bankTypes = {"Chest", "Banker", "Bankbooth"};

	
	public boolean coalbag, gauntlets;
	public String selectedBar, selectedBank;
	public boolean completedGUI = false;

	public void createComponents() {
		// The frame
		final JFrame frame = new JFrame("JJ's Superheater");
		frame.setSize(310, 165);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
		// Stuff on the panel
		JButton start;
		JLabel barInfo, bankInfo;
		JCheckBox checkCoalbag;
		JCheckBox wearGauntlets;
		JComboBox<String> bar, bankType;
		
		JPanel panel = new JPanel();
		barInfo = new JLabel("Select the bar of choice");
    	bar = new JComboBox<String>();
        for (int i = 0; i < bars.length; i++){
        	bar.addItem(bars[i]);
        }
        
        bankInfo = new JLabel("Select the banktype of choice");
        bankType = new JComboBox<String>();
        for (int i = 0; i < bankTypes.length; i++){
            bankType.addItem(bankTypes[i]);
        }
        
        checkCoalbag = new JCheckBox("Use coalbag");
        wearGauntlets = new JCheckBox("Wearing Goldsmith gauntlets");
        start = new JButton("Click me to start");
 
        // Adding them to the panel
        panel.add(barInfo);
        panel.add(bar);
        panel.add(checkCoalbag);
        panel.add(wearGauntlets);
        panel.add(bankInfo);
        panel.add(bankType);
        panel.add(start);
        frame.add(panel);
         
        System.out.println("added all");

        /*
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add the selected options to our data
            	 coalbag = checkCoalbag.getModel().isSelected();
            	 gauntlets = wearGauntlets.getModel().isSelected(); 
                 selectedBar = (String) bar.getSelectedItem();
                 selectedBank = (String) bankType.getSelectedItem();

                // Start script and hide GUI
                //GO = true;
                //START_TIME = System.currentTimeMillis();
                frame.setVisible(false);
                completedGUI = true;
            }
        });
        */
	}

    public static void main(String args[]){
    	final SuperheatGUI Gui = new SuperheatGUI();
    	Gui.createComponents();
     }
    	
    	
    /*
        JFrame frame = new SuperheatGUI();
        frame.setTitle("JJ's Superheater GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
      */
}
