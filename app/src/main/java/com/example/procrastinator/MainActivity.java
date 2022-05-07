package com.example.procrastinator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button_debug1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                debug1(v);
            }
        });
        Button button2 = (Button) findViewById(R.id.button_debug2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                debug2(v);
            }
        });
    }

    public void debug1(View view) {
        Toast.makeText(this, "debug1", Toast.LENGTH_SHORT).show();
        Context applicationContext = getApplicationContext();
        Intent serviceIntent = new Intent(applicationContext, MyService.class);
        applicationContext.startService(serviceIntent);
    }

    public void debug2(View view) {
        Toast.makeText(this, "debug2", Toast.LENGTH_SHORT).show();
        Context applicationContext = getApplicationContext();
        Intent serviceIntent = new Intent(applicationContext, MyService.class);
        applicationContext.stopService(serviceIntent);
    }
}