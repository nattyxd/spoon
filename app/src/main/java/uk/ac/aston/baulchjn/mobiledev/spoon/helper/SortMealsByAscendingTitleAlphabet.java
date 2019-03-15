package uk.ac.aston.baulchjn.mobiledev.spoon.helper;

import java.util.Comparator;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

public class SortMealsByAscendingTitleAlphabet implements Comparator<MealItem>
{
    @Override
    public int compare(MealItem mealItem, MealItem t1) {
        return mealItem.getTitle().compareToIgnoreCase(t1.getTitle());
    }
}