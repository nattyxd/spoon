package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import java.util.Comparator;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealItem;

public class SortMealsByAscendingReviewAlphabet implements Comparator<MealItem>
{
    @Override
    public int compare(MealItem mealItem, MealItem t1) {
        return mealItem.getDescription().compareToIgnoreCase(t1.getDescription());
    }
}