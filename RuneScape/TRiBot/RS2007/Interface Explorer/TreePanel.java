package scripts.jjsinterfaceexplorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.tribot.api.General;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;

public class TreePanel extends JPanel {
	
	private JPanel searchPanel;
	private JLabel searchLabel;
	private JTextField searchField;
	private JButton searchButton;
	
	private JScrollPane scrollPane;
	private InfoPanel infoPanel;
	
	private JTree tree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;

	private RSInterfaceMaster[] masterInterfaces;
	private RSInterfaceChild[][] childInterfaces;
	private RSInterfaceComponent[][][] componentInterfaces;

	private DefaultMutableTreeNode[] masterNodes;
	private DefaultMutableTreeNode[][] childNodes;
	private DefaultMutableTreeNode[][][] componentNodes;
	
	public TreePanel(InfoPanel infoPanel){
		setLayout(new BorderLayout());
		this.infoPanel = infoPanel;
		
		// SEARCH PANEL
		searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchLabel = new JLabel("Enter search term:");
		searchPanel.add(searchLabel, BorderLayout.WEST);
		searchField = new JTextField();
		searchField.addKeyListener(new EnterListener());
		searchPanel.add(searchField, BorderLayout.CENTER);
		searchButton = new JButton("Search");
		searchButton.addActionListener(new SearchListener());
		searchPanel.add(searchButton, BorderLayout.EAST);
		add(searchPanel, BorderLayout.NORTH);
		
		// TREE PANEL
		root = new DefaultMutableTreeNode("RSInterface Tree");
		tree = new JTree(root);
		tree.addTreeSelectionListener(new SelectionListener());
		treeModel = (DefaultTreeModel) tree.getModel();
		createTree(false);
		
		scrollPane = new JScrollPane(tree,  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(300, 445));
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private DefaultMutableTreeNode addNode(String name, DefaultMutableTreeNode parent){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
		parent.add(node);
		return node;
	}
	
	public void createTree(boolean clear){
		if(clear){
			//System.out.println("Clearing tree");

			for(DefaultMutableTreeNode node : masterNodes){
				treeModel.removeNodeFromParent(node);
			}
		}

		// MASTER
		masterInterfaces = Interfaces.getAll();
		if(masterInterfaces != null && masterInterfaces.length > 0){
			masterNodes = new DefaultMutableTreeNode[masterInterfaces.length];
			// CHILD
			childInterfaces = new RSInterfaceChild[masterInterfaces.length][];
			childNodes = new DefaultMutableTreeNode[masterInterfaces.length][];
			// COMPONENT
			componentInterfaces = new RSInterfaceComponent[masterInterfaces.length][][];
			componentNodes = new DefaultMutableTreeNode[masterInterfaces.length][][];
	
			// MASTER INTERFACES
			for(int i = 0; i < masterInterfaces.length; i++){
				masterNodes[i] = addNode("RSInterfaceMaster #" + masterInterfaces[i].getIndex(), root);
				// Item check
				RSItem[] masterItems = masterInterfaces[i].getItems();
				if(masterItems != null && masterItems.length > 0){
					System.out.println("Master interface #" + masterInterfaces[i].getIndex());
					for(RSItem item : masterItems){
						System.out.println("Item id: " + item.getID());
					}
				}

				// CHILD INTERFACES
				childInterfaces[i] = masterInterfaces[i].getChildren();
				if(childInterfaces[i] != null && childInterfaces[i].length > 0){
					childNodes[i] = new DefaultMutableTreeNode[childInterfaces[i].length];
					// COMPONENT
					componentInterfaces[i] = new RSInterfaceComponent[childInterfaces[i].length][];
					componentNodes[i] = new DefaultMutableTreeNode[childInterfaces[i].length][];
				
					for(int j = 0; j < childInterfaces[i].length; j++){
			
						childNodes[i][j] = addNode("RSInterfaceChild #" + childInterfaces[i][j].getIndex(), masterNodes[i]);
						// Item check
						RSItem[] childItems = childInterfaces[i][j].getItems();
						if(childItems != null && childItems.length > 0){
							System.out.println("Child interface #" + childInterfaces[i][j].getIndex());
							for(RSItem item : childItems){
								System.out.println("Item id: " + item.getID());
							}
						}
						
						// COMPONENT INTERFACES
						componentInterfaces[i][j] = childInterfaces[i][j].getChildren();
						if(componentInterfaces[i][j] != null && componentInterfaces[i][j].length > 0){
							componentNodes[i][j] = new DefaultMutableTreeNode[componentInterfaces[i][j].length];
							for(int k = 0; k < componentInterfaces[i][j].length; k++){
								componentNodes[i][j][k] = addNode("RSInterfaceComponent #" + componentInterfaces[i][j][k].getIndex(), childNodes[i][j]);
								// Item check
								RSItem[] compItems = componentInterfaces[i][j][k].getItems();
								if(compItems != null && compItems.length > 0){
									System.out.println("Component interface #" + componentInterfaces[i][j][k].getIndex());
									for(RSItem item : compItems){
										System.out.println("Item id: " + item.getID());
									}
								}
								
							}
						}
					}
				}
			}
		}
		
		treeModel.reload();
		revalidate();
		repaint();
		
		//System.out.println("Succesfully created tree");
	}
	
	private class SelectionListener implements TreeSelectionListener {

		private RSInterface getInterface(TreeNode node){
			//System.out.println("getInterface(" + node + ")");
	
			if(masterNodes != null && masterNodes.length > 0)
				for(int i = 0; i < masterNodes.length; i++)
					if(masterNodes[i] != null && masterNodes[i].equals(node)){
						//System.out.println("Node equals masterNodes[" + i + "]");
						return masterInterfaces[i];
					}
				
			if(childNodes != null && childNodes.length > 0)
				for(int i = 0; i < childNodes.length; i++)
					if(childNodes[i] != null && childNodes[i].length > 0)
						for(int j = 0; j < childNodes[i].length; j++)
							if(childNodes[i][j] != null && childNodes[i][j].equals(node)){
								//System.out.println("Node equals childNodes[" + i + "][" + j + "]");
								return childInterfaces[i][j];
							}

			if(componentNodes != null && componentNodes.length > 0)
				for(int i = 0; i < componentNodes.length; i++)
					if(componentNodes[i] != null && componentNodes[i].length > 0)
						for(int j = 0; j < componentNodes[i].length; j++)
							if(componentNodes[i][j] != null && componentNodes[i][j].length > 0)
								for(int k = 0; k < componentNodes[i][j].length; k++)
									if(componentNodes[i][j][k] != null && componentNodes[i][j][k].equals(node)){
										//System.out.println("Node equals compNodes[" + i + "][" + j + "[" + k + "]");
										return componentInterfaces[i][j][k];
									}
			
			//System.out.println("Couldn't find the correct interface for " + node);
			return null;
		}
		
		
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			//System.out.println("Selected item!");
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
			
			//System.out.println("Node: " + node);
			RSInterface selectedInterface = getInterface(node);
			//System.out.println("Selected interface: " + selectedInterface);
			if(selectedInterface != null){
				Rectangle absoluteBounds = selectedInterface.getAbsoluteBounds();
				RSInterface absoluteParent = selectedInterface.getAbsoluteParent();
				Point absolutePosition = selectedInterface.getAbsolutePosition();
				String[] actions = selectedInterface.getActions();
				RSInterface[] children = selectedInterface.getChildren();
				int componentIndex = selectedInterface.getComponentIndex();
				int componentItem = selectedInterface.getComponentItem();
				int componentStack = selectedInterface.getComponentStack();
				int height = selectedInterface.getHeight();
				int index = selectedInterface.getIndex();
				RSItem[] items = selectedInterface.getItems();
				int modelID = selectedInterface.getModelID();
				int modelType = selectedInterface.getModelType();
				int modelZoom = selectedInterface.getModelZoom();
				RSInterface parent = selectedInterface.getParent();
				int parentID = selectedInterface.getParentID();
				int rotationX = selectedInterface.getRotationX();
				int rotationY = selectedInterface.getRotationY();
				int rotationZ = selectedInterface.getRotationZ();
				int scrollMaxH = selectedInterface.getScrollMaxH();
				int scrollMaxV = selectedInterface.getScrollMaxV();
				String text = selectedInterface.getText();
				int textureID = selectedInterface.getTextureID();
				int uid = selectedInterface.getUID();
				int width = selectedInterface.getWidth();
				int x = selectedInterface.getX();
				int y = selectedInterface.getY();
	
				infoPanel.setText(absoluteBounds, absoluteParent, absolutePosition,
						actions, children, componentIndex, componentItem,
						componentStack, height, index, items, modelID, modelType,
						modelZoom, parent, parentID, rotationX, rotationY,
						rotationZ, scrollMaxH, scrollMaxV, text, textureID, uid,
						width, x, y);
			}
		}
		
	}
	
