package com.example.procrastinator;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

    Handler handler;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "onBind", Toast.LENGTH_SHORT).show();
        periodicTask();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        periodicTask();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.handler.removeCallbacksAndMessages(null);
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    private void periodicTask() {
        this.handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "periodicTask", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 10000);
            }
        }, 10000);
    }
}