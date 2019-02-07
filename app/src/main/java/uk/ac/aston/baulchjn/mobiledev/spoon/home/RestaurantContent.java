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
    public static final String URL_JSON = "https://places.cit.api.here.com/places/v1/discover/search?at=52.4870897%2C-1.8902916&q=american+restaurant&Accept-Language=en-GB%2Cen-US%3Bq%3D0.9%2Cen%3Bq%3D0.8&app_id=auI3sSIwYT1YkAkKkm9f&app_code=UblO-Dyzdg-QCADIYvRWEw#";
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
                            JSONArray jsonArray = response.getJSONObject("results").getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                RestaurantItem restaurantItem = new RestaurantItem();
                                restaurantItems.add(restaurantItem);
//                                restaurantItem.setName("Test");
//                                restaurantItem.setDesc("Test");
                                restaurantItem.setName(jsonArray.getJSONObject(i).getString("title"));
                                restaurantItem.setDesc(jsonArray.getJSONObject(i).getString("title"));
                                restaurantItem.setVicinity(jsonArray.getJSONObject(i).getString("vicinity"));
//                                restaurantItem.setRestaurantType(jsonArray.getJSONObject(i).getString("types"));
//                                restaurantItem.setImageURL(jsonArray.getJSONObject(i).getString("image_url"));
//                                restaurantItem.setStarRating(jsonArray.getJSONObject(i).getString("rating"));
//                                 restaurantItem.setRestaurantVisited(jsonArray.getJSONObject(i).getBoolean("restaurantVisited"));
//                                ITEM_MAP.put("restaurant", createRestaurantItem(0));
                                Log.i("RestaurantContent", "Got restaurant: " + restaurantItem.getName() + "");
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