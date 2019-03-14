package uk.ac.aston.baulchjn.mobiledev.spoon.home;


import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.view.View;

import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.BookingsFragment;
import uk.ac.aston.baulchjn.mobiledev.spoon.DatabaseHelper;

public class MealContent {
    public static List<MealItem> mealItems;
    private static Context context;
    private static MealRecyclerAdapter mealRecyclerAdapter;
    private static DatabaseHelper dbHelper;

    public static void populateMeals(Context applicationContext, MealRecyclerAdapter adapter) {
        context = applicationContext;
        mealRecyclerAdapter = adapter;
        dbHelper = new DatabaseHelper(context);

        setMealsToAllMeals();
    }

    private static void setMealsToAllMeals() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    mealRecyclerAdapter.mealList.clear();
                    mealRecyclerAdapter.mealList.addAll(dbHelper.getAllMealsAsList());
                    mealRecyclerAdapter.notifyDataSetChanged();
                    if(MealContent.mealItems.size() > 0){
                        BookingsFragment.noBookingsText.setVisibility(View.GONE);
                        BookingsFragment.noBookingsArrow.setVisibility(View.GONE);
                    }
                } catch(SQLiteConstraintException e){
                    // the restaurant already exists
                }
            }
        }).start();
    }
}