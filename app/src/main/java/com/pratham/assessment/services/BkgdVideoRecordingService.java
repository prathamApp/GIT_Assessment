package com.pratham.assessment.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;

import com.pratham.assessment.utilities.Assessment_Constants;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

public class BkgdVideoRecordingService extends Service {
    private static final String TAG = "RecorderService";
    private static Camera mServiceCamera;
    private SurfaceHolder mSurfaceHolder;
    private boolean mRecordingStatus;
    private static MediaRecorder mMediaRecorder;

    @Override
    public void onCreate() {
        mRecordingStatus = false;
        //mServiceCamera = CameraRecorder.mCamera;
        if (mServiceCamera == null)
            mServiceCamera = Camera.open(1);
//        SurfaceView mSurfaceView = Activity_WebView.surfaceView;
//        mSurfaceHolder = Activity_WebView.mSurfaceHolder;

        super.onCreate();
        if (!mRecordingStatus)
            startRecording();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        stopRecording();
        mRecordingStatus = false;
        super.onDestroy();
    }

    public boolean startRecording() {
        try {
            Log.d(TAG, "startRecording: ");
            //mServiceCamera = Camera.open();
//            mServiceCamera = openFrontFacingCamera();
            // Both are required for Portrait Video

       /*     mServiceCamera.setDisplayOrientation(90);
            Camera.Parameters params = mServiceCamera.getParameters();
            mServiceCamera.setParameters(params);
            Camera.Parameters p = mServiceCamera.getParameters();
            final List<Camera.Size> listSize = p.getSupportedPreviewSizes();
            Camera.Size mPreviewSize = listSize.get(2);
            Log.v(TAG, "use: width = " + mPreviewSize.width
                    + " height = " + mPreviewSize.height);
            p.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
            mServiceCamera.setParameters(p);

            try {
                mServiceCamera.setPreviewDisplay(mSurfaceHolder);
                mServiceCamera.stopPreview();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }*/





            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
                mServiceCamera.unlock();

                mMediaRecorder.setCamera(mServiceCamera);
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            }
            // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
//            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
            //todo change video path to app internal

//            mMediaRecorder.setOutputFile(new File(Environment.getExternalStorageDirectory() + "/myVideo.mp4").getAbsolutePath());
            mMediaRecorder.setOutputFile(new File(Environment.getExternalStorageDirectory() + "/.Assessment/Content/videoMonitoring/" + "myVideo.mp4").getAbsolutePath());
//            mMediaRecorder.setVideoFrameRate(30);
//            mMediaRecorder.setVideoSize(mPreviewSize.width, mPreviewSize.height);
//            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            mRecordingStatus = true;

            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Camera openFrontFacingCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.toString());
                }
            } else {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.toString());
                }
            }
        }
        return cam;
    }

    public void stopRecording() {
        Log.d(TAG, "stopRecording: ");
        try {
            mServiceCamera.reconnect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {

            mMediaRecorder.stop();
            mMediaRecorder.reset();

            mServiceCamera.stopPreview();
            mMediaRecorder.release();

            mServiceCamera.release();
            mServiceCamera = null;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
