package risk.gui.map_editor;

import risk.contorller.*;
import risk.gui.MainFrame;
import risk.gui.MenuPanel;
import risk.gui.component.CountryComponent;
import risk.gui.utilities.ColorList;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class MapEditorPanel extends JPanel {
	public static final int HEIGHT = 800;
	public static final int WIDTH = 1200;
	
	private static MapEditorPanel mapEditorPanel;
	private MapDisplayPanel mapDisplayPanel;
	private CountryEditPanel countryEditPanel;
	private EditorMenuPanel editorMenuPanel; 
	
	private static MapEditorController controller;
	
	private MapEditorPanel() {	
	    this.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
		
	    countryEditPanel = CountryEditPanel.getInstance();
	    c.weightx = 0.1;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.anchor = GridBagConstraints.PAGE_START;
	    enableAllComponent(countryEditPanel, false);
	    this.add(countryEditPanel, c);
	    
	    mapDisplayPanel = MapDisplayPanel.getInstance();	
	    c.weightx = 0.1;
	    c.gridheight = 2;
	    c.gridx = 1;
	    c.gridy = 0;
	    enableAllComponent(mapDisplayPanel, false);
	    this.add(mapDisplayPanel.getScrollPane(), c);
	    
		editorMenuPanel = EditorMenuPanel.getInstance();
		c.gridx = 0;
	    c.gridy = 1;
	    c.anchor = GridBagConstraints.PAGE_END;
	    this.add(editorMenuPanel, c);
	    
	    controller = MapEditorController.getInstance();
	}
	
	public static MapEditorPanel getInstance() {
		if (mapEditorPanel == null) {
			mapEditorPanel = new MapEditorPanel();
		}
		return mapEditorPanel;
	}

	public static MapEditorController getController() {
		if (controller == null) {
			controller = MapEditorController.getInstance();
		}
		
		return controller;
	}
	
	private void enableAllComponent(Container container, boolean enable) {
		Component[] componentList = container.getComponents();
		for (Component component : componentList) {
			if (component instanceof Container) {
				enableAllComponent((Container)component, enable);
			}
			component.setEnabled(enable);
		}
		container.setEnabled(enable);
	}
	
	public void enableMapDisplayPanel() {
		enableAllComponent(mapDisplayPanel, true);
	}
	
	public void enableCountryEditPanel() {
		enableAllComponent(countryEditPanel, true);
		countryEditPanel.revalidate();
	}
	
	public void enableAllComponent() {
		enableAllComponent(countryEditPanel, true);
		enableAllComponent(mapDisplayPanel, true);
	}
	
	public void updateAll() {
		enableCountryEditPanel();
	}
}






