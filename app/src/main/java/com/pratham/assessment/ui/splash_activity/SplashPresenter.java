package com.pratham.assessment.ui.splash_activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.dao.StatusDao;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.domain.Modal_RaspFacility;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Status;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.services.LocationService;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.FileUtils;
import com.pratham.assessment.utilities.SDCardUtil;

import net.lingala.zip4j.core.ZipFile;

import org.androidannotations.annotations.EBean;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.pratham.assessment.AssessmentApplication.sharedPreferences;
import static com.pratham.assessment.ui.splash_activity.SplashActivity.appDatabase;
import static com.pratham.assessment.utilities.Assessment_Constants.CURRENT_VERSION;

@EBean
public class SplashPresenter implements SplashContract.SplashPresenter {
    static String fpath, appname;
    Context context;
    SplashContract.SplashView splashView;

    public SplashPresenter(Context context) {
        this.context = context;
        this.splashView = (SplashContract.SplashView) context;
    }


    /*public SplashPresenter(Context context, SplashContract.SplashView splashView) {
        this.context = context;
        this.splashView = splashView;
    }*/

    @Override
    public void checkVersion() {
       /* String currentVersion = Assessment_Utility.getCurrentVersion(context);
        String updatedVersion = sharedPreferences.getString(CURRENT_VERSION, "-1");
        if (updatedVersion != null) {
            if (updatedVersion.equalsIgnoreCase("-1")) {
                if (Assessment_Utility.isDataConnectionAvailable(context)) {
                    try {
                        new GetLatestVersion(this).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else splashView.startApp();
            } else {
                if (updatedVersion != null && currentVersion != null && isCurrentVersionEqualsPlayStoreVersion(currentVersion, updatedVersion)) {
                    splashView.showUpdateDialog();
                } else
                    splashView.startApp();
            }
        } else*/
        splashView.startApp();

    }

    @Override
    public void versionObtained(String latestVersion) {
       /* if (latestVersion != null) {
            sharedPreferences.edit().putString(CURRENT_VERSION, latestVersion).apply();
            checkVersion();
        } else {
            splashView.startApp();
        }*/
    }

