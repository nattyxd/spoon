package uk.ac.aston.baulchjn.mobiledev.spoon.home;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.FragmentStateContainer;
import uk.ac.aston.baulchjn.mobiledev.spoon.MainActivity;
import uk.ac.aston.baulchjn.mobiledev.spoon.R;

public class deprecated_HomeFragment extends Fragment {

    private List<RestaurantItem> rv_list;
    private RecyclerView recyclerView;

    private DatabaseHelper dbHelper;

    public static TextView youHaveReservationText;
    public static TextView youHaveReservationSubtitle;
    public static Button youHaveReservationButton;

    public deprecated_HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        youHaveReservationText = view.findViewById(R.id.item_reservation_notification);
        youHaveReservationSubtitle = view.findViewById(R.id.item_reservation_notification_subtitle);
        Button youHaveReservationButton = view.findViewById(R.id.viewReservationButton);

        dbHelper = new DatabaseHelper(getContext());

        final List<BookingItem> bookings = dbHelper.getAllBookingsAsList();

        if(bookings == null || bookings.size() == 0){
            youHaveReservationText.setText("There's no upcoming bookings!");
            youHaveReservationButton.setEnabled(false);
            youHaveReservationButton.getBackground().setTint(Color.parseColor("#c2cfd3"));
            youHaveReservationSubtitle.setText("Go into restaurants, select a restaurant, and create a booking to make one!");
        } else {
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

//        recyclerView = (RecyclerView) view.findViewById(R.id.home_rv);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        rv_list = new ArrayList<>();
//        rv_list.add(new RestaurantItem("Marketing Information", R.drawable.ic_event_white_24dp));
//        rv_list.add(new RestaurantItem("Find Restaurants Quickly", R.drawable.ic_event_white_24dp));
//        rv_list.add(new RestaurantItem("My Restaurant Visits", R.drawable.ic_restaurant_white_24dp));
//        rv_list.add(new RestaurantItem("My Favourite Restaurants", R.drawable.ic_settings));
//
//
//        RestaurantRecyclerAdapter mAdapter = new RestaurantRecyclerAdapter(rv_list);
//
//        recyclerView.setAdapter(mAdapter);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

}
