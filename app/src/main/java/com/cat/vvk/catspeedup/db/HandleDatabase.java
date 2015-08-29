package com.cat.vvk.catspeedup.db;

import android.content.Context;

/**
 * Created by vivek-pc on 8/8/2015.
 */
public class HandleDatabase {

    private static HandleDatabase sInstance;

    private Context context;

    public static synchronized HandleDatabase getInstance( Context ctx) {
             return new HandleDatabase(ctx);
    }
    public HandleDatabase(Context ctx) {
        this.context = ctx;
    }

    /*
 * Creating a todo
 */

}
