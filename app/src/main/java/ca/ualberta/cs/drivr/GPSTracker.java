package ca.ualberta.cs.drivr;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by justin on 09/11/16.
 */

/**
 * Able to get User Current Location
 */

public class GPSTracker implements LocationListener {


    private Context mContext;
    protected LocationManager locationManger;

    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;

    private boolean canGetLocation = false;

    Location myLocation;
    // get
    Location location;

    double myLatitude;
    double myLongitude;


    public GPSTracker(Context context) {
        this.mContext = context;
        myLocation = getLocation();
    }

    //
    public Location getLocation() {
        try {
            locationManger = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // Check that App can get Location
            isGPSEnabled = locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled  && !isNetworkEnabled) {
                // Services not Enabled
                enableGPS();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    location = null;
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
                        if (locationManger != null) {
                            location = locationManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            if (location != null) {
                                myLatitude = location.getLatitude();
                                myLongitude = location.getLongitude();
                            }
                        }

                    }


                }
                if (isGPSEnabled) {
                    location = null;
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                        if (locationManger != null) {
                            location = locationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                myLatitude = location.getLatitude();
                                myLongitude = location.getLongitude();
                            }
                        }

                    }


                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (myLocation != null) {
            return myLocation;
        }
        else {
            // Android Emulator Can't Use Wifi
            myLocation = new Location("Edmonton");
            myLocation.setLatitude(53.5232);
            myLocation.setLongitude(-113.5263);
            myLatitude = myLocation.getLatitude();
            myLongitude = myLocation.getLongitude();

            return myLocation;
        }
    }

    // end use of GPS
    public void stopUsingGPS() {
        if (locationManger != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManger.removeUpdates(GPSTracker.this);
            }

        }
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void enableGPS(){
        AlertDialog.Builder GPSDialog = new AlertDialog.Builder(mContext);

        GPSDialog.setTitle("Please turn on Location Settings");
        GPSDialog.setMessage("GPS and/or A Network Provider is Required");

        GPSDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });


         GPSDialog.show();

    }



    @Override
    public void onLocationChanged(Location location) {

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
