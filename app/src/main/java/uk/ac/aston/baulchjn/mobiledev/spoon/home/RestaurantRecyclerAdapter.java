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


public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ViewHolder> {
    public List<RestaurantItem> restaurantList;

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
    public void onBindViewHolder(@NonNull RestaurantRecyclerAdapter.ViewHolder holder, int position) {
        int id = restaurantList.get(position).getId();
        String desc = restaurantList.get(position).getDesc();

        holder.desc.setText(desc);
        holder.image.setImageResource(id);


    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView desc;
        private ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            desc = mView.findViewById(R.id.item_desc);
            image = mView.findViewById(R.id.item_image);
        }
    }
}
