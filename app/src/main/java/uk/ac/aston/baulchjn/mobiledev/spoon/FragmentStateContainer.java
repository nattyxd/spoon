package uk.ac.aston.baulchjn.mobiledev.spoon;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.deprecated_HomeFragment;

public class FragmentStateContainer {
    private static FragmentStateContainer instance;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    final Fragment homeFragment = new deprecated_HomeFragment();
    final Fragment bookingsFragment = new BookingsFragment();
    final Fragment restaurantsFragment = new RestaurantsFragment();
    final Fragment mealsFragment = new MealsFragment();
    FragmentManager fm = null;
    Fragment active = homeFragment;

    private FragmentStateContainer() {
        //
    }

    public void initialise() {
        fm.beginTransaction().add(R.id.main_container, mealsFragment, "4").hide(mealsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, restaurantsFragment, "3").hide(restaurantsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, bookingsFragment, "2").hide(bookingsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();

        fragments.add(homeFragment);
        fragments.add(bookingsFragment);
        fragments.add(restaurantsFragment);
        fragments.add(mealsFragment);
    }

    public void switchFragmentState(int index) {
        fm.beginTransaction().hide(active).show(fragments.get(index)).commit();
        active = fragments.get(index);
    }

    public void setFragmentManager(FragmentManager manager) {
        fm = manager;
    }

    public static FragmentStateContainer getInstance() {
        if (instance == null) {
            instance = new FragmentStateContainer();
        }

        return instance;
    }
}
