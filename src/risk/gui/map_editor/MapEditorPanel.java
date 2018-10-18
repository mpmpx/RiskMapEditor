package risk.gui.map_editor;

import risk.contorller.*;
import risk.game.Country;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.JPanel;

/**
 * MapEditorPanel is the class which provides an instance of JPanel. 
 * It is the main panel of the map editor of the Risk game which is divided
 * into three areas. CountryEdit panel shows information of the selected country
 * and allows users for editing; MapDisplay panel displays all graphic information
 * of the map. EditorMenu panel provides several buttons managing the map editor.
 */
public class MapEditorPanel extends JPanel {
	public static final int HEIGHT = 800;
	public static final int WIDTH = 1200;
	
	private MapDisplayPanel mapDisplayPanel;
	private CountryEditPanel countryEditPanel;
	private EditorMenuPanel editorMenuPanel; 
	
	private MapEditorController controller;
	
	/**
	 * Constructor of the class. Initialize all class variables and create
	 * three sub-panels.
	 */
	public MapEditorPanel() {	
	    this.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    controller = new MapEditorController(this);
	    
	    countryEditPanel = new CountryEditPanel(controller);
	    c.weightx = 0.1;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.anchor = GridBagConstraints.PAGE_START;
	    enableAllComponent(countryEditPanel, false);
	    this.add(countryEditPanel, c);
	    
	    mapDisplayPanel = new MapDisplayPanel(controller);	
	    c.weightx = 0.1;
	    c.gridheight = 2;
	    c.gridx = 1;
	    c.gridy = 0;
	    enableAllComponent(mapDisplayPanel.getScrollPane(), false);
	    this.add(mapDisplayPanel.getScrollPane(), c);
	    
		editorMenuPanel = new EditorMenuPanel(controller);
		c.gridx = 0;
	    c.gridy = 1;
	    c.anchor = GridBagConstraints.PAGE_END;
	    this.add(editorMenuPanel, c);
	}	
	
	/**
	 * Set the size of MapDisplay panel and its scroll panel.
	 * @param width is the width of the MapDisplay panel.
	 * @param height is the height of the MapDisplay panel.
	 */
	public void setMapSize(int width, int height) {
		this.mapDisplayPanel.setPreferredSize(new Dimension(width, height));
		this.mapDisplayPanel.revalidate();
		if (width < MapDisplayPanel.WIDTH) {
			this.mapDisplayPanel.getScrollPane().setPreferredSize(new Dimension(width, MapDisplayPanel.HEIGHT));
		} 
		else if (height < MapDisplayPanel.HEIGHT){
			this.mapDisplayPanel.getScrollPane().setPreferredSize(new Dimension(MapDisplayPanel.WIDTH, height));
		}
		else {
			this.mapDisplayPanel.getScrollPane().setPreferredSize(new Dimension( MapDisplayPanel.WIDTH, MapDisplayPanel.HEIGHT));
		}
		this.mapDisplayPanel.getScrollPane().revalidate();
	}
	
	/**
	 * Add and draw a new country on the display panel.
	 * @param country is the country to be added.
	 */
	public void addCountry(Country country) {
		mapDisplayPanel.addCountry(country);
	}
	
	/**
	 * Recursively enable or disable all components of the given container.
	 * @param container is the container to be handled.
	 * @param enable is the signal determines whether to enable or disable.
	 */
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
	
	/**
	 * Recursively enable or disable all components of the MapDisplay panel.
	 * @param enable is the signal determines whether to enable or disable.
	 */
	public void enableMapDisplayPanel(boolean enable) {
		enableAllComponent(mapDisplayPanel.getScrollPane(), enable);
	}
	
	/**
	 * Recursively enable or disable all components of the CountryEdit panel.
	 * @param enable is the signal determines whether to enable or disable.
	 */
	public void enableCountryEditPanel(boolean enable) {
		enableAllComponent(countryEditPanel, enable);
	}
	
	/**
	 * Update all information of the CountryEdit panel.
	 */
	public void updateEditPanel() {
		countryEditPanel.updateInfo();
	}
	
	/**
	 * Repaint and update all information of the MapDisplay panel.
	 */
	public void updateMapDisplay() {
		mapDisplayPanel.repaint();
		mapDisplayPanel.updateInfo();
		
	}
	
	/**
	 * Remove a country on the display panel according to given location.
	 * @param location is the location of the country to be removed.
	 */
	public void removeCountry(Point location) {
		mapDisplayPanel.removeCountry(location);
	}
	
	/**
	 * Clean up all information of mapDisplay panel and CountryEdit panel.
	 */
	public void clear() {
		mapDisplayPanel.clear();
		countryEditPanel.clear();
	}
	
	/**
	 * Clean up all information of countryEdit panel.
	 */
	public void clearEditPanel() {
		countryEditPanel.clear();
	}
}