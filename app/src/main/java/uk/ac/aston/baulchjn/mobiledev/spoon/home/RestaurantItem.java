package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import java.io.Serializable;
import java.util.ArrayList;

public class RestaurantItem implements Serializable {
    private String name;
    private String desc;
    private String vicinity;
    private String restaurantType;
    private String telephoneNo;
    private String starRating;
    private String imageURL;
    private ArrayList<String> tags;
    private boolean visited;

    // restaurantName/Address/Type/Phone/Rating/ImageURL/bool visited

    public RestaurantItem(){
        this.tags = new ArrayList<>();
    }

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

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public ArrayList<String> getTags(){
        return this.tags;
    }

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
}
