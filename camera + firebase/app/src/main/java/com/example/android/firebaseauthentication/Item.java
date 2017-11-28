package com.example.android.firebaseauthentication;

/**
 * Created by taylan on 16.11.2017.
 */
public class Item {

    private String menuName;
    private int menuImage;


    public Item(String menuName, int menuImage) {
        this.menuName = menuName;
        this.menuImage = menuImage;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getMenuImage() {
        return menuImage;
    }
}