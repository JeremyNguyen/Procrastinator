package com.example.procrastinator;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "onReceive.if()", Toast.LENGTH_SHORT).show();
            Log.d( "Boot","onReceive.if()");
            SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("date", (new Date()).toString());
            editor.apply();
            Intent serviceIntent = new Intent(context, MyService.class);
            Toast.makeText(context, "onReceive.end", Toast.LENGTH_SHORT).show();
            Log.e("Boot","######################################################################");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            }
        } else {
            Toast.makeText(context, "onReceive.else()", Toast.LENGTH_SHORT).show();
        }
    }
}