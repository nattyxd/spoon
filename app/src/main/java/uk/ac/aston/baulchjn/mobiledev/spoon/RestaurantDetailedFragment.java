package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;


/**
 *
 */
public class RestaurantDetailedFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_VICINITY = "vicinity";
    private static final String ARG_TAG1 = "tag1";
    private static final String ARG_TAG2 = "tag2";
    private static final String ARG_TAG3 = "tag3";
    private static final String ARG_RESTAURANT = "restaurant";

    private String name;
    private String vicinity;
    private String tag1;
    private String tag2;
    private String tag3;
    private RestaurantItem restaurant;

    public RestaurantDetailedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView nameView = this.getView().findViewById(R.id.restaurant_name);
        TextView vicinityView = this.getView().findViewById(R.id.restaurant_vicinity);
        TextView tagsView = this.getView().findViewById(R.id.restaurant_tags);

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            nameView.setText(bundle.getString("name"));
            vicinityView.setText(bundle.getString("vicinity"));

            // Logic to set tags
            tagsView.setText("This needs updating.. but for now just have the first tag: " + bundle.getString("tag1"));
            restaurant = (RestaurantItem) bundle.getSerializable("restaurant");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            vicinity = getArguments().getString(ARG_VICINITY);
            tag1 = getArguments().getString(ARG_TAG1);
            tag2 = getArguments().getString(ARG_TAG2);
            tag3 = getArguments().getString(ARG_TAG3);
            restaurant = (RestaurantItem) getArguments().getSerializable("restaurant");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_detailed, container, false);
//        TextView nameView = view.findViewById(R.id.restaurant_name);
//        TextView vicinityView = view.findViewById(R.id.restaurant_vicinity);
        TextView tagsView = view.findViewById(R.id.restaurant_tags);
//
//        nameView.setText(name);
//        vicinityView.setText(vicinity);
        tagsView.setText("Replace this with tag1,tag2,tag3, etc");
//
        Button bookBtn = view.findViewById(R.id.book_btn);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurant", restaurant);
                FragmentStateContainer.getInstance().switchFragmentState(5, bundle);
            }
        });

        return view;
    }


}
