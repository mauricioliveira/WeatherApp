package pt.com.mauricioliveira.weatherapp.Interface;

import pt.com.mauricioliveira.weatherapp.Model.WeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by moliveira on 11-09-2017.
 */

public interface WeatherAPI {
    @GET("weather?")
    Call<WeatherData> getCurrentWeather(@Query("lat") double lat,
                                        @Query("lon") double lon,
                                        @Query("APPID") String appid,
                                        @Query("units") String units,
                                        @Query("lang") String lang);
}
