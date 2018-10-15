package risk.game;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.naming.SizeLimitExceededException;

public class RiskMap {

	public static final int MAX_CONTINENT = 32;
	public static final int MAX_COUNTRY = 255;
		
	private HashMap<String, Continent> continentList;
	private HashMap<Point, Country> countryList;
	
	public RiskMap() {
		continentList = new HashMap<String, Continent>();
		countryList = new HashMap<Point, Country>();
	}
	
	public boolean addContinent(String name, int value) {
		if (continentList.size() > MAX_CONTINENT) {
			return false;
		}
		
		Continent newContinent = new Continent(name, value);
		continentList.put(newContinent.getName(), newContinent);
		return true;
	}
	
	public boolean addContinent(Continent continent) {
		if (continentList.size() > MAX_CONTINENT) {
			return false;
		}
		
		continentList.put(continent.getName(), continent);
		return true;
	}
	
	public void removeContinent(String continentName) {
		for (Country country : countryList.values()) {
			if (country.getContinentName() == continentName) {
				country.setContinentName("unsigned");
			}
		}
		continentList.remove(continentName);
	}
	
	public LinkedList<Continent> getContinentList() {
		LinkedList<Continent> list = new LinkedList<Continent>();
		for (Continent continent : continentList.values()) {
			list.add(continent);
		}
		
		return list;
	}
	
	public boolean addCountry(Country country) {
		if (countryList.size() > 255) {
			return false;
		}
		
		countryList.put(country.getLocation(), country);
		return true;
	}
	
	public void removeCountry(Country country) {
		Country removedCountry = countryList.remove(country.getLocation());
		
		for (Country c : countryList.values()) {
			c.removeAdjacentCountry(removedCountry.getName());
		}
	}

	public LinkedList<Country> getCountryList() {
		LinkedList<Country> list = new LinkedList<Country>();
		for (Country country : countryList.values()) {
			list.add(country);
		}
		
		return list;
	}
	
//TODO
/*	public boolean addEdge(Country start, Country dest) {
		start.addAdjacentCountry(dest.getName());
		dest.addAdjacentCountry(start.getName());
	}
*/	
	
// TODO	
//	public boolean addEdge(Point start, Point dest) {
		
//	}
	
}





