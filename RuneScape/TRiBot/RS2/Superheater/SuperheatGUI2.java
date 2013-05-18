package scripts;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SuperheatGUI2 extends JFrame{
	static boolean guiDone = false;
	static boolean coalbag = false;
	static boolean gauntlets = false;
	static String selectedBank = "";
	static String selectedBar = "";
	
	public void createGUI(){
		String[] bars = {"Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune"},
				 bankTypes = {"Chest", "Banker", "Bankbooth"};
		
		// The frame
		final JFrame frame = new JFrame("JJ's Superheater");
		frame.setSize(320, 175);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// The panel
		JPanel panel = new JPanel();
		JLabel barInfo = new JLabel("Select the bar of choice");
    	final JComboBox<String> barChoice = new JComboBox<String>(bars);
    	final JCheckBox checkCoalbag = new JCheckBox("Use coalbag");
    	final JCheckBox wearGauntlets = new JCheckBox("Wearing Goldsmith gauntlets");
		JLabel bankInfo = new JLabel("Select the banktype of choice");
		final JComboBox<String> bankChoice = new JComboBox<String>(bankTypes);
		JButton start = new JButton("Click me to start");
		
		// Adding action listener to start
		class ClickListener implements ActionListener{
			public void actionPerformed(ActionEvent event){
				guiDone = true;
				System.out.println("I was clicked");
				
				coalbag = checkCoalbag.getModel().isSelected();
				selectedBar = (String) barChoice.getSelectedItem();
				gauntlets = wearGauntlets.getModel().isSelected();
				selectedBank = (String) bankChoice.getSelectedItem();
				
				frame.setVisible(false);
				frame.dispose();
			}
		}
		
		ActionListener listener = new ClickListener();
		start.addActionListener(listener);
		
		
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
		
		// Calculating the position
		Window[] windows = Window.getWindows();
		for (int i = 0; i < windows.length; i++){
			Point size = new Point(windows[i].getSize().width, windows[i].getSize().height);
			if (size.x > 760 && size.y > 800){
				Point loc = windows[i].getLocation();
				//Point P = new Point(loc.x + size.x/2, loc.y + size.y/2);
				Point P = new Point(loc.x + 100, loc.y + 200);
				frame.setLocation(P);
				break;
			}
		}

		frame.setVisible(true);
	}
	
	
	public static void main(String args[]){
		java.awt.EventQueue.invokeLater(new Runnable(){
   			@Override
			public void run() {
				SuperheatGUI2 g = new SuperheatGUI2();
				g.createGUI();
				System.out.println("Gui started");			
			}
	   	});
		
		while (!guiDone){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Gui closed");
		
		// Printing out the data from the GUI
		if (coalbag){
			System.out.println("Coalbag is selected");
		}else{
			System.out.println("Coalbag isnt selected");
		}
		
		System.out.println("You have chosen: " + selectedBar);
		
		if (gauntlets){
			System.out.println("Gauntlets is selected");
		}else{
			System.out.println("Gauntlets aren't selected");
		}
		
		System.out.println("You have chosen: " + selectedBank);
		
		
		// Sleeping to show results.
		try {
			Thread.sleep(5000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     
}
