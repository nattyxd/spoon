package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BookingDaoAccess {

    @Insert
    void insertSingleBookingItem(BookingItem bookingItem);
    @Insert
    void insertMultipleBookingItems (List<BookingItem> bookingItemList);
    @Query("SELECT * FROM BookingItem WHERE bookingID = :bookingID")
    BookingItem fetchOneBookingbyName(String bookingID);
    @Query("SELECT * FROM BookingItem WHERE bookingID = :bookingID")
    BookingItem fetchOneBookingbyID(String bookingID);
    @Query("SELECT * FROM BookingItem")
    List<BookingItem> fetchAllBookings();

    @Update
    void updateBooking(BookingItem bookingItem);
    @Delete
    void deleteBookingItem(BookingItem bookingItem);
}
