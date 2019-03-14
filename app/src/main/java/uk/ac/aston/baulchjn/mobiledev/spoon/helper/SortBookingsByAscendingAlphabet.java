package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import java.util.Comparator;

import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;

public class SortBookingsByAscendingAlphabet implements Comparator<BookingItem>
{
    private DatabaseHelper dbHelper;

    public SortBookingsByAscendingAlphabet(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
}
    @Override
    public int compare(BookingItem bookingItem, BookingItem t1) {
        String name1 = dbHelper.getRestaurantByHereID(bookingItem.getRestaurantID()).getName();
        String name2 = dbHelper.getRestaurantByHereID(t1.getRestaurantID()).getName();

        return name1.compareToIgnoreCase(name2);
    }
}