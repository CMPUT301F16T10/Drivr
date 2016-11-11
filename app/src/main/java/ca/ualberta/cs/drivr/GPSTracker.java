/*
 * Copyright 2016 CMPUT301F16T10
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
 * http://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
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

    /**
     * Initiates A GPS Tracker
     * .
     */
    public GPSTracker(Context context) {
        this.mContext = context;
        myLocation = getLocation();
    }

    /**
     * Gets the User Current Location through GPS or Wifi
     * @return returns user current Location
     * .
     */

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

    /**
     * End the updating through the GPS
     */
    public void stopUsingGPS() {
        if (locationManger != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManger.removeUpdates(GPSTracker.this);
            }

        }
    }

    /**
     * Get users Location object
     * @return returns user current Location
     * .
     */
    public Location getMyLocation() {
        return myLocation;
    }

    /**
     * Opens settings Activity to allow user to turn on GPs
     * .
     */
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
