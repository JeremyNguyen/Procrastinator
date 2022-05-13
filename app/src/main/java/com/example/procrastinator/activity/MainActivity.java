package com.example.procrastinator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;

public class MainActivity extends BaseActivity {

    String mode = AppConstant.MODE_EDIT;
    Task task = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonListTask = findViewById(R.id.button_list_tasks);
        buttonListTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListTasksActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        if (intent != null) {
            task = intent.getParcelableExtra("task");
            mode = intent.getParcelableExtra("mode");
        }
    }
}