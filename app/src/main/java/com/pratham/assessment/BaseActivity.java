package com.pratham.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.tasks.Task;
import com.pratham.assessment.async.CopyDbToOTG;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.custom_dialogs.CustomLodingDialog;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.EventMessage;
import com.pratham.assessment.interfaces.PermissionResult;
import com.pratham.assessment.services.STTService;
import com.pratham.assessment.services.TTSService;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.PermissionUtils;

import net.alhazmy13.catcho.library.Catcho;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.pratham.assessment.constants.Assessment_Constants.LANGUAGE;
import static com.pratham.assessment.constants.Assessment_Constants.TransferredImages;


/**
 * Created by Ameya on 15-Mar-18.
 */

public class BaseActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener/*, PermissionResult*/ {

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
    private static final int SHOW_OTG_TRANSFER_DIALOG = 9;
    private static final int SDCARD_LOCATION_CHOOSER = 10;
    private static final int SHOW_OTG_SELECT_DIALOG = 11;
    private static final int HIDE_OTG_TRANSFER_DIALOG_SUCCESS = 12;
    private static final int HIDE_OTG_TRANSFER_DIALOG_FAILED = 13;
    CustomLodingDialog pushDialog;
    CustomLodingDialog sd_builder;
    LottieAnimationView push_lottie;
    TextView txt_push_dialog_msg;
    TextView txt_push_error;
    RelativeLayout rl_btn;
    Button ok_btn, eject_btn;

    private AppUpdateManager appUpdateManager;
    private Task<AppUpdateInfo> appUpdateInfoTask;
    private int APP_UPDATE_TYPE_SUPPORTED = AppUpdateType.FLEXIBLE;
    private int REQUEST_UPDATE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assessment_Utility utility = new Assessment_Utility();
        audioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ButtonClickSound = MediaPlayer.create(this, R.raw.click);//new MediaPlayer instance

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ttsService = new TTSService(getApplication());
        ttsService.setActivity(this);
        ttsService.setSpeechRate(0.7f);
        ttsService.setLanguage(new Locale("en", "IN"));

        String langId = FastSave.getInstance().getString(LANGUAGE, "1");
        Assessment_Utility.setLocaleByLanguageId(this, langId);

//        checkForUpdate();
        muteFlg = false;
        Catcho.Builder(this)
                .activity(CatchoActivity_.class)
                .recipients("ankita.lakhamade27@gmail.com")
                .build();
        Log.d("@path@@", AssessmentApplication.assessPath);

        String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_RECORD_AUDIO,
                PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
                PermissionUtils.Manifest_ACCESS_FINE_LOCATION
        };

        /*if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            if (!isPermissionsGranted(this, permissionArray)) {
                askCompactPermissionsInSplash(permissionArray, this);
            }
        }*/
    }

    /*    private void overrideDefaultTypefaces() {
            FontChanger.overrideDefaultFont(this, "DEFAULT", "fonts/lohit_oriya.ttf");
            FontChanger.overrideDefaultFont(this, "MONOSPACE", "fonts/lohit_oriya.ttf");
            FontChanger.overrideDefaultFont(this, "SERIF", "fonts/lohit_oriya.ttf");
            FontChanger.overrideDefaultFont(this, "SANS_SERIF", "fonts/lohit_oriya.ttf");
            FontChanger.overrideDefaultFont(this, "quicksand_bold", "fonts/lohit_oriya.ttf");
        }*/
    public static void setMute(int m) {


        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            if (m == 1 && !muteFlg) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                muteFlg = true;
            } else if (m == 0 && muteFlg) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
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


    /* *//*
     * @param context    current Context
     * @param permission String permission to ask
     * @return boolean true/false
     *//*
    public boolean isPermissionGranted(Context context, String permission) {
        boolean granted = ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED));
        return granted;
    }

    */

    /*
     * @param context     current Context
     * @param permissions String[] permission to ask
     * @return boolean true/false
     */
/*    public boolean isPermissionsGranted(Context context, String permissions[]) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        boolean granted = true;

        for (String permission : permissions) {
            if (!(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }

        return granted;
    }
*/
   /* private void internalRequestPermission(String[] permissionAsk) {
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


    }*/

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

      *//*  if (permissionResult != null) {
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
        }*//*

    }

    *//**
     * @param permission       String permission ask
     * @param permissionResult callback PermissionResult
     *//*
    public void askCompactPermission(String permission, PermissionResult permissionResult) {
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);

    }*/

    /**
     * @param permissions      String[] permissions ask
     * @param permissionResult callback PermissionResult
     */
    public void askCompactPermissions(String permissions[], PermissionResult permissionResult) {
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk, this);

    }

