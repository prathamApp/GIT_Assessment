package com.pratham.assessment.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.interfaces.PermissionResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Ameya on 15-Mar-18.
 */

public class SplashSupportActivity extends BaseActivity implements MediaPlayer.OnCompletionListener {

  /*  public static TTSService ttsService;
    public static STTService sttService;
*/
    static CountDownTimer cd;
    static Long timeout = (long) 20000 * 60;
    static Long duration = timeout;
    static Boolean setTimer = false;
    static String pauseTime;

    private final int KEY_PERMISSION = 200;
    private PermissionResult permissionResult;
    private String permissionsAsk[];
    private static AudioManager audioManager;
    public static MediaPlayer ButtonClickSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
 //       ButtonClickSound = MediaPlayer.create(this, R.raw.click);//new MediaPlayer instance
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
            if (!isPermissionGranted(SplashSupportActivity.this, permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }
        }


        if (permissionsNotGranted.isEmpty()) {

            if (permissionResult != null)
                permissionResult.permissionGranted();

        } else {

            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            ActivityCompat.requestPermissions(SplashSupportActivity.this, arrayPermissionNotGranted, KEY_PERMISSION);

        }


    }

   /* @Override
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

        if (permissionResult != null) {
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
        }

    }
*/
    /**
     * @param permission       String permission ask
     * @param permissionResult callback PermissionResult
     */
    public void askCompactPermission(String permission, PermissionResult permissionResult) {
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);

    }

    /*
     * @param permissions      String[] permissions ask
     * @param permissionResult callback PermissionResult
     */
   /* public void askCompactPermissionsInSplash(String permissions[], PermissionResult permissionResult) {
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);

    }
*/
    public void openSettingsApp(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            startActivity(intent);
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCompletion(MediaPlayer mp) { }
}