    @Override
    public void copyDataBase() {

        try {
            new AsyncTask<Void, Integer, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    splashView.showProgressDialog();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    File folder_file, db_file;
                    try {
                        ArrayList<String> sdPath = FileUtils.getExtSdCardPaths(context);
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrathamBackups" + "/assessment_database", null, SQLiteDatabase.OPEN_READONLY);
                        if (db != null) {
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Score Where sentFlag=0", null);
                                List<Score> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Score detail = new Score();
                                        detail.setScoreId(content_cursor.getInt(content_cursor.getColumnIndex("ScoreId")));
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setStudentID(content_cursor.getString(content_cursor.getColumnIndex("StudentID")));
                                        detail.setDeviceID(content_cursor.getString(content_cursor.getColumnIndex("DeviceID")));
                                        detail.setResourceID(content_cursor.getString(content_cursor.getColumnIndex("ResourceID")));
                                        detail.setQuestionId(content_cursor.getInt(content_cursor.getColumnIndex("QuestionId")));
                                        detail.setScoredMarks(content_cursor.getInt(content_cursor.getColumnIndex("ScoredMarks")));
                                        detail.setTotalMarks(content_cursor.getInt(content_cursor.getColumnIndex("TotalMarks")));
                                        detail.setStartDateTime(content_cursor.getString(content_cursor.getColumnIndex("StartDateTime")));
                                        detail.setEndDateTime(content_cursor.getString(content_cursor.getColumnIndex("EndDateTime")));
                                        detail.setLevel(content_cursor.getInt(content_cursor.getColumnIndex("Level")));
                                        detail.setLabel(content_cursor.getString(content_cursor.getColumnIndex("Label")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        detail.setIsAttempted(content_cursor.getString(content_cursor.getColumnIndex("isAttempted")).equalsIgnoreCase("true") ? true : false);
                                        detail.setIsCorrect(content_cursor.getString(content_cursor.getColumnIndex("isCorrect")).equalsIgnoreCase("true") ? true : false);
                                        detail.setUserAnswer(content_cursor.getString(content_cursor.getColumnIndex("userAnswer")));
                                        detail.setExamId(content_cursor.getString(content_cursor.getColumnIndex("examId")));
                                        detail.setPaperId(content_cursor.getString(content_cursor.getColumnIndex("paperId")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getScoreDao().addScoreList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Session Where sentFlag=0", null);
                                List<Session> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Session detail = new Session();
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setFromDate(content_cursor.getString(content_cursor.getColumnIndex("fromDate")));
                                        detail.setToDate(content_cursor.getString(content_cursor.getColumnIndex("toDate")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getSessionDao().addSessionList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Attendance Where sentFlag=0", null);
                                List<Attendance> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Attendance detail = new Attendance();
                                        detail.setAttendanceID(content_cursor.getInt(content_cursor.getColumnIndex("attendanceID")));
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setDate(content_cursor.getString(content_cursor.getColumnIndex("Date")));
                                        detail.setGroupID(content_cursor.getString(content_cursor.getColumnIndex("GroupID")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getAttendanceDao().addAttendanceList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM AssessmentPaperForPush Where sentFlag=0", null);
                                List<AssessmentPaperForPush> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        AssessmentPaperForPush detail = new AssessmentPaperForPush();
                                        detail.setPaperStartTime(content_cursor.getString(content_cursor.getColumnIndex("paperStartTime")));
                                        detail.setPaperEndTime(content_cursor.getString(content_cursor.getColumnIndex("paperEndTime")));
                                        detail.setLanguageId(content_cursor.getString(content_cursor.getColumnIndex("languageId")));
                                        detail.setExamId(content_cursor.getString(content_cursor.getColumnIndex("examId")));
                                        detail.setSubjectId(content_cursor.getString(content_cursor.getColumnIndex("subjectId")));
                                        detail.setOutOfMarks(content_cursor.getString(content_cursor.getColumnIndex("outOfMarks")));
                                        detail.setPaperId(content_cursor.getString(content_cursor.getColumnIndex("paperId")));
                                        detail.setTotalMarks(content_cursor.getString(content_cursor.getColumnIndex("totalMarks")));
                                        detail.setExamTime(content_cursor.getString(content_cursor.getColumnIndex("examTime")));
                                        detail.setCorrectCnt(content_cursor.getInt(content_cursor.getColumnIndex("CorrectCnt")));
                                        detail.setWrongCnt(content_cursor.getInt(content_cursor.getColumnIndex("wrongCnt")));
                                        detail.setSkipCnt(content_cursor.getInt(content_cursor.getColumnIndex("SkipCnt")));
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setStudentId(content_cursor.getString(content_cursor.getColumnIndex("studentId")));
                                        detail.setExamName(content_cursor.getString(content_cursor.getColumnIndex("examName")));
                                        detail.setQuestion1Rating(content_cursor.getString(content_cursor.getColumnIndex("question1Rating")));
                                        detail.setQuestion2Rating(content_cursor.getString(content_cursor.getColumnIndex("question2Rating")));
                                        detail.setQuestion3Rating(content_cursor.getString(content_cursor.getColumnIndex("question3Rating")));
                                        detail.setQuestion4Rating(content_cursor.getString(content_cursor.getColumnIndex("question4Rating")));
                                        detail.setQuestion5Rating(content_cursor.getString(content_cursor.getColumnIndex("question5Rating")));
                                        detail.setQuestion6Rating(content_cursor.getString(content_cursor.getColumnIndex("question6Rating")));
                                        detail.setQuestion7Rating(content_cursor.getString(content_cursor.getColumnIndex("question7Rating")));
                                        detail.setQuestion8Rating(content_cursor.getString(content_cursor.getColumnIndex("question8Rating")));
                                        detail.setQuestion9Rating(content_cursor.getString(content_cursor.getColumnIndex("question9Rating")));
                                        detail.setQuestion10Rating(content_cursor.getString(content_cursor.getColumnIndex("question10Rating")));
                                        detail.setFullName(content_cursor.getString(content_cursor.getColumnIndex("FullName")));
                                        detail.setGender(content_cursor.getString(content_cursor.getColumnIndex("Gender")));
                                        detail.setIsniosstudent(content_cursor.getString(content_cursor.getColumnIndex("isniosstudent")));
                                        detail.setAge(content_cursor.getInt(content_cursor.getColumnIndex("Age")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().insertAllPapersForPush(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM SupervisorData Where sentFlag=0", null);
                                List<SupervisorData> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        SupervisorData detail = new SupervisorData();
                                        detail.setsId(content_cursor.getInt(content_cursor.getColumnIndex("sId")));
                                        detail.setAssessmentSessionId(content_cursor.getString(content_cursor.getColumnIndex("assessmentSessionId")));
                                        detail.setSupervisorId(content_cursor.getString(content_cursor.getColumnIndex("supervisorId")));
                                        detail.setSupervisorName(content_cursor.getString(content_cursor.getColumnIndex("supervisorName")));
                                        detail.setSupervisorPhoto(content_cursor.getString(content_cursor.getColumnIndex("supervisorPhoto")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getSupervisorDataDao().insertAll(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Logs Where sentFlag=0", null);
                                List<Modal_Log> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Modal_Log detail = new Modal_Log();
                                        detail.setLogId(content_cursor.getInt(content_cursor.getColumnIndex("logId")));
                                        detail.setDeviceId(content_cursor.getString(content_cursor.getColumnIndex("deviceId")));
                                        detail.setCurrentDateTime(content_cursor.getString(content_cursor.getColumnIndex("currentDateTime")));
                                        detail.setErrorType(content_cursor.getString(content_cursor.getColumnIndex("errorType")));
                                        detail.setExceptionMessage(content_cursor.getString(content_cursor.getColumnIndex("exceptionMessage")));
                                        detail.setExceptionStackTrace(content_cursor.getString(content_cursor.getColumnIndex("exceptionStackTrace")));
                                        detail.setGroupId(content_cursor.getString(content_cursor.getColumnIndex("groupId")));
                                        detail.setLogDetail(content_cursor.getString(content_cursor.getColumnIndex("LogDetail")));
                                        detail.setMethodName(content_cursor.getString(content_cursor.getColumnIndex("methodName")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getLogsDao().insertAllLogs(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!AssessmentApplication.isTablet) {
                                try {
                                    Cursor content_cursor;
                                    content_cursor = db.rawQuery("SELECT * FROM Student Where newFlag=0", null);
                                    List<Student> contents = new ArrayList<>();
                                    if (content_cursor.moveToFirst()) {
                                        while (!content_cursor.isAfterLast()) {
                                            Student detail = new Student();
                                            detail.setStudentID(content_cursor.getString(content_cursor.getColumnIndex("StudentID")));
                                            detail.setStudentUID(content_cursor.getString(content_cursor.getColumnIndex("StudentUID")));
                                            detail.setFirstName(content_cursor.getString(content_cursor.getColumnIndex("FirstName")));
                                            detail.setMiddleName(content_cursor.getString(content_cursor.getColumnIndex("MiddleName")));
                                            detail.setLastName(content_cursor.getString(content_cursor.getColumnIndex("LastName")));
                                            detail.setFullName(content_cursor.getString(content_cursor.getColumnIndex("FullName")));
                                            detail.setGender(content_cursor.getString(content_cursor.getColumnIndex("Gender")));
                                            detail.setRegDate(content_cursor.getString(content_cursor.getColumnIndex("regDate")));
                                            detail.setAge(content_cursor.getInt(content_cursor.getColumnIndex("Age")));
                                            detail.setVillageName(content_cursor.getString(content_cursor.getColumnIndex("villageName")));
                                            detail.setNewFlag(content_cursor.getInt(content_cursor.getColumnIndex("newFlag")));
                                            detail.setDeviceId(content_cursor.getString(content_cursor.getColumnIndex("DeviceId")));
                                            detail.setIsniosstudent(content_cursor.getString(content_cursor.getColumnIndex("isniosstudent")));
                                            contents.add(detail);
                                            content_cursor.moveToNext();
                                        }
                                    }
                                    AppDatabase.getDatabaseInstance(context).getStudentDao().insertAll(contents);
                                    content_cursor.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            BackupDatabase.backup(context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //addStartTime();
                    super.onPostExecute(aVoid);
                    splashView.dismissProgressDialog();
                    splashView.showButton();
                    BackupDatabase.backup(context);
                }

            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    @Override
    public void pushData() {
        new PushDataToServer(context).execute();
    }*/

    @Override
    public boolean getSdCardPath() {

        ArrayList<String> base_path = SDCardUtil.getExtSdCardPaths(context);
        if (base_path.size() > 0) {
            String path = base_path.get(0).replace("[", "");
            path = path.replace("]", "");
            fpath = path;
        } else
            fpath = Environment.getExternalStorageDirectory().getAbsolutePath();

        File file = new File(fpath + Assessment_Constants.ASSESSMENT_FOLDER_PATH + AppDatabase.DB_NAME);

        if (file.exists()) {
            Assessment_Constants.ext_path = fpath + "/";
            Log.d("getSD", "getSdCardPath: " + Assessment_Constants.ext_path);
            Assessment_Constants.SD_CARD_Content = true;
            return true;
        } else {
            Assessment_Constants.SD_CARD_Content = false;
            return false;
        }
    }

    @Override
    public void doInitialEntries(AppDatabase appDatabase) {
        try {
            Status status;
            status = new Status();
            status.setStatusKey("DeviceId");
            status.setValue("" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            status.setDescription("" + Build.SERIAL);
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("CRLID");
            status.setValue("default");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("DeviceName");
            status.setValue(Assessment_Utility.getDeviceName());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("gpsFixDuration");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("prathamCode");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("apkType");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("Latitude");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("Longitude");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("GPSDateTime");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("CurrentSession");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("SdCardPath");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AppLang");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AppStartDateTime");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            //new Entries
            status = new Status();
            status.setStatusKey("ActivatedForGroups");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("programId");
            status.setValue("1");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group1");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group2");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group3");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group4");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group5");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("village");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("ActivatedDate");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AssessmentSession");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AndroidID");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("DBVersion");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("SerialID");
            status.setValue(Assessment_Utility.getDeviceSerialID());
            appDatabase.getStatusDao().insert(status);


            status = new Status();
            status.setStatusKey("OsVersionName");
            status.setValue(Assessment_Utility.getOSVersion());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("OsVersionNum");
            status.setValue(Assessment_Utility.getOSVersionNo() + "");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AvailableStorage");
            status.setValue(Assessment_Utility.getAvailableStorage());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("ScreenResolution");
            status.setValue(Assessment_Utility.getScreenResolution((AppCompatActivity) context));
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("Manufacturer");
            status.setValue(Assessment_Utility.getManufacturer());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("Model");
            status.setValue(Assessment_Utility.getModel());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("ApiLevel");
            status.setValue(Assessment_Utility.getApiLevel() + "");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("InternalStorageSize");
            status.setValue(Assessment_Utility.getInternalStorageSize() + "");
            appDatabase.getStatusDao().insert(status);

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            status.setStatusKey("wifiMAC");
            status.setValue(macAddress);
            appDatabase.getStatusDao().insert(status);

            setAppName(status);
            setAppVersion(status);
            BackupDatabase.backup(context);

            addStartTime();
//            getSdCardPath();
            requestLocation();
            sharedPreferences.edit().putBoolean(Assessment_Constants.INITIAL_ENTRIES, true).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @WorkerThread
    @Override
    public void copyZipAndPopulateMenu() {
        try {
//            updateNewEntriesInStatusTable();
            if (!sharedPreferences.getBoolean(Assessment_Constants.KEY_ASSET_COPIED, false)) {
                splashView.showProgressDialog();
                File mydir = null;
                mydir = new File(AssessmentApplication.contentSDPath + Assessment_Constants.ASSESSMENT_FOLDER_PATH);
                if (!mydir.exists())
                    mydir.mkdirs();

                String path = AssessmentApplication.contentSDPath + Assessment_Constants.ASSESSMENT_FOLDER_PATH;
                copyFile(context, path);

                sharedPreferences.edit().putBoolean(Assessment_Constants.KEY_ASSET_COPIED, true).apply();

                if (!sharedPreferences.getBoolean(Assessment_Constants.INITIAL_ENTRIES, false))
                    doInitialEntries(appDatabase);
                if (!sharedPreferences.getBoolean(Assessment_Constants.KEY_MENU_COPIED, false))
                    populateMenu();

            } else {
                splashView.gotoNextActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateNewEntriesInStatusTable() {
        Status status;
        try {

            status = new Status();
            String OsVersionName = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("OsVersionName");
            if (OsVersionName == null || OsVersionName.equalsIgnoreCase("")) {
                status.setStatusKey("OsVersionName");
                status.setValue(Assessment_Utility.getOSVersion());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("OsVersionName", Assessment_Utility.getOSVersion());
            }

            status = new Status();
            String OsVersionNum = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("OsVersionNum");
            if (OsVersionNum == null || OsVersionNum.equalsIgnoreCase("")) {

                status.setStatusKey("OsVersionNum");
                status.setValue(Assessment_Utility.getOSVersionNo() + "");
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("OsVersionNum", Assessment_Utility.getOSVersionNo() + "");

            }
            status = new Status();
            String AvailableStorage = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("AvailableStorage");
            if (AvailableStorage == null || AvailableStorage.equalsIgnoreCase("")) {
                status.setStatusKey("AvailableStorage");
                status.setValue(Assessment_Utility.getAvailableStorage());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("AvailableStorage", Assessment_Utility.getAvailableStorage());

            }
            status = new Status();
            String ScreenResolution = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("ScreenResolution");
            if (ScreenResolution == null || ScreenResolution.equalsIgnoreCase("")) {
                status.setStatusKey("ScreenResolution");
                status.setValue(Assessment_Utility.getScreenResolution((AppCompatActivity) context));
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("ScreenResolution", Assessment_Utility.getScreenResolution((AppCompatActivity) context));
            }

            status = new Status();
            String Manufacturer = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("Manufacturer");
            if (Manufacturer == null || Manufacturer.equalsIgnoreCase("")) {
                status.setStatusKey("Manufacturer");
                status.setValue(Assessment_Utility.getManufacturer());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("Manufacturer", Assessment_Utility.getManufacturer());
            }

            status = new Status();
            String Model = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("Model");
            if (Model == null || Model.equalsIgnoreCase("")) {

                status.setStatusKey("Model");
                status.setValue(Assessment_Utility.getModel());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("Model", Assessment_Utility.getModel());
            }


            status = new Status();
            String ApiLevel = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("ApiLevel");
            if (ApiLevel == null || ApiLevel.equalsIgnoreCase("")) {

                status.setStatusKey("ApiLevel");
                status.setValue(Assessment_Utility.getApiLevel() + "");
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("ApiLevel", Assessment_Utility.getApiLevel() + "");
            }

            status = new Status();
            String InternalStorageSize = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("InternalStorageSize");
            if (InternalStorageSize == null || InternalStorageSize.equalsIgnoreCase("")) {

                status.setStatusKey("InternalStorageSize");
                status.setValue(Assessment_Utility.getInternalStorageSize() + "");
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("InternalStorageSize", Assessment_Utility.getInternalStorageSize() + "");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unzipFile(String source, String destination) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
            new File(source).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populateSDCardMenu() {
//        updateNewEntriesInStatusTable();
        if (!sharedPreferences.getBoolean(Assessment_Constants.SD_CARD_Content_STR, false)) {
            if (!sharedPreferences.getBoolean(Assessment_Constants.INITIAL_ENTRIES, false))
                doInitialEntries(appDatabase);
            copyDBFile();
            try {
                File db_file;
                db_file = new File(Environment.getExternalStorageDirectory().toString() + "/.AssessmentInternal/" + AppDatabase.DB_NAME);
                if (db_file.exists()) {
                    Assessment_Constants.SD_CARD_Content = true;
                    SQLiteDatabase db = SQLiteDatabase.openDatabase(db_file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                    if (db != null) {
                        Cursor content_cursor;
                        try {
                            content_cursor = db.rawQuery("SELECT * FROM ContentTable", null);
                            //populate contents
                            List<ContentTable> contents = new ArrayList<>();
                            if (content_cursor.moveToFirst()) {
                                while (!content_cursor.isAfterLast()) {
                                    ContentTable detail = new ContentTable();
                                    detail.setNodeId("" + content_cursor.getString(content_cursor.getColumnIndex("nodeId")));
                                    detail.setNodeType("" + content_cursor.getString(content_cursor.getColumnIndex("nodeType")));
                                    detail.setNodeTitle("" + content_cursor.getString(content_cursor.getColumnIndex("nodeTitle")));
                                    detail.setNodeKeywords("" + content_cursor.getString(content_cursor.getColumnIndex("nodeKeywords")));
                                    detail.setNodeAge("" + content_cursor.getString(content_cursor.getColumnIndex("nodeAge")));
                                    detail.setNodeDesc("" + content_cursor.getString(content_cursor.getColumnIndex("nodeDesc")));
                                    detail.setNodeServerImage("" + content_cursor.getString(content_cursor.getColumnIndex("nodeServerImage")));
                                    detail.setNodeImage("" + content_cursor.getString(content_cursor.getColumnIndex("nodeImage")));
                                    detail.setResourceId("" + content_cursor.getString(content_cursor.getColumnIndex("resourceId")));
                                    detail.setResourceType("" + content_cursor.getString(content_cursor.getColumnIndex("resourceType")));
                                    detail.setResourcePath("" + content_cursor.getString(content_cursor.getColumnIndex("resourcePath")));
                                    detail.setLevel("" + content_cursor.getInt(content_cursor.getColumnIndex("level")));
                                    detail.setContentLanguage("" + content_cursor.getString(content_cursor.getColumnIndex("contentLanguage")));
                                    detail.setParentId("" + content_cursor.getString(content_cursor.getColumnIndex("parentId")));
                                    detail.setContentType("" + content_cursor.getString(content_cursor.getColumnIndex("contentType")));
                                    detail.setIsDownloaded("true");
                                    detail.setOnSDCard(true);
                                    contents.add(detail);
                                    content_cursor.moveToNext();
                                }
                            }
                            appDatabase.getContentTableDao().addContentList(contents);
                            content_cursor.close();
                            sharedPreferences.edit().putBoolean(Assessment_Constants.SD_CARD_Content_STR, true).apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                BackupDatabase.backup(context);
                splashView.gotoNextActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            BackupDatabase.backup(context);
            splashView.gotoNextActivity();
        }
    }

    private void copyFile(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream in = assetManager.open("assessData.zip");
            OutputStream out = new FileOutputStream(path + "assessData.zip");
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            unzipFile(AssessmentApplication.contentSDPath + Assessment_Constants.ASSESSMENT_FOLDER_PATH + "assessData.zip", AssessmentApplication.contentSDPath + "/.Assessment");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void copyDBFile() {
        AssetManager assetManager = context.getAssets();
        try {
            File direct = new File(Environment.getExternalStorageDirectory().toString() + "/.assessmentInternal");
            if (!direct.exists()) direct.mkdir();

            InputStream in = new FileInputStream(Assessment_Constants.ext_path + Assessment_Constants.ASSESSMENT_FOLDER_PATH + AppDatabase.DB_NAME);
//            InputStream in = assetManager.open("assessData.zip");
            OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/.assessmentInternal/" + AppDatabase.DB_NAME);
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void populateMenu() {
        try {
            File folder_file, db_file;
            if (!sharedPreferences.getBoolean(Assessment_Constants.KEY_MENU_COPIED, false)) {

/*                if (Assessment_Constants.SD_CARD_Content)
                    folder_file = new File(Assessment_Constants.ext_path+ Assessment_Constants.ASSESSMENT_FOLDER_PATH);
                else*/
                folder_file = new File(AssessmentApplication.contentSDPath + Assessment_Constants.ASSESSMENT_FOLDER_PATH);

                if (folder_file.exists()) {
                    Log.d("-CT-", "doInBackground Assessment_Constants.ext_path: " + Assessment_Constants.ext_path);
                    db_file = new File(folder_file + "/" + AppDatabase.DB_NAME);
//                    db_file = new File(folder_file.getAbsolutePath() + "/" + AppDatabase.DB_NAME);
                    if (db_file.exists()) {
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(db_file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                        if (db != null) {
                            Cursor content_cursor;
                            try {
                                content_cursor = db.rawQuery("SELECT * FROM ContentTable", null);
                                //populate contents
                                List<ContentTable> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        ContentTable detail = new ContentTable();
                                        detail.setNodeId(content_cursor.getString(content_cursor.getColumnIndex("nodeId")));
                                        detail.setNodeType(content_cursor.getString(content_cursor.getColumnIndex("nodeType")));
                                        detail.setNodeTitle(content_cursor.getString(content_cursor.getColumnIndex("nodeTitle")));
                                        detail.setNodeKeywords(content_cursor.getString(content_cursor.getColumnIndex("nodeKeywords")));
                                        detail.setNodeAge(content_cursor.getString(content_cursor.getColumnIndex("nodeAge")));
                                        detail.setNodeDesc(content_cursor.getString(content_cursor.getColumnIndex("nodeDesc")));
                                        detail.setNodeServerImage(content_cursor.getString(content_cursor.getColumnIndex("nodeServerImage")));
                                        detail.setNodeImage(content_cursor.getString(content_cursor.getColumnIndex("nodeImage")));
                                        detail.setResourceId(content_cursor.getString(content_cursor.getColumnIndex("resourceId")));
                                        detail.setResourceType(content_cursor.getString(content_cursor.getColumnIndex("resourceType")));
                                        detail.setResourcePath(content_cursor.getString(content_cursor.getColumnIndex("resourcePath")));
                                        detail.setLevel("" + content_cursor.getInt(content_cursor.getColumnIndex("level")));
                                        detail.setContentLanguage(content_cursor.getString(content_cursor.getColumnIndex("contentLanguage")));
                                        detail.setParentId(content_cursor.getString(content_cursor.getColumnIndex("parentId")));
                                        detail.setContentType(content_cursor.getString(content_cursor.getColumnIndex("contentType")));
//                                        if (!Assessment_Constants.SMART_PHONE)
                                        detail.setOnSDCard(true);
/*                                        else
                                            detail.setOnSDCard(false);*/
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                appDatabase.getContentTableDao().addContentList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            sharedPreferences.edit().putBoolean(Assessment_Constants.KEY_MENU_COPIED, true).apply();
            //TODO start Exit Service
            //context.startService(new Intent(context, AppExitService.class));
            BackupDatabase.backup(context);
            splashView.gotoNextActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestLocation() {
        new LocationService(context).checkLocation();
    }

    private void setAppVersion(Status status) {
        if (AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("apkVersion") == null) {
            status = new Status();

            status.setStatusKey("apkVersion");
            PackageInfo pInfo = null;
            String verCode = "";
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                verCode = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            status.setValue(verCode);
            appDatabase.getStatusDao().insert(status);

        } else {
            status.setStatusKey("apkVersion");

            PackageInfo pInfo = null;
            String verCode = "";
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                verCode = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            status.setValue(verCode);
            appDatabase.getStatusDao().insert(status);

        }
    }

    private void setAppName(Status status) {
        String appname = "";
        if (AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("appName") == null) {
            CharSequence c = "";
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List l = am.getRunningAppProcesses();
            Iterator i = l.iterator();
            PackageManager pm = context.getPackageManager();
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                appname = c.toString();
                Log.w("LABEL", c.toString());
            } catch (Exception e) {
            }

            status = new Status();
            status.setStatusKey("appName");
            status.setValue(appname);
            appDatabase.getStatusDao().insert(status);

        } else {
            CharSequence c = "";
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List l = am.getRunningAppProcesses();
            Iterator i = l.iterator();
            PackageManager pm = context.getPackageManager();
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                appname = c.toString();
                Log.w("LABEL", c.toString());
            } catch (Exception e) {
            }
            status = new Status();
            status.setStatusKey("appName");
            status.setValue(appname);
            appDatabase.getStatusDao().insert(status);
        }
    }


    private void addStartTime() {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    String appStartTime = AssessmentApplication.getCurrentDateTime(false, "");
                    StatusDao statusDao = appDatabase.getStatusDao();
                    statusDao.updateValue("AppStartDateTime", appStartTime);
                    BackupDatabase.backup(context);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }


    private boolean isCurrentVersionEqualsPlayStoreVersion(String currentVersion, String
            playStoreVersion) {
        if (currentVersion.equalsIgnoreCase(playStoreVersion))
            return false;
        return true;
    }


    private void getFacility() {
        try {
            new AsyncTask<Object, Void, Object>() {

                @Override
                protected Object doInBackground(Object... objects) {

                    JSONObject object = new JSONObject();
                    try {
                        object.put("username", "pratham");
                        object.put("password", "pratham");
                        getFacilityIdfromRaspberry(Assessment_Constants.FACILITY_ID, Assessment_Constants.RASP_IP + "/api/session/", object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                }
            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFacilityIdfromRaspberry(final String requestType, String url, JSONObject data) {
        AndroidNetworking.post(url)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(data)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        saveFacility(requestType, response);
                    }

                    @Override
                    public void onError(ANError anError) {
//                            contentPresenter.notifyError(requestType/*, null*/);
                        Log.d("Error::", anError.getErrorDetail());
                        Log.d("Error::", anError.getMessage());
                        Log.d("Error::", anError.getResponse().toString());
                    }
                });
    }

    private void saveFacility(String header, String response) {

        if (header.equalsIgnoreCase(Assessment_Constants.FACILITY_ID)) {
            Gson gson = new Gson();

            Modal_RaspFacility facility = gson.fromJson(response, Modal_RaspFacility.class);
            Assessment_Constants.FACILITY_ID = facility.getFacilityId();
        }
    }

}