package uk.ac.aston.baulchjn.mobiledev.spoon.home;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import uk.ac.aston.baulchjn.mobiledev.spoon.BookingDatabase;
import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;

import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookingContent {
    public static List<BookingItem> bookingItems = new ArrayList<>();
    private static BookingDatabase bookingDatabase;
    private static Context context;
    private static BookingRecyclerAdapter bookingRecyclerAdapter;
    private static DatabaseHelper dbHelper;

    private final static String DATABASE_NAME = "RESTAURANT_DB";

    public static void populateBookings(Context applicationContext, BookingRecyclerAdapter adapter) {
        context = applicationContext;
        bookingRecyclerAdapter = adapter;
        dbHelper = new DatabaseHelper(context);

        final Migration FROM_2_TO_1 = new Migration(2, 1) {
            @Override
            public void migrate(final SupportSQLiteDatabase database) {
                //
            }
        };
        final Migration MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                //
            }
        };

        bookingDatabase = Room.databaseBuilder(context,
                BookingDatabase.class, DATABASE_NAME)

//                .addMigrations(MIGRATION_1_2)
//                .addMigrations(FROM_2_TO_1)
                .fallbackToDestructiveMigration()
                .build();


        setBookingsToAllBookings();
    }

    private static void setBookingsToAllBookings() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    //bookingItems = bookingDatabase.daoAccess().fetchAllBookings();
                    //bookingItems = dbHelper.getAllBookingsAsList();
                    System.out.println("bookingItems has: " + bookingItems.size() + " items inside of it.");
                    // TODO: Synchronisation issue here, we notify before query complete
                    bookingRecyclerAdapter.bookingList = dbHelper.getAllBookingsAsList();
                    //bookingRecyclerAdapter.notifyDataSetChanged();
                } catch(SQLiteConstraintException e){
                    // the restaurant already exists
                }
            }
        }).start();
    }
}