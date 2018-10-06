package risk.gui;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Map 
{

	private int i;
	private int j;
	
	private String name;
	
	private String[] continentsArray;
	private String[] adjacenciesArray;
	private ArrayList<Country> countriesList;
	private ArrayList<Country> adjacenciesList;
	private ArrayList<Country> memberCountries;
    private boolean isLoaded,isAdjacent;
    private HashMap<String, Country> countriesMap;
    private HashMap<String, Continent> continentsMap;
	
    public Map() {}

    public boolean loadMap(String[] countriesArray, String[] adjacenciesFileArray, String[] continentsFileArray) {
		
		isLoaded = false;
	
		countriesMap = new HashMap<String, Country>();
		continentsMap = new HashMap<String, Continent>();
		for (i = 0; i < countriesArray.length; i++) 
        {
		    countriesMap.put(countriesArray[i], new Country(countriesArray[i]));
		}
		countriesList = new ArrayList<Country>(countriesMap.values());
		    for (i = 0; i < adjacenciesFileArray.length; i++) 
            {		
			    System.out.println("Build new adjacenciesArray" + adjacenciesFileArray[i] + "...");	
			    adjacenciesArray = adjacenciesFileArray[i].split(",");
			    System.out.println("Build new adjacenciesList...");			
			    adjacenciesList = new ArrayList<Country>();
	        	for (j = 1; j < adjacenciesArray.length; j++)
                {
    				System.out.println("Add to adjacenciesList: " + adjacenciesArray[j]);
    				adjacenciesList.add(countriesMap.get(adjacenciesArray[j]));
    			}
    			countriesMap.get(adjacenciesArray[0]).addAdjacencies(adjacenciesList);
    		}
			for (i = 0; i < continentsFileArray.length; i++)
            {
			    continentsArray = continentsFileArray[i].split(",");
			    memberCountries = new ArrayList<Country>();
			    for (j = 2; j < continentsArray.length; j++)
                    {
        				System.out.println("Add memberCountry to " + continentsArray[0] + ": " + continentsArray[j]);
        				memberCountries.add(countriesMap.get(continentsArray[j]));
        			}
    			continentsMap.put(continentsArray[0], new Continent(continentsArray[0], Integer.parseInt(continentsArray[1]), memberCountries));
    		}
		
		isLoaded = true;
		
		return isLoaded;
	}

    public ArrayList<Country> getCountries() {
		return countriesList;
    }
	
	public boolean checkAdjacency(String countryA, String countryB) {
		if (countriesMap.get(countryA).getAdjacencies().contains(countriesMap.get(countryB))) {
			isAdjacent = true;
		} else {
			isAdjacent = false;
		}
		
		return isAdjacent;
	}
}
