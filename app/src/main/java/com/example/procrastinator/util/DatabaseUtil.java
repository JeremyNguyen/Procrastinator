package com.example.procrastinator.util;

import android.util.Log;

import com.example.procrastinator.model.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseUtil {

    public static void addTask(Task task, FirebaseFirestore db) {
        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> Log.d("DatabaseUtil", "Task added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error adding document", e));
    }
}
