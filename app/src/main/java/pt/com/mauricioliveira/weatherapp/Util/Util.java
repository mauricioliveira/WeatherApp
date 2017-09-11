package pt.com.mauricioliveira.weatherapp.Util;

/**
 * Created by moliveira on 11-09-2017.
 */

public class Util {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "f4a43230f6c618b1b2f39cc8c6b1d42c";
    public static final String UNITS = "metric";

    //method will get a image from OpenWeather
    public static String getImageUrl(String icon) {
        return String.format("http://openweathermap.org/img/w/%s.png", icon);
    }
}
