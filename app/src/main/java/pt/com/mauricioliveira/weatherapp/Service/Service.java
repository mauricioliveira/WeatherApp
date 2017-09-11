package pt.com.mauricioliveira.weatherapp.Service;

import pt.com.mauricioliveira.weatherapp.Interface.WeatherAPI;
import pt.com.mauricioliveira.weatherapp.Model.WeatherData;
import pt.com.mauricioliveira.weatherapp.Util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by moliveira on 11-09-2017.
 */

public class Service {

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Util.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static WeatherAPI weatherAPIService = retrofit.create(WeatherAPI.class);
}
