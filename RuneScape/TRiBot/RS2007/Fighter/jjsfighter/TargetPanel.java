package scripts.jjsfighter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSNPC;

/** Constructs a panel where the user can select the targets to attack */
public class TargetPanel extends JPanel {

	private JPanel infoPanel, listPanel, buttonPanel;
	private JLabel monsterInfoLabel, targetInfoLabel;
	private JButton addMonsterButton, removeTargetButton;
	
	private JScrollPane monsterScroll, targetScroll;
	private JList<String> monsterList, targetList;
	private ArrayList<RSNPC> monsters, targets;
	
	private ClickListener clickListener;

	/** Sets up all components */
	public TargetPanel(){
		setLayout(new BorderLayout());

		// Panels
		infoPanel = new JPanel(new GridLayout(1, 2));
		listPanel = new JPanel(new GridLayout(1, 2));
		buttonPanel = new JPanel(new GridLayout(1, 2));
		
		// Listeners
		clickListener = new ClickListener();
		
		// Monster info
		monsterInfoLabel = new JLabel("Available monsters:");
		infoPanel.add(monsterInfoLabel);
		// Monster list
		monsters = new ArrayList<RSNPC>();
		fillMonsters();
		monsterList = new JList<String>();
		monsterScroll = new JScrollPane(monsterList);
		updateMonsterModel();
		listPanel.add(monsterScroll);
		// Monster buttons
		addMonsterButton = new JButton("Add selected monster(s)");
		addMonsterButton.addActionListener(clickListener);
		buttonPanel.add(addMonsterButton);
		
		// Target info
		targetInfoLabel = new JLabel("Target monsters:");
		infoPanel.add(targetInfoLabel);
		// Target list
		targets = new ArrayList<RSNPC>();
		targetList = new JList<String>();
		targetScroll = new JScrollPane(targetList);
		listPanel.add(targetScroll);
		// Target buttons
		removeTargetButton = new JButton("Remove selected target(s)");
		removeTargetButton.addActionListener(clickListener);
		buttonPanel.add(removeTargetButton);
	
		// Adding panels
		add(infoPanel, BorderLayout.NORTH);
		add(listPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/** Fills the monster list with the available npcs to kill */
	private void fillMonsters(){
		monsters.clear();
		RSNPC[] npcs = NPCs.getAll();
		if(npcs != null && npcs.length > 0)
			for(RSNPC npc : npcs)
				monsters.add(npc);
	}
	
	/** Updates the monster jlist */
	private void updateMonsterModel(){
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(RSNPC monster : monsters)
			model.addElement(monster.getName() + " (ID: " + monster.getID() + ")");
		monsterList.setModel(model);
	}
	
	/** Updates the target jlist */
	private void updateTargetModel(){
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(RSNPC target : targets)
			model.addElement(target.getName() + " (ID: " + target.getID() + ")");
		targetList.setModel(model);
	}
	
	/** Handles the clicking events on buttons */
	private class ClickListener implements ActionListener {

		/** Checking what button has been clicked */
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			
			if(button.equals(addMonsterButton)){
				// Adding all selected indexes to the target list
				System.out.println("Add monster button has been clicked!");
				int[] selected = monsterList.getSelectedIndices();
				
				if(selected != null && selected.length > 0){
					for(int i : selected)
						targets.add(monsters.get(i));
					updateTargetModel();
				}
			}else if(button.equals(removeTargetButton)){
				// Removing all selected indexes from the target list
				System.out.println("Remove target button has been clicked!");
				int[] selected = targetList.getSelectedIndices();
				
				if(selected != null && selected.length > 0){
					ArrayList<RSNPC> removeList = new ArrayList<RSNPC>();
					for(int i : selected)
						removeList.add(targets.get(i));
					for(RSNPC npc : removeList)
						targets.remove(npc);
					updateMonsterModel();
				}
			}
		}
		
	}

}
