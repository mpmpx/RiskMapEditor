package risk.controller;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import risk.game.*;

/**
 * ReadFileController is responsible for reading map files. Additionally check format of the file
 * and make map validation.
 */
public class ReadFileController {

	private HashMap<String, Continent> continentMap;
	private HashMap<String, Territory> territoryMap;
	private HashMap<String, LinkedList<String>> edgeMap;
	private String imagePath;
	private BufferedImage image;
	private String label;
	
	private boolean bMap, bContinent, bTerritory;
	
	/**
	 * Constructor Method
	 */
	public ReadFileController() {
		continentMap = new HashMap<String, Continent>();
		territoryMap = new HashMap<String, Territory>();
		edgeMap = new HashMap<String, LinkedList<String>>();
		image = null;
		
		bMap = false;
		bContinent = false;
		bTerritory = false;
	}
	
	/**
	 * Check whether necessary blocks are in the map file.
	 * @throws IOException if the block name is invalid
	 */
    private void validateBlockName() throws Exception  {
        if (!(bMap && bContinent && bTerritory)) {
            throw new Exception("block name is invalid!");
        }
    }

    /**
     * Method to check the value of continents and validation of the continent of each territory.
     * @throws IOException if the value of a continent is 0 or some countries contain
     * an invalid continent.
     */
    private void validateContinent() throws Exception {
    	boolean found = false;
    	
    	if (continentMap.size() > RiskMap.MAX_CONTINENT) {
    		throw new Exception("Number of the continents exceeds the Maximum " + RiskMap.MAX_CONTINENT + ".");
    	}
    	
    	for (Continent continent : continentMap.values()) {
    		if (continent.getValue() < 0) {
    			throw new Exception("Continent " + continent.getName() + " has invalid value");
    		}
    	}
    	
    	for (Territory territory : territoryMap.values()) {
    		found = false;
    		for (Continent continent : continentMap.values()) {
    			if (continent.getName().equals(territory.getContinentName())){
    				found = true;
    			}
    		}
    		
    		if (!found) {
    			throw new Exception("Continent of " + territory.getName() + ": " + territory.getContinentName() + " is invalid.");
    		}
    	}
    }

    /**
     * Check whether all continents are subgraph.
     * @throws Exception if one of continents is not subgraph.
     */
    private void validateConnectedContinent() throws Exception {
    	HashMap<String, HashMap<String, Territory>> tmpContinentMap = new HashMap<String, HashMap<String, Territory>>();

    	for (Continent continent : continentMap.values()) {
    		tmpContinentMap.put(continent.getName(), new HashMap<String, Territory>());
    	}
    	
    	for (Territory territory : territoryMap.values()) {
    		tmpContinentMap.get(territory.getContinentName()).put(territory.getName(), territory);
    	}
    	
    	for (String continentName : tmpContinentMap.keySet()) {
    		HashMap<String, Territory> tmpTerritoryMap = tmpContinentMap.get(continentName);
        	String firstTerritory = null;
        	for (String name : tmpTerritoryMap.keySet()) {
        		firstTerritory = name;
        		break;
        	}
        	
        	HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
        	for (Territory territory : tmpTerritoryMap.values()) {
        		visited.put(territory.getName(), false);
        	}
      
        	Stack<String> stack = new Stack<String>();
        	stack.push(firstTerritory);
        	
        	while (!stack.isEmpty()) {
        		String currentTerritory = stack.pop();
        		if (visited.containsKey(currentTerritory)) {	
        			if (visited.get(currentTerritory) == false) {
        				visited.replace(currentTerritory, true);
        				for (String adjacentTerritory : edgeMap.get(currentTerritory)) {
        					stack.push(adjacentTerritory);
        				}
        			}
        		}
        	}
        	    	
        	for (Boolean status : visited.values()) {
        		if (status == false) {
    				throw new Exception(continentName + " is not a connected subgraph");
        		}
        	}
    	}
    }
    
    /**
     * This Method is used to validate every territory and their adjacent countries.
     * @throws IOException if a territory's adjacent territory is not valid or a territory's adjacent
     * territory does not links to itself.
     */
    private void validateTerritory() throws Exception {
    	if (territoryMap.size() > RiskMap.MAX_COUNTRY) {
			throw new Exception("Number of countries exceeds the Maximum: " + RiskMap.MAX_COUNTRY + ".");
    	}
    	
    	for (Territory territory : territoryMap.values()) {
    		if (edgeMap.get(territory.getName()).size() > RiskMap.MAX_ADJACENT_COUNTRIES) {
    			throw new Exception(territory.getName() + " has more than 10 adjacent countries.");
    		}
    		for (String adjacent : edgeMap.get(territory.getName())) {
    			if (territoryMap.containsKey(adjacent) && edgeMap.containsKey(adjacent)) {
    				if (!edgeMap.get(adjacent).contains(territory.getName())) {
    					throw new Exception(territory.getName() + " and " + adjacent + " do not link to each other.");
    				}
    			}
    			else {
    				throw new Exception("Adjacent territory of " + territory.getName() + ": " + adjacent + " is invalid.");
    			}
    		}
    	}
    }
    
