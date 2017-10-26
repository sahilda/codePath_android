package com.codepath.android.themeswitcher.app;

import android.app.Activity;
import android.content.Intent;

public class Utils {

    private static int sTheme;

    public final static int THEME_MATERIAL_LIGHT = 0;
    public final static int THEME_GIACT_GOLDFISH = 1;

    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
                return;
            case THEME_MATERIAL_LIGHT:
                activity.setTheme(R.style.Theme_Material_Light);
                return;
            case THEME_GIACT_GOLDFISH:
                activity.setTheme(R.style.Theme_Giact_Goldfish);
                return;
        }
    }

}
