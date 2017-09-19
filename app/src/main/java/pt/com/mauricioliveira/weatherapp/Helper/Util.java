package pt.com.mauricioliveira.weatherapp.Helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by moliveira on 11-09-2017.
 */

public class Util {

    //method will get a image from OpenWeather
    public static String getImageUrl(String icon) {
        return String.format("http://openweathermap.org/img/w/%s.png", icon);
    }

    public static String getDateNow (){
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long) unixTimeStamp * 100);

        return dateFormat.format(date);
    }
}
