package com.example.procrastinator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        if (mode.equals(AppConstant.MODE_REMIND)) {
            View viewEdit = findViewById(R.id.view_edit);
            viewEdit.setVisibility(View.GONE);
            View viewRemind = findViewById(R.id.view_remind);
            viewRemind.setVisibility(View.VISIBLE);
        }
    }

    private void init(){
        Intent intent = getIntent();
        task = (Task) Optional.ofNullable(intent.getParcelableExtra(AppConstant.EXTRA_TASK)).orElse(new Task());
        mode = Optional.ofNullable(intent.getStringExtra(AppConstant.EXTRA_MODE)).orElse(AppConstant.MODE_EDIT);
    }
}