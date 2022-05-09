package com.example.procrastinator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procrastinator.R;
import com.example.procrastinator.model.Task;

public class ViewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Log.d("ViewTaskActivity", "onCreate");
        Intent intent = getIntent();
        Task task = intent.getParcelableExtra("task");
        Log.d("ViewTaskActivity", task.toString());
    }
}