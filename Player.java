package DataStructure;


import java.util.ArrayList;
import java.util.List;

public class Player {

    private int id;

    private String username;

    private int army;

    private List<Country> territory = new ArrayList<>();

    private List<Continent> continents = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public List<Country> getTerritory() {
        return territory;
    }

    public void setTerritory(List<Country> territory) {
        this.territory = territory;
    }

    public List<Continent> getContinents() {
        return continents;
    }

    public void setContinents(List<Continent> continents) {
        this.continents = continents;
    }

    public Player(int id, int army){

        this.id = id;

        this.army = army;
    }

    public int gainArmy(){

        int gainedArmy = Math.max(getTerritory().size()/3,3);

        if (getContinents()!=null&& getContinents().isEmpty()){

            for (Continent continent : getContinents()) {

                gainedArmy += continent.getArmy();
            }
        }

        setArmy(getArmy()+gainedArmy);

        return gainedArmy;
    }

    public String getTerritoryString(){

        if (territory.isEmpty()) return "";

        StringBuilder stringBuilder = new StringBuilder();

        territory.forEach(i->stringBuilder.append(i+" "));

        return stringBuilder.toString();
    }

    public String getContinentString(){

        if (continents.isEmpty()) return "";

        StringBuilder stringBuilder = new StringBuilder();

        territory.forEach(i-> stringBuilder.append(i+" "));

        return stringBuilder.toString();
    }
}
