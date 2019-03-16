package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealItem;
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

    private Button addMealBtn;
    private Button viewRestaurantBtn;
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

            // See if a meal exists already and change the listener if so
            final MealItem tempMeal = dbHelper.getMealsByBookingID(booking.getBookingID());

            if(tempMeal != null){
                addMealBtn.setText("Go to linked Meal");

                // if it's not null it should go to the meal instead of making them create a new one
                addMealBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("booking", booking);
                        bundle.putSerializable("restaurant", restaurant);
                        bundle.putSerializable("meal", tempMeal);
                        MainActivity.navigation.getMenu().findItem(R.id.navigation_meals).setChecked(true);
                        FragmentStateContainer.getInstance().switchFragmentState(10, bundle);
                    }
                });
            } else {
                addMealBtn.setText("Add Meal");
                addMealBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("booking", booking);
                        bundle.putSerializable("restaurant", restaurant);
                        FragmentStateContainer.getInstance().switchFragmentState(8, bundle);
                    }
                });
            }

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
        addMealBtn = view.findViewById(R.id.addMealBtn);
        viewRestaurantBtn = view.findViewById(R.id.viewRestaurantBtn);
        editBtn = view.findViewById(R.id.editBookingBtn);
        shareBtn = view.findViewById(R.id.shareBookingBtn);
        deleteBtn = view.findViewById(R.id.deleteBookingBtn);

        addMealBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putSerializable("booking", booking);
                bundle.putSerializable("restaurant", restaurant);
                FragmentStateContainer.getInstance().switchFragmentState(8, bundle);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("booking", booking);
                bundle.putSerializable("restaurant", restaurant);
                FragmentStateContainer.getInstance().switchFragmentState(7, bundle);
            }
        });

        viewRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurant", restaurant);
                MainActivity.navigation.getMenu().findItem(R.id.navigation_restaurants).setChecked(true);
                FragmentStateContainer.getInstance().switchFragmentState(4, bundle);
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dbHelper.deleteBooking(booking);
                                FragmentStateContainer.getInstance().switchFragmentState(1, null);
                                BookingContent.bookingItems.remove(booking);
                                BookingsFragment.mAdapter.notifyDataSetChanged();
                                if(BookingContent.bookingItems.size() == 0){
                                    BookingsFragment.noBookingsText.setVisibility(View.VISIBLE);
                                    BookingsFragment.noBookingsArrow.setVisibility(View.VISIBLE);
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure? This action cannot be undone").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

}
