package com.sahilda.nytimessearch.models;

import org.parceler.Parcel;

import java.util.HashSet;
import java.util.Set;

@Parcel
public class SearchQuery {

    public String mBeginDate;
    public SortType mSortType;
    public String mQuery;
    public int mPage;
    public Set<NewsDeskType> mNewsDeskTypes;

    public SearchQuery() {
        this.mQuery = null;
        this.mBeginDate = null;
        this.mSortType = null;
        this.mPage = 0;
        mNewsDeskTypes = new HashSet<>();
    }

    public SearchQuery(String query) {
        this.mQuery = query;
        this.mBeginDate = null;
        this.mSortType = null;
        this.mPage = 0;
        mNewsDeskTypes = new HashSet<>();
    }

    public String getBeginDate() {
        return mBeginDate;
    }

    public void setBeginDate(String beginDate) {
        this.mBeginDate = beginDate;
    }

    public SortType getSortType() {
        return mSortType;
    }

    public void setSortType(SortType SortType) {
        this.mSortType = SortType;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String Query) {
        this.mQuery = Query;
    }

    public Set<NewsDeskType> getNewsDeskType() {
        return mNewsDeskTypes;
    }

    public void setNewsDeskType(Set<NewsDeskType> newsDeskTypes) {
        this.mNewsDeskTypes = newsDeskTypes;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        this.mPage = page;
    }

}
