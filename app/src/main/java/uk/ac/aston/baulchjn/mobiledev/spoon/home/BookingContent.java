package uk.ac.aston.baulchjn.mobiledev.spoon.home;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import uk.ac.aston.baulchjn.mobiledev.spoon.BookingDatabase;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookingContent {
    public static List<BookingItem> bookingItems = new ArrayList<>();
    private static BookingDatabase bookingDatabase;
    private static Context context;

    public static void populateBookings(Context applicationContext) {
        context = applicationContext;

        bookingDatabase = Room.databaseBuilder(context,
                BookingDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();


        setBookingsToAllBookings();
    }

    private static void setBookingsToAllBookings() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                BookingItem booking = new BookingItem();
                booking.setRestaurantID(booking.getHereID());
                booking.setDateOfBooking(dateEditor.getText().toString());
                booking.setTimeOfBooking(timeEditor.getText().toString());
                booking.setNumPeopleAttending(Integer.parseInt(numAttendeesEditor.getText().toString()));
                bookingDatabase.daoAccess().insertSingleBookingItem(booking);

                try{
                    restaurantDatabase.daoAccess().insertSingleRestaurantItem(restaurant);
                } catch(SQLiteConstraintException e){
                    // the restaurant already exists
                }
            }
        }).start();
        BookingItem bookingItem = new BookingItem();
        bookingItems.add(bookingItem);
    }
}