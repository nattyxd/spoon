package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.Toast;

import com.here.android.mpa.cluster.ClusterLayer;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapView;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.nokia.maps.restrouting.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class RestaurantsMapViewFragment extends Fragment {

    // map embedded in the map fragment
    private Map map = null;
    private MapView mapView = null;
    private boolean initialCenter = true;
    private boolean initialLocation = true;

    private Location userLocation;

    private ClusterLayer restaurantCluster = null;
    private ClusterLayer userPositionCluster = null;

    private Toast instanceToast;

    private static View view;

    private MapGesture.OnGestureListener tapGestureListener = new MapGesture.OnGestureListener.OnGestureListenerAdapter()
    {
        @Override
        public boolean onTapEvent(PointF p) {
            final double MINIMUM_ZOOM_TO_GUESS = 13; // We won't guess where the user tapped more zoomed out than this level
            final double HEURISTIC_TAP = 50; // increase this to increase the range of false taps allowed

            // only respond to tap events if the user is zoomed beyond a certain point.
            double zoomAmount = map.getZoomLevel();

            if(zoomAmount < MINIMUM_ZOOM_TO_GUESS){
                return false; // it's not worth guessing
            }

            GeoCoordinate c = map.pixelToGeo(p);

            RestaurantItem closestTap = null;
            double nearestDistance = Double.MAX_VALUE;

            for(int i = 0; i < RestaurantContent.restaurantItems.size(); i++){
                GeoCoordinate restaurantLocation = new GeoCoordinate(Double.parseDouble(RestaurantContent.restaurantItems.get(i).getLatitude()), Double.parseDouble(RestaurantContent.restaurantItems.get(i).getLongitude()));

                double theDistance = c.distanceTo(restaurantLocation);
                if(theDistance < nearestDistance){
                    closestTap = RestaurantContent.restaurantItems.get(i);
                    nearestDistance = theDistance;
                }
            }

            // generate a heuristic based on whether or not the tap was close enough
            if(nearestDistance < HEURISTIC_TAP){
                // we are confident enough to say the user tapped the restaurant.
                Log.i("spoonlogcat: ", closestTap.getName() + ", distance: " + nearestDistance);
            }

            return true;
        }

        @Override
        public void onPanEnd(){
            shouldDisplayOffScreenLocationToast();
        }

        public void onMultiFingerManipulationEnd(){
            shouldDisplayOffScreenLocationToast();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, mapFragment).commit();

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_restaurants_map_view, container, false);

            ApplicationContext appCtx = new ApplicationContext(getContext());

            boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                    getContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                    "android.intent.action.MAIN"); /* ATTENTION! Do not forget to update {YOUR_INTENT_NAME} */

            mapFragment.init(appCtx, new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {

                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();
                        mapFragment.getMapGesture().addOnGestureListener(tapGestureListener, 10, true);
//                        mapFragment.getView().setPadding(0, 60, 0 , 0);


                        restaurantsWereRefreshed();
                        userLocationChanged();

                        setupButtons(); // enable interactivity only when the map is done
                    } else {
                        Log.e("spoonlogcat:", "ERROR: Cannot initialize Map Fragment: " + error.getStackTrace());
                    }
                }
            });
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }


        return view;
    }

    public void userLocationChanged(){
        Log.i("spoonlogcat: ", "New user location received!");
        userLocation = RestaurantsFragment.bestUserLocation;

        if(map == null){
            return; // wait for new locations
        }

        if(initialLocation){
            centreMapOnUserLocation();
            initialLocation = false; // only center once
        }

        try{
            // add the user's geolocation to the map
            if(userPositionCluster != null){
                map.removeClusterLayer(userPositionCluster);
            }
            userPositionCluster = new ClusterLayer();
            final MapMarker userMapMarker = new MapMarker();
            final GeoCoordinate userPosition = new GeoCoordinate(userLocation.getLatitude(), userLocation.getLongitude());
            userMapMarker.setCoordinate(userPosition);

            Image image = new Image();
            try{
                image.setImageResource(R.drawable.user_geolocation_raster);
            } catch(IOException e){
                e.printStackTrace();
            }

            map.addClusterLayer(userPositionCluster);

            userMapMarker.setIcon(image);

            userMapMarker.setTitle("You are here");
            userPositionCluster.addMarker(userMapMarker);
        } catch (Exception e){
            e.printStackTrace();
            // this isn't a huge error, just means we got a new location before we were ready for it
        }
    }

    // Update the map when we get new fresh data
    public void restaurantsWereRefreshed(){
//        centreMapOnUserLocation();

        // remove existing restaurants on refresh
        if(restaurantCluster != null){
            map.removeClusterLayer(restaurantCluster);
        }
        restaurantCluster = new ClusterLayer();

        // add the restaurants to the cluster
        for(int i = 0; i < RestaurantContent.restaurantItems.size(); i++){
            final MapMarker mapMarker = new MapMarker();
            final double latitude = Double.parseDouble(RestaurantContent.restaurantItems.get(i).getLatitude());
            final double longitude = Double.parseDouble(RestaurantContent.restaurantItems.get(i).getLongitude());

            final GeoCoordinate geoCoordinate = new GeoCoordinate(latitude, longitude);

            mapMarker.setCoordinate(geoCoordinate);
            restaurantCluster.addMarker(mapMarker);
        }
        map.addClusterLayer(restaurantCluster);
    }

    private void centreMapOnFirstRestaurant(){
        RestaurantItem firstRestaurant = RestaurantContent.restaurantItems.get(0);

        double latitude = Double.parseDouble(firstRestaurant.getLatitude());
        double longitude = Double.parseDouble(firstRestaurant.getLongitude());
        GeoCoordinate coord = new GeoCoordinate(latitude, longitude);
        map.setCenter(coord, Map.Animation.NONE);
    }

    private void centreMapOnUserLocation(){
        GeoCoordinate coord = new GeoCoordinate(userLocation.getLatitude(), userLocation.getLongitude());

        if(initialCenter){
            map.setCenter(coord, Map.Animation.NONE);
            initialCenter = false;
        } else {
            map.setCenter(coord, Map.Animation.BOW);
        }
        map.setZoomLevel(15);
    }

    // Displays a toast if there are off screen locations
    private void shouldDisplayOffScreenLocationToast(){
        GeoBoundingBox box = map.getBoundingBox();

        List<RestaurantItem> restaurants = RestaurantContent.restaurantItems;

        int outOfZoomRestaurantCount = 0;

        for(int i = 0; i < restaurants.size(); i++){
            RestaurantItem currentRestaurant = restaurants.get(i);
            GeoCoordinate restaurantCoord = new GeoCoordinate(Double.parseDouble(currentRestaurant.getLatitude()), Double.parseDouble(currentRestaurant.getLongitude()));
            if(!box.contains(restaurantCoord)){
                outOfZoomRestaurantCount++;
            }
        }

        if(outOfZoomRestaurantCount > 0){
            String text = "";
            if(outOfZoomRestaurantCount > 1){
                text = getString(R.string.en_restaurantsMapFragment_xRestaurantsOutsideMap_Plural, outOfZoomRestaurantCount);
            } else {
                text = getString(R.string.en_restaurantsMapFragment_xRestaurantsOutsideMap_Singular);
            }

            try{
                if(instanceToast != null && instanceToast.getView().getWindowVisibility() == View.VISIBLE){
                    instanceToast.setText(text);
                    instanceToast.show();
                } else {
                    instanceToast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
                    instanceToast.show();
                }
            } catch (Exception ex){
                ex.printStackTrace();
                Log.e("spoonlogcat: " , "Toast got shown from wrong thread.");
            }
        }
    }

    public RestaurantsMapViewFragment(){
        // Required empty constructor
    }

    private void setupButtons(){
        ImageButton gpsButton = view.findViewById(R.id.centreMapOnUserButton);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Centering", Toast.LENGTH_SHORT).show();
                centreMapOnUserLocation();
            }
        });
    }

    public Location getCenterOfMap(){
        GeoCoordinate c = map.getCenter();
        Location l = new Location("");
        l.setLatitude(c.getLatitude());
        l.setLongitude(c.getLongitude());
        return l;
    }

//    private OnEngineInitListener engineInitHandler = new OnEngineInitListener() {
//        @Override
//        public void onEngineInitializationCompleted(Error error) {
//            if (error == Error.NONE) {
//
//                // more map initial settings
//            } else {
//                Log.e("spoonlogcat: ", "ERROR: Cannot initialize MapEngine " + error);
//                Log.e("spoonlogcat: ", error.getDetails());
//                Log.e("spoonlogcat: ", error.getStackTrace());
//            }
//        }
//    };

//    @Override
//    public void onResume() {
//        super.onResume();
//        MapEngine.getInstance().onResume();
//        if (mapView != null) {
//            mapView.onResume();
//        }
//    }

//    @Override
//    public void onPause() {
//        if (mapView != null) {
//            mapView.onPause();
//        }
//        MapEngine.getInstance().onPause();
//        super.onPause();
//    }
}
