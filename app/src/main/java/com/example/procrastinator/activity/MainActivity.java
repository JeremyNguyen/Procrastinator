package com.example.procrastinator.activity;

import static com.example.procrastinator.constant.AppConstant.MODE_CREATE;
import static com.example.procrastinator.constant.AppConstant.MODE_EDIT;
import static com.example.procrastinator.constant.AppConstant.MODE_REMIND;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;

import java.util.Optional;

public class MainActivity extends BaseActivity {

    String mode;
    Task task;
    boolean showDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setTitle();
        setTaskTitle();

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
        } else {
            TextView textView = findViewById(R.id.mainTaskTitleText);
            textView.setText(task.getTitle());
        }
    }

    public void onRadioButtonClicked(View view) {
        showDatePicker = ((RadioButton) view).isChecked() && view.getId() == R.id.mainRadioDatePicker;
        LinearLayout layoutButtons = findViewById(R.id.mainLayoutButtons);
        LinearLayout layoutDatePicker = findViewById(R.id.mainLayoutDatePicker);
        if (showDatePicker) {
            layoutButtons.setVisibility(View.GONE);
            layoutDatePicker.setVisibility(View.VISIBLE);
        } else {
            layoutButtons.setVisibility(View.VISIBLE);
            layoutDatePicker.setVisibility(View.GONE);
        }
    }

    public void onRemindButtonClicked(View view) {
        Button button = (Button) view;
        Toast.makeText(getApplicationContext(), button.getText(), Toast.LENGTH_SHORT).show();
    }

    public void onEditButtonClicked(View view) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstant.EXTRA_TASK, task);
        intent.putExtra(AppConstant.EXTRA_MODE, MODE_EDIT);
        context.startActivity(intent);
    }
}