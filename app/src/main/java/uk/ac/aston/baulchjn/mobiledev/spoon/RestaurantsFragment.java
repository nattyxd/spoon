package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.R;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantRecyclerAdapter;

public class RestaurantsFragment extends Fragment {

    private List<RestaurantItem> rv_list;
    private RecyclerView recyclerView;
    public RestaurantsFragment.RestaurantsFragmentInteraction listener;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialise the list of restaurant items.
        if (getArguments() != null) {
            Log.i("RR", "restaurantsFragmentMade");

        }
        RestaurantContent.jsonRequest(getActivity().getApplicationContext());
        rv_list = RestaurantContent.restaurantItems;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.restaurants_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_list = new ArrayList<>();

        RestaurantRecyclerAdapter mAdapter = new RestaurantRecyclerAdapter(rv_list);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    public interface RestaurantsFragmentInteraction{
        void restaurantFragmentInteraction(RestaurantItem restaurantItem);
    }

}
