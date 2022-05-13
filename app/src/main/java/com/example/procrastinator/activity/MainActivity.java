package com.example.procrastinator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;

import java.util.Optional;

public class MainActivity extends BaseActivity {

    String mode;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonListTask = findViewById(R.id.button_list_tasks);
        buttonListTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListTasksActivity.class);
            startActivity(intent);
        });

        init();
    }

    private void init(){
        Intent intent = getIntent();
        task = (Task) Optional.ofNullable(intent.getParcelableExtra("task")).orElse(new Task());
        mode = (String) Optional.ofNullable(intent.getParcelableExtra("mode")).orElse(AppConstant.MODE_EDIT);
    }
}