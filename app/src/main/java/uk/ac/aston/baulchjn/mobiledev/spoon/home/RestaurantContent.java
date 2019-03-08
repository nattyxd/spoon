package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.here.android.mpa.common.GeoCoordinate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

import uk.ac.aston.baulchjn.mobiledev.spoon.MainActivity;
import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RestaurantContent {
    // Get Data from JSON File
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    public static List<RestaurantItem> restaurantItems = new ArrayList<>();
    private RecyclerView myrv;

    /**
     * Json call for real data
     */

    public static void jsonRequest(Activity activity, Context context, final RestaurantRecyclerAdapter adapter, Location location, final Callable<Void> onComplete) {
        final String latitude_val = String.valueOf(location.getLatitude());
        final String longitude_val = String.valueOf(location.getLongitude());

        //final String URL_JSON = "https://places.cit.api.here.com/places/v1/discover/search?at=52.4870897%2C-1.8902916&q=american+restaurant&Accept-Language=en-GB%2Cen-US%3Bq%3D0.9%2Cen%3Bq%3D0.8&app_id=auI3sSIwYT1YkAkKkm9f&app_code=UblO-Dyzdg-QCADIYvRWEw#";
        String URL_JSON = "https://places.cit.api.here.com/places/v1/discover/search?at=" + latitude_val + "%2C" + longitude_val + "&q=restaurant&Accept-Language=en-GB%2Cen-US%3Bq%3D0.9%2Cen%3Bq%3D0.8&app_id=auI3sSIwYT1YkAkKkm9f&app_code=UblO-Dyzdg-QCADIYvRWEw#";

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(context);
        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, URL_JSON, null,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONObject("results").getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                RestaurantItem restaurantItem = new RestaurantItem();
                                restaurantItems.add(restaurantItem);
//                                restaurantItem.setName("Test");
//                                restaurantItem.setDesc("Test");
                                restaurantItem.setHereID(jsonArray.getJSONObject(i).getString("id"));
                                restaurantItem.setName(jsonArray.getJSONObject(i).getString("title"));
                                restaurantItem.setDesc(jsonArray.getJSONObject(i).getString("title"));

                                // set the latitude and longitude
                                restaurantItem.setLatitude(jsonArray.getJSONObject(i).getJSONArray("position").getString(0));
                                restaurantItem.setLongitude(jsonArray.getJSONObject(i).getJSONArray("position").getString(1));

                                restaurantItem.setVicinity(jsonArray.getJSONObject(i).getString("vicinity"));

                                JSONArray tags = jsonArray.getJSONObject(i).optJSONArray("tags");
                                if (tags != null) {
                                    for (int j = 0; j < tags.length(); j++) {
                                        switch (j) {
                                            case 0:
                                                restaurantItem.setTag1(tags.getJSONObject(j).getString("title"));
                                                break;
                                            case 1:
                                                restaurantItem.setTag2(tags.getJSONObject(j).getString("title"));
                                                break;
                                            case 2:
                                                restaurantItem.setTag3(tags.getJSONObject(j).getString("title"));
                                                break;
                                        }
                                    }
                                }

//                                restaurantItem.setRestaurantType(jsonArray.getJSONObject(i).getString("types"));
//                                restaurantItem.setImageURL(jsonArray.getJSONObject(i).getString("image_url"));
//                                restaurantItem.setStarRating(jsonArray.getJSONObject(i).getString("rating"));
//                                 restaurantItem.setRestaurantVisited(jsonArray.getJSONObject(i).getBoolean("restaurantVisited"));
//                                ITEM_MAP.put("restaurant", createRestaurantItem(0));
                                Log.i("RestaurantContent", "Got restaurant: " + restaurantItem.getName() + "");
                            }
                            adapter.notifyDataSetChanged();
                            try{
                                onComplete.call();
                            } catch (Exception ex){
                                ex.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        ListBookingFragmentInteraction(restaurantItems);


                        //  Toast.makeText(RestaurantContent.this, "Size of Listen " + String.valueOf(restaurantItem.size()), Toast.LENGTH_SHORT).show();
//                ListBookingFragmentInteraction(restaurantItem);
                    }

                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON object request "arrayRequest" to the request queue
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(arrayRequest);
    }
}