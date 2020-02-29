package com.pratham.assessment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.utilities.Assessment_Utility;


import net.alhazmy13.catcho.library.Catcho;
import net.alhazmy13.catcho.library.error.CatchoError;

public class CatchoTransparentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                CatchoError error = (CatchoError) getIntent().getSerializableExtra(Catcho.ERROR);
                Modal_Log log = new Modal_Log();
                log.setCurrentDateTime(Assessment_Utility.GetCurrentDateTime());
                log.setErrorType("ERROR");
                log.setExceptionMessage(error.toString());
                log.setExceptionStackTrace(error.getError());
                log.setMethodName("NO_METHOD");
                log.setGroupId(FastSave.getInstance().getString("", "no_group"));
                log.setDeviceId("");
                AppDatabase.getDatabaseInstance(CatchoTransparentActivity.this).getLogsDao().insertLog(log);
                new BackupDatabase().backup(CatchoTransparentActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                finishAffinity();
                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }.execute();
    }
}
