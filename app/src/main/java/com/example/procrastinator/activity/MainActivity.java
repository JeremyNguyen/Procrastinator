package com.example.procrastinator.activity;

import static com.example.procrastinator.constant.AppConstant.MODE_CREATE;
import static com.example.procrastinator.constant.AppConstant.MODE_EDIT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class MainActivity extends BaseActivity {

    FirebaseFirestore db;
    String mode = MODE_CREATE;
    Task task;
    Timestamp timestamp;
    private final String[] categories = AppConstant.CATEGORIES;
    EditText taskTitle, taskContent;
    Button deleteButton;
    Spinner categorySpinner;
    SwitchMaterial switchMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        setTitle();
        setCategorySpinner();
        setCalendarView();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        task = (Task) Optional.ofNullable(intent.getParcelableExtra(AppConstant.EXTRA_TASK)).orElse(new Task());
        mode = Optional.ofNullable(intent.getStringExtra(AppConstant.EXTRA_MODE)).orElse(MODE_CREATE);
        taskTitle = findViewById(R.id.mainTaskTitle);
        taskContent = findViewById(R.id.mainTaskContent);
        switchMaterial = findViewById(R.id.mainTaskShared);
        deleteButton = findViewById(R.id.mainButtonDelete);
        if (MODE_EDIT.equals(mode)) {
            taskTitle.setText(task.getTitle());
            taskContent.setText(task.getContent());
            switchMaterial.setChecked(task.isShared());
            categorySpinner.setSelection(Arrays.asList(categories).indexOf(task.getCategory()));
            String user = DatabaseUtil.getUser(this);
            if (AppConstant.USER_NOEMIE.equals(user)) {
                timestamp = task.getRemindNoemie();
            } else {
                timestamp = task.getRemindJeremy();
            }
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    private void setTitle() {
        TextView titleTextView = findViewById(R.id.mainTitleText);
        if (MODE_CREATE.equals(mode)) {
            titleTextView.setText(R.string.titleTaskAdd);
        } else if (MODE_EDIT.equals(mode)) {
            titleTextView.setText(R.string.titleTaskEdit);
        }
    }

    private void setCategorySpinner() {
        categorySpinner = findViewById(R.id.mainTaskCategory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }

    private void setCalendarView() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Date date = new Date(year - 1900, month, dayOfMonth);
            timestamp = new Timestamp(date);
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean showDatePicker = ((RadioButton) view).isChecked() && view.getId() == R.id.mainRadioDatePicker;
        RelativeLayout layoutButtons = findViewById(R.id.mainLayoutButtons);
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
        timestamp = DatabaseUtil.getTimestampForButton(view.getId());
        Toast.makeText(this, "Remind me in " + ((Button) view).getText(), Toast.LENGTH_SHORT).show();
    }

    public void onDeleteButtonClicked(View view) {
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setTitle("Confirm task deletion")
                .setPositiveButton("Confirm", (dialogBox, id) -> {
                    DatabaseUtil.deleteTask(task, db, this);
                })
                .setNegativeButton("Cancel", (dialogBox, id) -> dialogBox.cancel());
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void onConfirmButtonClicked(View view) {
        boolean hasErrors = false;
        String title = taskTitle.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(this, R.string.taskTitleError, Toast.LENGTH_LONG).show();
            hasErrors = true;
        } else {
            task.setTitle(title);
        }
        task.setContent(taskContent.getText().toString());
        task.setCategory(categorySpinner.getSelectedItem().toString());
        task.setShared(switchMaterial.isChecked());
        if (timestamp == null) {
            Toast.makeText(this, R.string.taskTimestampError, Toast.LENGTH_LONG).show();
            hasErrors = true;
        } else {
            if (task.isShared()) {
                task.setRemindJeremy(timestamp);
                task.setRemindNoemie(timestamp);
            } else {
                String user = DatabaseUtil.getUser(this);
                if (AppConstant.USER_NOEMIE.equals(user)) {
                    task.setRemindNoemie(timestamp);
                } else {
                    task.setRemindJeremy(timestamp);
                }
            }
        }
        if (!hasErrors) {
            if (MODE_CREATE.equals(mode)) {
                DatabaseUtil.addTask(task, db, this);
            } else {
                DatabaseUtil.updateTask(task, db, this);
            }
        }
    }
}