package ru.binarysimple.geekhab;

import java.util.ArrayList;

public class UserList {
    public ArrayList<User> items;
    private String total_count;

    public String getTotal_count() {
        return total_count;
    }

    public ArrayList<User> getItems() {
        return items;
    }
}