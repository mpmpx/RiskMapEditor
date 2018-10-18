package DataStructure;


import java.io.File;
import java.util.List;

public class GameMap {

    private static final GameMap instance = new GameMap();

    public static GameMap getInstance() {
        return instance;
    }

    private File image;

    private Coordinator coordinator;

    private Boolean wrap;

    private String author;

    private String scroll;

    private Boolean warn;

    private List<Continent> continents;

    private List<Country> territories;

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public Boolean getWrap() {
        return wrap;
    }

    public void setWrap(Boolean wrap) {
        this.wrap = wrap;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getScroll() {
        return scroll;
    }

    public void setScroll(String scroll) {
        this.scroll = scroll;
    }

    public List<Continent> getContinents() {
        return continents;
    }

    public void setContinents(List<Continent> continents) {
        this.continents = continents;
    }

    public List<Country> getTerritories() {
        return territories;
    }

    public void setTerritories(List<Country> territories) {
        this.territories = territories;
    }

    public Boolean getWarn() {
        return warn;
    }

    public void setWarn(Boolean warn) {
        this.warn = warn;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}

