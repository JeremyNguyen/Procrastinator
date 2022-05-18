package com.example.procrastinator.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.procrastinator.R;
import com.example.procrastinator.adapter.TasksAdapter;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.listener.OnCategorySelectedListener;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListTasksActivity extends BaseActivity {

    FirebaseFirestore db;
    TasksAdapter tasksAdapter;
    private final List<Task> tasks = new ArrayList<>();
    private final String[] categories = AppConstant.CATEGORIES;
    OnCategorySelectedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        db = FirebaseFirestore.getInstance();

        ListView listView = findViewById(R.id.listView);
        tasksAdapter = new TasksAdapter(this, tasks);
        listView.setAdapter(tasksAdapter);

        Spinner categorySpinner = findViewById(R.id.category_spinner);
        listener = new OnCategorySelectedListener(tasks, tasksAdapter, categories, this);
        categorySpinner.setOnItemSelectedListener(listener);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseUtil.getTasksUpdateAdapter(tasks, listener, db);
    }
}