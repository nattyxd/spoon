package uk.ac.aston.baulchjn.mobiledev.spoon.home;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.util.Strings;

import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.FragmentStateContainer;
import uk.ac.aston.baulchjn.mobiledev.spoon.MainActivity;
import uk.ac.aston.baulchjn.mobiledev.spoon.R;

public class HomeFragment extends Fragment {

    private List<RestaurantItem> rv_list;
    private RecyclerView recyclerView;

    private static DatabaseHelper dbHelper;

    public static TextView youHaveReservationText;
    public static TextView youHaveReservationSubtitle;
    public static Button youHaveReservationButton;

    private static View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        notifyUpcomingReservationModule(getContext());

        Button nearMeNow = view.findViewById(R.id.NearMeNow);
        Button nearMeNow2 = view.findViewById(R.id.NearMeNow2);
        Button staffPicks = view.findViewById(R.id.StaffPicks);
        Button staffPicks2 = view.findViewById(R.id.StaffPicks2);
        ImageView burger = view.findViewById(R.id.burgerImage);
        FloatingActionButton button = view.findViewById(R.id.fab);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentStateContainer.getInstance().switchFragmentState(2, null);
                MainActivity.navigation.getMenu().findItem(R.id.navigation_restaurants).setChecked(true);
            }
        };

        nearMeNow.setOnClickListener(listener);
        nearMeNow2.setOnClickListener(listener);
        staffPicks.setOnClickListener(listener);
        staffPicks2.setOnClickListener(listener);
        burger.setOnClickListener(listener);
        button.setOnClickListener(listener);

        return view;
    }

    public static void notifyUpcomingReservationModule(Context context){
        youHaveReservationText = view.findViewById(R.id.item_reservation_notification);
        youHaveReservationSubtitle = view.findViewById(R.id.item_reservation_notification_subtitle);
        youHaveReservationButton = view.findViewById(R.id.viewReservationButton);

        dbHelper = new DatabaseHelper(context);

        final List<BookingItem> bookings = dbHelper.getAllBookingsAsList();

        if(bookings == null || bookings.size() == 0){
            youHaveReservationText.setText("There's no upcoming bookings!");
            youHaveReservationButton.setEnabled(false);
            youHaveReservationButton.getBackground().setTint(Color.parseColor("#c2cfd3"));
            youHaveReservationSubtitle.setText("Go into restaurants, select a restaurant, and create a booking to make one!");
        } else {
            youHaveReservationButton.setEnabled(true);
            youHaveReservationButton.getBackground().setTint(Color.parseColor("#1ebdf0"));

            RestaurantItem associatedRestaurant = dbHelper.getRestaurantByHereID(bookings.get(0).getRestaurantID());
            String associatedRestaurantName = associatedRestaurant.getName();

            if(Strings.isEmptyOrWhitespace(associatedRestaurantName)){
                // missing restaurant.. okay a bit weird probably means the db got corrupted
                youHaveReservationText.setText(context.getResources().getText(R.string.en_home_upcomingreservation_title, bookings.get(0).getTimeOfBooking()));
            } else {
                youHaveReservationText.setText(context.getResources().getText(R.string.en_home_upcomingreservation_title, associatedRestaurantName));
            }
            youHaveReservationText.setText("You have an upcoming booking! At " + bookings.get(0).getTimeOfBooking());
            youHaveReservationSubtitle.setText(bookings.get(0).getNumPeopleAttending() + " attendee(s) are coming!");
            youHaveReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("booking", bookings.get(0));
                    FragmentStateContainer.getInstance().switchFragmentState(6, bundle);
                    MainActivity.navigation.getMenu().findItem(R.id.navigation_bookings).setChecked(true);
                }
            });
        }
    }

}
