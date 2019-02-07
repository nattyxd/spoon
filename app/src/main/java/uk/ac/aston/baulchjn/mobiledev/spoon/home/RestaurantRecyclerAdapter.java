package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.R;
import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;


public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ViewHolder> {
    public List<RestaurantItem> restaurantList;
    public RestaurantsFragment.RestaurantsFragmentInteraction listener;


    public RestaurantRecyclerAdapter(List<RestaurantItem> list) {
        this.restaurantList = list;
    }

    @NonNull
    @Override
    public RestaurantRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.restaurantItem = restaurantList.get(position);
        holder.desc.setText(restaurantList.get(position).getDesc());
        //  holder.rrate.setText(restaurantItems.get(position).getRestaurantRating());
        // smooth image loading
        //Glide.with(mListener).load(mData.get(position).getImage_url()).into(RestaurantThumbnail);
        //holder.RestaurantThumbnail.setImageResource(image_url);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!= listener){
                    listener.restaurantFragmentInteraction(holder.restaurantItem);
                }
            }
        });
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
        }
    }
}
