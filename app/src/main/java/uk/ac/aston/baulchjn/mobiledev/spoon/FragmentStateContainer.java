package uk.ac.aston.baulchjn.mobiledev.spoon;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.HomeFragment;

public class FragmentStateContainer {
    private static FragmentStateContainer instance;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    final Fragment homeFragment = new HomeFragment();
    final Fragment bookingsFragment = new BookingsFragment();
    final Fragment restaurantsFragment = new RestaurantsFragment();
    final Fragment mealsFragment = new MealsFragment();
    final Fragment restaurantDetailedFragment = new RestaurantDetailedFragment();
    final Fragment bookRestaurantFragment = new BookRestaurantFragment();
    final Fragment bookingDetailsFragment = new BookingDetailsFragment();
    final Fragment editBookingFragment = new EditBookingFragment();
    final Fragment newMealFragment = new NewMealFragment();
    final Fragment editMealFragment = new EditMealFragment();
    final Fragment mealDetailedFragment = new MealDetailedFragment();

    FragmentManager fm = null;
    private static Activity activity = null;
    Fragment active = homeFragment;
    public Bundle activeBundle;

    private FragmentStateContainer() {
        //
    }

    public void initialise() {
        fm.beginTransaction().add(R.id.main_container, mealDetailedFragment, "11").hide(mealDetailedFragment).commit();
        fm.beginTransaction().add(R.id.main_container, editMealFragment, "10").hide(editMealFragment).commit();
        fm.beginTransaction().add(R.id.main_container, newMealFragment, "9").hide(newMealFragment).commit();
        fm.beginTransaction().add(R.id.main_container, editBookingFragment, "8").hide(editBookingFragment).commit();
        fm.beginTransaction().add(R.id.main_container, bookingDetailsFragment, "7").hide(bookingDetailsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, bookRestaurantFragment, "6").hide(bookRestaurantFragment).commit();
        fm.beginTransaction().add(R.id.main_container, restaurantDetailedFragment, "5").hide(restaurantDetailedFragment).commit();
        fm.beginTransaction().add(R.id.main_container, mealsFragment, "4").hide(mealsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, restaurantsFragment, "3").hide(restaurantsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, bookingsFragment, "2").hide(bookingsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();

        fragments.add(homeFragment); // 0
        fragments.add(bookingsFragment); // 1
        fragments.add(restaurantsFragment); // 2
        fragments.add(mealsFragment); // 3
        fragments.add(restaurantDetailedFragment); // 4
        fragments.add(bookRestaurantFragment); // 5
        fragments.add(bookingDetailsFragment); // 6
        fragments.add(editBookingFragment); // 7
        fragments.add(newMealFragment); // 8
        fragments.add(editMealFragment); // 9
        fragments.add(mealDetailedFragment); // 10
    }

    public void switchFragmentState(int index, Bundle bundle) {
        fm.beginTransaction().hide(active).show(fragments.get(index)).commit();
        activeBundle = bundle;
        active = fragments.get(index);
        active.onResume();

        switch(index){
            case 0:
                setBarToSort();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;

        }

        Log.i("spoonlogcat", "testing");

    }

    public Fragment getActive(){
        return active;
    }

    public int getActiveIndex(){
        return fragments.indexOf(active);
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

    private void setBarToSort(){
        ActionBar bar = getActivity().getActionBar();
        System.out.print("lol");
    }
}
