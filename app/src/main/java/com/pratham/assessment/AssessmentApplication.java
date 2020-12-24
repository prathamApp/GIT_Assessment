package com.pratham.assessment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.isupatches.wisefy.WiseFy;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.font.FontChanger;
import com.pratham.assessment.constants.APIs;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.pratham.assessment.constants.Assessment_Constants.PREFS_VERSION;

public class AssessmentApplication extends Application {


    public static final boolean isTablet = false;

    public static String cosPath = "", networkSSID = "PrathamHotSpot-" + Build.SERIAL;
    public static String uploadDataUrl = "http://swap.prathamcms.org/api/Assessment/AssesmentPushData";
    public static String uploadScienceUrl = APIs.baseAzureURL + "api/pushassessment/AssessmentPushData";
    public static String uploadScienceFilesUrl = APIs.baseAzureURL + "api/question/pushFiles";
    String sdCardPathString = null;
    public static MediaPlayer bubble_mp, bgMusic;
    public static WiseFy wiseF;
    public static boolean contentExistOnSD = false, LocationFlg = false;
    public static String contentSDPath = "";
    public static SharedPreferences sharedPreferences;

    //    public static HashMap<String, FileState> sendFileStates;
//    public static HashMap<String, FileState> recieveFileStates;
    public static String IMAG_PATH;
    public static String VOICE_PATH;
    public static String VEDIO_PATH;
    public static String MUSIC_PATH;
    public static String FILE_PATH;
    public static String assessPath = "";
//    OkHttpClient okHttpClient;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    public static AssessmentApplication assessmentApplication;
    private static final DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    public static String path;

    @Override
    public void onCreate() {
        super.onCreate();
        if (assessmentApplication == null) {
            assessmentApplication = this;
        }
        FastSave.init(getApplicationContext());

        sharedPreferences = getSharedPreferences(PREFS_VERSION, Context.MODE_PRIVATE);
        assessPath = Assessment_Utility.getInternalPath(this);

        if (assessPath != null) {
            File mydir = null;
            mydir = new File(assessPath + "/.Assessment");
            if (!mydir.exists())
                mydir.mkdirs();
            mydir = new File(assessPath + "/.Assessment/Content");
            if (!mydir.exists())
                mydir.mkdirs();
         /*   mydir = new File(assessPath + "/.Assessment/English/Game");
            if (!mydir.exists())
                mydir.mkdirs();*/
        }
        Log.d("COS.pradigiPath", "COS.pradigiPath: " + Assessment_Constants.STORING_IN + "++" + assessPath);

//        new UCEHandler.Builder(this).addCommaSeparatedEmailAddresses("ankita.lakhamade27@gmail.com").setTrackActivitiesEnabled(true).build();


//        makeDir();
//        if (Assessment_Constants.SMART_PHONE)
//            Assessment_Constants.ext_path = assessPath;


/*        bgMusic = MediaPlayer.create(this, R.raw.bg_sound);
        bgMusic.start();

        bgMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bgMusic.start();
            }
        });*/
//        bubble_mp = MediaPlayer.create(this, R.raw.click);
        wiseF = new WiseFy.Brains(getApplicationContext()).logging(true).getSmarts();
//        setCOSPath();
//        sendFileStates = new HashMap<String, FileState>();
//        recieveFileStates = new HashMap<String, FileState>();
//        okHttpClient = new OkHttpClient().newBuilder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .build();
//        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);


        // overrideDefaultTypefaces();

    }

    private void overrideDefaultTypefaces() {
        FontChanger.overrideDefaultFont(this, "DEFAULT", "fonts/lohit_oriya.ttf");
        FontChanger.overrideDefaultFont(this, "MONOSPACE", "fonts/lohit_oriya.ttf");
        FontChanger.overrideDefaultFont(this, "SERIF", "fonts/lohit_oriya.ttf");
        FontChanger.overrideDefaultFont(this, "SANS_SERIF", "fonts/lohit_oriya.ttf");
        FontChanger.overrideDefaultFont(this, "quicksand_bold", "fonts/lohit_oriya.ttf");
    }

    private void makeDir() {
        String folder = assessPath + "/.Assessment";
        File f = new File(folder);
        if (!f.exists()) f.mkdirs();

    }


    /*public String getExtPath() {
        String sdCardPathString = null;
        ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(AssessmentApplication);
        for (String path : sdcard_path) {
            if (new File(path + "/.EngGame").exists()) {
                sdCardPathString = path + "/.EngGame/";
            }
        }
        ext_path = sdCardPathString;
        return sdCardPathString;
    }*/

    public static String getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return dateTimeFormat.format(cal.getTime());
    }


    public static String getVersion() {
        Context context = AssessmentApplication.assessmentApplication;
        String packageName = context.getPackageName();
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Unable to find the name", packageName + " in the package");
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static UUID getUniqueID() {
        return UUID.randomUUID();
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDateTime(boolean timerTime, String appStartTime) {
        if (timerTime) {
            return assessmentApplication.getAccurateTimeStamp(appStartTime);
        } else {
            Calendar cal = Calendar.getInstance();
            return dateTimeFormat.format(cal.getTime());
        }
    }

    public static AssessmentApplication getInstance() {
        return assessmentApplication;
    }

    public static int getRandomNumber(int min, int max) {
        return min + (new Random().nextInt(max));
    }

    public void setCOSPath() {
        cosPath = Assessment_Utility.getInternalPath(this) + "/" + "English";
        File f = new File(cosPath);
        if (!f.exists()) f.mkdirs();
    }

    static int count;
    static Timer timer;

    public static void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count++;
            }
        }, 1000, 1000);
    }

    public static void resetTimer() {
        if (timer != null) {
            timer.cancel();
            count = 0;
        } else {
            count = 00;
        }
    }

    public static int getTimerCount() {
        return count;
    }

    public static String getAccurateTimeStamp(String appStartTime) {
        try {
            Log.d("TAG:::", "getAccurateTimeStamp: " + appStartTime);
            Date gpsDateTime = dateTimeFormat.parse(appStartTime);
            // Add Seconds to Gps Date Time
            Calendar addSec = Calendar.getInstance();
            addSec.setTime(gpsDateTime);
            Log.d("doInBackground", "getTimerCount: " + getTimerCount());
            addSec.add(addSec.SECOND, getTimerCount());

            return dateTimeFormat.format(addSec.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasRealRemovableSdCard(Context context) {
        return ContextCompat.getExternalFilesDirs(context, null).length >= 2;
    }

    public static String getAccurateDate() {
        // String to Date
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String gpsTime = BaseActivity.appDatabase.getStatusDao().getValue("GPSDateTime");
                    Date gpsDateTime = null;
                    gpsDateTime = dateFormat.parse(gpsTime);
                    // Add Seconds to Gps Date Time
                    Calendar addSec = Calendar.getInstance();
                    addSec.setTime(gpsDateTime);
                    addSec.add(addSec.SECOND, getTimerCount());
                    String updatedTime = dateFormat.format(addSec.getTime());
                    return updatedTime;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute();
        return null;
    }

    public static String getUploadDataUrl() {
        return uploadDataUrl;
    }

    public void setExistingSDContentPath(String path) {
        contentExistOnSD = true;
        contentSDPath = path;
    }

}
