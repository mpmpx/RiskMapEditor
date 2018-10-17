package risk.io;

import java.awt.Point;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import risk.game.Continent;
import risk.game.Country;
import risk.game.RiskMap;


public class RiskMapIO {

    private boolean bMapHead = false;
    private boolean bContinent = false;
    private boolean bterritories = false;

    private String label = "";

    private LinkedHashMap<String, Continent> continentMap = new LinkedHashMap<>();

    private LinkedHashMap<String, Country> countryMap = new LinkedHashMap<>();
    
    private RiskMap map;
    private HashMap<String, Country> countryHashMap;
    private LinkedList<Continent> continentList;
    private HashMap<String, LinkedList<String>> edgeHashMap;
    
    private int mapHeight;
    private int mapWidth;

    
    public RiskMapIO() {
        map = new RiskMap();
        countryHashMap = new HashMap<String, Country>();
        continentList = new LinkedList<Continent>();
        edgeHashMap = new HashMap<String, LinkedList<String>>();
        mapHeight = 0;
        mapWidth = 0;
    }

    private void validateBlockName() {
        if (!(bContinent && bterritories)) {
         //   throw new IllegalArgumentException("bolck name is invalid!");
        }
    }

    private void validateContinent() {
    	boolean found = false;
    	for (Continent continent : continentList) {
    		if (continent.getValue() == 0) {
    			throw new IllegalArgumentException("Continent " + continent.getName() + " has value 0");
    		}
    	}
    	
    	for (Country country : countryHashMap.values()) {
    		found = false;
    		for (Continent continent : continentList) {
    			if (continent.getName().equals(country.getContinentName())) {
    				System.out.println(continent.getName() + ", " + country.getContinentName());
    				found = true;
    			}
    		}
    		
    		if (!found) {
    			throw new IllegalArgumentException("Continent of " + country.getName() + ": " + country.getContinentName() + " is invalid.");
    		}
    	}
    }

    private void validateCountry() {
    	for (Country country : countryHashMap.values()) {
    		for (String adjacent : edgeHashMap.get(country.getName())) {
    			if (countryHashMap.containsKey(adjacent) && edgeHashMap.containsKey(adjacent)) {
    				if (!edgeHashMap.get(adjacent).contains(country.getName())) {
    					throw new IllegalArgumentException(country.getName() + " and " + adjacent + "do not link to each other.");
    				}
    			}
    			else {
    				throw new IllegalArgumentException("Adjacent country of " + country.getName() + ": " + adjacent + " is invalid.");
    			}
    		}
    	}
    }

    public RiskMap readFile(String pathName) throws IOException {
    	File file = new File(pathName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Stream<String> mapFile = bufferedReader.lines();
        mapFile = mapFile.filter(l -> !"".equals(l.trim()));

        mapFile.forEach(line -> {
            line = line.trim();
            if (line.equals("[Map]") || line.equals("[Territories]") || line.equals("[Continents]")) {
                label = line;
            } else {
                switch (label.toLowerCase()) {
                    case "[map]": {
                        break;
                    }
                    case "[continents]": {
                        String[] token = line.split("=");
                        try {
                            Continent continent = new Continent(token[0], Integer.parseInt(token[1]));
                            continentList.add(continent) ;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        bContinent = true;
                        break;
                    }
                    case "[territories]": {
                        String[] token = line.split(",");
                        try {
                            Country country = new Country();                            
                            if (countryHashMap.containsKey(token[0])) {
                            	countryHashMap.remove(token[0]);
                            } 
                            country.setName(token[0]);
                            country.setLocation(Integer.parseInt(token[1]), Integer.parseInt(token[2]));        
                            country.setContinentName(token[3]);
                            countryHashMap.put(country.getName(), country);
                            mapWidth = Math.max(mapWidth, country.getX());
                            mapHeight = Math.max(mapHeight, country.getY());
                            
                            edgeHashMap.put(country.getName(), new LinkedList<String>());                          
                            for (int i = 4; i < token.length; i++) {
                            	edgeHashMap.get(country.getName()).add(token[i]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bterritories = true;
                        break;
                    }
                    default:
                        break;
                }
            }

        });
        bufferedReader.close();
        this.validateBlockName();
        this.validateContinent();
        this.validateCountry();
        
        map.setSize(mapWidth + 100, mapHeight + 100);
        for (Continent continent : continentList) {
        	map.addContinent(continent);
        }
        
        for (Country country : countryHashMap.values()) {
        	for (String adjacent : edgeHashMap.get(country.getName())) {
        		Point location = countryHashMap.get(adjacent).getLocation();
        		country.addAdjacentCountry(location);
        	}
        	map.addCountry(country);
        }
        
        return map;
    }

    public void saveFile(RiskMap map, String fileName) throws IOException {   	    	
        this.map = map;

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));;
        try {
        	writer.write("[Map]" + "\r\n");
            writer.write("\r\n");

            writer.write("[Continents]" + "\r\n");
            for (Continent continent : map.getContinentList()) {
            	writer.write(continent.getName() + "=" + continent.getValue() + "\r\n");
            }
            writer.write("\r\n");

            writer.write("[Territories]" + "\r\n");
            for (Continent continent : map.getContinentList()) {
            	for (Country country : map.getCountryList()) {
            		if (country.getContinentName().equals(continent.getName())) {
            			writer.write(country.getName()+ "," + country.getX() + "," + country.getY() + "," + country.getContinentName());
            			for (Point adjacent : country.getAdjacentCountryList()) {
            				writer.write("," + map.getCountry(adjacent).getName());
            			}
            			writer.write("\r\n");
            		}
            	}
    			writer.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            writer.close();
        }
        writer.close();
    }


}
