package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;

public class SortBookingsByAscendingDate implements Comparator<BookingItem>
{
    @Override
    public int compare(BookingItem bookingItem, BookingItem t1) {
        String unparsedRestaurant1 = bookingItem.getDateOfBooking();
        String unParsedRestaurant2 = t1.getDateOfBooking();
        Long parsedDate1 = 0L;
        Long parsedDate2 = 0L;
        try {
            parsedDate1 = new SimpleDateFormat("dd/MM/yyyy").parse(unparsedRestaurant1).getTime();
            parsedDate2 = new SimpleDateFormat("dd/MM/yyyy").parse(unParsedRestaurant2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedDate1.intValue() - parsedDate2.intValue();
    }
}