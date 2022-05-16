package com.example.procrastinator.activity;

import static com.example.procrastinator.constant.AppConstant.MODE_CREATE;
import static com.example.procrastinator.constant.AppConstant.MODE_EDIT;
import static com.example.procrastinator.constant.AppConstant.MODE_REMIND;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Optional;

public class MainActivity extends BaseActivity {

    FirebaseFirestore db;
    String mode;
    Task task;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        init();
        setTitle();
        setTaskTitle();
        setCalendarView();

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

    private void setCalendarView() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            date = new Date(year - 1900, month, dayOfMonth);
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean showDatePicker = ((RadioButton) view).isChecked() && view.getId() == R.id.mainRadioDatePicker;
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
        Timestamp timestamp;
        if (view.getId() == R.id.mainDatePickerConfirm) {
            timestamp = getFormattedTimestamp(date.getTime());
        } else {
            timestamp = getTimestampForButton(view.getId());
        }
        task.setRemindWhen(timestamp);
        DatabaseUtil.updateTask(task, db, this);
    }

    public void onEditButtonClicked(View view) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstant.EXTRA_TASK, task);
        intent.putExtra(AppConstant.EXTRA_MODE, MODE_EDIT);
        context.startActivity(intent);
        finish();
    }

    private Timestamp getTimestampForButton(int id) {
        long seconds = Timestamp.now().getSeconds();
        long deltaInDays = 0;
        switch(id) {
            case R.id.taskButtonOneDay:
                deltaInDays = 1;
                break;
            case R.id.taskButtonThreeDays:
                deltaInDays = 3;
                break;
            case R.id.taskButtonOneWeek:
                deltaInDays = 7;
                break;
            case R.id.taskButtonOneMonth:
                deltaInDays = 30;
                break;
            case R.id.taskButtonSixMonths:
                deltaInDays = 180;
                break;
            case R.id.taskButtonOneYear:
                deltaInDays = 365;
                break;
        }
        long millis = (seconds + (deltaInDays * 24 * 3600)) * 1000;
        return getFormattedTimestamp(millis);
    }

    private Timestamp getFormattedTimestamp(long millis) {
        Date date = new Date(millis);
        date.setHours(8);
        date.setMinutes(0);
        date.setSeconds(0);
        return new Timestamp(date);
    }
}