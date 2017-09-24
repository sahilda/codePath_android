package com.sahilda.nytimessearch.models;

public enum NewsDeskType {

    ARTS("Arts"),
    FASHION_AND_STYLE("Fashion & Style"),
    SPORTS("Sports");

    private String mType;

    NewsDeskType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

}
