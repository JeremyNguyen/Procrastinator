package com.example.procrastinator.activity;

import static com.example.procrastinator.constant.AppConstant.MODE_CREATE;
import static com.example.procrastinator.constant.AppConstant.MODE_EDIT;
import static com.example.procrastinator.constant.AppConstant.MODE_REMIND;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        init();
        setTitle();
        setTaskTitle();

        setViewTasksButton();

        if (mode.equals(MODE_REMIND)) {
            View viewEdit = findViewById(R.id.view_edit);
            viewEdit.setVisibility(View.GONE);
            View viewRemind = findViewById(R.id.view_remind);
            viewRemind.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        Intent intent = getIntent();
        task = (Task) Optional.ofNullable(intent.getParcelableExtra(AppConstant.EXTRA_TASK)).orElse(new Task());
        mode = Optional.ofNullable(intent.getStringExtra(AppConstant.EXTRA_MODE)).orElse(MODE_CREATE);
    }

    private void setTitle() {
        TextView titleTextView = findViewById(R.id.mainTitleText);
        String title = null;
        switch (mode) {
            case MODE_CREATE:
                title = "Add new task";
                break;
            case MODE_EDIT:
                title = "Edit task";
                break;
            case MODE_REMIND:
                title = "Process task";
        }
        titleTextView.setText(title);
    }

    private void setTaskTitle() {
        if (MODE_EDIT.equals(mode)) {
            EditText editText = findViewById(R.id.mainTaskTitle);
            editText.setText(task.getTitle());
        }
    }

    public void setViewTasksButton() {
        Button buttonListTask = findViewById(R.id.button_list_tasks);
        buttonListTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListTasksActivity.class);
            startActivity(intent);
        });
    }
}