    /**
     * Check whether the map is a connected graph.
     * @throws IOException if the the map read from the file is not a connected graph.
     */
    private void validateConnectedGraph() throws Exception {
    	String firstTerritory = null;
    	for (String name : territoryMap.keySet()) {
    		firstTerritory = name;
    		break;
    	}
    	
    	HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
    	for (Territory territory : territoryMap.values()) {
    		visited.put(territory.getName(), false);
    	}
  
    	Stack<String> stack = new Stack<String>();
    	stack.push(firstTerritory);
    	
    	while (!stack.isEmpty()) {
    		String currentTerritory = stack.pop();
    		if (visited.get(currentTerritory) == false) {
    			visited.replace(currentTerritory, true);
    			for (String adjacentTerritory : edgeMap.get(currentTerritory)) {
    				stack.push(adjacentTerritory);
    			}
    		}
    	}
    	    	
    	for (Boolean status : visited.values()) {
    		if (status == false) {
				throw new Exception("The map contained in the file is not a connected graph.");
    		}
    	}
    }
    
    /**
     * Get shape of the given territory.
     * @param territory get shape of this territory.
     */
    private LinkedList<Point> getTerritoryShape(Territory territory) {
    	LinkedList<Point> shape = new LinkedList<Point>();
    	Point location = territory.getLocation();
    	Stack<Point> stack = new Stack<Point>();
    	HashMap<Point, Boolean> visited = new HashMap<Point, Boolean>();
    	    	
    	int originColor = image.getRGB(location.x, location.y);
    	
    	stack.push(location);
    	
    	while (!stack.isEmpty()) {
    		Point currentLocation = stack.pop();
 
    		
    		if (image.getRGB(currentLocation.x, currentLocation.y) == originColor) {
    			if (currentLocation.x > 0 && currentLocation.x < image.getWidth()) {
    				if (currentLocation.y > 0 && currentLocation.y < image.getHeight()) {
    					if (visited.get(currentLocation) == null || visited.get(currentLocation) != true) {
    						shape.add(currentLocation);
    						visited.put(currentLocation, true);
    						//System.out.println(currentLocation.x + " " + currentLocation.y);
    					
    					
    		    			stack.push(new Point(currentLocation.x + 1, currentLocation.y));
    		    			stack.push(new Point(currentLocation.x - 1, currentLocation.y));
    		    			stack.push(new Point(currentLocation.x, currentLocation.y + 1));
    		    			stack.push(new Point(currentLocation.x, currentLocation.y - 1)); 
    					}
    				}
    			}
    		}
    		

    	}
    	
    	return shape;
    }
    
    
    /**
     * Method to read a .map file and store all information to an instance of RiskMap and check
     * correctness of every information.
     * @param pathName is the path name of the file to be read.
     * @return an instance of RiskMap.
     * @throws IOException if encounters IO error.
     */	
    public RiskMap readFile(String pathName) throws Exception {
    	RiskMap map = new RiskMap();
    
    	File file = new File(pathName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Stream<String> mapFile = bufferedReader.lines();
        mapFile = mapFile.filter(l -> !"".equals(l.trim()));

        mapFile.forEach(line -> {

            line = line.trim();
            if (line.equals("[Map]") || line.equals("[Territories]") || line.equals("[Continents]")) {
                label = line;
            } else {
                switch (label) {
                    case "[Map]": {
                    	String[] token = line.split("=");
                    	if (token[0].equals("image")) {
                			imagePath = new File(file.getParent(), token[1]).getPath();
                    		try {
								image = ImageIO.read(new File(imagePath));
							
                    		} catch (Exception e) {
								e.printStackTrace();
							}
                    		
                    		// Change the color model of the image to accommodate all colors.
                    	    BufferedImage newImage= new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    	    Graphics2D g2d= newImage.createGraphics();
                    	    g2d.drawImage(image, 0, 0, null);
                    	    g2d.dispose();
                    		map.setImage(newImage);
                    	}
                    	
                    	bMap = true;
                        break;
                    }
                    case "[Continents]": {
                        String[] token = line.split("=");
                        Continent continent = new Continent(token[0], Integer.parseInt(token[1]));
                        continentMap.put(continent.getName(), continent) ;
                        
                        bContinent = true;
                        break;
                    }
                    case "[Territories]": {
                        String[] token = line.split(",");
                        
                        Territory territory = new Territory();                            
                        
                        // Set basic information of a territory.
                        territory.setName(token[0]);
                        territory.setLocation(Integer.parseInt(token[1]), Integer.parseInt(token[2]));        
                        territory.setContinentName(token[3]);
                        territoryMap.put(territory.getName(), territory);    
                        territory.setShape(getTerritoryShape(territory));
                        
                        edgeMap.put(territory.getName(), new LinkedList<String>());                          
                        for (int i = 4; i < token.length; i++) {
                        	edgeMap.get(territory.getName()).add(token[i]);
                        }

                        bTerritory = true;
                        break;
                    }
                    default:
                        break;
                }
            }

        });
        bufferedReader.close();
        
        // Map validation.
        validateBlockName();
        validateContinent();
        validateTerritory();
        validateConnectedGraph();
    	validateConnectedContinent();
        
        for (Territory territory : territoryMap.values()) {
        	continentMap.get(territory.getContinentName()).addTerritory(territory);
        	map.addTerritory(territory);
        }
        
        for (Continent continent : continentMap.values()) {
        	map.addContinent(continent);
        }
        
        map.addLink(edgeMap);
        
        return map;
    }
}
