package com.sahilda.todoapp;

public class Item {

    public long _id;
    public String name;

    public Item(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
