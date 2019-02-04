package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.R;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantRecyclerAdapter;

public class RestaurantsFragment extends Fragment {

    private List<RestaurantItem> rv_list;
    private RecyclerView recyclerView;

    public RestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.home_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_list = new ArrayList<>();
        // TODO: Add the home_item_upcomingreservation object to the list here.
        rv_list.add(new RestaurantItem("Marketing Information", R.drawable.ic_event_white_24dp));
        rv_list.add(new RestaurantItem("Find Restaurants Quickly", R.drawable.ic_event_white_24dp));
        rv_list.add(new RestaurantItem("My Restaurant Visits", R.drawable.ic_restaurant_white_24dp));
        rv_list.add(new RestaurantItem("Lol", R.drawable.ic_settings));
        rv_list.add(new RestaurantItem("Test", R.drawable.ic_settings));
        rv_list.add(new RestaurantItem("Random", R.drawable.ic_settings));
        rv_list.add(new RestaurantItem("Ting", R.drawable.ic_settings));



        RestaurantRecyclerAdapter mAdapter = new RestaurantRecyclerAdapter(rv_list);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

}
