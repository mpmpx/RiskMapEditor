package DataStructure;

import java.util.ArrayList;
import java.util.List;

public class Continent {

    private String name;

    private int army;

    private List<Country> countries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public Continent(String name, int army) {

        if ("".equals(name.trim())||army==0){

            throw new IllegalArgumentException("Invalid data for continent: "+ name+", army:"+army);
        }
        this.name = name;

        this.army = army;

        this.countries = new ArrayList<>();
    }
}
