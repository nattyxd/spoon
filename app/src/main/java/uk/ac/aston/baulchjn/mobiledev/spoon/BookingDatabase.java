package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingDaoAccess;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;

@Database(entities = {BookingItem.class}, version = 1, exportSchema = false)
public abstract class BookingDatabase extends RoomDatabase {
    public abstract BookingDaoAccess daoAccess() ;
}
