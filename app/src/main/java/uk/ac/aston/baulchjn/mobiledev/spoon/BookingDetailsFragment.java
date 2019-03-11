package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;

public class BookingDetailsFragment extends Fragment {

    private BookingItem booking;

    public BookingDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            booking = (BookingItem) bundle.getSerializable("booking");
            booking.getBookingID();

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
        return inflater.inflate(R.layout.fragment_booking_details, container, false);
    }

}