/*    public void openSettingsApp(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            startActivity(intent);
        }


    }*/


    public void ActivityOnPause() {

        setTimer = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("$$$", "BaseActivity-ActivityOnPause");

                appDatabase = AppDatabase.getDatabaseInstance(BaseActivity.this);
                Log.d("$$$", "BaseActivity2-ActivityOnPause");

                /*appDatabase = Room.databaseBuilder(BaseActivity.this,
                        AppDatabase.class, AppDatabase.DB_NAME)
                        .build();*/
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


    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @SuppressLint({"MissingPermission", "SetTextI18n"})
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_OTG_TRANSFER_DIALOG:
                    showSDBuilderDialog();
                    break;
                case SHOW_OTG_SELECT_DIALOG:
                    ShowOTGPushDialog();
                    rl_btn.setVisibility(View.GONE);
                    break;
                case HIDE_OTG_TRANSFER_DIALOG_SUCCESS:
                    push_lottie.setAnimation("success.json");
                    push_lottie.playAnimation();
                    int days = appDatabase.getScoreDao().getTotalActiveDeviceDays();
                    txt_push_dialog_msg.setText("Data of " + days + " days and\n" + TransferredImages + " Images\nCopied Successfully!!");
                    rl_btn.setVisibility(View.VISIBLE);
                    break;
                case HIDE_OTG_TRANSFER_DIALOG_FAILED:
                    push_lottie.setAnimation("error_cross.json");
                    push_lottie.playAnimation();
                    txt_push_dialog_msg.setText(R.string.data_copying_failed);
                    txt_push_dialog_msg.setTextColor(getResources().getColor(R.color.colorRed));
                    txt_push_error.setVisibility(View.GONE);
                    rl_btn.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void ShowOTGPushDialog() {
        pushDialog = new CustomLodingDialog(this);
        pushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pushDialog.setContentView(R.layout.app_send_success_dialog);
        Objects.requireNonNull(pushDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pushDialog.setCancelable(false);
        pushDialog.setCanceledOnTouchOutside(false);
        pushDialog.show();

        push_lottie = pushDialog.findViewById(R.id.push_lottie);
        txt_push_dialog_msg = pushDialog.findViewById(R.id.txt_push_dialog_msg);
        txt_push_error = pushDialog.findViewById(R.id.txt_push_error);
        rl_btn = pushDialog.findViewById(R.id.rl_btn);
        ok_btn = pushDialog.findViewById(R.id.ok_btn);
        eject_btn = pushDialog.findViewById(R.id.eject_btn);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushDialog.dismiss();
            }
        });
        eject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejectOTG();
                pushDialog.dismiss();
            }
        });


    }

    private void ejectOTG() {
        Intent i = new Intent(android.provider.Settings.ACTION_MEMORY_CARD_SETTINGS);
        startActivity(i);
    }

    private void showSDBuilderDialog() {
        final CustomLodingDialog dialog = new CustomLodingDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert_sd_card);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button txt_choose_sd_card = dialog.findViewById(R.id.txt_choose_sd_card);
        txt_choose_sd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(intent, SDCARD_LOCATION_CHOOSER);
                    }
                }, 200);
                dialog.dismiss();

            }
        });

    }


    @Subscribe
    public void updateFlagsWhenPushed(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(Assessment_Constants.OTG_INSERTED)) {
                mHandler.sendEmptyMessage(SHOW_OTG_TRANSFER_DIALOG);
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.BACKUP_DB_COPIED)) {
                mHandler.sendEmptyMessage(HIDE_OTG_TRANSFER_DIALOG_SUCCESS);
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.BACKUP_DB_NOT_COPIED)) {
                mHandler.sendEmptyMessage(HIDE_OTG_TRANSFER_DIALOG_FAILED);
            }
        }
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
        hideSystemUI();
        BackupDatabase.backup(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SDCARD_LOCATION_CHOOSER) {
            if (data != null && data.getData() != null) {
                Uri treeUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                AssessmentApplication.getInstance().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
                mHandler.sendEmptyMessage(SHOW_OTG_SELECT_DIALOG);
                try {

                    new CopyDbToOTG().execute(treeUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUEST_UPDATE) {
            Log.e("########## 4 ->", "Activity Result");
            switch (requestCode) {
                case RESULT_OK:
                    if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.FLEXIBLE) {
                        Log.e("#", "App Updated Successfully");
                    } else {
                        Log.e("#", "Update Started");
                    }
                case RESULT_CANCELED:
                    Log.e("#", "Update Cancelled");
                case ActivityResult.RESULT_IN_APP_UPDATE_FAILED:
                    Log.e("#", "Update Failed");

            }
        }
    }

    private void hideSystemUI() {
// Set the IMMERSIVE flag.
// Set the content to appear under the system bars so that the content
// doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

   /* @Override
    public void permissionGranted() {

    }

    @Override
    public void permissionDenied() {
        Toast.makeText(BaseActivity.this, getString(R.string.give_camera_permissions), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permissionForeverDenied() {
        Toast.makeText(BaseActivity.this, getString(R.string.give_camera_permissions), Toast.LENGTH_SHORT).show();
        finish();

    }*/

    //Flexible Update
    /*   public void checkForUpdate() {
     *//*
        if (BuildConfig.DEBUG) {
            appUpdateManager = new FakeAppUpdateManager(this);
            ((FakeAppUpdateManager) appUpdateManager).setUpdateAvailable(0);
            Log.e("##########  ->", "Fake");
        } else {
*//*
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Log.e("##########  ->", "Original");
//        }
        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(installStateUpdatedListener);

        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        Log.e("########## 1 ->", String.valueOf(appUpdateInfoTask));
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            Log.e("########## 2 ->", "SuccessListener");
            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                    appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                //send message if update is available
                EventMessage updateAvailable = new EventMessage();
                updateAvailable.setMessage("New update available");

                EventBus.getDefault().post(updateAvailable);

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Upgrade to new better version !");
                builder.setCancelable(false);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action
                        dialog.dismiss();
                        if (Assessment_Utility.isDataConnectionAvailable(BaseActivity.this)) {
                          startUpdate();
                        } else {
                            Assessment_Utility.showAlertDialogue(BaseActivity.this, "No internet connection! Try updating later.");
                        }
                    }
                });
                builder.show();


            } else {
                Log.e("########## 5 ->", "No Update available");
            }
        });
    }*/

    //Listener for checking Install Status
  /*  InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        //send message if update is downloaded
                        Log.e("#", "InstallStateUpdated: state: " + state.installStatus());
                        appUpdateManager.completeUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        Log.e("#", "InstallStateInstalled: state: " + state.installStatus());
                        if (appUpdateManager != null) {
                            appUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.e("#", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };
*/
  /*  public void startUpdate() {
        // Start an update.
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfoTask.getResult(),
                    AppUpdateType.FLEXIBLE,
                    this,
                    REQUEST_UPDATE);
            Log.e("########## 3 ->", "All Condition true");
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
*//*        if (BuildConfig.DEBUG) {
            FakeAppUpdateManager fakeAppUpdate = (FakeAppUpdateManager) appUpdateManager;
            if (fakeAppUpdate.isConfirmationDialogVisible()) {
                fakeAppUpdate.userAcceptsUpdate();
                fakeAppUpdate.downloadStarts();
                fakeAppUpdate.downloadCompletes();
                fakeAppUpdate.completeUpdate();
                fakeAppUpdate.installCompletes();
            }
        }*//*

    }*/

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


    private void internalRequestPermission(String[] permissionAsk, Context context) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();

        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(context, permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }
        }


        if (permissionsNotGranted.isEmpty()) {

            if (permissionResult != null)
                permissionResult.permissionGranted();

        } else {

            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            ActivityCompat.requestPermissions((Activity) context, arrayPermissionNotGranted, KEY_PERMISSION);

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

    /**
     * @param permission       String permission ask
     * @param permissionResult callback PermissionResult
     */
    public void askCompactPermission(String permission, PermissionResult permissionResult) {
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk, this);

    }

    /**
     * @param permissions      String[] permissions ask
     * @param permissionResult callback PermissionResult
     */
    public void askCompactPermissionsInSplash(String permissions[], PermissionResult permissionResult, Context context) {
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk, context);

    }

    public void openSettingsApp(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            startActivity(intent);
        }

       /* Intent intent = new Intent("android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS");
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        }

        final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
        final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args";

        Bundle bundle = new Bundle();
        String showArgs = "package:" + context.getPackageName();
        bundle.putString(EXTRA_FRAGMENT_ARG_KEY, showArgs);
        intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, showArgs);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle);

        try {
            context.startActivity(intent);
            String toastText = "Find PowerShade here";
            Toast.makeText(context, toastText, LENGTH_LONG).show();
        } catch (Exception e) {
            // ask user to grant permission manually
        }*/
    }

}