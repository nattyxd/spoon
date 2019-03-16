package uk.ac.aston.baulchjn.mobiledev.spoon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.helper.MealsSwipeToDeleteCallback;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByAscendingAlphabet;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByAscendingDate;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByAscendingDistance;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortBookingsByNumAttendees;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortMealsByAscendingReviewAlphabet;
import uk.ac.aston.baulchjn.mobiledev.spoon.helper.SortMealsByAscendingTitleAlphabet;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealClickListener;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealContent;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealRecyclerAdapter;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantContent;

public class MealsFragment extends Fragment {
    private View view;

    private RecyclerView recyclerView;

    public static MealRecyclerAdapter mAdapter;
    public static TextView noMealsText;
    public static ImageView noMealsArrow1;
    public static ImageView noMealsArrow2;

    private DatabaseHelper dbHelper;

    public MealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meals, container, false);
        recyclerView = view.findViewById(R.id.meals_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noMealsText = view.findViewById(R.id.noMealsInfoText);
        noMealsArrow1 = view.findViewById(R.id.noMealsArrow1);
        noMealsArrow2 = view.findViewById(R.id.noMealsArrow2);

        MealContent.mealItems = new ArrayList<>();
        mAdapter = new MealRecyclerAdapter(MealContent.mealItems, new MealClickListener() {
            @Override
            public void onItemClick(MealItem item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("meal", item);

                // TODO: Change this to meal details fragment
                FragmentStateContainer.getInstance().switchFragmentState(10, bundle);
            }
        });

        mAdapter.setView(view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MealsSwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        MealContent.populateMeals(getContext(), mAdapter);

        dbHelper = new DatabaseHelper(getContext());

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar bar = ((MainActivity)getActivity()).getSupportActionBar();
        bar.setTitle("Meals");
        setHasOptionsMenu(true);

//        BookingContent.jsonRequest(getActivity().getApplicationContext(), mAdapter); // prob need to replace with like BookingContent.getContent
        //rv_list = RestaurantContent.restaurantItems;
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.meals_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.action_sort:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Sort");
                dialogBuilder.setItems(R.array.en_restaurants_SortOptions, new DialogInterface.OnClickListener() {
                    //                dialogBuilder.setItems(R.array.en_meals_SortOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                // alphabetical ascending
                                Collections.sort(MealContent.mealItems, new SortMealsByAscendingTitleAlphabet());
                                break;
                            case 1:
                                // alphabetical descending
                                Collections.sort(MealContent.mealItems, Collections.reverseOrder(new SortMealsByAscendingTitleAlphabet()));
                                break;
                            case 2:
                                // description alphabetical ascending
                                Collections.sort(MealContent.mealItems, new SortMealsByAscendingReviewAlphabet());
                                break;
                            case 3:
                                // description alphabetical descending
                                Collections.sort(MealContent.mealItems, Collections.reverseOrder(new SortMealsByAscendingReviewAlphabet()));
                                break;
                            default:
                                Collections.sort(MealContent.mealItems, new SortMealsByAscendingTitleAlphabet());
                                break;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

                dialogBuilder.create().show();
        }
        return true;
    }
}
