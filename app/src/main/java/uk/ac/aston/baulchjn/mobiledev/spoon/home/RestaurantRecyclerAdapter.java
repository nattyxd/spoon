package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.android.mpa.common.GeoCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.aston.baulchjn.mobiledev.spoon.R;
import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;


public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ViewHolder> {
    public List<RestaurantItem> restaurantList;
    //public RestaurantsFragment.RestaurantsFragmentInteraction listener;
    private final RestaurantClickListener clickListener;
    private View view;

    public RestaurantRecyclerAdapter(List<RestaurantItem> list, RestaurantClickListener listener) {
        this.restaurantList = list;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public RestaurantRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.restaurantItem = restaurantList.get(position);
        GeoCoordinate r = new GeoCoordinate(Double.parseDouble(restaurantList.get(position).getLatitude()), Double.parseDouble(restaurantList.get(position).getLongitude()));
        GeoCoordinate user = new GeoCoordinate(RestaurantsFragment.bestUserLocation.getLatitude(), RestaurantsFragment.bestUserLocation.getLongitude());
        double distance = r.distanceTo(user);

        String description = restaurantList.get(position).getDesc() + " - " + Math.round(distance) + "m away.";
        holder.desc.setText(description);


        Random generator = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.burger);
        list.add(R.drawable.chicken);
        list.add(R.drawable.mcdonalds);
        list.add(R.drawable.pizza);
        list.add(R.drawable.pub);
        list.add(R.drawable.mexican);
        list.add(R.drawable.spanish);
        list.add(R.drawable.breakfast);
        list.add(R.drawable.desert1);
        list.add(R.drawable.dessert2);
        list.add(R.drawable.french);
        list.add(R.drawable.french2);
        list.add(R.drawable.indian);
        list.add(R.drawable.indian2);
        list.add(R.drawable.burger2);
        list.add(R.drawable.chicken_lettuce);
        list.add(R.drawable.fishchips);
        list.add(R.drawable.healthy);
        list.add(R.drawable.indiantakeaway);
        list.add(R.drawable.pasta);
        list.add(R.drawable.takeaway);
        list.add(R.drawable.wrap);




        int randomIndex = generator.nextInt(list.size() -1);

        holder.image.setImageDrawable(view.getContext().getDrawable(list.get(randomIndex)));

        holder.bind(restaurantList.get(position), clickListener);

        //  holder.rrate.setText(restaurantItems.get(position).getRestaurantRating());
        // smooth image loading
        //Glide.with(mListener).load(mData.get(position).getImage_url()).into(RestaurantThumbnail);
        //holder.RestaurantThumbnail.setImageResource(image_url);
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!= listener){
                    listener.restaurantFragmentInteraction(holder.restaurantItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RestaurantItem restaurantItem;
        private View mView;
        private TextView desc;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = this.itemView.findViewById(R.id.item_desc);
            image = this.itemView.findViewById(R.id.item_image);
        }

        public void bind(final RestaurantItem item, final RestaurantClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
