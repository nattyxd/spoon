package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Activity;
import android.os.Bundle;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapView;
import com.here.android.mpa.mapping.SupportMapFragment;

import java.io.File;


public class RestaurantsMapViewFragment extends Fragment {

    // map embedded in the map fragment
    private Map map = null;
    private MapView mapView = null;

    // map fragment embedded in this activity
    private SupportMapFragment mapFragment = null;

    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_restaurants_map_view, container, false);

            mapView = (MapView) view.findViewById(R.id.restaurants_MapView);
//            ApplicationContext appCtx = new ApplicationContext(getContext());

            map = new Map();
            mapView.setMap(map);
//            MapEngine.getInstance().init(appCtx, engineInitHandler);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }


        return view;
    }

    public RestaurantsMapViewFragment(){
        // Required empty constructor
    }

//    private OnEngineInitListener engineInitHandler = new OnEngineInitListener() {
//        @Override
//        public void onEngineInitializationCompleted(Error error) {
//            if (error == Error.NONE) {
//
//                // more map initial settings
//            } else {
//                Log.e("spoonlogcat: ", "ERROR: Cannot initialize MapEngine " + error);
//                Log.e("spoonlogcat: ", error.getDetails());
//                Log.e("spoonlogcat: ", error.getStackTrace());
//            }
//        }
//    };

//    @Override
//    public void onResume() {
//        super.onResume();
//        MapEngine.getInstance().onResume();
//        if (mapView != null) {
//            mapView.onResume();
//        }
//    }

//    @Override
//    public void onPause() {
//        if (mapView != null) {
//            mapView.onPause();
//        }
//        MapEngine.getInstance().onPause();
//        super.onPause();
//    }
}
