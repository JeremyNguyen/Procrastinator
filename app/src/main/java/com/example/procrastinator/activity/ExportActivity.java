package com.example.procrastinator.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.procrastinator.R;
import com.example.procrastinator.model.Task;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ExportActivity extends BaseActivity {

    FirebaseFirestore db;
    private final ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        EditText editText = findViewById(R.id.exportEditText);

        db = FirebaseFirestore.getInstance();

        DatabaseUtil.getTasksUpdateText(tasks, editText, db);
    }
}