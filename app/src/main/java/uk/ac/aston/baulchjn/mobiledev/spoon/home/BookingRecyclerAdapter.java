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

import com.here.android.mpa.common.GeoCoordinate;

import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.BookingsFragment;
import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;
import uk.ac.aston.baulchjn.mobiledev.spoon.R;
import uk.ac.aston.baulchjn.mobiledev.spoon.RestaurantsFragment;


public class BookingRecyclerAdapter extends RecyclerView.Adapter<BookingRecyclerAdapter.ViewHolder> {
    public Context context;
    public List<BookingItem> bookingList;
    //public RestaurantsFragment.RestaurantsFragmentInteraction listener;
    private final BookingClickListener clickListener;
    private BookingItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View view;
    private DatabaseHelper dbHelper;
    private RestaurantItem restaurant;

    public BookingRecyclerAdapter(List<BookingItem> list, BookingClickListener listener) {
        this.bookingList = list;
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
    public BookingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bookingItem = bookingList.get(position);

        restaurant = dbHelper.getRestaurantByHereID(holder.bookingItem.getRestaurantID());
        holder.restaurantName.setText(restaurant.getName());

        String vicinity = restaurant.getVicinity().replace("<br/>", ", ");

        holder.restaurantViscinity.setText(vicinity);
        holder.date.setText(bookingList.get(position).getDateOfBooking());
        holder.bind(bookingList.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = bookingList.get(position);
        mRecentlyDeletedItemPosition = position;
        bookingList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(view, "Booking Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingRecyclerAdapter.this.undoDelete();
            }
        });

        if(BookingContent.bookingItems.size() == 0){
            BookingsFragment.noBookingsText.setVisibility(View.VISIBLE);
            BookingsFragment.noBookingsArrow.setVisibility(View.VISIBLE);
        }

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event){
                if(event == 1){
                    // user triggered event, DON'T delete the entry
                    BookingsFragment.noBookingsText.setVisibility(View.GONE);
                    BookingsFragment.noBookingsArrow.setVisibility(View.GONE);
                    return;
                }
                // safe to remove the booking from the db
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        dbHelper.deleteBooking(mRecentlyDeletedItem);

//                        final long result = dbHelper.addBooking(booking);
                    }
                }).start();
            }
        });


        snackbar.show();
    }

    private void undoDelete() {
        bookingList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BookingItem bookingItem;
        private View mView;
        private TextView restaurantName;
        private TextView restaurantViscinity;
        private TextView date;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            restaurantName = this.itemView.findViewById(R.id.restaurantName);
            date = this.itemView.findViewById(R.id.item_date);
            restaurantViscinity = this.itemView.findViewById(R.id.viscinity);
        }

        public void bind(final BookingItem item, final BookingClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
