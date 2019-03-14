package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class MealItem implements Serializable {
    @NonNull
    @PrimaryKey
    private String mealID;
    private String name;
    private String latitude;
    private String longitude;
    private String vicinity;
    private String restaurantType; // Not populated
    private String telephoneNo; // Not populated
    private String starRating; // Not populated
    private String imageURL; // Not populated
    private String tag1;
    private String tag2;
    private String tag3;
    private boolean visited;





    // restaurantName/Address/Type/Phone/Rating/ImageURL/bool visited

    public MealItem(){
//        this.tags = new ArrayList<>();
    }

    public String getHereID() { return hereID;}

    public void setHereID(String hereID){ this.hereID = hereID; }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag1(String tag1) { this.tag1 = tag1; }

    public void setTag2(String tag2) { this.tag2 = tag2; }

    public void setTag3(String tag3) { this.tag3 = tag3; }

    public String getTag1() { return tag1; }

    public String getTag2 () { return tag2; }

    public String getTag3 () { return tag3; }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(String restaurantType) {
        this.restaurantType = restaurantType;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }


}