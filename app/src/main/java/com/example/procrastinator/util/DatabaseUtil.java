package com.example.procrastinator.util;

import android.util.Log;

import com.example.procrastinator.adapter.TasksAdapter;
import com.example.procrastinator.model.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Objects;

public class DatabaseUtil {

    public static void addTask(Task task, FirebaseFirestore db) {
        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> Log.d("DatabaseUtil", "Task added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error adding document", e));
    }

    public static void getTasks(List<Task> tasks, TasksAdapter tasksAdapter, FirebaseFirestore db) {
        db.collection("tasks").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Task t = new Task(document);
                            tasks.add(t);
                            Log.d("DatabaseUtil", t.toString());
                        }
                        tasksAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("DatabaseUtil", "Error getting documents.", task.getException());
                    }
                });
    }
}
