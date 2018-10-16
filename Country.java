package DataStructure;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Country {

    private Color color;

    private String name;

    private List<Country> adjacentCountry;

    private Coordinator coordinator;

    private Continent continent;

    private Player player;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Country> getAdjacentCountry() {
        return adjacentCountry;
    }

    public void setAdjacentCountry(List<Country> adjacentCountry) {
        this.adjacentCountry = adjacentCountry;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Country(String name, Coordinator coordinator) {

        this.name = name;

        this.coordinator = coordinator;

        this.adjacentCountry = new ArrayList<>();
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {

        this.continent = continent;
    }

    public Country(String name){

        this.name = name;

        this.adjacentCountry = new ArrayList<>();
    }
}

