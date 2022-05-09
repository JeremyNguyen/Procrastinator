package com.example.procrastinator.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procrastinator.R;
import com.example.procrastinator.adapter.TasksAdapter;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ListTasksActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private final ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        db = FirebaseFirestore.getInstance();

        ListView listView = findViewById(R.id.listView);

        TasksAdapter tasksAdapter = new TasksAdapter(this, tasks);
        listView.setAdapter(tasksAdapter);

        DatabaseUtil.getTasks(tasks, tasksAdapter, db);
    }
}