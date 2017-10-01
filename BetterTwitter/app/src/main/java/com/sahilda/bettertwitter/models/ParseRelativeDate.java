package com.sahilda.bettertwitter.models;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ParseRelativeDate {

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            relativeDate = relativeDate.replace(" second ago", "s");
            relativeDate = relativeDate.replace(" seconds ago", "s");
            relativeDate = relativeDate.replace(" minute ago", "m");
            relativeDate = relativeDate.replace(" minutes ago", "m");
            relativeDate = relativeDate.replace(" hour ago", "h");
            relativeDate = relativeDate.replace(" hours ago", "h");
            relativeDate = relativeDate.replace("yesterday", "1 day ago");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
