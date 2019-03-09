package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import java.util.Comparator;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

public class SortRestaurantByAscendingAlphabet implements Comparator<RestaurantItem>
{
    @Override
    public int compare(RestaurantItem restaurantItem, RestaurantItem t1) {
        return restaurantItem.getName().compareToIgnoreCase(t1.getName());
    }
}