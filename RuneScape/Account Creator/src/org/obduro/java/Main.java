package org.obduro.java;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.obduro.java.gui.CreatorWindow;
import org.obduro.java.gui.HelpWindow;
import org.obduro.java.gui.VerifierWindow;

public class Main extends JFrame implements ActionListener{
	JMenuBar menubar;
	JMenu help;
	JMenuItem about;
	JButton creator, verifier;
	
	public Main() {
		setLayout(new FlowLayout());
		
		menubar = new JMenuBar();
		add(menubar);
		
		help = new JMenu("Help");
		menubar.add(help);
		
		about = new JMenuItem("About");
		about.addActionListener(this);
		help.add(about);
		
		setJMenuBar(menubar);
		
		creator = new JButton("Creator");
		creator.addActionListener(this);
		add(creator);
		
		verifier = new JButton("Verifier");
		verifier.addActionListener(this);
		add(verifier);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == about){
			HelpWindow hw = new HelpWindow(Main.this);
			hw.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			hw.setSize(300, 100);
			hw.setLocationRelativeTo(this);
			hw.setTitle("Help Window");
			hw.setVisible(true);
		}else if(e.getSource() == creator){
			CreatorWindow cw = new CreatorWindow(Main.this);
			cw.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			cw.setSize(300, 300);
			cw.setLocationRelativeTo(this);
			cw.setTitle("Creator Window");
			cw.setVisible(true);
			return;
		}else if (e.getSource() == verifier){
			VerifierWindow vw = new VerifierWindow(Main.this);
			vw.setVisible(true);
			return;
		}
	}
	
	public static void main(String[] args){
		Main main = new Main();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(350, 100);
		main.setTitle("JJ's Account Creator Tool");
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
}
