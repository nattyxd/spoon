package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.BookingsFragment;
import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.MealsFragment;
import uk.ac.aston.baulchjn.mobiledev.spoon.R;


public class MealRecyclerAdapter extends RecyclerView.Adapter<MealRecyclerAdapter.ViewHolder> {
    public Context context;
    public List<MealItem> mealList;
    //public RestaurantsFragment.RestaurantsFragmentInteraction listener;
    private final MealClickListener clickListener;
    private MealItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View view;
    private DatabaseHelper dbHelper;
    private RestaurantItem restaurant;
    private BookingItem booking;

    public MealRecyclerAdapter(List<MealItem> list, MealClickListener listener) {
        this.mealList = list;
        this.clickListener = listener;
    }

    public void setView(View view){
        this.view = view;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();

        dbHelper = new DatabaseHelper(context); // TODO: Make singleton.
    }

    @NonNull
    @Override
    public MealRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mealItem = mealList.get(position);

        restaurant = dbHelper.getRestaurantByHereID(holder.mealItem.getRestaurantHereID());
        booking = dbHelper.getBookingByBookingID(holder.mealItem.getBookingID());

        holder.mealTitle.setText(holder.mealItem.getTitle());

        String vicinity = restaurant.getVicinity().replace("<br/>", ", ");

        holder.mealSubtitle.setText(holder.mealItem.getDescription());
        if(booking != null){
            holder.date.setText("Associated with your booking at " + restaurant.getName() + " on " + booking.getDateOfBooking() + " at " + booking.getTimeOfBooking() + ".");
        } else {
            holder.date.setText("Associated with your deleted booking at " + restaurant.getName());
        }
        holder.bind(mealList.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = mealList.get(position);
        mRecentlyDeletedItemPosition = position;
        mealList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

        if(MealContent.mealItems.size() == 0){
            MealsFragment.noMealsText.setVisibility(View.VISIBLE);
            MealsFragment.noMealsArrow1.setVisibility(View.VISIBLE);
            MealsFragment.noMealsArrow2.setVisibility(View.VISIBLE);
        }
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(view, "Meal Deleted",
                Snackbar.LENGTH_LONG);
        dbHelper.deleteMeal(mRecentlyDeletedItem);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });

        if(BookingContent.bookingItems.size() == 0){
            BookingsFragment.noBookingsText.setVisibility(View.VISIBLE);
            BookingsFragment.noBookingsArrow.setVisibility(View.VISIBLE);
        }

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event){
                if(event == DISMISS_EVENT_ACTION){
                    // user triggered event, DON'T delete the entry
                    MealsFragment.noMealsText.setVisibility(View.GONE);
                    MealsFragment.noMealsArrow1.setVisibility(View.GONE);
                    MealsFragment.noMealsArrow2.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            dbHelper.addMeal(mRecentlyDeletedItem);

//                        final long result = dbHelper.addBooking(booking);
                        }
                    }).start();
                    return;
                }
            }
        });


        snackbar.show();
    }

    private void undoDelete() {
        mealList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RestaurantItem restaurantItem;
        private MealItem mealItem;
        private View mView;
        private TextView mealTitle;
        private TextView mealSubtitle;
        private TextView date;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);


            mealTitle = this.itemView.findViewById(R.id.restaurantName);
            mealSubtitle = this.itemView.findViewById(R.id.viscinity);
            date = this.itemView.findViewById(R.id.item_date);
        }

        public void bind(final MealItem item, final MealClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
