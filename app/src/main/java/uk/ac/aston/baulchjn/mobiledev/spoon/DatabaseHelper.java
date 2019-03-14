package uk.ac.aston.baulchjn.mobiledev.spoon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.baulchjn.mobiledev.spoon.home.BookingItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.MealItem;
import uk.ac.aston.baulchjn.mobiledev.spoon.home.RestaurantItem;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DBName = "localdb";

    private static final String restaurantsTable = "Restaurants";
    private static final String restaurantHereID = "HereID";
    private static final String restaurantName = "Name";
    private static final String restaurantDesc = "Desc";
    private static final String restaurantLatitude = "Latitude";
    private static final String restaurantLongitude = "Longitude";
    private static final String restaurantVicinity = "Vicinity";
    private static final String restaurantType = "Type";
    private static final String restaurantTelephoneNo = "TelephoneNo";
    private static final String restaurantStarRating = "StarRating";
    private static final String restaurantImageURL = "ImageURL";
    private static final String restaurantTag1 = "Tag1";
    private static final String restaurantTag2 = "Tag2";
    private static final String restaurantTag3 = "Tag3";
    private static final String restaurantVisited = "Visited";

    private static final String bookingsTable = "Bookings";
    private static final String bookingID = "BookingID";
    private static final String bookingRestaurantID = "RestaurantID";
    private static final String bookingNumPeopleAttending = "NumPeopleAttending";
    private static final String bookingDateOfBooking = "DateOfBooking";
    private static final String bookingTimeOfBooking = "TimeOfBooking";

    private static final String mealsTable = "Meals";
    private static final String mealID = "MealID";
    private static final String mealRestaurantID = "RestaurantID"; // restaurant associated with meal
    private static final String mealBookingID = "BookingID"; // booking associated with meal
    private static final String mealDescription = "Description";
    private static final String mealStarRating = "StarRating";
    private static final String mealImageName = "ImagePath"; // path to image on local storage

    public DatabaseHelper(Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + restaurantsTable + " (");
        sqlBuilder.append(restaurantHereID + " TEXT PRIMARY KEY, ");
        sqlBuilder.append(restaurantName + " TEXT, ");
        sqlBuilder.append(restaurantDesc + " TEXT, ");
        sqlBuilder.append(restaurantLatitude + " TEXT, ");
        sqlBuilder.append(restaurantLongitude + " TEXT, ");
        sqlBuilder.append(restaurantVicinity + " TEXT, ");
        sqlBuilder.append(restaurantType + " TEXT, ");
        sqlBuilder.append(restaurantTelephoneNo + " TEXT, ");
        sqlBuilder.append(restaurantStarRating + " INTEGER, ");
        sqlBuilder.append(restaurantImageURL + " TEXT, ");
        sqlBuilder.append(restaurantTag1 + " TEXT, ");
        sqlBuilder.append(restaurantTag2 + " TEXT, ");
        sqlBuilder.append(restaurantTag3 + " TEXT, ");
        sqlBuilder.append(restaurantVisited + " INTEGER)");
        Log.i("spoonlogcat DBHELPER: ", sqlBuilder.toString());
        db.execSQL(sqlBuilder.toString());

        sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + bookingsTable + " (");
        sqlBuilder.append(bookingID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append(bookingRestaurantID + " TEXT, ");
        sqlBuilder.append(bookingNumPeopleAttending + " INTEGER, ");
        sqlBuilder.append(bookingDateOfBooking + " TEXT, ");
        sqlBuilder.append(bookingTimeOfBooking + " TEXT)");
        Log.i("spoonlogcat DBHELPER: ", sqlBuilder.toString());
        db.execSQL(sqlBuilder.toString());

        sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + mealsTable + " (");
        sqlBuilder.append(mealID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append(mealBookingID + " INTEGER, ");
        sqlBuilder.append(mealRestaurantID + " TEXT, ");
        sqlBuilder.append(mealDescription + " TEXT, ");
        sqlBuilder.append(mealStarRating + " TEXT, ");
        sqlBuilder.append(mealImageName + " TEXT)");
        Log.i("spoonlogcat DBHELPER: ", sqlBuilder.toString());
        db.execSQL(sqlBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + restaurantsTable);
        db.execSQL("DROP TABLE IF EXISTS " + bookingsTable);
        db.execSQL("DROP TABLE IF EXISTS " + mealsTable);
        onCreate(db);
    }

    public long addRestaurant(RestaurantItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(restaurantHereID, item.getHereID());
        contentValues.put(restaurantName, item.getName());
        contentValues.put(restaurantDesc, item.getDesc());
        contentValues.put(restaurantLatitude, item.getLatitude());
        contentValues.put(restaurantLongitude, item.getLongitude());
        contentValues.put(restaurantVicinity, item.getVicinity());
        contentValues.put(restaurantType, item.getRestaurantType());
        contentValues.put(restaurantTelephoneNo, item.getTelephoneNo());
        contentValues.put(restaurantStarRating, item.getStarRating());
        contentValues.put(restaurantImageURL, item.getImageURL());
        contentValues.put(restaurantTag1, item.getTag1());
        contentValues.put(restaurantTag2, item.getTag2());
        contentValues.put(restaurantTag3, item.getTag3());
        contentValues.put(restaurantVisited, item.isVisited());

        long result = db.insertOrThrow(restaurantsTable, null, contentValues);
        return result;
    }

    public long addBooking(BookingItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(bookingID, item.getBookingID());
        contentValues.put(bookingRestaurantID, item.getRestaurantID());
        contentValues.put(bookingNumPeopleAttending, item.getNumPeopleAttending());
        contentValues.put(bookingDateOfBooking, item.getDateOfBooking());
        contentValues.put(bookingTimeOfBooking, item.getTimeOfBooking());

        long result = db.insert(bookingsTable, null, contentValues);
        return result;
    }

    public int updateBooking(BookingItem booking){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(bookingDateOfBooking, booking.getDateOfBooking());
        values.put(bookingTimeOfBooking, booking.getTimeOfBooking());
        values.put(bookingNumPeopleAttending, booking.getNumPeopleAttending());

        return db.update(bookingsTable, values,bookingID + "=?", new String[]{ String.valueOf(booking.getBookingID()) });
    }

    public void deleteBooking(BookingItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(bookingsTable, bookingID + "=?", new String[]{ String.valueOf(item.getBookingID()) });
    }

    public RestaurantItem getRestaurantByHereID(String requestedHereID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + restaurantsTable + " WHERE " + restaurantHereID + "='" + requestedHereID + "'", null);
        RestaurantItem item = new RestaurantItem();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            item = new RestaurantItem();
            item.setHereID(cursor.getString(cursor.getColumnIndex(restaurantHereID)));
            item.setName(cursor.getString(cursor.getColumnIndex(restaurantName)));
            item.setDesc(cursor.getString(cursor.getColumnIndex(restaurantDesc)));
            item.setLatitude(cursor.getString(cursor.getColumnIndex(restaurantLatitude)));
            item.setLongitude(cursor.getString(cursor.getColumnIndex(restaurantLongitude)));
            item.setVicinity(cursor.getString(cursor.getColumnIndex(restaurantVicinity)));
            item.setRestaurantType(cursor.getString(cursor.getColumnIndex(restaurantType)));
            item.setTelephoneNo(cursor.getString(cursor.getColumnIndex(restaurantTelephoneNo)));
            item.setStarRating(Integer.toString(cursor.getInt(cursor.getColumnIndex(restaurantStarRating))));
            item.setImageURL(cursor.getString(cursor.getColumnIndex(restaurantImageURL)));
            item.setTag1(cursor.getString(cursor.getColumnIndex(restaurantTag1)));
            item.setTag2(cursor.getString(cursor.getColumnIndex(restaurantTag2)));
            item.setTag3(cursor.getString(cursor.getColumnIndex(restaurantTag3)));
            item.setVisited(cursor.getInt(cursor.getColumnIndex(restaurantVisited)) > 0);
        }
        return item;
    }

    public List<RestaurantItem> getAllRestaurantsAsList() {
        Cursor cursor = getAllRestaurants();
        ArrayList<RestaurantItem> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            RestaurantItem item = new RestaurantItem();
            item.setHereID(cursor.getString(cursor.getColumnIndex(restaurantHereID)));
            item.setName(cursor.getString(cursor.getColumnIndex(restaurantName)));
            item.setDesc(cursor.getString(cursor.getColumnIndex(restaurantDesc)));
            item.setLatitude(cursor.getString(cursor.getColumnIndex(restaurantLatitude)));
            item.setLongitude(cursor.getString(cursor.getColumnIndex(restaurantLongitude)));
            item.setVicinity(cursor.getString(cursor.getColumnIndex(restaurantVicinity)));
            item.setRestaurantType(cursor.getString(cursor.getColumnIndex(restaurantType)));
            item.setTelephoneNo(cursor.getString(cursor.getColumnIndex(restaurantTelephoneNo)));
            item.setStarRating(Integer.toString(cursor.getInt(cursor.getColumnIndex(restaurantStarRating))));
            item.setImageURL(cursor.getString(cursor.getColumnIndex(restaurantImageURL)));
            item.setTag1(cursor.getString(cursor.getColumnIndex(restaurantTag1)));
            item.setTag2(cursor.getString(cursor.getColumnIndex(restaurantTag2)));
            item.setTag3(cursor.getString(cursor.getColumnIndex(restaurantTag3)));
            item.setVisited(cursor.getInt(cursor.getColumnIndex(restaurantVisited)) > 0);
            items.add(item);
        }
        return items;
    }

    public void setRestaurantAsVisited(RestaurantItem restaurant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(restaurantVisited, 1);
        db.update(restaurantsTable, values, restaurantHereID + "=?", new String[]{ String.valueOf(restaurant.getHereID()) });
    }

    public void setRestaurantAsNotVisited(RestaurantItem restaurant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(restaurantVisited, 0);
        db.update(restaurantsTable, values, restaurantHereID + "=?", new String[]{ String.valueOf(restaurant.getHereID()) });
    }

    public List<BookingItem> getAllBookingsAsList() {
        Cursor cursor = getAllBookings();
        ArrayList<BookingItem> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            BookingItem item = new BookingItem();
            item.setBookingID(cursor.getInt(cursor.getColumnIndex(bookingID)));
            item.setRestaurantID(cursor.getString(cursor.getColumnIndex(bookingRestaurantID)));
            item.setNumPeopleAttending(cursor.getInt(cursor.getColumnIndex(bookingNumPeopleAttending)));
            item.setDateOfBooking(cursor.getString(cursor.getColumnIndex(bookingDateOfBooking)));
            item.setTimeOfBooking(cursor.getString(cursor.getColumnIndex(bookingTimeOfBooking)));
            items.add(item);
        }
        return items;
    }

    public long addMeal(MealItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mealBookingID, item.getBookingID());
        contentValues.put(mealRestaurantID, item.getRestaurantHereID());
        contentValues.put(mealDescription, item.getDescription());
        contentValues.put(mealImageName, item.getImageName());
        contentValues.put(mealStarRating, item.getStarRating());

        long result = db.insertOrThrow(mealsTable, null, contentValues);
        return result;
    }

    public void deleteMeal(MealItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(mealsTable, mealsTable + "=?", new String[]{ String.valueOf(item.getMealID()) });
    }

    public List<MealItem> getMealsByRestaurantID(String requestedHereID){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MealItem> items = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + mealsTable + " WHERE " + mealRestaurantID + "='" + requestedHereID + "'", null);
        MealItem item = new MealItem();
        while (cursor.moveToNext()) {
            cursor.moveToNext();
            item = new MealItem();
            item.setRestaurantHereID(cursor.getString(cursor.getColumnIndex(mealRestaurantID)));
            item.setBookingID(cursor.getInt(cursor.getColumnIndex(mealBookingID)));
            item.setMealID(cursor.getInt(cursor.getColumnIndex(mealID)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(mealDescription)));
            item.setImageName(cursor.getString(cursor.getColumnIndex(mealImageName)));
            item.setStarRating(cursor.getInt(cursor.getColumnIndex(mealStarRating)));
            items.add(item);
        }
        return items;
    }

    public MealItem getMealByMealID(int requestedMealID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + mealsTable + " WHERE " + mealID + "='" + requestedMealID + "'", null);
        MealItem item = new MealItem();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            item = new MealItem();
            item.setRestaurantHereID(cursor.getString(cursor.getColumnIndex(mealRestaurantID)));
            item.setBookingID(cursor.getInt(cursor.getColumnIndex(mealBookingID)));
            item.setMealID(cursor.getInt(cursor.getColumnIndex(mealID)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(mealDescription)));
            item.setImageName(cursor.getString(cursor.getColumnIndex(mealImageName)));
            item.setStarRating(cursor.getInt(cursor.getColumnIndex(mealStarRating)));
        }
        return item;
    }

    public MealItem getMealByBookingID(int requestedBookingID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + mealsTable + " WHERE " + mealBookingID + "='" + requestedBookingID + "'", null);
        MealItem item = new MealItem();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            item = new MealItem();
            item.setRestaurantHereID(cursor.getString(cursor.getColumnIndex(mealRestaurantID)));
            item.setBookingID(cursor.getInt(cursor.getColumnIndex(mealBookingID)));
            item.setMealID(cursor.getInt(cursor.getColumnIndex(mealID)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(mealDescription)));
            item.setImageName(cursor.getString(cursor.getColumnIndex(mealImageName)));
            item.setStarRating(cursor.getInt(cursor.getColumnIndex(mealStarRating)));
        }
        return item;
    }


    public List<MealItem> getAllMealsAsList() {
        Cursor cursor = getAllMeals();
        ArrayList<MealItem> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            MealItem item = new MealItem();
            item.setRestaurantHereID(cursor.getString(cursor.getColumnIndex(mealRestaurantID)));
            item.setBookingID(cursor.getInt(cursor.getColumnIndex(mealBookingID)));
            item.setMealID(cursor.getInt(cursor.getColumnIndex(mealID)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(mealDescription)));
            item.setImageName(cursor.getString(cursor.getColumnIndex(mealImageName)));
            item.setStarRating(cursor.getInt(cursor.getColumnIndex(mealStarRating)));
            items.add(item);
        }
        return items;
    }

    public Cursor getAllRestaurants() {
        return getAll(restaurantsTable);
    }

    public Cursor getAllBookings() {
        return getAll(bookingsTable);
    }

    public Cursor getAllMeals() {
        return getAll(mealsTable);
    }

    private Cursor getAll(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);
        return cursor;
    }
}
