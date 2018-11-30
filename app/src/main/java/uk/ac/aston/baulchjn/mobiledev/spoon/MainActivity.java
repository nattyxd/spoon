package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import uk.ac.aston.baulchjn.mobiledev.spoon.helper.BottomNavigationViewHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.HomeFragment;

public class MainActivity extends AppCompatActivity {


    final Fragment homeFragment = new HomeFragment();
    final Fragment bookingsFragment = new BookingsFragment();
    final Fragment restaurantsFragment = new RestaurantsFragment();
    final Fragment mealsFragment = new MealsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationViewHelper.disableShiftMode(navigation);

        fm.beginTransaction().add(R.id.main_container, mealsFragment, "4").hide(mealsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, restaurantsFragment, "3").hide(restaurantsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, bookingsFragment, "2").hide(bookingsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;

                case R.id.navigation_bookings:
                    fm.beginTransaction().hide(active).show(bookingsFragment).commit();
                    active = bookingsFragment;
                    return true;

                case R.id.navigation_restaurants:
                    fm.beginTransaction().hide(active).show(restaurantsFragment).commit();
                    active = restaurantsFragment;
                    return true;

                case R.id.navigation_meals:
                    fm.beginTransaction().hide(active).show(mealsFragment).commit();
                    active = mealsFragment;
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
