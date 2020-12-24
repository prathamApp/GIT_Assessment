package com.pratham.assessment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.ProcessPhoenix;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import net.alhazmy13.catcho.library.Catcho;
import net.alhazmy13.catcho.library.error.CatchoError;

public class CatchoTransparentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catcho);
        try {

            CatchoError error = (CatchoError) getIntent().getSerializableExtra(Catcho.ERROR);
            Modal_Log log = new Modal_Log();
            log.setCurrentDateTime(Assessment_Utility.getCurrentDateTime());
            log.setErrorType("ERROR");
            log.setExceptionMessage(error.toString());
            log.setExceptionStackTrace(error.getError());
            log.setMethodName("NO_METHOD");
            log.setGroupId(FastSave.getInstance().getString(Assessment_Constants.currentStudentID, "no_group"));
            log.setDeviceId("" + Assessment_Utility.getDeviceId(this));
            log.setLogDetail("Apk version : " + Assessment_Utility.getCurrentVersion(this) + " Apk type : " + (AssessmentApplication.isTablet ? "Tablet" : "Smartphone"));
            log.setSessionId(Assessment_Constants.currentSession);
            AppDatabase.getDatabaseInstance(CatchoTransparentActivity.this).getLogsDao().insertLog(log);
            BackupDatabase.backup(CatchoTransparentActivity.this);
            findViewById(R.id.catcho_button).setOnClickListener(v -> {
//            finishAffinity();
                ProcessPhoenix.triggerRebirth(CatchoTransparentActivity.this);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*        new AsyncTask<Object, Void, Object>() {
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
        }.execute();*/


    }
}
