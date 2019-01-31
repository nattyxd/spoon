package uk.ac.aston.baulchjn.mobiledev.spoon.home;

public class RestaurantItem {
    public String desc;
    public int id;

    public RestaurantItem(){

    }

    public RestaurantItem(String desc, int id) {
        this.desc = desc;
        this.id = id;
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
}
