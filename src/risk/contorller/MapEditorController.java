package risk.contorller;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import risk.game.Continent;
import risk.game.Country;
import risk.game.RiskMap;
import risk.gui.component.CountryComponent;
import risk.gui.map_editor.MapEditorPanel;
import risk.gui.utilities.ColorPool;
import risk.io.RiskMapIO;

/** 
 * MapEditorController class provides methods communicating between MapEditor panel and other map status and information.
 * It also provides method to load existing map into editor or save currently editing map in a .map file.
 */

public class MapEditorController {

	private static int defaultName;
	private MapEditorPanel mapEditorPanel;
	private RiskMap currentMap;
	private Country selectedCountry;
	private ColorPool colorPool;
	private HashMap<String, Color> continentColorHashMap;
	private LinkedList<Continent> continentList;
	private HashMap<Point, Country> countryHashMap;
	private HashMap<Point, LinkedList<Point>> edgeHashMap;
	
	private boolean isDisplayPanelActive;
	private boolean isEditPanelActive;
	private boolean isSaved;
	private boolean addCountryFlag;
	private RiskMapIO riskMapIO;
	
	/**
	 * Constructor initializes all class variables.
	 * @param mapEditorPanel is an instance of MapEditorPanel who called this controller.
	 */
	public MapEditorController(MapEditorPanel mapEditorPanel) {
		this.mapEditorPanel = mapEditorPanel;
		continentList = new LinkedList<Continent>();
		countryHashMap = new HashMap<Point, Country>();
		continentColorHashMap = new HashMap<String, Color>();
		edgeHashMap = new HashMap<Point, LinkedList<Point>>();
		selectedCountry = null;		
		colorPool = new ColorPool();
		riskMapIO = new RiskMapIO();
		
		InitFlag();
	}
	
	/**
	 * Initialize all flag variables.
	 */
	private void InitFlag() {
		isDisplayPanelActive = false;
		isEditPanelActive = false;
		isSaved = true;
		addCountryFlag = false;
		
	}
	
	/**
	 * Reinitialize all class variables and flag variables, and clean information of MapEditorPanel.
	 */
	public void clear() {
		currentMap = new RiskMap();
		continentList = new LinkedList<Continent>();
		countryHashMap = new HashMap<Point, Country>();
		continentColorHashMap = new HashMap<String, Color>();
		edgeHashMap = new HashMap<Point, LinkedList<Point>>();
		selectedCountry = null;		
		colorPool = new ColorPool();
		InitFlag();
		
		this.mapEditorPanel.clear();		
	}
	
	/**
	 * Reinitialize the controller and set related flags.
	 */
	public void createNewMap() {
		this.clear();
		isDisplayPanelActive = true;
		isSaved = true;
		defaultName = 0;
		
		this.mapEditorPanel.enableMapDisplayPanel(isDisplayPanelActive);
	}
	
	/**
	 * Load a .map file into the editor.
	 * @param fileAbsolutePath is the absolute path of the file to be loaded.
	 */
	public void loadMap(String fileAbsolutePath) {
		
		try {
			this.currentMap = riskMapIO.readFile(fileAbsolutePath);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this.mapEditorPanel, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.clear();
		this.mapEditorPanel.enableMapDisplayPanel(true);
		isSaved = true;
		riskMapIO = new RiskMapIO();
		
		this.mapEditorPanel.setMapSize(currentMap.getSize().width, currentMap.getSize().height);
		for (Continent continent : currentMap.getContinentList()) {
			this.addContinent(continent.getName(), continent.getValue());
		}
		
		for (Country country : currentMap.getCountryList()) {
			this.edgeHashMap.put(country.getLocation(), country.getAdjacentCountryList());

			this.countryHashMap.put(country.getLocation(), country);
			this.mapEditorPanel.addCountry(country);
		}		
		this.mapEditorPanel.updateMapDisplay();
	}

	/**
	 * Save current working map into a .map file.
	 * @param fileAbsolutePath is the absolute path of the save to be saved.
	 */
	public void saveMap(String fileAbsolutePath) {

		for (Continent continent : continentList) {
			currentMap.addContinent(continent);
		}
		
		try {
			for (Country country : countryHashMap.values()) {
				if (country.getContinentName() == null) {
					throw new Exception(country.getName() + " is not assigend with a continent");
				}
				
				if (edgeHashMap.get(country.getLocation()) == null) {
					throw new Exception(country.getName() + " is not connected to any other countries");
				}
				
				for (Point location : edgeHashMap.get(country.getLocation())) {
					country.addAdjacentCountry(location);
				}
				currentMap.addCountry(country);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.mapEditorPanel, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);	
			e.printStackTrace();
			return;
		}
			
		try {
			riskMapIO.saveFile(currentMap, fileAbsolutePath);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this.mapEditorPanel, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);			
			e.printStackTrace();
			return;
		}
		
		isSaved = true;
		currentMap = new RiskMap();
		riskMapIO = new RiskMapIO();
	}
	
