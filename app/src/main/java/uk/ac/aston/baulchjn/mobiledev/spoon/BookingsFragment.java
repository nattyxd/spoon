package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.helper.BookingsSwipeToDeleteCallback;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingClickListener;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingRecyclerAdapter;

public class BookingsFragment extends Fragment {
    private List<BookingItem> rv_list;
    private RecyclerView recyclerView;

    public BookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rv_list = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        recyclerView = view.findViewById(R.id.bookings_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        BookingRecyclerAdapter mAdapter = new BookingRecyclerAdapter(BookingContent.bookingItems, new BookingClickListener() {
            @Override
            public void onItemClick(BookingItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("date", item.getDateOfBooking());
                bundle.putString("time", item.getTimeOfBooking());
                bundle.putInt("numAttendees", item.getNumPeopleAttending());

                bundle.putSerializable("booking", item);

                FragmentStateContainer.getInstance().switchFragmentState(4, bundle);
            }
        });

        mAdapter.setView(view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BookingsSwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        BookingContent.populateBookings(getContext(), mAdapter);
//        BookingContent.jsonRequest(getActivity().getApplicationContext(), mAdapter); // prob need to replace with like BookingContent.getContent
        //rv_list = RestaurantContent.restaurantItems;
        return view;
    }
}
