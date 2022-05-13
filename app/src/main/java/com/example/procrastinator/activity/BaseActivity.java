package com.example.procrastinator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procrastinator.R;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseActivity extends AppCompatActivity {

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
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
        if (R.id.export == item.getItemId()) {
            Intent intent = new Intent(this, ExportActivity.class);
            startActivity(intent);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void debug1() {
        Toast.makeText(this, "debug1", Toast.LENGTH_SHORT).show();
        Task task = new Task();
        task.setContent("content");
        task.setTitle("title");
        task.setAuthor("author");
        task.setCategory("Shopping");
        task.setRemindWhen(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        task.setShared(true);
        DatabaseUtil.addTask(task, db);
    }

    public void debug2() {
        Toast.makeText(this, "debug2", Toast.LENGTH_SHORT).show();
    }
}