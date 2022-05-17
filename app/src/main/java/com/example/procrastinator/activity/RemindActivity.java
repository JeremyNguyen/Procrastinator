package com.example.procrastinator.activity;

import static com.example.procrastinator.constant.AppConstant.MODE_EDIT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class RemindActivity extends BaseActivity {

    FirebaseFirestore db;
    Task task;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        db = FirebaseFirestore.getInstance();

        TextView textView = findViewById(R.id.mainTaskTitleText);
        textView.setText(task.getTitle());
        setCalendarView();
    }

    private void setCalendarView() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            date = new Date(year - 1900, month, dayOfMonth);
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
        Timestamp timestamp;
        if (view.getId() == R.id.mainDatePickerConfirm) {
            timestamp = DatabaseUtil.getFormattedTimestamp(date.getTime());
        } else {
            timestamp = DatabaseUtil.getTimestampForButton(view.getId());
        }
        DatabaseUtil.updateTaskRemind(timestamp, task.getId(), db, this);
    }

    public void onEditButtonClicked(View view) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstant.EXTRA_TASK, task);
        intent.putExtra(AppConstant.EXTRA_MODE, MODE_EDIT);
        context.startActivity(intent);
        finish();
    }
}