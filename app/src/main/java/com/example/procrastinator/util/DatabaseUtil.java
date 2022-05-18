package com.example.procrastinator.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.listener.OnCategorySelectedListener;
import com.example.procrastinator.model.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DatabaseUtil {

    private static final String COLLECTION_TASKS = "tasks";
    private static final String TASKS_SHARED = "shared";
    private static final String TASKS_AUTHOR = "author";
    private static final String TASKS_REMIND_JEREMY = "remindJeremy";
    private static final String TASKS_REMIND_NOEMIE = "remindNoemie";

    public static void addTask(Task task, FirebaseFirestore db, Context context) {
        String user = getUser(context);
        if (user != null) {
            task.setAuthor(user);
            db.collection(COLLECTION_TASKS)
                    .add(task)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("DatabaseUtil", "Task added with ID: " + documentReference.getId());
                        Toast.makeText(context, "Task created", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    })
                    .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error adding document", e));
        }
    }

    public static void updateTask(Task task, FirebaseFirestore db, Context context) {
        String user = getUser(context);
        if (user != null) {
            db.collection(COLLECTION_TASKS).document(task.getId()).set(task)
                    .addOnSuccessListener(unused -> {
                        Log.d("DatabaseUtil", "Task updated with ID: " + task.getId());
                        Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    })
                    .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error setting document", e));
        }
    }

    public static void deleteTask(Task task, FirebaseFirestore db, Context context) {
        String user = getUser(context);
        if (user != null) {
            db.collection(COLLECTION_TASKS).document(task.getId()).delete()
                    .addOnSuccessListener(unused -> {
                        Log.d("DatabaseUtil", "Task deleted with ID: " + task.getId());
                        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    })
                    .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error deleting document", e));
        }
    }

    public static void updateTaskRemind(Timestamp timestamp, String taskId, FirebaseFirestore db, Context context) {
        String user = getUser(context);
        if (user != null) {
            String remindColumn = TASKS_REMIND_JEREMY;
            if (AppConstant.USER_NOEMIE.equals(user)) {
                remindColumn = TASKS_REMIND_NOEMIE;
            }
            db.collection(COLLECTION_TASKS)
                    .document(taskId).update(remindColumn, timestamp)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("DatabaseUtil", "Updated task with ID: " + taskId);
                        Toast.makeText(context, "Task reminder set", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    })
                    .addOnFailureListener(e -> Log.w("DatabaseUtil", "Error updating document", e));
        }
    }

    public static void getTasksSendReminders(Context context, FirebaseFirestore db) {
        String user = getUser(context);
        if (user != null) {
            getQueryTasksReminders(user, context, db).addOnCompleteListener(queries -> {
                DatabaseUtil.sendReminders(queries, context);
            });
        }
    }

    public static void getTasksUpdateAdapter(List<Task> tasks, OnCategorySelectedListener listener, FirebaseFirestore db) {
        String user = getUser(listener.getContext());
        if (user != null) {
            getQueryTasks(listener.getContext(), db)
                    .addOnCompleteListener(queries -> {
                        populateTasks(tasks, queries, user);
                        listener.onItemSelected(null, null, 0, 0);
                    });
        }
    }

    public static void getTasksUpdateText(List<Task> tasks, EditText editText, FirebaseFirestore db) {
        String user = getUser(editText.getContext());
        if (user != null) {
            getQueryTasks(editText.getContext(), db)
                    .addOnCompleteListener(queries -> {
                        populateTasks(tasks, queries, user);
                        editText.setText(tasks.toString());
                    });
        }
    }

    private static com.google.android.gms.tasks.Task<List<Object>> getQueryTasks(Context context, FirebaseFirestore db) {
        String user = getUser(context);
        com.google.android.gms.tasks.Task<QuerySnapshot> sharedTasksQuery = db.collection(COLLECTION_TASKS).whereEqualTo(TASKS_AUTHOR, user).get();
        com.google.android.gms.tasks.Task<QuerySnapshot> userTasksQuery = db.collection(COLLECTION_TASKS).whereEqualTo(TASKS_SHARED, true).get();
        return Tasks.whenAllSuccess(sharedTasksQuery, userTasksQuery);
    }

    private static com.google.android.gms.tasks.Task<List<Object>> getQueryTasksReminders(String user, Context context, FirebaseFirestore db) {
        String remindColumn = TASKS_REMIND_JEREMY;
        if (AppConstant.USER_NOEMIE.equals(user)) {
            remindColumn = TASKS_REMIND_NOEMIE;
        }
        com.google.android.gms.tasks.Task<QuerySnapshot> sharedTasksQuery = db.collection(COLLECTION_TASKS)
                .whereEqualTo(TASKS_AUTHOR, user)
                .whereLessThan(remindColumn, Timestamp.now())
                .get();
        com.google.android.gms.tasks.Task<QuerySnapshot> userTasksQuery = db.collection(COLLECTION_TASKS)
                .whereEqualTo(TASKS_SHARED, true)
                .whereLessThan(remindColumn, Timestamp.now())
                .get();
        return Tasks.whenAllSuccess(sharedTasksQuery, userTasksQuery);
    }

    private static void populateTasks(List<Task> tasks, com.google.android.gms.tasks.Task<List<Object>> queries, String user) {
        if (queries.isSuccessful()) {
            Set<Task> set = new HashSet<>();
            for (Object o : Objects.requireNonNull(queries.getResult())) {
                QuerySnapshot query = (QuerySnapshot) o;
                for (DocumentSnapshot document : query.getDocuments()) {
                    Task task = new Task(document);
                    set.add(task);
                }
            }
            List<Task> sortedList = set.stream().sorted(getTaskComparator(user)).collect(Collectors.toList());
            tasks.clear();
            tasks.addAll(sortedList);
        } else {
            Log.w("DatabaseUtil", "Error getting documents", queries.getException());
        }
    }

    private static Comparator<Task> getTaskComparator(String user) {
        return (o1, o2) -> {
            if (AppConstant.USER_NOEMIE.equals(user)) {
                return o1.getRemindNoemie().compareTo(o2.getRemindNoemie());
            }
            return o1.getRemindJeremy().compareTo(o2.getRemindJeremy());
        };
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
            Log.w("DatabaseUtil", "Error sending reminders", queries.getException());
        }
    }

    public static String getUser(Context context) {
        SharedPreferences settings = context.getSharedPreferences(AppConstant.PREFERENCES_NAME, 0);
        String user = settings.getString(AppConstant.PREFERENCE_USER, null);
        if (user == null) {
            Toast.makeText(context, AppConstant.USER_NOT_SET, Toast.LENGTH_LONG).show();
        }
        return user;
    }

    public static Timestamp getFormattedTimestamp(long millis, Context context) {
        String user = getUser(context);
        Date date = new Date(millis);
        date.setHours(AppConstant.USER_NOEMIE.equals(user) ? 8 : 20);
        date.setMinutes(0);
        date.setSeconds(0);
        return new Timestamp(date);
    }

    public static Timestamp getTimestampForButton(View view) {
        long seconds = Timestamp.now().getSeconds();
        long deltaInDays = 0;
        switch (view.getId()) {
            case R.id.taskButtonOneDay:
                deltaInDays = 1;
                break;
            case R.id.taskButtonThreeDays:
                deltaInDays = 3;
                break;
            case R.id.taskButtonOneWeek:
                deltaInDays = 7;
                break;
            case R.id.taskButtonOneMonth:
                deltaInDays = 30;
                break;
            case R.id.taskButtonSixMonths:
                deltaInDays = 180;
                break;
            case R.id.taskButtonOneYear:
                deltaInDays = 365;
                break;
        }
        long millis = (seconds + (deltaInDays * 24 * 3600)) * 1000;
        return getFormattedTimestamp(millis, view.getContext());
    }
}
