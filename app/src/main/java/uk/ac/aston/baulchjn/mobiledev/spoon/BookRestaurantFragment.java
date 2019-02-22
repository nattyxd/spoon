package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;


/**
 *
 */
public class BookRestaurantFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RESTAURANT = "restaurant";

    // TODO: Rename and change types of parameters
    private String name;
    private String vicinity;
    private ArrayList<String> tags;
    private RestaurantItem restaurant;

    // Fragment Controls
    private EditText timeEditor;
    private EditText dateEditor;
    private EditText numAttendeesEditor;

    // TODO BROKEN
    private final String DATABASE_NAME = "RESTAURANT_DB";
    //    private final String DATABASE_NAME = getContext().getResources().getString(R.string.restaurant_db_name);
    private BookingDatabase bookingDatabase;
    private RestaurantDatabase restaurantDatabase;
    private DatabaseHelper dbHelper;

    private View view;

    public BookRestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            restaurant = (RestaurantItem) bundle.getSerializable(ARG_RESTAURANT);
            Log.i("spoonlogcat", "Wooooo! We're gonna book a restaurant...." + restaurant.toString());

            TextView youAreBooking = view.findViewById(R.id.youAreBooking);
            youAreBooking.setText(getString(R.string.en_bookrestaurant_youarebooking, restaurant.getName()));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurant = (RestaurantItem) getArguments().getSerializable(ARG_RESTAURANT);

        }

        dbHelper = new DatabaseHelper(getContext());

        // DB Migrations
        final Migration FROM_1_TO_2 = new Migration(1, 2) {
            @Override
            public void migrate(final SupportSQLiteDatabase database) {
                //
            }
        };

        final Migration MIGRATION_2_1 = new Migration(2, 1) {
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


        restaurantDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                RestaurantDatabase.class, DATABASE_NAME)
//                .addMigrations(MIGRATION_1_2)
//                .addMigrations(MIGRATION_2_1)
                .fallbackToDestructiveMigration()
                .build();

        bookingDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                BookingDatabase.class, DATABASE_NAME)
//                .addMigrations(MIGRATION_1_2)
//                .addMigrations(MIGRATION_2_1)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_restaurant, container, false);

        final Calendar myCalendar = Calendar.getInstance();

        numAttendeesEditor = view.findViewById(R.id.bookRestaurantNumAttendeesEditor);

        dateEditor = view.findViewById(R.id.bookRestaurantDateEditText);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                dateEditor.setText(sdf.format(myCalendar.getTime()));
            }

        };

        dateEditor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeEditor = view.findViewById(R.id.bookRestaurantTimeEditText);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String formattedMinute;
                        if (Integer.toString(minute).length() == 1) {
                            formattedMinute = "0" + minute;
                        } else {
                            formattedMinute = Integer.toString(minute);
                        }

                        timeEditor.setText(hourOfDay + ":" + formattedMinute);
                    }
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);

        timeEditor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });


//        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                RestaurantItem restaurantItem = new RestaurantItem();
////                restaurantItem.setDesc("Description");
////                restaurantItem.setName("Restaurant Nameeeeee");
////                restaurantDatabase.daoAccess().insertSingleRestaurantItem(restaurantItem);
////            }
////        }) .start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestaurantItem restaurantItem = restaurantDatabase.daoAccess().fetchOneRestaurantbyName("Restaurant Name");
                System.out.println("the restaurant you asked for is..." + restaurantItem);
            }
        }).start();
        Button bookBtn = view.findViewById(R.id.createBookingBtn);



        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take a big breath. We're going to make a booking..
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        // Form validation
                        if (dateEditor.getText().toString() == null || dateEditor.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please ensure you select a date.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (timeEditor.getText().toString() == null || timeEditor.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please ensure you select a time.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (numAttendeesEditor.getText().toString() == null || numAttendeesEditor.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please ensure you select a number of attendees.", Toast.LENGTH_LONG).show();
                            System.out.println("Exit thread gracefully");

                            return;
                        }

                        System.out.println(numAttendeesEditor.getText().toString() == null || numAttendeesEditor.getText().toString().isEmpty());
                        System.out.println("The number of attendees is equal to: ");
                        System.out.println(numAttendeesEditor.getText().toString());

                        BookingItem booking = new BookingItem();
                        booking.setRestaurantID(restaurant.getHereID());
                        booking.setDateOfBooking(dateEditor.getText().toString());
                        booking.setTimeOfBooking(timeEditor.getText().toString());
                        booking.setNumPeopleAttending(Integer.parseInt(numAttendeesEditor.getText().toString()));
                        long result = dbHelper.addBooking(booking);
                        Log.i("spoonlogcat", "L is: " + result);

                        try {
                            dbHelper.addRestaurant(restaurant);
                        }
                        catch (SQLiteConstraintException ex) {
                            ex.printStackTrace();
                        }

                        /*long l = bookingDatabase.daoAccess().insertSingleBookingItem(booking);
                        Log.i("spoonlogcat", "L is: " + l);

                        try{
                            restaurantDatabase.daoAccess().insertSingleRestaurantItem(restaurant);
                        } catch(SQLiteConstraintException e){
                            // the restaurant already exists
                        }*/
                    }
                }).start();
            }
        });


        return view;
    }
}
