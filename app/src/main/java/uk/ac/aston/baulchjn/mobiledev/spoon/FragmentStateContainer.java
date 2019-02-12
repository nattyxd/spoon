package uk.ac.aston.baulchjn.mobiledev.spoon;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.deprecated_HomeFragment;

public class FragmentStateContainer {
    private static FragmentStateContainer instance;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    final Fragment homeFragment = new deprecated_HomeFragment();
    final Fragment bookingsFragment = new BookingsFragment();
    final Fragment restaurantsFragment = new RestaurantsFragment();
    final Fragment mealsFragment = new MealsFragment();
    final Fragment restaurantDetailedFragment = new RestaurantDetailedFragment();
    final Fragment bookRestaurantFragment = new BookRestaurantFragment();
    FragmentManager fm = null;
    private static Activity activity = null;
    Fragment active = homeFragment;
    public Bundle activeBundle;

    private FragmentStateContainer() {
        //
    }

    public void initialise() {
        fm.beginTransaction().add(R.id.main_container, bookRestaurantFragment, "6").hide(bookRestaurantFragment).commit();
        fm.beginTransaction().add(R.id.main_container, restaurantDetailedFragment, "5").hide(restaurantDetailedFragment).commit();
        fm.beginTransaction().add(R.id.main_container, mealsFragment, "4").hide(mealsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, restaurantsFragment, "3").hide(restaurantsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, bookingsFragment, "2").hide(bookingsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();

        fragments.add(homeFragment);
        fragments.add(bookingsFragment);
        fragments.add(restaurantsFragment);
        fragments.add(mealsFragment);
        fragments.add(restaurantDetailedFragment);
        fragments.add(bookRestaurantFragment);
    }

    public void switchFragmentState(int index, Bundle bundle) {
        fm.beginTransaction().hide(active).show(fragments.get(index)).commit();
        activeBundle = bundle;
        active = fragments.get(index);
        active.onResume();

        Log.i("spoonlogcat", "testing");

    }

    public void launchRestaurantDetailed(RestaurantItem restaurant){
//        FragmentStateContainer.getInstance().switchFragmentState(4, bundle);
    }

    public void bookRestaurant(RestaurantItem restaurant){
        switchFragmentState(5, null);
    }

    public void setFragmentManager(FragmentManager manager) {
        fm = manager;
    }

    public static void setActivity(Activity activity){
        FragmentStateContainer.activity = activity;
    }

    public static Activity getActivity(){
        return FragmentStateContainer.activity;
    }

    public static FragmentStateContainer getInstance() {
        if (instance == null) {
            instance = new FragmentStateContainer();
        }

        return instance;
    }
}