	private class SearchListener implements ActionListener {

		private boolean search(String text){
			System.out.println("Search started");
			
			if(masterInterfaces != null && masterInterfaces.length > 0)
				for(int i = 0; i < masterInterfaces.length; i++)
					if(masterInterfaces[i] != null){
						String s = masterInterfaces[i].getText();
						if(s != null && s.toLowerCase().contains(text)){
							System.out.println("FOUND TEXT!");
	
							//tree.clearSelection();
							TreePath path = new TreePath(masterNodes[i].getPath());
							System.out.println("Path: " + path);
							
							tree.setSelectionPath(path);
							tree.expandPath(path);
							//tree.scrollPathToVisible(path);
						
							return true;
						}
					}
				
			if(childInterfaces != null && childInterfaces.length > 0)
				for(int i = 0; i < childInterfaces.length; i++)
					if(childInterfaces[i] != null && childInterfaces[i].length > 0)
						for(int j = 0; j < childInterfaces[i].length; j++)
							if(childInterfaces[i][j] != null){
								String s = childInterfaces[i][j].getText();
								if(s != null && s.toLowerCase().contains(text)){
									System.out.println("FOUND TEXT!");
									
									//tree.clearSelection();
									TreePath path = new TreePath(childNodes[i][j].getPath());
									System.out.println("Path: " + path);
									
									tree.setSelectionPath(path);
									tree.expandPath(path);
									//tree.scrollPathToVisible(path);
									
									return true;
								}
							}

			if(componentInterfaces != null && componentInterfaces.length > 0)
				for(int i = 0; i < componentInterfaces.length; i++)
					if(componentInterfaces[i] != null && componentInterfaces[i].length > 0)
						for(int j = 0; j < componentInterfaces[i].length; j++)
							if(componentInterfaces[i][j] != null && componentInterfaces[i][j].length > 0)
								for(int k = 0; k < componentInterfaces[i][j].length; k++)
									if(componentInterfaces[i][j][k] != null){
										String s = componentInterfaces[i][j][k].getText();
										if(s != null && s.toLowerCase().contains(text)){
											System.out.println("FOUND TEXT!");
											
											//tree.clearSelection();
											TreePath path = new TreePath(componentNodes[i][j][k].getPath());
											System.out.println("Path: " + path);

											tree.setSelectionPath(path);
											tree.expandPath(path);
											//tree.scrollPathToVisible(path);
											
											return true;
										}
									}
			
			System.out.println("Search ended");
			return false;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			System.out.println("SEARCH CLICKED");
			String text = searchField.getText();
			if(text != null && text.length() > 0){
				text = text.toLowerCase();
				System.out.println("Searching for: " + text);
				if(search(text)){
					System.out.println("Reloading");
					treeModel.reload();
					revalidate();
				}
			}
		}
		
	}
	
	private class EnterListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				System.out.println("Enter pressed!");
				searchButton.doClick(General.random(50, 150));
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
	}
	
}