	/**
	 * Get the status of whether the map has already been saved.
	 * @return the status of whether the map has already been saved.
	 */
	public boolean isMapSaved() {
		return isSaved;
	}
	
	/**
	 * Set the flag of whether a country is being creating.
	 * @param flag shows the status of whether a country is being creating.
	 */
	public void setAddCountryFlag(boolean flag) {
		addCountryFlag = flag;
	}

	/**
	 * Get the flag of whether a country is being creating.
	 * @return the flag status.
	 */
	public boolean getAddCountryFlag(){
		return addCountryFlag;
	}
	
	/**
	 * Set the size of MapDisplayPanel based on the given information.
	 * @param width is the width of the map.
	 * @param height is the height of the map.
	 */
	public void setMapSize(int width, int height) {
		this.mapEditorPanel.setMapSize(width, height);
	}
	
	/**
	 * Update the status of the given country. Also reflect changes on the MapDisplay panel.
	 * @param country is the country being updated.
	 */
	public void updateCountryInfo(Country country) {
		countryHashMap.put(country.getLocation(), country);
		this.mapEditorPanel.updateMapDisplay();
		isSaved = false;
	}
	

	/**
	 * Set the currently selected country by the given information of location.
	 * Enable and reflect changes on the CountryEdit panel.
	 * @param point is the location of selected country.
	 */
	public void setSelectedCountry(Point point) {
		this.isEditPanelActive = true;	
		this.selectedCountry = countryHashMap.get(point);
		this.mapEditorPanel.enableCountryEditPanel(isEditPanelActive);		
		this.mapEditorPanel.updateEditPanel();
	}
	
	/**
	 * Get the selected country.
	 * @return the selected country.
	 */
	public Country getSelectedCountry() {
		return selectedCountry;
	}
	
	/**
	 * Add a new country and assign a default name to it.
	 * Update related flags.
	 * @param countryComponent is the country component with country's information.
	 */
	public void addCountry(CountryComponent countryComponent) {
		Country country = new Country(countryComponent.getCenterLocation());
		country.setName("" + (++defaultName));
		countryHashMap.put(countryComponent.getCenterLocation(), country);
		addCountryFlag = false;
		isSaved = false;
	}
	
	/**
	 * Add a new country and assign a default name to it.
	 * Update related flags.
	 * @param country is the country to be added.
	 */
	public void addCountry(Country country) {
		country.setName("" + (++defaultName));
		countryHashMap.put(country.getLocation(), country);
		addCountryFlag = false;
		isSaved = false;
	}
	
