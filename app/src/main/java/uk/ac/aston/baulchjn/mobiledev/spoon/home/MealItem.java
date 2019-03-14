package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class MealItem implements Serializable {
    @NonNull
    @PrimaryKey
    // Fields
    private Integer mealID;
    private String restaurantHereID;
    private int bookingID;
    private String description;
    private int starRating;
    private String imageName;

    // Concrete
    private Bitmap processedImage;

    public MealItem(){

    }

    @NonNull
    public Integer getMealID() {
        return mealID;
    }

    public void setMealID(@NonNull Integer mealID) {
        this.mealID = mealID;
    }

    public String getRestaurantHereID() {
        return restaurantHereID;
    }

    public void setRestaurantHereID(String restaurantHereID) {
        this.restaurantHereID = restaurantHereID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void parseBitmap(){

    }
}