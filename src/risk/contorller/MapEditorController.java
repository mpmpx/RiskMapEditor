package risk.contorller;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import risk.game.Continent;
import risk.game.Country;
import risk.game.RiskMap;
import risk.gui.component.CountryComponent;
import risk.gui.map_editor.MapEditorPanel;
import risk.gui.utilities.ColorPool;
import risk.io.RiskMapIO;

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
	
	private void InitFlag() {
		isDisplayPanelActive = false;
		isEditPanelActive = false;
		isSaved = true;
		addCountryFlag = false;
		
	}
	
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
	
	public void createNewMap() {
		this.clear();
		isDisplayPanelActive = true;
		isSaved = true;
		defaultName = 0;
		
		
		this.mapEditorPanel.enableMapDisplayPanel(isDisplayPanelActive);
	}
	
	public void loadMap(String fileAbsolutePath) {
		this.clear();
		this.mapEditorPanel.enableMapDisplayPanel(true);
		isSaved = true;
		riskMapIO = new RiskMapIO();
		
		try {
			this.currentMap = riskMapIO.readFile(fileAbsolutePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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

	public void saveMap(String fileAbsolutePath) {
		isSaved = true;
		currentMap = new RiskMap();
		riskMapIO = new RiskMapIO();
		
		for (Continent continent : continentList) {
			currentMap.addContinent(continent);
		}

		for (Country country : countryHashMap.values()) {
			for (Point location : edgeHashMap.get(country.getLocation())) {
				country.addAdjacentCountry(location);
			}
			currentMap.addCountry(country);
		}
	
		try {
			riskMapIO.saveFile(currentMap, fileAbsolutePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isMapSaved() {
		return isSaved;
	}
	
	public void setAddCountryFlag(boolean flag) {
		addCountryFlag = flag;
	}
	
	public void setMapSize(int width, int height) {
		this.mapEditorPanel.setMapSize(width, height);
	}
	
	public void updateCountryInfo(Country country) {
		countryHashMap.put(country.getLocation(), country);
		this.mapEditorPanel.updateMapDisplay();
		isSaved = false;
	}
	
	public boolean getAddCountryFlag(){
		return addCountryFlag;
	}
	
	public void setSelectedCountry(Point point) {
		this.isEditPanelActive = true;	
		this.selectedCountry = countryHashMap.get(point);
		this.mapEditorPanel.enableCountryEditPanel(isEditPanelActive);		
		this.mapEditorPanel.updateEditPanel();
	}
	
	public Country getSelectedCountry() {
		return selectedCountry;
	}
	
	public void addCountry(CountryComponent countryComponent) {
		Country country = new Country(countryComponent.getCenterLocation());
		country.setName("" + (++defaultName));
		countryHashMap.put(countryComponent.getCenterLocation(), country);
		addCountryFlag = false;
		isSaved = false;
	}
	
	public void addCountry(Country country) {
		country.setName("" + (++defaultName));
		countryHashMap.put(country.getLocation(), country);
		addCountryFlag = false;
		isSaved = false;
	}
	
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
	
	public HashMap<Point, Country> getCountryHashMap() {
		return countryHashMap;
	}
	
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
	
		
	public HashMap<Point, LinkedList<Point>> getEdgeHashMap() {
		return this.edgeHashMap;
	}
	
	public void addContinent(String continentName, int continentValue) {
		continentList.add(new Continent(continentName, continentValue));
		continentColorHashMap.put(continentName, colorPool.get());
		isSaved = false;
	}
	
	public boolean isContinentDuplicate(String continentName) {
		for (Continent continent : continentList) {
			if (continent.getName().equals(continentName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isMaxContinent() {
		return continentList.size() == 32;
	}
	
	public LinkedList<Continent> getContinentList() {
		return continentList;
	}
	
	public HashMap<String, Color> getContinentColorHashMap() {
		return continentColorHashMap;
	}
}
