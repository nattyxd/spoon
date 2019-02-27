package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.R;


public class BookingRecyclerAdapter extends RecyclerView.Adapter<BookingRecyclerAdapter.ViewHolder> {
    public Context context;
    public List<BookingItem> bookingList;
    //public RestaurantsFragment.RestaurantsFragmentInteraction listener;
    private final BookingClickListener clickListener;
    private BookingItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View view;

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
    }

    @NonNull
    @Override
    public BookingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bookingItem = bookingList.get(position);
        holder.desc.setText(bookingList.get(position).getDateOfBooking());
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
        Snackbar snackbar = Snackbar.make(view, "temp text",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingRecyclerAdapter.this.undoDelete();
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
        private TextView desc;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = this.itemView.findViewById(R.id.item_desc);
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
