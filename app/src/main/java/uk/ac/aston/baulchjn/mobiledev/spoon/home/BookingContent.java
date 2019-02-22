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
    private static BookingRecyclerAdapter bookingRecyclerAdapter;

    private final static String DATABASE_NAME = "RESTAURANT_DB";

    public static void populateBookings(Context applicationContext, BookingRecyclerAdapter adapter) {
        context = applicationContext;
        bookingRecyclerAdapter = adapter;

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

                try{
                    bookingItems = bookingDatabase.daoAccess().fetchAllBookings();
                    // TODO: Synchronisation issue here, we notify before query complete
                    bookingRecyclerAdapter.notifyDataSetChanged();
                } catch(SQLiteConstraintException e){
                    // the restaurant already exists
                }
            }
        }).start();
    }
}