package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantDaoAccess;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

@Database(entities = {RestaurantItem.class}, version = 2, exportSchema = false)
public abstract class RestaurantDatabase extends RoomDatabase {
    public abstract RestaurantDaoAccess daoAccess() ;
}
