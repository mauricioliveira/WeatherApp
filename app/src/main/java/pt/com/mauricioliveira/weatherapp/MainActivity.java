package pt.com.mauricioliveira.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.com.mauricioliveira.weatherapp.GPSTracker.GpsTracker;
import pt.com.mauricioliveira.weatherapp.Interface.WeatherAPI;
import pt.com.mauricioliveira.weatherapp.Model.WeatherData;
import pt.com.mauricioliveira.weatherapp.Service.Service;
import pt.com.mauricioliveira.weatherapp.Util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txtCity)
    TextView txtCity;
    @BindView(R.id.txtLastUpdate)
    TextView txtLastUpdate;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.txtHumidityValue)
    TextView txtHumidityValue;
    @BindView(R.id.txtCelsius)
    TextView txtCelsius;
    @BindView(R.id.min_max_value)
    TextView min_max_value;
    @BindView(R.id.wind_speed_value)
    TextView wind_speed_value;
    Service service;
    GpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);

        Util.checkAppPermissions(MainActivity.this);

        gpsTracker = new GpsTracker(MainActivity.this);

        //check if GPS is enable
        if (gpsTracker.isCanGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lon = gpsTracker.getLongitude();
            getCurrentWeather(lat, lon);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void getCurrentWeather(double lat, double lon) {

        WeatherAPI weatherAPI = service.weatherAPIService;

        Call<WeatherData> weatherDataCall = weatherAPI.getCurrentWeather(lat, lon, Util.API_KEY, Util.UNITS, Util.LANGUAGE);

        weatherDataCall.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();
                    txtCity.setText(weatherData.getName());
                    txtLastUpdate.setText("Last Update: " + Util.getDateNow());
                    txtDescription.setText(weatherData.getWeatherList().get(0).getDescription());
                    txtHumidityValue.setText(weatherData.getMain().getHumidity() + "%");
                    txtCelsius.setText(weatherData.getMain().getTemp() + "°C");
                    min_max_value.setText(weatherData.getMain().getTemp_min() + "°C / " + weatherData.getMain().getTemp_max() + "°C");
                    wind_speed_value.setText(weatherData.getWind().getSpeed() + " m/s");
                    Picasso.with(MainActivity.this)
                            .load(Util.getImageUrl(weatherData.getWeatherList().get(0).getIcon()))
                            .into(imageView);
                } else {
                    Log.e("Erro", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        gpsTracker.stopUsingGPS();
    }

    @Override
    protected void onResume() {
        super.onResume();
        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();
        getCurrentWeather(lat, lon);
    }
}