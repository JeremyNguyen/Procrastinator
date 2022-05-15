package com.example.procrastinator.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class WatchDogWorker extends Worker {

    private static final String uniqueWorkName = "com.example.procrastinator.worker.WatchDogWorker";
    FirebaseFirestore db;

    public WatchDogWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        db = FirebaseFirestore.getInstance();
    }

    public static void enqueueSelf(Context context) {
        Log.d("WatchDogWorker", "enqueueSelf");
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                WatchDogWorker.class, 15, TimeUnit.MINUTES)
                .build();
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork(uniqueWorkName, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("WatchDogWorker", "doWork");
        DatabaseUtil.getTasksSendReminders(getApplicationContext(), db);
        return Result.success();
    }
}