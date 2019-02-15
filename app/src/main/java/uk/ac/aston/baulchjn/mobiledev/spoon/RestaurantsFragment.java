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

import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantClickListener;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantRecyclerAdapter;

public class RestaurantsFragment extends Fragment {
    private List<RestaurantItem> rv_list;
    private RecyclerView recyclerView;

    public RestaurantsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        recyclerView = view.findViewById(R.id.restaurants_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RestaurantRecyclerAdapter mAdapter = new RestaurantRecyclerAdapter(RestaurantContent.restaurantItems, new RestaurantClickListener() {
            @Override
            public void onItemClick(RestaurantItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("name", item.getName());
                bundle.putString("vicinity", item.getVicinity());
                bundle.putString("tag1", item.getTag1());
                bundle.putString("tag2", item.getTag2());
                bundle.putString("tag3", item.getTag3());
                bundle.putSerializable("restaurant", item);


                // launch the RestaurantDetailedFragment with the correct restaurant
                FragmentStateContainer.getInstance().switchFragmentState(4, bundle);
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RestaurantContent.jsonRequest(getActivity().getApplicationContext(), mAdapter);
        //rv_list = RestaurantContent.restaurantItems;
        return view;
    }
}
