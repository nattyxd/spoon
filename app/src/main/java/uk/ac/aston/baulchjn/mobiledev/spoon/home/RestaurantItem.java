package uk.ac.aston.baulchjn.mobiledev.spoon.home;

import java.io.Serializable;

public class RestaurantItem implements Serializable {
    public String desc;
    public int id;
    // restaurantName/Address/Type/Phone/Rating/ImageURL/bool visited

    public RestaurantItem(){

    }

    public RestaurantItem(String desc, int id) {
        this.desc = desc;
        this.id = id;

        // TODO: declare all the shit in the constructor

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // TODO: GETTERS AND SETTERS FOR ALL THE EXTRA SHIT
}
