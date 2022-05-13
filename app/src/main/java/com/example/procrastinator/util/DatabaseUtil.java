package com.example.procrastinator.util;

import android.util.Log;
import android.widget.EditText;

import com.example.procrastinator.adapter.TasksAdapter;
import com.example.procrastinator.model.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class DatabaseUtil {

    private static final String COLLECTION_TASKS = "tasks";

    public static void addTask(Task task, FirebaseFirestore db) {
        db.collection(COLLECTION_TASKS)
                .add(task)
                .addOnSuccessListener(documentReference -> Log.d("DatabaseUtil", "Task added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error adding document", e));
    }

    public static void getTasksUpdateAdapter(List<Task> tasks, TasksAdapter tasksAdapter, FirebaseFirestore db) {
        db.collection(COLLECTION_TASKS).get()
                .addOnCompleteListener(task -> {
                    populateTasks(tasks, task);
                    tasksAdapter.notifyDataSetChanged();
                });
    }

    public static void getTasksUpdateText(List<Task> tasks, EditText editText, FirebaseFirestore db) {
        db.collection(COLLECTION_TASKS).get()
                .addOnCompleteListener(task -> {
                    populateTasks(tasks, task);
                    editText.setText(tasks.toString());
                });
    }

    private static void populateTasks(List<Task> tasks, com.google.android.gms.tasks.Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                Task t = new Task(document);
                tasks.add(t);
            }
        } else {
            Log.w("DatabaseUtil", "Error getting documents.", task.getException());
        }
    }
}
