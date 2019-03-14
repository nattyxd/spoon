package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteConstraintException;
import android.icu.util.Calendar;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 *
 */
public class EditBookingFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RESTAURANT = "restaurant";
    private static final String ARG_BOOKING = "booking";

    private RestaurantItem restaurant;
    private BookingItem booking;

    private Calendar myCalendar;

    // Text views
    private TextView youAreEditing;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView numPeopleTextView;

    // Fragment Controls
    private EditText timeEditor;
    private EditText dateEditor;
    private EditText numAttendeesEditor;

    private DatabaseHelper dbHelper;

    private Thread editBookingThread;

    private View view;

    public EditBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            // TODO: Restaurant null check maybe necessary here if we support editing of booking through non here restaurant means
            restaurant = (RestaurantItem) bundle.getSerializable("restaurant");
            booking = (BookingItem) bundle.getSerializable("booking");

            if(restaurant == null || booking == null){
                // sometimes on app resumes this could be called with a blank bundle.. should probably investigate why but in the meantime catch it with this
                return;
            }

            youAreEditing = view.findViewById(R.id.subtitle);
            String name = restaurant.getName();
            String date = booking.getDateOfBooking();
            String time = booking.getTimeOfBooking();
            youAreEditing.setText(getString(R.string.en_editBooking_youAreEditing, name, date, time));

            dateEditor.setText(booking.getDateOfBooking());
            timeEditor.setText(booking.getTimeOfBooking());
            numAttendeesEditor.setText(Integer.toString(booking.getNumPeopleAttending()));

            int year = Integer.parseInt("20" + booking.getDateOfBooking().substring(6, 8));
            int monthOfYear = Integer.parseInt(booking.getDateOfBooking().substring(3, 5));
            int dayOfMonth = Integer.parseInt(booking.getDateOfBooking().substring(0, 2));

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurant = (RestaurantItem) getArguments().getSerializable(ARG_RESTAURANT);
            booking = (BookingItem) getArguments().getSerializable(ARG_BOOKING);
        }

        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_booking, container, false);

        myCalendar = Calendar.getInstance();

        dateTextView = view.findViewById(R.id.dateTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        numPeopleTextView = view.findViewById(R.id.numPeopleTextView);

        numAttendeesEditor = view.findViewById(R.id.bookRestaurantNumAttendeesEditor);

        dateEditor = view.findViewById(R.id.mealTitle);
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

        Button editBookingBtn = view.findViewById(R.id.editBookingBtn);

        editBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take a big breath. We're going to edit a booking..
                if(editBookingThread != null && editBookingThread.isAlive()){
                    // prevent spam clicks while the worker thread is still working.
                    return;
                }

                editBookingThread = new Thread(new Runnable() {
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

                        System.out.println("The number of attendees is equal to: ");
                        System.out.println(numAttendeesEditor.getText().toString());


                        // This should be technically unnecessary for editing restaurants, possibly remove this later
                        try {
                            long result = dbHelper.addRestaurant(restaurant);
                            Log.i("spoonlogcat: ", "Created new restaurant entry with ID " + result);
                        }
                        catch (SQLiteConstraintException ex) {
                            ex.printStackTrace();
                        }

//                        booking.setRestaurantID(restaurant.getHereID());
                        booking.setDateOfBooking(dateEditor.getText().toString());
                        booking.setTimeOfBooking(timeEditor.getText().toString());
                        booking.setNumPeopleAttending(Integer.parseInt(numAttendeesEditor.getText().toString()));
                        final int result = dbHelper.updateBooking(booking);
                        if(result != 1){
                            Log.e("spoonlogcat: ", "Something went horribly wrong and we updated an incorrect number of rows!");
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                youAreEditing.setText(getString(R.string.en_editBooking_youAreEditing, restaurant.getName(), booking.getDateOfBooking(), booking.getTimeOfBooking()));
                            }
                        });
                        BookingsFragment.mAdapter.notifyDataSetChanged(); // ensure adapter reflects changes

                        Snackbar snackbar = Snackbar
                                .make(view, "Booking Edited Successfully!", Snackbar.LENGTH_LONG)
                                .setAction("View Booking Now", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("booking", booking);
                                        MainActivity.navigation.getMenu().findItem(R.id.navigation_bookings).setChecked(true);
                                        FragmentStateContainer.getInstance().switchFragmentState(6, bundle);

                                    }
                                });

                        snackbar.show();

                        Log.i("spoonlogcat", "L is: " + result);
                    }
                });

                editBookingThread.start();
            }
        });


        return view;
    }
}
