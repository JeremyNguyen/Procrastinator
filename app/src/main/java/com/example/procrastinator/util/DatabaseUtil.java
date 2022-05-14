package com.example.procrastinator.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.listener.OnTaskSelectedListener;
import com.example.procrastinator.model.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class DatabaseUtil {

    private static final String COLLECTION_TASKS = "tasks";
    private static final String TASKS_SHARED = "shared";
    private static final String TASKS_AUTHOR = "author";

    public static void addTask(Task task, FirebaseFirestore db) {
        db.collection(COLLECTION_TASKS)
                .add(task)
                .addOnSuccessListener(documentReference -> Log.d("DatabaseUtil", "Task added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error adding document", e));
    }

    public static void getTasksUpdateAdapter(List<Task> tasks, OnTaskSelectedListener listener, FirebaseFirestore db) {
        getQueryTasks(listener.getContext(), db)
                .addOnCompleteListener(task -> {
                    populateTasks(tasks, task);
                    listener.onItemSelected(null, null, 0, 0);
                });
    }

    public static void getTasksUpdateText(List<Task> tasks, EditText editText, FirebaseFirestore db) {
        getQueryTasks(editText.getContext(), db)
                .addOnCompleteListener(task -> {
                    populateTasks(tasks, task);
                    editText.setText(tasks.toString());
                });
    }

    private static com.google.android.gms.tasks.Task<List<Object>> getQueryTasks(Context context, FirebaseFirestore db) {
        SharedPreferences settings = context.getSharedPreferences(AppConstant.PREFERENCES_NAME, 0);
        String user = settings.getString(AppConstant.PREFERENCE_USER, "default");
        com.google.android.gms.tasks.Task<QuerySnapshot> sharedTasksQuery = db.collection(COLLECTION_TASKS).whereEqualTo(TASKS_AUTHOR, user).get();
        com.google.android.gms.tasks.Task<QuerySnapshot> userTasksQuery = db.collection(COLLECTION_TASKS).whereEqualTo(TASKS_SHARED, true).get();
        return Tasks.whenAllSuccess(sharedTasksQuery, userTasksQuery);
    }

    private static void populateTasks(List<Task> tasks, com.google.android.gms.tasks.Task<List<Object>> task) {
        if (task.isSuccessful()) {
            for (Object o : Objects.requireNonNull(task.getResult())) {
                QuerySnapshot query = (QuerySnapshot) o;
                for (DocumentSnapshot document : query.getDocuments()) {
                    Task t = new Task(document);
                    tasks.add(t);
                }
            }
        } else {
            Log.w("DatabaseUtil", "Error getting documents.", task.getException());
        }
    }
}
