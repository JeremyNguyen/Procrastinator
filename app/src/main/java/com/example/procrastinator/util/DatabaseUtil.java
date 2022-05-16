package com.example.procrastinator.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.listener.OnTaskSelectedListener;
import com.example.procrastinator.model.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

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

    public static void getTasksSendReminders(Context context, FirebaseFirestore db) {
        getQueryTasksReminders(context, db).addOnCompleteListener(queries -> {
            DatabaseUtil.sendReminders(queries, context);
        });
    }

    public static void getTasksUpdateAdapter(List<Task> tasks, OnTaskSelectedListener listener, FirebaseFirestore db) {
        getQueryTasks(listener.getContext(), db)
                .addOnCompleteListener(queries -> {
                    populateTasks(tasks, queries);
                    listener.onItemSelected(null, null, 0, 0);
                });
    }

    public static void getTasksUpdateText(List<Task> tasks, EditText editText, FirebaseFirestore db) {
        getQueryTasks(editText.getContext(), db)
                .addOnCompleteListener(queries -> {
                    populateTasks(tasks, queries);
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

    private static com.google.android.gms.tasks.Task<List<Object>> getQueryTasksReminders(Context context, FirebaseFirestore db) {
        SharedPreferences settings = context.getSharedPreferences(AppConstant.PREFERENCES_NAME, 0);
        String user = settings.getString(AppConstant.PREFERENCE_USER, "default");
        com.google.android.gms.tasks.Task<QuerySnapshot> sharedTasksQuery = db.collection(COLLECTION_TASKS)
                .whereEqualTo(TASKS_AUTHOR, user)
                .whereLessThan("remindWhen", Timestamp.now())
                .get();
        com.google.android.gms.tasks.Task<QuerySnapshot> userTasksQuery = db.collection(COLLECTION_TASKS)
                .whereEqualTo(TASKS_SHARED, true)
                .whereLessThan("remindWhen", Timestamp.now())
                .get();
        return Tasks.whenAllSuccess(sharedTasksQuery, userTasksQuery);
    }

    private static void populateTasks(List<Task> tasks, com.google.android.gms.tasks.Task<List<Object>> queries) {
        if (queries.isSuccessful()) {
            Set<Task> set = new TreeSet<>();
            for (Object o : Objects.requireNonNull(queries.getResult())) {
                QuerySnapshot query = (QuerySnapshot) o;
                for (DocumentSnapshot document : query.getDocuments()) {
                    Task task = new Task(document);
                    set.add(task);
                }
            }
            tasks.addAll(set);
        } else {
            Log.w("DatabaseUtil", "Error getting documents.", queries.getException());
        }
    }

    private static void sendReminders(com.google.android.gms.tasks.Task<List<Object>> queries, Context context) {
        if (queries.isSuccessful()) {
            Set<Task> set = new HashSet<>();
            for (Object o : Objects.requireNonNull(queries.getResult())) {
                QuerySnapshot query = (QuerySnapshot) o;
                for (DocumentSnapshot document : query.getDocuments()) {
                    Task task = new Task(document);
                    if (!AppConstant.CATEGORY_SHOPPING.equals(task.getCategory())) {
                        set.add(task);
                    }
                }
            }
            for (Task task : set) {
                NotificationUtil.sendReminderNotification(task, context);
            }
        } else {
            Log.w("DatabaseUtil", "Error getting documents.", queries.getException());
        }
    }
}
