package pt.com.mauricioliveira.weatherapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.com.mauricioliveira.weatherapp.GPSTracker.GpsTracker;
import pt.com.mauricioliveira.weatherapp.Helper.Constants;
import pt.com.mauricioliveira.weatherapp.Helper.Util;
import pt.com.mauricioliveira.weatherapp.Model.WeatherData;
import pt.com.mauricioliveira.weatherapp.WebService.Service;
import pt.com.mauricioliveira.weatherapp.WebService.WeatherAPI;
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
    private static final int ALL_PERMISSIONS_REQUEST = 1337;
    private static final String[] ALL_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);

        checkAppPermissions(this);

        gpsTracker = new GpsTracker(MainActivity.this);

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

        Call<WeatherData> weatherDataCall = weatherAPI.getCurrentWeather(lat, lon, Constants.API_KEY, Constants.UNITS, Constants.LANGUAGE);

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

    public static void checkAppPermissions(Activity activity){
        if (!hasPermissions(activity, ALL_PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, ALL_PERMISSIONS, ALL_PERMISSIONS_REQUEST);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermissions(Activity activity, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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