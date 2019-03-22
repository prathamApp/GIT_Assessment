package com.pratham.assessment.services;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.ui.splash_activity.SplashActivity;
import com.pratham.assessment.utilities.Assessment_Constants;

public class AppExitService extends Service {

    private AppDatabase appDatabase;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        try {

            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {

                        appDatabase = Room.databaseBuilder(AppExitService.this,
                                AppDatabase.class, AppDatabase.DB_NAME)
                                .build();

                        String EndTime=""+AssessmentApplication.getCurrentDateTime();

                        String toDateTemp = appDatabase.getSessionDao().getToDate(Assessment_Constants.currentSession);

                        if (toDateTemp.equalsIgnoreCase("na")) {
                            appDatabase.getSessionDao().UpdateToDate(Assessment_Constants.currentSession, EndTime);
                        }
                        String toDateAssessment = appDatabase.getSessionDao().getToDate(Assessment_Constants.assessmentSession);
                        if (toDateAssessment.equalsIgnoreCase("na")) {
                            appDatabase.getSessionDao().UpdateToDate(Assessment_Constants.assessmentSession, EndTime);
                        }


                        BackupDatabase.backup(AppExitService.this);
                        stopService(new Intent(AppExitService.this, SplashActivity.class));
                        stopSelf();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}