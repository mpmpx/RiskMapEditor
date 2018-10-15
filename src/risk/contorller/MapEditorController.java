package risk.contorller;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import risk.game.Continent;
import risk.game.Country;
import risk.game.RiskMap;
import risk.gui.component.CountryComponent;
import risk.gui.map_editor.MapEditorPanel;
import risk.gui.utilities.ColorList;

public class MapEditorController {
	
	private MapEditorPanel mapEditorPanel;
	private RiskMap currentMap;
	private Country selectedCountry;
	private HashMap<String, Color> continentColorHashMap;
	private static int defaultName = 0;
	
	
	private LinkedList<Continent> continentList;
	private HashMap<Point, Country> countryHashMap;
	private HashMap<Point, LinkedList<Point>> edgeHashMap;
	

	private boolean isDisplayPanelActive;
	private boolean isEditPanelActive;
	private boolean isSaved;
	private boolean addCountryFlag;
	
	
	public MapEditorController(MapEditorPanel mapEditorPanel) {
		this.mapEditorPanel = mapEditorPanel;
		continentList = new LinkedList<Continent>();
		countryHashMap = new HashMap<Point, Country>();
		continentColorHashMap = new HashMap<String, Color>();
		edgeHashMap = new HashMap<Point, LinkedList<Point>>();
		selectedCountry = null;		

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
		
		this.mapEditorPanel.clear();		
	}
	
	public void createNewMap() {
		isDisplayPanelActive = true;
		isSaved = true;
		
		this.clear();		
		this.mapEditorPanel.enableMapDisplayPanel(isDisplayPanelActive);
	}
	
	public void loadMap(String fileAbsolutePath) {
		this.clear();
		isSaved = true;
		System.out.println(fileAbsolutePath);
	}

	public void saveMap(String fileAbsolutePath) {
		isSaved = true;
		currentMap = new RiskMap();
		for (Continent continent : continentList) {
			currentMap.addContinent(continent);
		}
		
		if (edgeHashMap == null) {
			System.out.println("edgeHashMap is null");
		}
		else {
			System.out.println("edgeHashMap is not null");
			
			for (LinkedList<Point> clist : edgeHashMap.values()) {
				for (Point point : clist) {
					System.out.print("(" + point.x + ", " + point.y + "), ");
				}
				System.out.println();
			}
			
		}
		
		//countryHashMap.remove(new Point(0,0));
		for (Country country : countryHashMap.values()) {
			//System.out.println(country.getX() + ", " + country.getY());
			System.out.println(country.getName() +", " + country.getX() + ", " +country.getY());
			for (Point location : edgeHashMap.get(country.getLocation())) {
				country.addAdjacentCountry(location);
			}
			currentMap.addCountry(country);
		}
		
		
		System.out.println("[Continent]");
		for (Continent continent : currentMap.getContinentList()) {
			System.out.println(continent.getName() + ": " + continent.getValue());
		}
		System.out.println();
		
		for (Country country : currentMap.getCountryList()) {
			System.out.print(country.getName() + ", " + country.getContinentName() + ", " + country.getX() + ", " + country.getY());
			for (Point location : country.getAdjacentCountryList()) {
				String name = "";
				for (Country c : currentMap.getCountryList()) {
					if (location.equals(c.getLocation())) {
						name = c.getName();
					}
				}
				System.out.print(", " + name);
			}
			System.out.println();;
		}
		
		
		System.out.println(fileAbsolutePath);		
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
		//System.out.println("adding country");
		Country country = new Country(countryComponent.getCenterLocation());
		country.setName("" + (++defaultName));
		countryHashMap.put(countryComponent.getCenterLocation(), country);
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
				System.out.println(secondLocation.x + ", " + secondLocation.y);
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
		continentColorHashMap.put(continentName, ColorList.get());
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
