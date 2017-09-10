package pt.com.mauricioliveira.weatherapp.Model;

/**
 * Created by moliveira on 10-09-2017.
 */

public class Sys {
    private double message;
    private String country;
    private double sunrise;
    private double sunset;

    public Sys(double message, String country, double sunrise, double sunset) {
        this.message = message;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSunrise() {
        return sunrise;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }

    public double getSunset() {
        return sunset;
    }

    public void setSunset(double sunset) {
        this.sunset = sunset;
    }
}
