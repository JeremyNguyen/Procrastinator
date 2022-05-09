package com.example.procrastinator.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.procrastinator.util.NotificationUtil;

import java.util.concurrent.TimeUnit;

public class WatchDogWorker extends Worker {

    private static final String uniqueWorkName = "com.example.procrastinator.worker.WatchDogWorker";

    public WatchDogWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    public static void enqueueSelf(Context context) {
        Log.e("WatchDogWorker", "enqueueSelf");
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                WatchDogWorker.class, 15, TimeUnit.MINUTES)
                .build();
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork(uniqueWorkName, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("WatchDogWorker", "periodicWork");
        NotificationUtil.sendNotification("title", "message", getApplicationContext());
        return Result.success();
    }
}