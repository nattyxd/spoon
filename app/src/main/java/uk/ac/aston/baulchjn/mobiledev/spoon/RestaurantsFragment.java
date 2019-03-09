package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortRestaurantByDescendingAlphabet;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantClickListener;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantRecyclerAdapter;

import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortRestaurantByAscendingAlphabet;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RestaurantsFragment extends Fragment {
    private List<RestaurantItem> rv_list;
    private View view;
    private RecyclerView recyclerView;
    private RestaurantRecyclerAdapter adapter;
    private ViewPager viewPager;

    private TextView formattedHeader;
    private TextView additionalInfoHeadline;

    private ImageButton centreMapOnUserButton;
    public static Location bestUserLocation;

    private RestaurantsRecyclerViewFragment restaurantsRecyclerViewFragment;
    private RestaurantsMapViewFragment restaurantMapViewFragment;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    private Callable<Void> onJSONTaskCompleted = new Callable<Void>() {
        @Override
        public Void call() {
            Log.i("spoonlogcat:", "Woo the oncomplete fired");

            formattedHeader.setText(getResources().getString(R.string.en_restaurantFragment_wefoundXRestaunts, String.valueOf(RestaurantContent.restaurantItems.size())));
            restaurantMapViewFragment.restaurantsWereRefreshed();
            return null;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rv_list = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        viewPager = view.findViewById(R.id.restaurant_ViewPager);
        setupViewPager(viewPager);

        setupLocationListeners();

        TabLayout tabs = (TabLayout) view.findViewById(R.id.restaurant_TabLayout);
        tabs.setupWithViewPager(viewPager);

        formattedHeader = view.findViewById(R.id.formattedRestaurantHeader);
        additionalInfoHeadline = view.findViewById(R.id.en_RestaurantFragment_AdditionalInfo);

        setupButtons();

        return view;
    }

    private void setupViewPager(final ViewPager viewPager){
        restaurantsRecyclerViewFragment = new RestaurantsRecyclerViewFragment();
        restaurantMapViewFragment = new RestaurantsMapViewFragment();

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(restaurantsRecyclerViewFragment, "List View");
        adapter.addFragment(restaurantMapViewFragment, "Map View");

        viewPager.setAdapter(adapter);
    }

    public void recyclerFragmentWasInflated(){
//        recyclerView = view.findViewById(R.id.restaurants_rv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i("spoonlogcat: ", "recyclerFragmentWasInflated invoked");
        recyclerView = restaurantsRecyclerViewFragment.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RestaurantRecyclerAdapter(RestaurantContent.restaurantItems, new RestaurantClickListener() {
            @Override
            public void onItemClick(RestaurantItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getHereID());
                bundle.putString("name", item.getName());
                bundle.putString("vicinity", item.getVicinity());
                bundle.putString("tag1", item.getTag1());
                bundle.putString("tag2", item.getTag2());
                bundle.putString("tag3", item.getTag3());
                bundle.putSerializable("restaurant", item);


                // launch the RestaurantDetailedFragment with the correct restaurant
                FragmentStateContainer.getInstance().switchFragmentState(4, bundle);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getHereAPIRestaurants(bestUserLocation, onJSONTaskCompleted);

    }

    private void setupLocationListeners(){
        bestUserLocation = null;

        if(ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getContext(), "We need your location, or the map will not work! :(", Toast.LENGTH_SHORT).show();
            bestUserLocation = new Location("");
            bestUserLocation.setLatitude(52.486208);
            bestUserLocation.setLongitude(-1.888499);
        }

        LocationManager manager = ((MainActivity)getActivity()).getLocationManager();

        try{
            manager.requestLocationUpdates(manager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    bestUserLocation = location;
                    restaurantMapViewFragment.userLocationChanged();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } catch (Exception e){
            e.printStackTrace();
            // this exception will fire if the user hasn't given permission for GPS.
        }

        Location locationGPS = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location coarseGPS = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location passiveGPS = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if(locationGPS != null){
            bestUserLocation = locationGPS;
        } else if(coarseGPS != null){
            bestUserLocation = coarseGPS;
        } else if(passiveGPS != null){
            bestUserLocation = passiveGPS;
        } else {
            // location still unavailable?, default to Aston University
            Log.w("spoonlogcat: ", "Location missing!");
            bestUserLocation = new Location("");
            bestUserLocation.setLatitude(52.486208);
            bestUserLocation.setLongitude(-1.888499);
        }
    }

    private void getHereAPIRestaurants(Location location, Callable onTaskComplete){
        RestaurantContent.jsonRequest(getActivity(), getActivity().getApplicationContext(), adapter, location, onTaskComplete);
    }

    private void setupButtons(){
        ImageButton refreshButton = view.findViewById(R.id.refreshRestaurantsButton);
        ImageButton searchButton = view.findViewById(R.id.stringSearchRestaurantsButton);
        ImageButton filterButton = view.findViewById(R.id.filterRestaurantsButton);
        ImageButton sortButton = view.findViewById(R.id.sortRestaurantsButton);
        ImageButton resetButton = view.findViewById(R.id.clearRestaurantCriteriaButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHereAPIRestaurants(restaurantMapViewFragment.getCenterOfMap(), onJSONTaskCompleted);
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Sort");
                dialogBuilder.setItems(R.array.en_restaurants_SortOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                // alphabetical ascending
                                Collections.sort(RestaurantContent.restaurantItems, new SortRestaurantByAscendingAlphabet());
                                break;
                            case 1:
                                Collections.sort(RestaurantContent.restaurantItems, new SortRestaurantByDescendingAlphabet());
                            default:
                                break;
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                dialogBuilder.create().show();

            }
        });
    }
}