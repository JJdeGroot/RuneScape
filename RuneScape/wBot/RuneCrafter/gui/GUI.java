package org.obduro.runecrafter.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.obduro.runecrafter.types.Altar;
import org.obduro.runecrafter.types.BankLoc;
import org.obduro.runecrafter.types.Pouch;
import org.obduro.runecrafter.types.Essence;

public class GUI extends JFrame {

	private MainPanel mainPanel;
	private EssPanel essPanel;
	private TiaraPanel tiaraPanel;
	private PouchPanel pouchPanel;
	private JPanel panel;
	private JButton startButton;
	
	private Altar chosenAltar;
	private BankLoc chosenBank;
	private Essence chosenEssence;
	private Pouch[] chosenPouches;
	private boolean usingTiara;
	
	public GUI(){
		super("JJ's RuneCrafter GUI");
		setSize(400, 300);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		
		// PANELS		
		panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		
		mainPanel = new MainPanel();
		panel.add(mainPanel);
		
		essPanel = new EssPanel();
		panel.add(essPanel);
		
		tiaraPanel = new TiaraPanel();
		panel.add(tiaraPanel);
		
		pouchPanel = new PouchPanel();
		panel.add(pouchPanel);
		
		// FINISHING FRAME	
		startButton = new JButton("Start");
		startButton.addActionListener(new StartListener());
		
		add(panel, BorderLayout.CENTER);
		add(startButton, BorderLayout.SOUTH);
	}

	public Altar getChosenAltar(){
		return chosenAltar;
	}
	
	public BankLoc getChosenBank(){
		return chosenBank;
	}
	
	public Essence getChosenEssence(){
		return chosenEssence;
	}
	
	public Pouch[] getChosenPouches(){
		return chosenPouches;
	}

	public boolean usingTiara(){
		return usingTiara;
	}
	
	private class StartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Start clicked");
			chosenAltar = mainPanel.getSelectedAltar();
			chosenBank = mainPanel.getSelectedBank();
			chosenEssence = essPanel.getSelectedEssence();
			chosenPouches = pouchPanel.getSelectedPouches();
			usingTiara = tiaraPanel.getSelectedTiaraValue();
			dispose();
		}
		
	}
	
	
}
