package pt.com.mauricioliveira.weatherapp.Model;

/**
 * Created by moliveira on 10-09-2017.
 */

public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;

    public Weather(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        //capitalize first letter
        return description.substring(0, 1).toUpperCase() + description.substring(1);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
