package pt.com.mauricioliveira.weatherapp.GPSTracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by moliveira on 13-09-2017.
 */

public class GpsTracker extends Service implements LocationListener {

    private static final long MIN_DISTANCE_UPDATE = 10; //10 METERS
    private static final long MIN_TIME_BETWEEN_UPDATE = 1000; //1 Minute
    private final Context context;
    protected LocationManager locationManager;
    boolean isGpsEnable;
    boolean isNetWorkEnable;
    boolean canGetLocation = false;
    Location location;
    double lat;
    double lon;

    public GpsTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {
        try {

            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetWorkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (!isGpsEnable && !isNetWorkEnable) {
                } else {
                    this.canGetLocation = true;

                    if (isGpsEnable) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BETWEEN_UPDATE,
                                MIN_DISTANCE_UPDATE,
                                this
                        );

                        if (locationManager == null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }

                    if(location==null) {

                        // Get location from network provider
                        if (isNetWorkEnable) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BETWEEN_UPDATE,
                                    MIN_DISTANCE_UPDATE,
                                    this
                            );

                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                }
                            }
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }


    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        // return latitude
        return lat;
    }


    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }

        // return longitude
        return lon;
    }


    public boolean isCanGetLocation (){
        return this.canGetLocation;
    }


    public void showSettingsAlert (){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Gps Settings");

        alertDialog.setMessage("Gps is not enable. Do you want turn on?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //show alert dialog
        alertDialog.show();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            this.lat = location.getLatitude();
            this.lon = location.getLongitude();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
