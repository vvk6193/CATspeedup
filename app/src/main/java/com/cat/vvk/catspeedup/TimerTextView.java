package com.cat.vvk.catspeedup;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTextView extends TextView {

    private Timer timer;
    private Context ctx;
    private long startTime;
    private long delayTime=0;
    private long intervalTime=1;


    public TimerTextView(Context context) {
        super(context);
        this.ctx = context;
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ctx = context;
//        initAttrs(attrs);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
//        initAttrs(attrs);
    }

    void initAttrs(AttributeSet attr) {
        this.delayTime = attr.getAttributeIntValue(0,0);
        this.intervalTime = attr.getAttributeIntValue(1,1);
    }

    private void runThread() {
        ((Activity) ctx).runOnUiThread(new MyRunner());
    }

    public void start() {
        timer = new Timer();
        startTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runThread();
            }
        }, delayTime, intervalTime);

    }

    public void stop() {
        if(timer != null)
        timer.cancel();
    }

    public void reset(){

    }

    private class MyRunner  implements Runnable {

        @Override
        public void run() {
            long elapsed = System.currentTimeMillis() - startTime;
            long sec = 0;
            long mill = 0;

            if (elapsed >= 1000) {
                sec = elapsed / 1000;
                mill = elapsed % 1000;
            }
            else
                mill = elapsed;

            TimerTextView.this.setText(sec + "." + mill);
        }
    }
}


