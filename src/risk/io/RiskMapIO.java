package risk.io;

import java.awt.Point;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import risk.game.Continent;
import risk.game.Country;
import risk.game.RiskMap;

/**
 * RiskMapIO class manages the reading and writing map information from or to a .map file.
 * It also provides a format checking when reading a .map file to ensure the information can
 * be correctly loaded into the map editor.
 */
public class RiskMapIO {
    private boolean bContinent = false;
    private boolean bterritories = false;
    private String label = "";

    private RiskMap map;
    private HashMap<String, Country> countryHashMap;
    private LinkedList<Continent> continentList;
    private HashMap<String, LinkedList<String>> edgeHashMap;
    
    private int mapHeight;
    private int mapWidth;

    /**
     * Constructor of the class. Initialize all class variables.
     */
    public RiskMapIO() {
        map = new RiskMap();
        countryHashMap = new HashMap<String, Country>();
        continentList = new LinkedList<Continent>();
        edgeHashMap = new HashMap<String, LinkedList<String>>();
        mapHeight = 0;
        mapWidth = 0;
    }

    /**
     * Check the format of block names.
     * @throws IOException if the file does not contain necessary blocks.
     */
    private void validateBlockName() throws IOException  {
        if (!(bContinent && bterritories)) {
            throw new IOException("bolck name is invalid!");
        }
    }

    /**
     * Check the value of continents and validation of the continent of each country.
     * @throws IOException if the value of a continent is 0 or some countries contain
     * an invalid continent.
     */
    private void validateContinent() throws IOException {
    	boolean found = false;
    	for (Continent continent : continentList) {
    		if (continent.getValue() == 0) {
    			throw new IOException("Continent " + continent.getName() + " has value 0");
    		}
    	}
    	
    	for (Country country : countryHashMap.values()) {
    		found = false;
    		for (Continent continent : continentList) {
    			if (continent.getName().equals(country.getContinentName())) {
    				found = true;
    			}
    		}
    		
    		if (!found) {
    			throw new IOException("Continent of " + country.getName() + ": " + country.getContinentName() + " is invalid.");
    		}
    	}
    }

    /**
     * Validate every country and their adjacent countries.
     * @throws IOException if a country's adjacent country is not valid or a country's adjacent
     * country does not links to itself.
     */
    private void validateCountry() throws IOException {
    	for (Country country : countryHashMap.values()) {
    		if (edgeHashMap.get(country.getName()).size() > Country.MAX_ADJACENT_COUNTRIES) {
    			throw new IOException(country.getName() + " has more than 10 adjacent countries.");
    		}
    		for (String adjacent : edgeHashMap.get(country.getName())) {
    			if (countryHashMap.containsKey(adjacent) && edgeHashMap.containsKey(adjacent)) {
    				if (!edgeHashMap.get(adjacent).contains(country.getName())) {
    					throw new IOException(country.getName() + " and " + adjacent + "do not link to each other.");
    				}
    			}
    			else {
    				throw new IOException("Adjacent country of " + country.getName() + ": " + adjacent + " is invalid.");
    			}
    		}
    	}
    }
    
    /**
     * Read a .map file and store all information to an instance of RiskMap and check
     * correctness of every information.
     * @param pathName is the path name of the file to be read.
     * @return an instance of RiskMap.
     * @throws IOException if encounters IO error.
     */
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
                        Continent continent = new Continent(token[0], Integer.parseInt(token[1]));
                        continentList.add(continent) ;
                        
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

    /**
     * Write information stored in the RiskMap to .map file with appropriate format.
     * @param map is an instance of RiskMap which contains all information.
     * @param fileName is the path name of the file to be written.
     * @throws IOException if encounters IO error.
     */
    public void saveFile(RiskMap map, String fileName) throws IOException {   	    	
        this.map = map;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));;
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

        writer.close();
    }

}
