package com.pratham.assessment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.ProcessPhoenix;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.ui.splash_activity.SplashActivity_;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import net.alhazmy13.catcho.library.Catcho;
import net.alhazmy13.catcho.library.error.CatchoError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_catcho_new)
public class CatchoActivity extends AppCompatActivity {
    @AfterViews
    public void init() {
        try {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        AppDatabase.getDatabaseInstance(CatchoActivity.this).getLogsDao().insertLog(log);
        BackupDatabase.backup(CatchoActivity.this);
            /*findViewById(R.id.catcho_button).setOnClickListener(v -> {
//            finishAffinity();
                ProcessPhoenix.triggerRebirth(CatchoActivity.this);
            });*/
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catcho_new);
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
            AppDatabase.getDatabaseInstance(CatchoActivity.this).getLogsDao().insertLog(log);
            BackupDatabase.backup(CatchoActivity.this);
            *//*findViewById(R.id.catcho_button).setOnClickListener(v -> {
//            finishAffinity();
                ProcessPhoenix.triggerRebirth(CatchoActivity.this);
            });*//*
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    @Click(R.id.catcho_button)
    public void onCatchoClick(){
        try {
            ProcessPhoenix.triggerRebirth(CatchoActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        startActivity(new Intent(CatchoActivity.this, SplashActivity_.class));
    }
}
