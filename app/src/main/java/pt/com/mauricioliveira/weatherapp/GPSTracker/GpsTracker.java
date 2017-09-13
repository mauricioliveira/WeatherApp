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
import android.widget.Toast;

/**
 * Created by moliveira on 13-09-2017.
 */

public class GpsTracker extends Service implements LocationListener {

    //minimum distance for update
    private static final long MIN_DISTANCE_UPDATE = 10; //10 METERS
    //minium time between update milliseconds
    private static final long MIN_TIME_BETWEEN_UPDATE = 1000; //1 Minute
    private final Context context;
    //Location Manager
    protected LocationManager locationManager;
    //flag for GPS status
    boolean isGpsEnable;
    //flag for network status
    boolean isNetWorkEnable;
    //flag for GPS location
    boolean canGetLocation = false;
    //location
    Location location;
    //latitude
    double lat;
    //longitude
    double lon;

    public GpsTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {
        try {

            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            //get GPS status
            isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //get Network status
            isNetWorkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (!isGpsEnable && !isNetWorkEnable) {
                    //no GPS/Network enable
                    Toast.makeText(context,"Without GPS/Network access",Toast.LENGTH_LONG).show();
                } else {
                    this.canGetLocation = true;

                    //if GPS is enable get lat/lon using GPS Services
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

    //stop using GPS
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }


    //get latitude
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        // return latitude
        return lat;
    }


    //get longitude
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }

        // return longitude
        return lon;
    }

    //check if gps/network is enable

    public boolean isCanGetLocation (){
        return this.canGetLocation;
    }

    //alert dialog for user turn on GPS

    public void showSettingsAlert (){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        //Title
        alertDialog.setTitle("Gps Settings");

        //Message
        alertDialog.setMessage("Gps is not enable. Do you want turn on?");

        //Pressing positive button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        //Pressing negative button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //show alert dialog
        alertDialog.show();
    }


    //default method for LocationListner
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
