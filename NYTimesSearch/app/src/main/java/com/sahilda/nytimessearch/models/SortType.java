package com.sahilda.nytimessearch.models;

public enum SortType {

    NEWEST("newest"),
    OLDEST("oldest");

    private String mType;

    SortType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

}
