package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import com.here.android.mpa.common.GeoCoordinate;

import java.util.Comparator;

import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

public class SortRestaurantByAscendingDistance implements Comparator<RestaurantItem>
{
    @Override
    public int compare(RestaurantItem restaurantItem, RestaurantItem t1) {
        GeoCoordinate firstRestaurant = new GeoCoordinate(Double.parseDouble(restaurantItem.getLatitude()), Double.parseDouble(restaurantItem.getLongitude()));
        GeoCoordinate secondRestaurant = new GeoCoordinate(Double.parseDouble(t1.getLatitude()), Double.parseDouble(t1.getLongitude()));

        GeoCoordinate userLocation = new GeoCoordinate(RestaurantsFragment.bestUserLocation.getLatitude(), RestaurantsFragment.bestUserLocation.getLongitude());

        double firstDistance = firstRestaurant.distanceTo(userLocation);
        double secondDistance = secondRestaurant.distanceTo(userLocation);
        return (int)(firstDistance - secondDistance);
    }
}