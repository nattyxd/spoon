package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortRestaurantByAscendingDistance;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortRestaurantByDescendingDistance;
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
    public static RestaurantRecyclerAdapter adapter;
    private ViewPager viewPager;
    private ActionBar mActionBar;

    private boolean[] selectedCategoriesIndex;
    public static HashMap<String, Boolean> categoriesToExclude; // stores which categories the user does NOT want to see (all on by default)

    public static Location bestUserLocation;

    public static boolean showVisitedRestaurants = true;
    public static boolean showNonVisitedRestaurants = true;

    private RestaurantsRecyclerViewFragment restaurantsRecyclerViewFragment;
    private RestaurantsMapViewFragment restaurantMapViewFragment;

    private ConstraintLayout restaurantsConstraintLayout;
    private TabLayout tabs;

    private EditText searchQuery;
    private Button searchButton;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    private Callable<Void> onJSONTaskCompleted = new Callable<Void>() {
        @Override
        public Void call() {
            Log.i("spoonlogcat:", "Woo the oncomplete fired");
            if(RestaurantContent.restaurantItems.size() > 0){
                RestaurantsRecyclerViewFragment.noRestaurantsText.setVisibility(View.GONE);
            } else {
                RestaurantsRecyclerViewFragment.noRestaurantsText.setVisibility(View.VISIBLE);
            }
            mActionBar.setTitle((RestaurantContent.restaurantItems.size() + " matches"));
            restaurantMapViewFragment.restaurantsWereRefreshed(); // TODO: This can throw errors if the map isn't initialised yet
            return null;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rv_list = new ArrayList<>();
        categoriesToExclude = new HashMap<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        viewPager = view.findViewById(R.id.restaurant_ViewPager);
        setupViewPager(viewPager);

        setupLocationListeners();

        tabs = (TabLayout) view.findViewById(R.id.restaurant_TabLayout);
        tabs.setupWithViewPager(viewPager);

        int categoriesLength = getResources().getStringArray(R.array.en_restaurant_category_options).length;
        selectedCategoriesIndex = new boolean[categoriesLength];
        for(int i = 0; i < categoriesLength; i++){
            selectedCategoriesIndex[i] = true;
        }

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.restaurantsToolBar);
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        mActionBar = ((MainActivity)getActivity()).getSupportActionBar();

        mActionBar.setTitle("Loading...");
        setHasOptionsMenu(true);

        restaurantsConstraintLayout = view.findViewById(R.id.restaurantsConstraintLayout);

        searchQuery = view.findViewById(R.id.searchQuery);
        searchButton = view.findViewById(R.id.searchButton);
        searchQuery.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.restaurantsToolBar);
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    private void setupViewPager(final ViewPager viewPager){
        restaurantsRecyclerViewFragment = new RestaurantsRecyclerViewFragment();
        restaurantMapViewFragment = new RestaurantsMapViewFragment();

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(restaurantsRecyclerViewFragment, "List View");
        adapter.addFragment(restaurantMapViewFragment, "Map View");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.restaurants_menu, menu);
        Drawable refreshDrawable = menu.findItem(R.id.action_refresh).getIcon();
        Drawable searchDrawable = menu.findItem(R.id.action_serch).getIcon();
        Drawable resetDrawable = menu.findItem(R.id.action_reset_filter).getIcon();

        refreshDrawable = DrawableCompat.wrap(refreshDrawable);
        DrawableCompat.setTint(refreshDrawable, ContextCompat.getColor(getContext(), R.color.white));

        searchDrawable = DrawableCompat.wrap(searchDrawable);
        DrawableCompat.setTint(searchDrawable, ContextCompat.getColor(getContext(), R.color.white));

        resetDrawable = DrawableCompat.wrap(resetDrawable);
        DrawableCompat.setTint(resetDrawable, ContextCompat.getColor(getContext(), R.color.red));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog.Builder dialogBuilder;

