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

        Button button = (Button) findViewById(R.id.button_debug);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                debug(v);
            }
        });
    }

    public void debug(View view) {
        Toast.makeText(this, "debug", Toast.LENGTH_SHORT).show();
    }
}