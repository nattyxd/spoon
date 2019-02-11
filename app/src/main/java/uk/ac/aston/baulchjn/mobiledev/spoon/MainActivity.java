package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import uk.ac.aston.baulchjn.mobiledev.spoon.helper.BottomNavigationViewHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.deprecated_HomeFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        FragmentStateContainer.getInstance().setFragmentManager(getSupportFragmentManager());
        FragmentStateContainer.getInstance().initialise();
        FragmentStateContainer.setActivity(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentStateContainer.getInstance().switchFragmentState(0, null);
                    return true;

                case R.id.navigation_bookings:
                    FragmentStateContainer.getInstance().switchFragmentState(1, null);
                    return true;

                case R.id.navigation_restaurants:
                    FragmentStateContainer.getInstance().switchFragmentState(2, null);
                    return true;

                case R.id.navigation_meals:
                    FragmentStateContainer.getInstance().switchFragmentState(3, null);
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
