package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

public class BookingDetailsFragment extends Fragment {

    private View view;
    private DatabaseHelper dbHelper;

    private BookingItem booking;
    private RestaurantItem restaurant;

    private TextView restaurantName;
    private TextView restaurantVicinity;
    private TextView bookingDate;
    private TextView bookingTime;
    private TextView bookingAttendees;
    
    private Button editBtn;
    private Button shareBtn;
    private Button deleteBtn;

    public BookingDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            booking = (BookingItem) bundle.getSerializable("booking");

            if(booking == null){
                // null check is important if the app loses focus
                return;
            }

            restaurant = dbHelper.getRestaurantByHereID(booking.getRestaurantID());

            List<RestaurantItem> restaurants = new ArrayList<>();
            restaurants = dbHelper.getAllRestaurantsAsList();

            restaurantName.setText(restaurant.getName());
            String formattedViscinityString = restaurant.getVicinity().replace("<br/>", ", ");
            restaurantVicinity.setText(formattedViscinityString);

            bookingDate.setText(booking.getDateOfBooking() + "");
            bookingTime.setText(booking.getTimeOfBooking() + "");

            if(booking.getNumPeopleAttending() > 1){
                bookingAttendees.setText(getResources().getString(R.string.en_bookingDetails_numAttendees_multipleAttendees, booking.getNumPeopleAttending()));
            } else if (booking.getNumPeopleAttending() == 1){
                bookingAttendees.setText(getResources().getString(R.string.en_bookingDetails_numAttendees_singleAttendee, booking.getNumPeopleAttending()));
            } else {
                // wtf
                Log.i("spoonlogcat: ", "numAttendees <= 0 ???????");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking_details, container, false);

        restaurantName = view.findViewById(R.id.restaurant_name);
        restaurantVicinity = view.findViewById(R.id.restaurant_vicinity);
        bookingDate = view.findViewById(R.id.bookingDate);
        bookingTime = view.findViewById(R.id.bookingTime);
        bookingAttendees = view.findViewById(R.id.bookingAttendeeCount);

        dbHelper = new DatabaseHelper(getContext());

        setupButtonListeners();

        return view;
    }

    private void setupButtonListeners(){
        editBtn = view.findViewById(R.id.editBookingBtn);
        shareBtn = view.findViewById(R.id.shareBookingBtn);
        deleteBtn = view.findViewById(R.id.setVisitedBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getResources().getString(R.string.en_shareBooking_shareString_Body, restaurant.getName(), booking.getDateOfBooking(), booking.getTimeOfBooking());
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.en_shareBooking_shareString_Intent_Title)));
            }
        });
    }

}
