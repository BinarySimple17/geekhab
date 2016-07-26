package ru.binarysimple.geekhab;

import java.util.ArrayList;

public class UserList {
    private String total_count;
    public ArrayList<User> items;

    public String getTotal_count() {
        return total_count;
    }

    public ArrayList<User> getItems() {
        return items;
    }
}