package com.example.procrastinator;

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

        Button button1 = findViewById(R.id.button_debug1);
        button1.setOnClickListener(this::debug1);
        Button button2 = findViewById(R.id.button_debug2);
        button2.setOnClickListener(this::debug2);
    }

    public void debug1(View view) {
        Toast.makeText(this, "debug1", Toast.LENGTH_SHORT).show();
        WatchDogWorker.enqueueSelf(getApplicationContext());
    }

    public void debug2(View view) {
        Toast.makeText(this, "debug2", Toast.LENGTH_SHORT).show();
        NotificationUtil.sendNotification("title", "message", getApplicationContext());
    }
}