package com.example.procrastinator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procrastinator.R;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        Button button1 = findViewById(R.id.button_debug1);
        Button button2 = findViewById(R.id.button_debug2);
        Button buttonListTask = findViewById(R.id.button_list_tasks);

        button1.setOnClickListener(this::debug1);
        button2.setOnClickListener(this::debug2);
        buttonListTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListTasksActivity.class);
            startActivity(intent);
        });
    }

    public void debug1(View view) {
        Toast.makeText(this, "debug1", Toast.LENGTH_SHORT).show();
        Task task = new Task();
        task.setContent("content");
        task.setTitle("title");
        task.setAuthor("author");
        task.setRemindWhen(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        task.setShared(true);
        DatabaseUtil.addTask(task, db);
    }

    public void debug2(View view) {
        Toast.makeText(this, "debug2", Toast.LENGTH_SHORT).show();
    }
}