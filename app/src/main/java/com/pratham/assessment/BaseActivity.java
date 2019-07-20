package com.pratham.assessment;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;


import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.interfaces.PermissionResult;
import com.pratham.assessment.services.STTService;
import com.pratham.assessment.services.TTSService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Ameya on 15-Mar-18.
 */

public class BaseActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    public static TTSService ttsService;
    public static STTService sttService;

    static CountDownTimer cd;
    static Long timeout = (long) 20000 * 60;
    static Long duration = timeout;
    static Boolean setTimer = false;
    static String pauseTime;

    private final int KEY_PERMISSION = 200;
    private PermissionResult permissionResult;
    private String permissionsAsk[];
    public static AppDatabase appDatabase;
    private static AudioManager audioManager;
    public static MediaPlayer ButtonClickSound;
    public static boolean muteFlg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ButtonClickSound = MediaPlayer.create(this, R.raw.click);//new MediaPlayer instance

        ttsService = new TTSService(getApplication());
        ttsService.setActivity(this);
        ttsService.setSpeechRate(0.7f);
        ttsService.setLanguage(new Locale("en", "IN"));
        sttService = STTService.init(getApplicationContext());
//        appDatabase = Room.databaseBuilder(BaseActivity.this,
//                AppDatabase.class, AppDatabase.DB_NAME)
//                .allowMainThreadQueries()
//                .build();

        muteFlg = false;

        //new MediaPlayer instance
         /*Catcho.Builder(this)
                .activity(CatchoTransparentActivity.class)
                .recipients("your-email@domain.com")
                .build();
*/
    }

    public static void setMute(int m) {

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            if (m == 1 && !muteFlg) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                muteFlg = true;
            } else if (m == 0 && muteFlg) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);
                muteFlg = false;
            }
        } else {

            if (m == 1 && !muteFlg) {
//            audioManager.adjustVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
//                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
//                audioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//                audioManager.setStreamMute(AudioManager.STREAM_RING, true);
//                audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                muteFlg = true;
            } else if (m == 0 && muteFlg) {
//            audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
//                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
//                audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
//                audioManager.setStreamMute(AudioManager.STREAM_RING, false);
//                audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                muteFlg = false;
            }
        }
    }


    /**
     * @param context    current Context
     * @param permission String permission to ask
     * @return boolean true/false
     */
    public boolean isPermissionGranted(Context context, String permission) {
        boolean granted = ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED));
        return granted;
    }

    /**
     * @param context     current Context
     * @param permissions String[] permission to ask
     * @return boolean true/false
     */
    public boolean isPermissionsGranted(Context context, String permissions[]) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        boolean granted = true;

        for (String permission : permissions) {
            if (!(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }

        return granted;
    }


    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();

        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(BaseActivity.this, permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }
        }


        if (permissionsNotGranted.isEmpty()) {

            if (permissionResult != null)
                permissionResult.permissionGranted();

        } else {

            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            ActivityCompat.requestPermissions(BaseActivity.this, arrayPermissionNotGranted, KEY_PERMISSION);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode != KEY_PERMISSION) {
            return;
        }

        List<String> permissionDenied = new LinkedList<>();
        boolean granted = true;

        for (int i = 0; i < grantResults.length; i++) {

            if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                granted = false;
                permissionDenied.add(permissions[i]);
            }

        }

      /*  if (permissionResult != null) {
            if (granted) {
                permissionResult.permissionGranted();
            } else {
                for (String s : permissionDenied) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, s)) {
                        permissionResult.permissionForeverDenied();
                        return;
                    }
                }

                permissionResult.permissionDenied();


            }
        }*/

    }

    /**
     * @param permission       String permission ask
     * @param permissionResult callback PermissionResult
     */
    public void askCompactPermission(String permission, PermissionResult permissionResult) {
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);

    }

    /**
     * @param permissions      String[] permissions ask
     * @param permissionResult callback PermissionResult
     */
    public void askCompactPermissions(String permissions[], PermissionResult permissionResult) {
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);

    }

    public void openSettingsApp(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            startActivity(intent);
        }


    }


    public void ActivityOnPause() {

        setTimer = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase = Room.databaseBuilder(BaseActivity.this,
                        AppDatabase.class, AppDatabase.DB_NAME)
                        .build();
                String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");
                if (AppStartDateTime == null)
                    pauseTime = AssessmentApplication.getCurrentDateTime(false, "");
                else
                    pauseTime = AssessmentApplication.getCurrentDateTime(true, AppStartDateTime);
                return null;
            }
        }.execute();
        Log.d("APP_END", "onFinish: Startd the App: " + duration);
        Log.d("APP_END", "onFinish: Startd the App: " + pauseTime);

        cd = new CountDownTimer(duration, 1000) {
            //cd = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                duration = millisUntilFinished;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onFinish() {
                new AsyncTask<Object, Void, Object>() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        try {

                            String curSession = appDatabase.getStatusDao().getValue("CurrentSession");
                            String toDateTemp = appDatabase.getSessionDao().getToDate(curSession);

                            Log.d("AppExitService:", "curSession : " + curSession + "      toDateTemp : " + toDateTemp);

                            if (toDateTemp.equalsIgnoreCase("na")) {
                                appDatabase.getSessionDao().UpdateToDate(curSession, pauseTime);
                            }
                            BackupDatabase.backup(BaseActivity.this);
                            finishAffinity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();


            }
        }.start();
    }

    public void ActivityResumed() {
        if (setTimer) {
            setTimer = false;
            cd.cancel();
            duration = timeout;
            Log.d("APP_END", "ActivityResumed: in IF: " + duration);
        }
        Log.d("APP_END", "ActivityResumed: duration: " + duration);

    }


    @Override
    protected void onPause() {
        super.onPause();
        ActivityOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityResumed();
        BackupDatabase.backup(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}