	/**
	 * Delete a country by giving information. Also delete the related
	 * country component on the MapDisplay panel.
	 * @param country is the country to be deleted.
	 */
	public void deleteCountry(Country country) {
		countryHashMap.remove(country.getLocation());
		Point countryLocation = country.getLocation();
		LinkedList<Point> adjacentLocation = edgeHashMap.get(countryLocation);
		edgeHashMap.remove(countryLocation);
		if (adjacentLocation != null) {
			for (Point location : adjacentLocation) {
				edgeHashMap.get(location).remove(countryLocation);
			}		
		}
		
		this.isEditPanelActive = false;
		this.mapEditorPanel.enableCountryEditPanel(isEditPanelActive);
		this.selectedCountry = null;
		this.mapEditorPanel.clearEditPanel();
		this.mapEditorPanel.removeCountry(country.getLocation());
		isSaved = false;
	}
	
	/**
	 * Get a hash map which contains all countries with keys of country's location.
	 * @return the hash map.
	 */
	public HashMap<Point, Country> getCountryHashMap() {
		return countryHashMap;
	}
	
	/**
	 * Add a new link between two countries.
	 * Reflect changes on CountryEdit panel and MapDisplay panel.
	 * @param firstLocation is the location of first country.
	 * @param secondLocation is the location of second country.
	 */
	public void addLink(Point firstLocation, Point secondLocation) {
		if (!edgeHashMap.containsKey(firstLocation)){
			LinkedList<Point> adjacentCountryList = new LinkedList<Point>();
			edgeHashMap.put(firstLocation, adjacentCountryList);
		}

		if (!edgeHashMap.get(firstLocation).contains(secondLocation)) {
				edgeHashMap.get(firstLocation).add(secondLocation);
		}
		
		
		if (!edgeHashMap.containsKey(secondLocation)){
			LinkedList<Point> adjacentCountryList = new LinkedList<Point>();
			edgeHashMap.put(secondLocation, adjacentCountryList);
		}

		if (!edgeHashMap.get(secondLocation).contains(firstLocation)) {
				edgeHashMap.get(secondLocation).add(firstLocation);
		}
		
		this.mapEditorPanel.updateEditPanel();
		this.mapEditorPanel.updateMapDisplay();
		isSaved = false;
	}
	
	/**
	 * Remove a link from the selected country.
	 * @param name is the name of country which links to the selected country.
	 */
	public void removeLink(String name) {
		Point firstLocation = selectedCountry.getLocation();
		Point secondLocation = null;
		
		for (Country country : countryHashMap.values()) {
			if (name.equals(country.getName())) {
				secondLocation = country.getLocation();
			}
		}
		
		edgeHashMap.get(firstLocation).remove(secondLocation);
		edgeHashMap.get(secondLocation).remove(firstLocation);
		this.mapEditorPanel.updateEditPanel();
		this.mapEditorPanel.updateMapDisplay();
		isSaved = false;
	}
	
	/**
	 * Get the hash map which contains all edges.
	 * @return the hash map which contains all edges.
	 */
	public HashMap<Point, LinkedList<Point>> getEdgeHashMap() {
		return this.edgeHashMap;
	}
	
	/**
	 * Add a new continent.
	 * @param continentName is the name of the continent.
	 * @param continentValue is the value of the continent.
	 */
	public void addContinent(String continentName, int continentValue) {
		continentList.add(new Continent(continentName, continentValue));
		continentColorHashMap.put(continentName, colorPool.get());
		isSaved = false;
	}
	
	/**
	 * Check whether a continent has already been added.
	 * @param continentName the name of the continent.
	 * @return true if the giving continent has already been added. Otherwise, false.
	 */
	public boolean isContinentDuplicate(String continentName) {
		for (Continent continent : continentList) {
			if (continent.getName().equals(continentName)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check whether the number of continents meets the maximum.
	 * @return true is the number of continents meets the maximum. Otherwise, false.
	 */
	public boolean isMaxContinent() {
		return continentList.size() == 32;
	}
	
	/**
	 * Get a linked list of all continents.
	 * @return a linked list of all continents.
	 */
	public LinkedList<Continent> getContinentList() {
		return continentList;
	}
	
	/**
	 * Get a hash map of colors of continents.
	 * @return a hash map of colors of continents.
	 */
	public HashMap<String, Color> getContinentColorHashMap() {
		return continentColorHashMap;
	}
}
