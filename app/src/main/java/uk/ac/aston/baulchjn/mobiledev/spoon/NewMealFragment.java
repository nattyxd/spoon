package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 *
 */
public class NewMealFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RESTAURANT = "restaurant";

    private ArrayList<String> tags;
    private RestaurantItem restaurant;
    private BookingItem booking;

    // Text views
    private TextView mealTitleText;
    private TextView mealReviewText;
    private TextView mealImageText;

    // Fragment Controls
    private EditText mealTitle;
    private EditText mealReview;
    private ImageView mealImage;

    private DatabaseHelper dbHelper;

    private Thread makeMealThread;

    private View view;

    public NewMealFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = FragmentStateContainer.getInstance().activeBundle;
        if (bundle != null) {
            // TODO: Restaurant null check maybe necessary here if we support creation of booking through non here restaurant means
            restaurant = (RestaurantItem) bundle.getSerializable("restaurant");
            booking = (BookingItem) bundle.getSerializable("booking");

            if(restaurant == null || booking == null){
                return;
            }

            Log.i("spoonlogcat", "Wooooo! We're gonna make a new booking" + restaurant.toString());

            TextView subtitle = view.findViewById(R.id.subtitle);
            subtitle.setText(getString(R.string.en_newMealsFragment_SubTitle, restaurant.getName(), booking.getDateOfBooking(), booking.getTimeOfBooking()));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurant = (RestaurantItem) getArguments().getSerializable(ARG_RESTAURANT);

        }

        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_meal, container, false);

        mealTitle = view.findViewById(R.id.mealTitle);
        mealReview = view.findViewById(R.id.mealDescription);
        mealImage = view.findViewById(R.id.mealImageView);

        mealTitleText = view.findViewById(R.id.title);
        mealReviewText = view.findViewById(R.id.reviewTextView);

        Button addMealBtn = view.findViewById(R.id.createMealBtn);

        mealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                file = Uri.fromFile(getOutputMediaFile());
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                startActivityForResult(intent, 100);
            }
        });

        addMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take a big breath. We're going to make a meal (not of this)..
                if(makeMealThread != null && makeMealThread.isAlive()){
                    // prevent spam clicks
                    return;
                }

                makeMealThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        // Hide the keyboard so the snackbar gets shown
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        // Form validation
                        if (mealTitle.getText().toString() == null || mealTitle.getText().toString().isEmpty()) {
                            mealTitleText.setTextColor(getResources().getColor(R.color.red));
                            return;
                        } else {
                            mealTitleText.setTextColor((getResources().getColor(R.color.textViewDefault)));
                        }

                        if (mealReview.getText().toString() == null || mealReview.getText().toString().isEmpty()) {
                            mealReviewText.setTextColor(getResources().getColor(R.color.red));
                            return;
                        } else {
                            mealReviewText.setTextColor(getResources().getColor(R.color.textViewDefault));
                        }

                        final MealItem meal = new MealItem();
                        meal.setRestaurantHereID(restaurant.getHereID());
                        meal.setBookingID(booking.getBookingID());
                        meal.setStarRating(-1); // not implemented yet
                        meal.setTitle(mealTitle.getText().toString());
                        meal.setDescription(mealReview.getText().toString());
//                        meal.setImageName(Integer.parseInt(numAttendeesEditor.getText().toString()));
                        final long result = dbHelper.addMeal(meal);
                        meal.setMealID((int) (long) result); // should be safe as meals won't exceed 2 billion..

                        MealContent.mealItems.add(meal);
                        MealsFragment.noMealsText.setVisibility(View.GONE);
                        MealsFragment.noMealsArrow1.setVisibility(View.GONE);
                        MealsFragment.noMealsArrow2.setVisibility(View.GONE);
                        MealsFragment.mAdapter.notifyDataSetChanged();

                        Snackbar snackbar = Snackbar
                                .make(view, "Meal Created Successfully!", Snackbar.LENGTH_LONG)
                                .setAction("View Meal Now", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("meal", meal);
                                        MainActivity.navigation.getMenu().findItem(R.id.navigation_meals).setChecked(true);
                                        FragmentStateContainer.getInstance().switchFragmentState(10, bundle);

                                    }
                                });

                        snackbar.show();
                    }
                });

                makeMealThread.start();
            }
        });

        return view;
    }
}
