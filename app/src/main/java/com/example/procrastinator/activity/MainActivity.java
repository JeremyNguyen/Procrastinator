package com.example.procrastinator.activity;

import static com.example.procrastinator.constant.AppConstant.MODE_CREATE;
import static com.example.procrastinator.constant.AppConstant.MODE_EDIT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Optional;

public class MainActivity extends BaseActivity {

    FirebaseFirestore db;
    String mode;
    Task task;
    Date date;
    private final String[] categories = AppConstant.CATEGORIES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        setTitle();
        setCategorySpinner();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        task = (Task) Optional.ofNullable(intent.getParcelableExtra(AppConstant.EXTRA_TASK)).orElse(new Task());
        mode = Optional.ofNullable(intent.getStringExtra(AppConstant.EXTRA_MODE)).orElse(MODE_CREATE);
        if (MODE_EDIT.equals(mode)) {
            EditText editText = findViewById(R.id.mainTaskTitle);
            editText.setText(task.getTitle());
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

    private  void setCategorySpinner() {
        Spinner categorySpinner = findViewById(R.id.mainTaskCategory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }
}