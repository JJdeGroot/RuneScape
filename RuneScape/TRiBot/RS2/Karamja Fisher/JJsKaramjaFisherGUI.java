package scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JJsKaramjaFisherGUI implements ActionListener {
	final private String[] fishChoices = {"Lobster", "Swordfish", "Tuna"};
	
	private JFrame frame;
	private JPanel panel;
	private JComboBox<String> fishComboBox;
	private JButton startButton;
	private JCheckBox dropBox;
	
	private boolean finished = false;
	private String fishChoice;
	private boolean dropTuna;
	
	private void createGUI(){
		frame = new JFrame("JJ's Karamja Fisher GUI");
		panel = new JPanel();
		
		fishComboBox = new JComboBox<String>(fishChoices);
		fishComboBox.addActionListener(this);
		
		dropBox = new JCheckBox("Drop tuna");
		dropBox.setEnabled(false);
		
		startButton = new JButton("Start");
		startButton.addActionListener(this);

		panel.add(fishComboBox);
		panel.add(dropBox);
		panel.add(startButton);

		frame.add(panel);
		frame.setSize(300, 75);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void create(){
		createGUI();
	}
	
	public boolean isFinished(){
		return this.finished;
	}
	
	public String getFish(){
		return this.fishChoice;
	}
	
	public boolean areDropping(){
		return this.dropTuna;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startButton){
			fishChoice = (String)fishComboBox.getSelectedItem();
			if(fishChoice.equals("Swordfish")){
				dropTuna = dropBox.isSelected();
			}
			//System.out.println("Fish choice: " + fishChoice);
			//System.out.println("Dropping? " + dropTuna);
			
			frame.dispose();
			finished = true;
		}else{
			if(e.getSource() == fishComboBox){
				fishChoice = (String)fishComboBox.getSelectedItem();
				if(fishChoice.equals("Swordfish")){
					if(!dropBox.isEnabled()){
						dropBox.setEnabled(true);
					}
				}else{
					if(dropBox.isEnabled()){
						dropBox.setEnabled(false);
					}
				}
			}
		}
	}
}