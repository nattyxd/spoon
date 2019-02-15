package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RestaurantDaoAccess {

    @Insert
    void insertSingleRestaurantItem(RestaurantItem restaurantItem);
    @Insert
    void insertMultipleRestaurantItems (List<RestaurantItem> restaurantItemList);
    @Query("SELECT * FROM RestaurantItem WHERE name = :name")
    RestaurantItem fetchOneRestaurantbyName(String name);
    @Query("SELECT * FROM RestaurantItem WHERE hereID = :hereID")
    RestaurantItem fetchOneRestaurantbyHereID(String hereID);
    @Update
    void updateRestaurant(RestaurantItem restaurantItem);
    @Delete
    void deleteRestaurantItem(RestaurantItem restaurantItem);
}
