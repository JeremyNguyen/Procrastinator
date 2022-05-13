package com.example.procrastinator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;

    String mode = AppConstant.MODE_EDIT;
    Task task = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        Button buttonListTask = findViewById(R.id.button_list_tasks);
        buttonListTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListTasksActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        if (intent != null) {
            Log.d("intent", intent.getPackage());
            task = intent.getParcelableExtra("task");
            mode = intent.getParcelableExtra("mode");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.debug1 == item.getItemId()) {
            debug1();
        }
        if (R.id.debug2 == item.getItemId()) {
            debug2();
        }
        return (super.onOptionsItemSelected(item));
    }

    public void debug1() {
        Toast.makeText(this, "debug1", Toast.LENGTH_SHORT).show();
        Task task = new Task();
        task.setContent("content");
        task.setTitle("title");
        task.setAuthor("author");
        task.setRemindWhen(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        task.setShared(true);
        DatabaseUtil.addTask(task, db);
    }

    public void debug2() {
        Toast.makeText(this, "debug2", Toast.LENGTH_SHORT).show();
    }
}