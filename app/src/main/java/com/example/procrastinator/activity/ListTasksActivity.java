package com.example.procrastinator.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.procrastinator.R;
import com.example.procrastinator.adapter.TasksAdapter;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListTasksActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db;
    TasksAdapter tasksAdapter;
    private final List<Task> tasks = new ArrayList<>();
    private final String [] categories = AppConstant.CATEGORIES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        db = FirebaseFirestore.getInstance();

        Spinner categorySpinner = findViewById(R.id.category_spinner);
        categorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);

        ListView listView = findViewById(R.id.listView);
        tasksAdapter = new TasksAdapter(this, tasks);
        listView.setAdapter(tasksAdapter);
        DatabaseUtil.getTasksUpdateAdapter(tasks, tasksAdapter, db);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        List<Task> filteredTasks = tasks.stream().filter(task -> categories[position].equals(task.getCategory())).collect(Collectors.toList());
        tasksAdapter.setItems(filteredTasks);
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}