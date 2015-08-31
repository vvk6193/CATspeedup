package com.cat.vvk.catspeedup.presentor;

import android.app.Activity;

/**
 * Created by vivek-pc on 8/29/2015.
 */
public class TableLayoutHandler {

    private String operationType;
    private Activity activity;

    public TableLayoutHandler(Activity activity,String type) {
        this.activity = activity;
        this.operationType = type;
    }

}
