import DataStructure.*;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;


public class RiskMapOP {

    private static boolean bMapHead = false;
    private static boolean bContinent = false;
    private static boolean bterritories = false;

    private static String label = "";

    private static LinkedHashMap<String, Continent> continentMap = new LinkedHashMap<>();

    private static LinkedHashMap<String, Country> countryMap = new LinkedHashMap<>();

    private static int w = 0;

    private static int h = 0;

    private static Continent getContinent(String name) {

        return continentMap.get(name);
    }

    private static List<Continent> getAllContinents() {

        return new ArrayList<>(continentMap.values());
    }

    private static void addContinent(Continent continent) {

        continentMap.put(continent.getName(), continent);
    }

    private static List<Country> getAllCountry() {

        return new ArrayList<>(countryMap.values());
    }

    private static Country getCountry(String name) {

        return countryMap.get(name);
    }

    private static void addCountry(Country country) {

        countryMap.put(country.getName(), country);
    }

    private static void validateName() {
        if (!(bMapHead && bContinent && bterritories)) {
            throw new IllegalArgumentException("bolck name is invalid!");
        }
    }

    private static void validateContinent() {

    }

    private static void validateCountry() {

        Country country = countryMap.values().stream().filter(i -> i.getCoordinator() == null)
                .findAny().orElse(null);

        if (country != null) throw new IllegalArgumentException("Illegal adjacent country: " + country.getName());

        countryMap.values().forEach(c -> {

            List<Country> adjacent = c.getAdjacentCountry();

            adjacent.forEach(a -> {
                if (!a.getAdjacentCountry().contains(c))

                    throw new IllegalArgumentException("Illegal adjacent country " + c.getName() + " and " + a.getName());
            });
        });
    }

    public static boolean readFile(File file) throws IOException {

        GameMap map = GameMap.getInstance();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        Stream<String> mapFile = bufferedReader.lines();

        mapFile = mapFile.filter(l -> !"".equals(l.trim()));


        mapFile.forEach(s -> {

            s = s.trim();

            if (s.equals("[Map]") || s.equals("[Territories]") || s.equals("[Continents]")) {

                label = s;

            } else {

                switch (label.toLowerCase()) {

                    case "[map]": {

                        String[] split = s.split("=");

                        switch (split[0].trim()) {

                            case "author":
                                map.setAuthor(split[1]);
                                break;

                            case "scroll":
                                map.setScroll(split[1]);
                                break;

                            case "image":
                                map.setImage(new File(split[1]));
                                break;

                            case "wrap":
                                map.setWrap(split[1].trim().toLowerCase().equals("yes"));
                                break;

                            case "warn":
                                map.setWarn(split[1].trim().toLowerCase().equals("yes"));
                                break;

                            default:
                                break;
                        }
                        bMapHead = true;

                        break;

                    }

                    case "[continents]": {

                        String[] str = s.split("=");

                        try {

                            Continent continent = new Continent(str[0], Integer.parseInt(str[1]));

                            RiskMapOP.addContinent(continent);


                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        bContinent = true;

                        break;
                    }

                    case "[territories]": {

                        String[] str = s.split(",");


                        try {

                            int x = Integer.parseInt(str[1]);

                            int y = Integer.parseInt(str[2]);

                            Country country;

                            if (RiskMapOP.getCountry(str[0]) != null) {

                                country = RiskMapOP.getCountry(str[0]);

                                country.setCoordinator(new Coordinator(x, y));

                            } else country = new Country(str[0], new Coordinator(x, y));

                            w = Math.max(w, x);

                            h = Math.max(h, y);

                            String continentName = str[3];

                            Continent continent = RiskMapOP.getContinent(continentName);

                            if (continent == null) throw new IllegalArgumentException("Continent is invalid for: " + s);

                            continent.getCountries().add(country);

                            country.setContinent(continent);

                            RiskMapOP.addContinent(continent);

                            for (int i = 4; i < str.length; i++) {

                                Country adjacent = RiskMapOP.getCountry(str[i]);

                                if (adjacent != null) {

                                    country.getAdjacentCountry().add(adjacent);

                                } else {

                                    RiskMapOP.addCountry(new Country(str[i]));

                                    country.getAdjacentCountry().add(RiskMapOP.getCountry(str[i]));
                                }
                            }

                            RiskMapOP.addCountry(country);

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

        RiskMapOP.validateName();

        RiskMapOP.validateContinent();

        RiskMapOP.validateCountry();

        map.setContinents(RiskMapOP.getAllContinents());

        map.setTerritories(RiskMapOP.getAllCountry());

        map.setCoordinator(new Coordinator(w, h));

        return true;
    }

    public static boolean saveFile(String PathOut) throws IOException {

        boolean bState = false;

        GameMap map = GameMap.getInstance();

        FileWriter fw;
        BufferedWriter bufw;
        try {
            fw = new FileWriter(PathOut);
            bufw = new BufferedWriter(fw);

            bufw.write("[Map]" + "\n");
            bufw.write("author=" + map.getAuthor() + "\n");
            bufw.write("warn=" + map.getWarn() + "\n");
            bufw.write("image=" + map.getImage() + "\n");
            bufw.write("wrap=" + map.getWarn() + "\n");
            bufw.write("scroll=" + map.getScroll() + "\n");
            bufw.write("\n");

            List<Continent> continents = map.getContinents();
            bufw.write("[continents]" + "\n");
            for (int i = 0; i < continents.size(); i++) {
                bufw.write(continents.get(i).getName() + "=" + continents.get(i).getArmy() + "\n");
            }
            bufw.write("\n");

            bufw.write("[Territories]" + "\n");
            List<Country> territories = map.getTerritories();
            LinkedList<String> str = new LinkedList<String>();
            for (int i = 0; i < territories.size(); i++) {
                Country country = territories.get(i);
                List<Country> AdjacentCountry = country.getAdjacentCountry();
                String strAdjacentCountry = "";
                for (int j = 0; j < AdjacentCountry.size(); j++) {
                    strAdjacentCountry += "," + AdjacentCountry.get(j).getName();
                }
                str.add(country.getName() + "," + country.getCoordinator().getX() + "," + country.getCoordinator().getY() + ","
                        + country.getContinent().getName() + strAdjacentCountry);
            }


            for (int j = 0; j < continents.size(); j++) {
                String Strcontinents = continents.get(j).getName();
                for (int i = 0; i < str.size(); i++) {
                    if(str.get(i).contains(Strcontinents)) {
                        bufw.write(str.get(i)+"\n");
                    }

                    bufw.flush();
                }
                bufw.write("\n");

            }
            
            bState = true;
        } catch (IOException e) {
            e.printStackTrace();
            bState = false;
        }
        return bState;
    }


    public static void main(String[] args) throws IOException {
        RiskMapOP fileOP = new RiskMapOP();

        try {
            String MapFileInpath = "/Users/cys/RISK/Africa.map";
            fileOP.readFile(new File(MapFileInpath));


            String MapFileOutpath = "/Users/cys/RISK/Africa_new.map";
            fileOP.saveFile(MapFileOutpath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


