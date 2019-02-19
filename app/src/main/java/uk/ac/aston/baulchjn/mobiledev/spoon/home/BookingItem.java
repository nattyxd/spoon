package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class BookingItem implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Integer bookingID;
    private String restaurantID; // the hereID of the BookingItem that should exist in the DB
    private int numPeopleAttending;
    private String dateOfBooking; // in format dd-MM-YYYY
    private String timeOfBooking;


    public BookingItem(){
        // Required (yet empty) constructor
    }

    @NonNull
    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(@NonNull String bookingID) {
        this.bookingID = bookingID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getNumPeopleAttending() {
        return numPeopleAttending;
    }

    public void setNumPeopleAttending(int numPeopleAttending) {
        this.numPeopleAttending = numPeopleAttending;
    }

    public String getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(String dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public String getTimeOfBooking() {
        return timeOfBooking;
    }

    public void setTimeOfBooking(String timeOfBooking) {
        this.timeOfBooking = timeOfBooking;
    }
}