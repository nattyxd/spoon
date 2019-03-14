package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import android.content.Context;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;

import java.util.Comparator;

import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

public class SortBookingsByNumAttendees implements Comparator<BookingItem>
{
    @Override
    public int compare(BookingItem bookingItem, BookingItem t1) {
        return (int)(bookingItem.getNumPeopleAttending() - t1.getNumPeopleAttending());
    }
}