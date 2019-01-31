package uk.ac.aston.baulchjn.mobiledev.spoon.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.R;

public class deprecated_HomeFragment extends Fragment {

    private List<RestaurantItem> rv_list;
    private RecyclerView recyclerView;

    public deprecated_HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        recyclerView = (RecyclerView) view.findViewById(R.id.home_rv);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        rv_list = new ArrayList<>();
//        // TODO: Add the home_item_upcomingreservation object to the list here.
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
