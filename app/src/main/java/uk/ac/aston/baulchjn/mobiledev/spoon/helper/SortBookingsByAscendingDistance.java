package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import android.content.Context;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;

import java.util.Comparator;

import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

public class SortBookingsByAscendingDistance implements Comparator<BookingItem>
{
    private DatabaseHelper dbHelper;
    private Context context;

    public SortBookingsByAscendingDistance(Context context, DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
        this.context = context;
}
    @Override
    public int compare(BookingItem bookingItem, BookingItem t1) {
        try{
            RestaurantItem restaurant1 = dbHelper.getRestaurantByHereID(bookingItem.getRestaurantID());
            RestaurantItem restaurant2 = dbHelper.getRestaurantByHereID(t1.getRestaurantID());

            GeoCoordinate firstRestaurant = new GeoCoordinate(Double.parseDouble(restaurant1.getLatitude()), Double.parseDouble(restaurant1.getLongitude()));
            GeoCoordinate secondRestaurant = new GeoCoordinate(Double.parseDouble(restaurant2.getLatitude()), Double.parseDouble(restaurant2.getLongitude()));

            GeoCoordinate userLocation = new GeoCoordinate(RestaurantsFragment.bestUserLocation.getLatitude(), RestaurantsFragment.bestUserLocation.getLongitude());

            double firstDistance = firstRestaurant.distanceTo(userLocation);
            double secondDistance = secondRestaurant.distanceTo(userLocation);

            return (int)(firstDistance - secondDistance);
        } catch (Exception e){
            Toast.makeText(context, "Sorry, you need to view the Restaurants tab before using this search.. This bug will be fixed in an upcoming version.", Toast.LENGTH_SHORT);
            e.printStackTrace();
            return -1;
        }
    }
}