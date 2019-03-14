package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.helper.BookingsSwipeToDeleteCallback;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByAscendingAlphabet;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByAscendingDate;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByAscendingDistance;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByNumAttendees;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortRestaurantByAscendingAlphabet;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortRestaurantByAscendingDistance;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortRestaurantByDescendingDistance;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingClickListener;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingRecyclerAdapter;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;

public class BookingsFragment extends Fragment {
    private List<BookingItem> rv_list;
    private RecyclerView recyclerView;
    private Button sortButton;
    private Button filterButton;

    public static BookingRecyclerAdapter mAdapter;
    public static TextView noBookingsText;
    public static ImageView noBookingsArrow;

    private DatabaseHelper dbHelper;

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
        noBookingsText = view.findViewById(R.id.noBookingInfoText);
        noBookingsArrow = view.findViewById(R.id.noBookingsArrow);

        BookingContent.bookingItems = new ArrayList<>();
        mAdapter = new BookingRecyclerAdapter(BookingContent.bookingItems, new BookingClickListener() {
            @Override
            public void onItemClick(BookingItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("date", item.getDateOfBooking());
                bundle.putString("time", item.getTimeOfBooking());
                bundle.putInt("numAttendees", item.getNumPeopleAttending());

                bundle.putSerializable("booking", item);

                FragmentStateContainer.getInstance().switchFragmentState(6, bundle);
            }
        });

        mAdapter.setView(view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BookingsSwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        BookingContent.populateBookings(getContext(), mAdapter);

        sortButton = view.findViewById(R.id.sortButton);
        filterButton = view.findViewById(R.id.filterButton);

        dbHelper = new DatabaseHelper(getContext());

        setupButtons();
//        BookingContent.jsonRequest(getActivity().getApplicationContext(), mAdapter); // prob need to replace with like BookingContent.getContent
        //rv_list = RestaurantContent.restaurantItems;
        return view;
    }

    private void setupButtons(){
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Sort");
                dialogBuilder.setItems(R.array.en_bookings_SortOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                // alphabetical ascending
                                Collections.sort(BookingContent.bookingItems, new SortBookingsByAscendingAlphabet(dbHelper));
                                break;
                            case 1:
                                // alphabetical descending
                                Collections.sort(BookingContent.bookingItems, Collections.reverseOrder(new SortBookingsByAscendingAlphabet(dbHelper)));
                                break;
                            case 2:
                                // distance ascending
                                Collections.sort(BookingContent.bookingItems, new SortBookingsByAscendingDistance(getContext(), dbHelper));
                                break;
                            case 3:
                                // distance descending
                                Collections.sort(BookingContent.bookingItems, Collections.reverseOrder(new SortBookingsByAscendingDistance(getContext(), dbHelper)));
                                break;
                            case 4:
                                // date ascending
                                Collections.sort(BookingContent.bookingItems, new SortBookingsByAscendingDate());
                                break;
                            case 5:
                                // date descending
                                Collections.sort(BookingContent.bookingItems, Collections.reverseOrder(new SortBookingsByAscendingDate()));
                                break;
                            case 6:
                                // num attendees ascending
                                Collections.sort(BookingContent.bookingItems, new SortBookingsByNumAttendees());
                                break;
                            case 7:
                                // num attendees descending
                                Collections.sort(BookingContent.bookingItems, Collections.reverseOrder(new SortBookingsByNumAttendees()));
                                break;
                            default:
                                Collections.sort(BookingContent.bookingItems, new SortBookingsByAscendingAlphabet(dbHelper));
                                break;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

                dialogBuilder.create().show();

            }
        });
    }
}
