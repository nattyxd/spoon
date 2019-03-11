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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 *
 */
public class BookRestaurantFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RESTAURANT = "restaurant";

    private String name;
    private String vicinity;
    private ArrayList<String> tags;
    private RestaurantItem restaurant;

    // Text views
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView numPeopleTextView;

    // Fragment Controls
    private EditText timeEditor;
    private EditText dateEditor;
    private EditText numAttendeesEditor;

    private final String DATABASE_NAME = "RESTAURANT_DB";
    //    private final String DATABASE_NAME = getContext().getResources().getString(R.string.restaurant_db_name);
    private BookingDatabase bookingDatabase;
    private RestaurantDatabase restaurantDatabase;
    private DatabaseHelper dbHelper;

    private Thread makeBookingThread;

    private View view;

    public BookRestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            restaurant = (RestaurantItem) bundle.getSerializable("restaurant");
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_restaurant, container, false);

        final Calendar myCalendar = Calendar.getInstance();

        dateTextView = view.findViewById(R.id.dateTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        numPeopleTextView = view.findViewById(R.id.numPeopleTextView);

        numAttendeesEditor = view.findViewById(R.id.bookRestaurantNumAttendeesEditor);

        dateEditor = view.findViewById(R.id.bookRestaurantDateEditText);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                RestaurantItem restaurantItem = restaurantDatabase.daoAccess().fetchOneRestaurantbyName("Restaurant Name");
//                System.out.println("the restaurant you asked for is..." + restaurantItem);
//            }
//        }).start();
        Button bookBtn = view.findViewById(R.id.createBookingBtn);



        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take a big breath. We're going to make a booking..
                if(makeBookingThread != null && makeBookingThread.isAlive()){
                    // prevent spam clicks
                    return;
                }

                makeBookingThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        // Hide the keyboard so the snackbar gets shown
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        // Form validation
                        if (dateEditor.getText().toString() == null || dateEditor.getText().toString().isEmpty()) {
                            dateTextView.setTextColor(getResources().getColor(R.color.red));
                            return;
                        } else {
                            dateTextView.setTextColor(getResources().getColor(R.color.textViewDefault));
                        }

                        if (timeEditor.getText().toString() == null || timeEditor.getText().toString().isEmpty()) {
                            timeTextView.setTextColor(getResources().getColor(R.color.red));
                            return;
                        } else {
                            timeTextView.setTextColor((getResources().getColor(R.color.textViewDefault)));
                        }

                        if (numAttendeesEditor.getText().toString() == null || numAttendeesEditor.getText().toString().isEmpty()) {
                            numPeopleTextView.setTextColor(getResources().getColor(R.color.red));
                            return;
                        } else {
                            numPeopleTextView.setTextColor((getResources().getColor(R.color.textViewDefault)));
                        }

                        System.out.println(numAttendeesEditor.getText().toString() == null || numAttendeesEditor.getText().toString().isEmpty());
                        System.out.println("The number of attendees is equal to: ");
                        System.out.println(numAttendeesEditor.getText().toString());

                        final BookingItem booking = new BookingItem();
                        booking.setRestaurantID(restaurant.getHereID());
                        booking.setDateOfBooking(dateEditor.getText().toString());
                        booking.setTimeOfBooking(timeEditor.getText().toString());
                        booking.setNumPeopleAttending(Integer.parseInt(numAttendeesEditor.getText().toString()));
                        final long result = dbHelper.addBooking(booking);

                        Snackbar snackbar = Snackbar
                                .make(view, "Booking Created Successfully!", Snackbar.LENGTH_LONG)
                                .setAction("View Booking Now", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("bookingID", result);
                                        FragmentStateContainer.getInstance().switchFragmentState(1, bundle);

                                    }
                                });

                        snackbar.show();

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
                });

                makeBookingThread.start();
            }
        });


        return view;
    }
}