//        setupButtons();

        switch(item.getItemId()){
            case R.id.action_refresh:
                mActionBar.setTitle("Loading...");
                getHereAPIRestaurants(restaurantMapViewFragment.getCenterOfMap(), onJSONTaskCompleted);
                break;
            case R.id.action_serch:
                if(searchQuery.getVisibility() == View.VISIBLE){
                    // HIDE
                    searchQuery.setVisibility(View.GONE);
                    searchButton.setVisibility(View.GONE);

                    searchQuery.getText()

                    ConstraintSet constraints = new ConstraintSet();
                    constraints.clone(restaurantsConstraintLayout);
                    constraints.connect(R.id.restaurant_TabLayout, ConstraintSet.TOP, R.id.restaurantsToolBar, ConstraintSet.BOTTOM);
                } else {
                    // SHOW
                    searchQuery.setVisibility(View.VISIBLE);
                    searchButton.setVisibility(View.VISIBLE);

                    ConstraintSet constraints = new ConstraintSet();
                    constraints.clone(restaurantsConstraintLayout);
                    constraints.connect(R.id.restaurant_TabLayout, ConstraintSet.TOP, R.id.searchQuery, ConstraintSet.BOTTOM);
                }
                break;
            case R.id.action_filter:
                final String[] categoriesArray = getResources().getStringArray(R.array.en_restaurant_category_options);

                dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Filter Restaurants");
                dialogBuilder.setMultiChoiceItems(categoriesArray, selectedCategoriesIndex, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedCategoriesIndex[which] = isChecked; // persist check status across constructions

                        // Exceptions for indexes 0 and 1, which are whether or not to include Visited/Non visited respectively
                        if(which == 0){
                            showVisitedRestaurants = isChecked;
                            return;
                        }
                        if(which == 1){
                            showNonVisitedRestaurants = isChecked;
                            return;
                        }

                        // Continue with normal cases
                        if(isChecked == false){
                            // exclude it in results
                            categoriesToExclude.put(categoriesArray[which], true);
                        } else {
                            // include it in results
                            categoriesToExclude.remove(categoriesArray[which]);
                        }
                    }
                });

                dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Toast.makeText(getContext(), "Filtering Results", Toast.LENGTH_SHORT).show();
                        RestaurantContent.filterOutRestaurants(getContext(), adapter, onJSONTaskCompleted);
                    }
                });

                dialogBuilder.create().show();
                break;
            case R.id.action_sort:
                dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Sort");
                dialogBuilder.setItems(R.array.en_restaurants_SortOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                // alphabetical ascending
                                Collections.sort(RestaurantContent.restaurantItems, new SortRestaurantByAscendingAlphabet());
                                break;
                            case 1:
                                // alphabetical descending
                                Collections.sort(RestaurantContent.restaurantItems, Collections.reverseOrder(new SortRestaurantByAscendingAlphabet()));
                                break;
                            case 2:
                                // distance ascending
                                Collections.sort(RestaurantContent.restaurantItems, new SortRestaurantByAscendingDistance());
                                break;
                            case 3:
                                // distance descending
                                Collections.sort(RestaurantContent.restaurantItems, new SortRestaurantByDescendingDistance());
                                break;
                            default:
                                Collections.sort(RestaurantContent.restaurantItems, new SortRestaurantByAscendingAlphabet());
                                break;
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                dialogBuilder.create().show();
                break;
            case R.id.action_reset_filter:
                showNonVisitedRestaurants = true;
                showVisitedRestaurants = true;
                categoriesToExclude.clear();
                int categoriesLength = getResources().getStringArray(R.array.en_restaurant_category_options).length;
                for(int i = 0; i < categoriesLength; i++){
                    selectedCategoriesIndex[i] = true;
                }
                RestaurantContent.filterOutRestaurants(getContext(), adapter, onJSONTaskCompleted);
                break;
        }

        return true;
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

        Location locationGPS = null;
        Location coarseGPS = null;
        Location passiveGPS = null;

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

            locationGPS = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            coarseGPS = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            passiveGPS = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        } catch (Exception e){
            e.printStackTrace();
            // this exception will fire if the user hasn't given permission for GPS.
        }


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
}