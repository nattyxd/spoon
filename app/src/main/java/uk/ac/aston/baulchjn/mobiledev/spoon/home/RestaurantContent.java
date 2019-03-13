package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;

public class RestaurantContent {
    // Get Data from JSON File
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    public static List<RestaurantItem> restaurantItems = new ArrayList<>(); // the master list of restaurants to display
    public static List<RestaurantItem> unfilteredRestaurantItems = new ArrayList<>(); // all restaurants (cached), including ones the user doesn't wish to currently see
    public static List<RestaurantItem> visitedRestaurants = new ArrayList<>();

    private RecyclerView myrv;

    /**
     * Json call for real data
     */

    public static void jsonRequest(Activity activity, final Context context, final RestaurantRecyclerAdapter adapter, Location location, final Callable<Void> onComplete) {
        final String latitude_val = String.valueOf(location.getLatitude());
        final String longitude_val = String.valueOf(location.getLongitude());

        //final String URL_JSON = "https://places.cit.api.here.com/places/v1/discover/search?at=52.4870897%2C-1.8902916&q=american+restaurant&Accept-Language=en-GB%2Cen-US%3Bq%3D0.9%2Cen%3Bq%3D0.8&app_id=auI3sSIwYT1YkAkKkm9f&app_code=UblO-Dyzdg-QCADIYvRWEw#";
        String URL_JSON = "https://places.cit.api.here.com/places/v1/discover/search?at=" + latitude_val + "%2C" + longitude_val + "&q=restaurant&Accept-Language=en-GB%2Cen-US%3Bq%3D0.9%2Cen%3Bq%3D0.8&app_id=auI3sSIwYT1YkAkKkm9f&app_code=UblO-Dyzdg-QCADIYvRWEw#";

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(context);

        unfilteredRestaurantItems.clear(); // clear restaurants on refresh of restaurants

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, URL_JSON, null,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONObject("results").getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                RestaurantItem restaurantItem = new RestaurantItem();
                                unfilteredRestaurantItems.add(restaurantItem);
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

                            filterOutRestaurants(context, adapter, onComplete);

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

    // TODO: This entire method is really inefficient and needs a refactor
    public static void refreshExistingVisitedRestaurantsList(Context context){
        // Add the visited restaurants back to the list of restaurants
        // TODO: Refactor this into a singleton
        // TODO: This should use an existing visited restuarants adapter that gets updated to maximise efficiency
        // TODO: Fix bug where duplicate restaurants can pop up if the user has already visited it and it's local
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        visitedRestaurants.clear();

        List<RestaurantItem> allRestaurants = dbHelper.getAllRestaurantsAsList();
        List<RestaurantItem> visitedRestaurantsTemp = new ArrayList<>();

        for(int i = 0; i < allRestaurants.size(); i++){
            if(allRestaurants.get(i).isVisited() == true){
                visitedRestaurantsTemp.add(allRestaurants.get(i));
            }
        }

        visitedRestaurants.addAll(visitedRestaurantsTemp);
        visitedRestaurantsTemp.clear();
    }

    public static void filterOutRestaurants(Context context, RestaurantRecyclerAdapter adapter, Callable<Void> onComplete){
        // at call time, RestaurantItems will only be set to whatever was pulled from the JSON
        restaurantItems = (ArrayList) new ArrayList<>(unfilteredRestaurantItems);
        adapter.restaurantList = restaurantItems;

        // check if we need to eliminate the restaurants the user hasn't visited from the list
        if(RestaurantsFragment.showNonVisitedRestaurants == false){
            restaurantItems.clear();
        }

        // check if we need to add visited restaurants to the list
        if(RestaurantsFragment.showVisitedRestaurants){
            refreshExistingVisitedRestaurantsList(context);
            restaurantItems.addAll(visitedRestaurants);
        }

        // check if we need to eliminate any restaurants from the list because of tags
        for (HashMap.Entry<String, Boolean> entry : RestaurantsFragment.categoriesToExclude.entrySet())
        {
            for(int i = 0; i < restaurantItems.size() - 1; i++){
                RestaurantItem currentRestaurant = restaurantItems.get(i);

                boolean shouldRemove = false;
                if(currentRestaurant.getTag1() != null){
                    if(currentRestaurant.getTag1().equals(entry.getKey())){
                        shouldRemove = true;
                    }
                }
                if(currentRestaurant.getTag2() != null){
                    if(currentRestaurant.getTag2().equals(entry.getKey())){
                        shouldRemove = true;
                    }
                }
                if(currentRestaurant.getTag3() != null){
                    if(currentRestaurant.getTag3().equals(entry.getKey())){
                        shouldRemove = true;
                    }
                }

                if(shouldRemove){
                    restaurantItems.remove(currentRestaurant);
                }
            }
        }


        adapter.notifyDataSetChanged();
        try{
            onComplete.call();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}