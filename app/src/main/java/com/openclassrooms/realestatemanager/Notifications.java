package com.openclassrooms.realestatemanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Objects;

public class Notifications extends Worker {

    WorkerParameters workerParameters;

    public Notifications(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        workerParameters = workerParams;
    }

    public static void launchWorker(Context context, boolean isCreate) {
        WorkRequest workRequest;
        Data.Builder data = new Data.Builder();
        data.putBoolean("isCreate", isCreate);
        workRequest = new OneTimeWorkRequest.Builder(Notifications.class).setInputData(data.build()).build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
        boolean isCreate = workerParameters.getInputData().getBoolean("isCreate", false);
        setNotifications(this.getApplicationContext(), isCreate);
        return Result.success();
    }

    public void setNotifications(Context context, boolean isCreate) {
        String channelId = "channel id";

        String text;

        if (isCreate){
            text = "New real estate created";
        } else {
            text = "modification saved";
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_villa_24)
                        .setContentText(text)
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, "notification", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
            notificationManager.notify("REALESTATEMANAGER", 7, notificationBuilder.build());
        }
    }
}
