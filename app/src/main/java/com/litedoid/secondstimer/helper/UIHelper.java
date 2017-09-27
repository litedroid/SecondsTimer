package com.litedoid.secondstimer.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIHelper
{
    private static final String TAG = UIHelper.class.getSimpleName();

    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View cf = activity.getCurrentFocus();

        if (cf != null)
        {
            Log.d(TAG, "hideKeyboard hiding");
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
