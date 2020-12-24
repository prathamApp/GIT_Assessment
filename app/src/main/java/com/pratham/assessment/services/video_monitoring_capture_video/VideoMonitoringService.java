package com.pratham.assessment.services.video_monitoring_capture_video;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.TextureView;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.constants.Assessment_Constants;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VideoMonitoringService extends Service {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;

    private static Camera mCamera;
    private static MediaRecorder mMediaRecorder;
    private static File mOutputFile;
    private static boolean isRecording = false;


    private final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
    };

    private IBinder ibinder = new MySeviceBinder();

    class MySeviceBinder extends Binder {
        public VideoMonitoringService getService() {
            return VideoMonitoringService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return ibinder;
    }

    @Override
    public void onDestroy() {
        Log.d("service:::", " destroyed" + Thread.currentThread().getId());
        super.onDestroy();
        releaseMediaRecorder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service:::", "" + Thread.currentThread().getId());
        mIsRandomGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                // startRandomNumberGenerator();

                //     startCapture();

            }
        }).start();
        return START_STICKY;
    }

    public static void startCapture() {

        // Camera is available and unlocked, MediaRecorder is prepared,
        // now you can start recording
        try {
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


    }

    public static boolean prepareVideoRecorder(TextureView textureView, String fileName) {

        // BEGIN_INCLUDE (configure_preview)
        // mCamera = CameraHelper.getDefaultCameraInstance();
        if (!isRecording) {
            isRecording = true;
            if (mCamera == null)
                mCamera = CameraHelper.getDefaultFrontFacingCameraInstance();

            // We need to make sure that our preview and recording video size are supported by the
            // camera. Query camera to find all the sizes and choose the optimal size given the
            // dimensions of our preview surface.
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
            List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
            Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                    mSupportedPreviewSizes, 300, 300);
            // Use the same size for recording profile.
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
            profile.videoFrameWidth = optimalSize.width;
            profile.videoFrameHeight = optimalSize.height;


            // likewise for the camera object itself.
            parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
            mCamera.setParameters(parameters);
            try {
                // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
                // with {@link SurfaceView}
                mCamera.setPreviewTexture(textureView.getSurfaceTexture());
            } catch (IOException e) {
                Log.e("service:::", "Surface texture is unavailable or unsuitable" + e.getMessage());
                return false;
            }
            // END_INCLUDE (configure_preview)


            // BEGIN_INCLUDE (configure_media_recorder)
            mMediaRecorder = new MediaRecorder();

            // Step 1: Unlock and set camera to MediaRecorder
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);

            // Step 2: Set sources
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
            mMediaRecorder.setProfile(profile);

            // Step 4: Set output file
            File root = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_VIDEO_MONITORING_PATH);
            if (!root.exists()) root.mkdir();
            mOutputFile = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_VIDEO_MONITORING_PATH +"/"+ fileName);

//            CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO);
            if (mOutputFile == null) {
                return false;
            }
            mMediaRecorder.setOutputFile(mOutputFile.getAbsolutePath());
            // END_INCLUDE (configure_media_recorder)

            // Step 5: Prepare configured MediaRecorder
            try {
                mMediaRecorder.prepare();
            } catch (IllegalStateException e) {
                Log.d("service:::", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
                releaseMediaRecorder();
                return false;
            } catch (IOException e) {
                Log.d("Service:::", "IOException preparing MediaRecorder: " + e.getMessage());
                releaseMediaRecorder();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


    public static void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
            releaseCamera();
        }
    }

    private static void releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
            isRecording = false;
        }
    }
}
