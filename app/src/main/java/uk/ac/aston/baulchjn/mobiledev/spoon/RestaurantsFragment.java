package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantClickListener;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantRecyclerAdapter;

public class RestaurantsFragment extends Fragment {
    private List<RestaurantItem> rv_list;
    private View view;
    private RecyclerView recyclerView;
    private ViewPager viewPager;

    private TextView formattedHeader;
    private TextView additionalInfoHeadline;

    private RestaurantsRecyclerViewFragment restaurantsRecyclerViewFragment;
    private RestaurantsMapViewFragment restaurantMapViewFragment;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    private Callable<Void> onJSONTaskCompleted = new Callable<Void>() {
        @Override
        public Void call() {
            Log.i("spoonlogcat:", "Woo the oncomplete fired");

            formattedHeader.setText(getResources().getString(R.string.en_restaurantFragment_wefoundXRestaunts, String.valueOf(RestaurantContent.restaurantItems.size())));
            additionalInfoHeadline.setText("Use the buttons below to centre on your location, search in a new location, sort, and filter.");
            return null;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rv_list = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        viewPager = view.findViewById(R.id.restaurant_ViewPager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.restaurant_TabLayout);
        tabs.setupWithViewPager(viewPager);


        formattedHeader = view.findViewById(R.id.formattedRestaurantHeader);
        additionalInfoHeadline = view.findViewById(R.id.en_RestaurantFragment_AdditionalInfo);

////        recyclerView = view.findViewById(R.id.restaurants_rv);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Log.i("spoonlogcat: ", "The Fragment is equal to..." + restaurantsRecyclerViewFragment);
////        recyclerView = restaurantsRecyclerViewFragment.getRecyclerView();
////        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        RestaurantRecyclerAdapter mAdapter = new RestaurantRecyclerAdapter(RestaurantContent.restaurantItems, new RestaurantClickListener() {
//            @Override
//            public void onItemClick(RestaurantItem item) {
//                Bundle bundle = new Bundle();
//                bundle.putString("name", item.getName());
//                bundle.putString("vicinity", item.getVicinity());
//                bundle.putString("tag1", item.getTag1());
//                bundle.putString("tag2", item.getTag2());
//                bundle.putString("tag3", item.getTag3());
//                bundle.putSerializable("restaurant", item);
//
//
//                // launch the RestaurantDetailedFragment with the correct restaurant
//                FragmentStateContainer.getInstance().switchFragmentState(4, bundle);
//            }
//        });
//
////        recyclerView.setAdapter(mAdapter);
////        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        RestaurantContent.jsonRequest(getActivity().getApplicationContext(), mAdapter, onJSONTaskCompleted);

        //rv_list = RestaurantContent.restaurantItems;
        return view;
    }

    private void setupViewPager(ViewPager viewPager){
        restaurantsRecyclerViewFragment = new RestaurantsRecyclerViewFragment();
        restaurantMapViewFragment = new RestaurantsMapViewFragment();

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(restaurantsRecyclerViewFragment, "List View");
        adapter.addFragment(restaurantMapViewFragment, "Map View");

        viewPager.setAdapter(adapter);
    }

    public void recyclerFragmentWasInflated(){
//        recyclerView = view.findViewById(R.id.restaurants_rv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i("spoonlogcat: ", "recyclerFragmentWasInflated invoked");
        recyclerView = restaurantsRecyclerViewFragment.getRecyclerView();
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

        RestaurantContent.jsonRequest(getActivity(), getActivity().getApplicationContext(), mAdapter, onJSONTaskCompleted);

    }

}
