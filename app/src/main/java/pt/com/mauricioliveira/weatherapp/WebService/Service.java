package pt.com.mauricioliveira.weatherapp.WebService;

import pt.com.mauricioliveira.weatherapp.Helper.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by moliveira on 11-09-2017.
 */

public class Service {

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static WeatherAPI weatherAPIService = retrofit.create(WeatherAPI.class);
}
