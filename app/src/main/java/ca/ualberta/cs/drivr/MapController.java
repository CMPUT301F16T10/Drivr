package ca.ualberta.cs.drivr;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by justin on 09/11/16.
 */
public class MapController {

    private GoogleMap map;
    private Location myLocation;
    private Location destination;
    private Location pickupLocation;
    private Marker passedMarker;
    private Double myLatitude;
    private Double myLongitude;

    private GPSTracker gpsTracker;


    public MapController(GoogleMap map, Context context) {
        // initialize map
        this.map = map;
        gpsTracker = new GPSTracker(context);
        // initialize location

        myLocation = gpsTracker.getMyLocation();
        myLatitude = myLocation.getLatitude();
        myLongitude = myLocation.getLongitude();

        // Zoom map in to current Location
        zoomToCurrentLocation();

    }

    // Call To add A Marker
    public void addDestination(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Destination");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        map.addMarker(markerOptions);

    }
    // Call automatically to zoom to currentLocation
    public void zoomToCurrentLocation(){


        // Cam
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(myLatitude, myLongitude))      // Sets the center of the map to location user
                .zoom(9)                   // Sets the zoom
                .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    // Call to add A Pick Up Marker
    public void addPickUp(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Pickup Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        map.addMarker(markerOptions);
    }



}
