package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;


/**
 *
 */
public class MealDetailedFragment extends Fragment {
    private DatabaseHelper dbHelper;

    private RestaurantItem restaurant;
    private MealItem meal;
    private BookingItem booking;

    private Button deleteBtn;

    public MealDetailedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView mealTitleView = this.getView().findViewById(R.id.mealTitle);
        TextView mealDescriptionView = this.getView().findViewById(R.id.mealDescription);
        TextView mealAuxiliaryView = this.getView().findViewById(R.id.mealAuxiliary);

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            meal = (MealItem) bundle.getSerializable("meal");

            try{
                restaurant = dbHelper.getRestaurantByHereID(meal.getRestaurantHereID());
                booking = dbHelper.getBookingByBookingID(meal.getBookingID());
            } catch(Exception e){
                // TODO: REALLY BAD BUG HERE WHEN SHARING MEAL, CRASHES APP
                Log.e("spoonlogcat: ", "FIX THIS BUG");
            }


            if(meal == null || restaurant == null){
                return;
            }


            // Set title
            mealTitleView.setText(meal.getTitle());
            mealDescriptionView.setText(meal.getDescription());

            if(booking != null){
                mealAuxiliaryView.setText("Associated with your booking at " + restaurant.getName() + " on " + booking.getDateOfBooking() + " at " + booking.getTimeOfBooking() + ".");
            } else {
                mealAuxiliaryView.setText("Associated with your deleted booking at " + restaurant.getName());
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            meal = (MealItem) savedInstanceState.getSerializable("meal");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            restaurant = (RestaurantItem) getArguments().getSerializable("restaurant");
            booking = (BookingItem) getArguments().getSerializable("booking");
            meal = (MealItem) getArguments().getSerializable("meal");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_detailed, container, false);

        Button editBtn = view.findViewById(R.id.editMealBtn);
        Button shareBtn = view.findViewById(R.id.shareMealBtn);
        deleteBtn = view.findViewById(R.id.deleteMealBtn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "";
                if(booking != null){
                    shareBody = getResources().getString(R.string.en_shareMeal_shareString_Body_IncludingBooking, restaurant.getName(), booking.getDateOfBooking(), booking.getTimeOfBooking());
                } else {
                    shareBody = getResources().getString(R.string.en_shareMeal_shareString_Body_DeletedBooking, restaurant.getName());
                }
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.en_shareMeal_shareString_Intent_Title)));
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurant", restaurant);
                bundle.putSerializable("booking", booking);
                bundle.putSerializable("meal", meal);
                FragmentStateContainer.getInstance().switchFragmentState(9, bundle);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dbHelper.deleteMeal(meal);
                                FragmentStateContainer.getInstance().switchFragmentState(3, null);
                                MealContent.mealItems.remove(meal);
                                MealsFragment.mAdapter.notifyDataSetChanged();
                                if(MealContent.mealItems.size() == 0){
                                    MealsFragment.noMealsText.setVisibility(View.VISIBLE);
                                    MealsFragment.noMealsArrow1.setVisibility(View.VISIBLE);
                                    MealsFragment.noMealsArrow2.setVisibility(View.VISIBLE);
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure? This action cannot be undone").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return view;
    }
}
