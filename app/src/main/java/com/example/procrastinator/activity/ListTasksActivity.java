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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListTasksActivity extends BaseActivity {

    FirebaseFirestore db;
    TasksAdapter tasksAdapter;
    private final List<Task> tasks = new ArrayList<>();
    private final String[] categories = AppConstant.CATEGORIES;
    OnCategorySelectedListener listener;
    SwitchMaterial switchMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        db = FirebaseFirestore.getInstance();

        switchMaterial = findViewById(R.id.switchOnlyMine);
        ListView listView = findViewById(R.id.listView);
        Spinner categorySpinner = findViewById(R.id.category_spinner);

        tasksAdapter = new TasksAdapter(this, tasks);

        listView.setAdapter(tasksAdapter);

        switchMaterial.setOnClickListener(v -> {
            listener.onItemSelected(null, null, categorySpinner.getSelectedItemPosition(), 0);
        });

        listener = new OnCategorySelectedListener(tasks, tasksAdapter, categories, switchMaterial, this);
        categorySpinner.setOnItemSelectedListener(listener);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseUtil.getTasksUpdateAdapter(tasks, listener, db);
    }
}