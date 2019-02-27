package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.BottomNavigationViewHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.deprecated_HomeFragment;

import static uk.ac.aston.baulchjn.mobiledev.spoon.FragmentStateContainer.getActivity;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int PERMISSION_REQUEST_CODE = 12345;

    private static final String[] PERMISSION_REQUEST_STRING = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

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

        requestRequiredPermissions();

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

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    private void requestRequiredPermissions(){


        if (EasyPermissions.hasPermissions(this, PERMISSION_REQUEST_STRING)) {
            Toast.makeText(this, "All permissions we need are granted.", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(MainActivity.this, "We need your location and internet access to enable the app to function.",
                    PERMISSION_REQUEST_CODE, PERMISSION_REQUEST_STRING);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            Toast.makeText(this, "Please enable permissions or the app will not work and crash.", Toast.LENGTH_SHORT).show();
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            EasyPermissions.requestPermissions(MainActivity.this, "We need your location and internet access to enable the app to function.",
                    PERMISSION_REQUEST_CODE, PERMISSION_REQUEST_STRING);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){

        }
    }
}
