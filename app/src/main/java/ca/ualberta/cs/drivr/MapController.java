package ca.ualberta.cs.drivr;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justin on 09/11/16.
 */
public class MapController implements DirectionCallback{

    private GoogleMap map;
    private Location myLocation;
    private LatLng destinationLatLng;
    private LatLng pickupLatlng;
    private Marker passedMarker;
    private Double myLatitude;
    private Double myLongitude;
    private Context mContext;

    private GPSTracker gpsTracker;
    private String serverKey = "AIzaSyB13lv5FV6dbDRec8NN173qj4HSHuNmPHE";

    public MapController(GoogleMap map, Context context) {
        // initialize map
        this.mContext = context;
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

    public void estimateCost(){
        //int totalDistance = myLocation.distanceTo()
        // float cost = totalDistance * 1.75
    }




    // Call to add A Pick Up Marker
    public void addPickUp(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Pickup Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        map.addMarker(markerOptions);
    }

    // Send two Locations to Map and draw a route between them
    public void addRequestOnMap(LatLng pickup, LatLng destination){

        this.pickupLatlng = pickup;
        this.destinationLatLng = destination;

            GoogleDirection.withServerKey(serverKey)
                    .from(pickup)
                    .to(destination)
                    .transitMode(TransportMode.DRIVING)
                    .execute(this);


    }


    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            map.addMarker(new MarkerOptions().position(pickupLatlng).title("Pickup").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            map.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            map.addPolyline(DirectionConverter.createPolyline(mContext,directionPositionList,3, Color.RED));



        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }
}
