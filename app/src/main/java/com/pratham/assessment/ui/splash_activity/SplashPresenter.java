package com.pratham.assessment.ui.splash_activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.dao.StatusDao;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Status;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.pratham.assessment.database.AppDatabase.appDatabase;

public class SplashPresenter implements SplashContract.SplashPresenter {
    static String fpath, appname;
    Context context;
    SplashContract.SplashView splashView;

    public SplashPresenter(Context context, SplashContract.SplashView splashView) {
        this.context = context;
        this.splashView = splashView;
    }

    /* @Override
     public void checkVersion() {
         String currentVersion = COS_Utility.getCurrentVersion(context);
         String updatedVersion = sharedPreferences.getString(CURRENT_VERSION, "-1");
         if (updatedVersion.equalsIgnoreCase("-1")) {
             if (COS_Utility.isDataConnectionAvailable(context)) {
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
     }
 */
 /*   @Override
    public void versionObtained(String latestVersion) {
        if (latestVersion != null ) {
            sharedPreferences.edit().putString(CURRENT_VERSION, latestVersion).apply();
            checkVersion();
        } else {
            splashView.startApp();
        }
    }
*/
   /* @Override
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
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cos_database", null, SQLiteDatabase.OPEN_READONLY);
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
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                appDatabase.getScoreDao().addScoreList(contents);
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
                                appDatabase.getSessionDao().addSessionList(contents);
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
                                appDatabase.getAttendanceDao().addAttendanceList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            BackupDatabase.backup(context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) { }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //addStartTime();
                    super.onPostExecute(aVoid);
                    splashView.dismissProgressDialog();
                    splashView.showButton();
                    //populateMenu();
                    BackupDatabase.backup(context);
                }

            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
  /*  @Override
    public void pushData() {
        new PushDataToServer(context).execute();
    }
*/
    @Override
    public void doInitialEntries(AppDatabase appDatabase) {
        try {
            com.pratham.assessment.domain.Status status;
            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("DeviceId");
            status.setValue("" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            status.setDescription("" + Build.SERIAL);
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("CRLID");
            status.setValue("default");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("DeviceName");
            status.setValue(Assessment_Utility.getDeviceName());
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("gpsFixDuration");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("prathamCode");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("apkType");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("Latitude");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("Longitude");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("GPSDateTime");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("CurrentSession");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("SdCardPath");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("AppLang");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("AppStartDateTime");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            //new Entries
            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("ActivatedForGroups");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("programId");
            status.setValue("1");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("group1");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("group2");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("group3");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("group4");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("group5");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("village");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("ActivatedDate");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("AssessmentSession");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("AndroidID");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("DBVersion");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new com.pratham.assessment.domain.Status();
            status.setStatusKey("SerialID");
            status.setValue(Assessment_Utility.getDeviceSerialID());
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
//            requestLocation();
            splashView.gotoNextActivity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 /*   @WorkerThread
    @Override
    public void copyZipAndPopulateMenu() {
        try {
            getSdCardPath();
            if (COS_Constants.SMART_PHONE) {
                if (!sharedPreferences.getBoolean(COS_Constants.KEY_ASSET_COPIED, false)) {
                    splashView.showProgressDialog();
                    File mydir = null;
                    *//*mydir = new File(COSApplication.pradigiPath + "/.LLA");
                    if (!mydir.exists()) mydir.mkdirs();

                    String path = COSApplication.pradigiPath + "/.LLA/";
                    copyFile(context, path);*//*
                    doInitialEntries(appDatabase);
                    sharedPreferences.edit().putBoolean(COS_Constants.KEY_ASSET_COPIED, true).apply();
                    if (!sharedPreferences.getBoolean(COS_Constants.KEY_MENU_COPIED, false)) {
                        populateMenu();
                    }

                } else {
                    splashView.gotoNextActivity();
                }
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
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
*/
 /*   private void copyFile(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream in = assetManager.open("English.zip");
            OutputStream out = new FileOutputStream(path + "English.zip");
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            unzipFile(COSApplication.pradigiPath + "/.LLA/English.zip", COSApplication.pradigiPath + "/.LLA");
        } catch (Exception e) {
            e.getMessage();
        }
    }*/

    /* public void populateMenu() {
         try {
             File folder_file, db_file;
             if (!sharedPreferences.getBoolean(COS_Constants.KEY_MENU_COPIED, false)) {
                 if (!COS_Constants.SMART_PHONE)
                     folder_file = new File(COS_Constants.ext_path);
                 else
                     folder_file = new File(COSApplication.pradigiPath );
 //                    folder_file = new File(COSApplication.pradigiPath + "/.LLA/English/");
                 if (folder_file.exists()) {
                     Log.d("-CT-", "doInBackground COS_Constants.ext_path: " + COS_Constants.ext_path);
                     db_file = new File(folder_file + "/" + AppDatabase.DB_NAME);
 //                    db_file = new File(folder_file.getAbsolutePath() + "/" + AppDatabase.DB_NAME);
                     if (db_file.exists()) {
                         COS_Constants.SD_CARD_Content=true;
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
                                         if (!COS_Constants.SMART_PHONE)
                                             detail.setOnSDCard(true);
                                         else
                                             detail.setOnSDCard(false);
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

             sharedPreferences.edit().putBoolean(COS_Constants.KEY_MENU_COPIED, true).apply();
             context.startService(new Intent(context, AppExitService.class));
             BackupDatabase.backup(context);
             splashView.gotoNextActivity();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 */
   /* private void requestLocation() {
        new LocationService(context).checkLocation();
    }
*/
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
            AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);

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
                    String appStartTime = Assessment_Utility.GetCurrentDateTime();
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



  /*  private void getFacility() {
        try {
            new AsyncTask<Object, Void, Object>() {

                @Override
                protected Object doInBackground(Object... objects) {

                    JSONObject object = new JSONObject();
                    try {
                        object.put("username", "pratham");
                        object.put("password", "pratham");
                        getFacilityIdfromRaspberry(COS_Constants.FACILITY_ID, COS_Constants.RASP_IP + "/api/session/", object);
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
*/
  /*  public void getFacilityIdfromRaspberry(final String requestType, String url, JSONObject data) {
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
                        Log.d("Error::", anError.getErrorDetail());
                        Log.d("Error::", anError.getMessage());
                        Log.d("Error::", anError.getResponse().toString());
                    }
                });
    }
*/
   /* private void saveFacility(String header, String response) {

        if (header.equalsIgnoreCase(COS_Constants.FACILITY_ID)) {
            Gson gson = new Gson();

            Modal_RaspFacility facility = gson.fromJson(response, Modal_RaspFacility.class);
            COS_Constants.FACILITY_ID = facility.getFacilityId();
        }
    }
*/
  /*  public void getSdCardPath() {
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
        } catch (Exception e) {//Name Not FOund Exception
        }
        if (appname.equals("City of Stories")) {
            ArrayList<String> base_path = SDCardUtil.getExtSdCardPaths(context);
            if (base_path.size() > 0) {
                String path = base_path.get(0).replace("[", "");
                path = path.replace("]", "");
                fpath = path;
            } else
                fpath = Environment.getExternalStorageDirectory().getAbsolutePath();
            fpath = fpath + "/.LLA/English/";
            File file = new File(fpath);
            COS_Constants.ext_path = fpath;
            Log.d("getSD", "getSdCardPath: " + COS_Constants.ext_path);
            if (file.exists())
                updateSdCardPath(fpath);
            else {
                File direct = new File(Environment.getExternalStorageDirectory().toString() + ".LLA");
                if (!direct.exists()) direct.mkdir();
                direct = new File(Environment.getExternalStorageDirectory().toString() + ".LLA/English");
                if (!direct.exists()) direct.mkdir();
                file = new File(Environment.getExternalStorageDirectory().toString() + ".LLA/English/");
                if (file.exists())
                    updateSdCardPath("" + Environment.getExternalStorageDirectory().toString() + "/.LLA/English/");
            }
        }
    }

    public void updateSdCardPath(final String path) {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    COS_Constants.ext_path = path;
                    COSApplication.pradigiPath = path;
                    Log.d("$path", "\n\n\n\n\n\n\n\n\n\nPATH: " + COS_Constants.ext_path + "\n\n\n\n\n\n\\n\n\n\n");
                    appDatabase.getStatusDao().updateValue("SdCardPath", "" + path);
                    BackupDatabase.backup(context);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }
*/
}