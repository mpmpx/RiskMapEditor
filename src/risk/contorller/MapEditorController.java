package risk.contorller;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import risk.gui.component.CountryComponent;
import risk.gui.utilities.ColorList;

public class MapEditorController {

	private static MapEditorController controller;
	
	private Object map;
	private Object country;
	private ArrayList<String> continentList;
	private ArrayList<Color> continentColorList;
	private HashMap<String, Color> continentColorHashMap;
	private HashMap<Point, CountryComponent> countryComponentHashMap;
	
	private CountryComponent selectedCountry;
	
	private static boolean addCountryFlag;
	
	
	private MapEditorController() {
		continentList = new ArrayList<String>(32);
		continentColorList = new ArrayList<Color>(32);
		
		addCountryFlag = false;
		countryComponentHashMap = new HashMap<Point, CountryComponent>();
		continentColorHashMap = new HashMap<String, Color>();
		
		selectedCountry = null;
	}
	
	public static MapEditorController getInstance() {
		if (controller == null) {
			controller = new MapEditorController();
		}
		
		return controller;
	}

	public HashMap<Point, CountryComponent> getCountryComponentHashMap() {
		return countryComponentHashMap;
	}
	
	public ArrayList<Color> getContinentColorList() {
		return continentColorList;
	}
	
	public void createNewMap() {
		
		
	}
	
	public void loadMap(String fileAbsolutePath) {
		System.out.println(fileAbsolutePath);
	}

	public void saveMap(String fileAbsolutePath) {
		System.out.println(fileAbsolutePath);		
	}
	
	public void setAddCountryFlag(boolean flag) {
		addCountryFlag = flag;
	}
	
	public boolean getAddCountryFlag(){
		return addCountryFlag;
	}
	
	public void setSelectedCountry(CountryComponent country) {
		selectedCountry = country;
	}
	
	public CountryComponent getSelectedCountry() {
		return selectedCountry;
	}
	
	public void addCountry(CountryComponent countryComponent) {
		Point centerOfCountry = new Point((int)countryComponent.getLocation().getX() - CountryComponent.Radius,
				(int)countryComponent.getLocation().getY() - CountryComponent.Radius);
		countryComponentHashMap.put(countryComponent.getCenterLocation(), countryComponent);
		MapEditorController.addCountryFlag = false;
	}
	
	public void deleteCountry() {
		
	}
	
	
	public void addContinent(String continentName, int continentValue) {
		continentList.add(continentName);
	}
	
	public boolean isContinentDuplicate(String continentName) {
		return continentList.contains(continentName);
	}
	
	public boolean isMaxContinent() {
		return continentList.size() == 32;
	}
	
	public ArrayList<String> getContinentList() {
		return continentList;
	}
}
