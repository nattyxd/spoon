package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestaurantContent {
    // Get Data from JSON File
    public static final String URL_JSON = "XD";
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    public static List<RestaurantItem> restaurantItems = new ArrayList<>();
    private RecyclerView myrv;

    /**
     * Json call for real data
     */

    public static void jsonRequest(Context context) {
        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(context);
        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, URL_JSON, null,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            System.out.println("I am trying to find you " + jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {

                                RestaurantItem restaurantItem = new RestaurantItem();

                                restaurantItem.setName(jsonArray.getJSONObject(i).getString("name"));
                                restaurantItem.setDesc(jsonArray.getJSONObject(i).getString("description"));
                                restaurantItem.setVicinity(jsonArray.getJSONObject(i).getString("vicinity"));
                                restaurantItem.setRestaurantType(jsonArray.getJSONObject(i).getString("types"));
                                restaurantItem.setImageURL(jsonArray.getJSONObject(i).getString("image_url"));
                                restaurantItem.setStarRating(jsonArray.getJSONObject(i).getString("rating"));
//                                 restaurantItem.setRestaurantVisited(jsonArray.getJSONObject(i).getBoolean("restaurantVisited"));
                                restaurantItems.add(restaurantItem);
//                                ITEM_MAP.put("restaurant", createRestaurantItem(0));
                                System.out.println("Got restaurant: " + restaurantItem.getName());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //ListBookingFragmentInteraction(restaurantItems);
                        //setupCyclerView(restaurantItems);


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