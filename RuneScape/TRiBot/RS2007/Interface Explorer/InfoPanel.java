package scripts.jjsinterfaceexplorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;

public class InfoPanel extends JPanel {

	private JCheckBox imageCheckBox;
	private JPanel labelPanel, detailPanel;
	private JScrollPane scrollPane;
	private JLabel[] labels;
	private JLabel imageLabel;
	
	private boolean drawing = true;
	private Rectangle bounds = null;
	
	public InfoPanel(){
		setLayout(new BorderLayout());
		addCheckBox();
	}
	
	public void setDrawing(boolean value){
		drawing = value;
	}
	
	public void addCheckBox(){
		imageCheckBox = new JCheckBox("Draw image of interface area");
		imageCheckBox.setSelected(drawing);
		imageCheckBox.addActionListener(new CheckListener());
		add(imageCheckBox, BorderLayout.NORTH);
	}
	
	public void addImagePanel(){
		if(bounds.x >= 0 && bounds.x+bounds.height <= 765 && bounds.y >= 0 && bounds.y+bounds.height <= 503){
			imageLabel = new JLabel(new ImageIcon(Screen.getGameImage().getSubimage(bounds.x, bounds.y, bounds.width, bounds.height)));
			detailPanel.add(imageLabel, BorderLayout.NORTH);
		}
	}

	public void setText(Rectangle absoluteBounds, RSInterface absoluteParent,
			Point position, String[] actions, RSInterface[] children,
			int componentIndex, int componentItem, int componentStack,
			int height, int index, RSItem[] items, int modelID, int modelType, int modelZoom,
			RSInterface parent, int parentID, int rotationX, int rotationY,
			int rotationZ, int scrollMaxH, int scrollMaxV, String text,
			int textureID, int uid, int width, int x, int y){
		
		//System.out.println("Updated info panel");
		bounds = absoluteBounds;
		removeAll();
		addCheckBox();

		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		if(drawing && bounds != null){
			addImagePanel();
		}
		
		// ACTIONS
		StringBuilder actionInfo = new StringBuilder();
		if(actions != null && actions.length > 0)
			for(int i = 0; i < actions.length; i++)
				if(i == actions.length-1)
					actionInfo.append("Action #" + i + ": " + actions[i]);
				else
					actionInfo.append("Action #" + i + ": " + actions[i] + ", ");
		
		// CHILDREN
		StringBuilder childInfo = new StringBuilder();
		if(children != null && children.length > 0)
			for(int i = 0; i < children.length; i++)
				if(i == children.length-1)
					childInfo.append("Interface child #" + i + ": " + children[i].getComponentIndex());
				else
					childInfo.append("Interface child #" + i + ": " + children[i].getComponentIndex() + ", ");	
		
		// ITEM INFO
		StringBuilder itemInfo = new StringBuilder();
		if(items != null && items.length > 0)
			for(int i = 0; i < items.length; i++)
				if(i == items.length-1)
					itemInfo .append("Item #" + i + ": " + items[i].getID());
				else
					itemInfo .append("Item #" + i + ": " + items[i].getID() + ", ");

		labels = new JLabel[27];
		labels[0] = new JLabel("Bounds: " + absoluteBounds);
		labels[1] = new JLabel("Absolute parent index: " + absoluteParent.getComponentIndex());
		labels[2] = new JLabel("Position: " + position);
		labels[3] = new JLabel("Actions: " + actionInfo.toString());
		labels[4] = new JLabel("Children: " + childInfo.toString());
		labels[5] = new JLabel("Component index: " + componentIndex);
		labels[6] = new JLabel("Component item: " + componentItem);
		labels[7] = new JLabel("Component stack: " + componentStack);
		labels[8] = new JLabel("Height: " + height);
		labels[9] = new JLabel("Index: " + index);
		labels[10] = new JLabel("Items: " + itemInfo.toString());
		labels[11] = new JLabel("Model ID: " +  modelID);
		labels[12] = new JLabel("Model Type: " + modelType);
		labels[13] = new JLabel("Model Zoom: " + modelZoom);
		labels[14] = new JLabel("Parent index: " + parent.getComponentIndex());
		labels[15] = new JLabel("Parent ID: " + parentID);
		labels[16] = new JLabel("Rotation X: " + rotationX);
		labels[17] = new JLabel("Rotation Y: " + rotationY);
		labels[18] = new JLabel("Rotation Z: " + rotationZ);
		labels[19] = new JLabel("Scroll Max Horizontal: " + scrollMaxH);
		labels[20] = new JLabel("Scroll Max Vertical: " + scrollMaxV);
		labels[21] = new JLabel("Text: " + text);
		labels[22] = new JLabel("Texture ID: " + textureID);
		labels[23] = new JLabel("UID: " + uid);
		labels[24] = new JLabel("Width: " + width);
		labels[25] = new JLabel("X: " + x);
		labels[26] = new JLabel("Y: " + y);

		labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(27, 1));
		for(JLabel label : labels){
			//System.out.println("Added label: " + label.getText());
			labelPanel.add(label);
		}
		detailPanel.add(labelPanel, BorderLayout.CENTER);

		scrollPane = new JScrollPane(detailPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane = new JScrollPane(detailPanel);
		scrollPane.setPreferredSize(new Dimension(300, 350));
		add(scrollPane, BorderLayout.CENTER);

		revalidate();
	}

	public Rectangle getRSRect(){
		return bounds;
	}
	
	private class CheckListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			//System.out.println("CHECKBOX CLICKED");

			setDrawing(imageCheckBox.isSelected());
			
			revalidate();
			invalidate();
			repaint();
		}
		
	}
	
}
