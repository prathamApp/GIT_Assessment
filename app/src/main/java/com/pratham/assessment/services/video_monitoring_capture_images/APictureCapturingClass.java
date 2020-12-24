package com.pratham.assessment.services.video_monitoring_capture_images;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.util.SparseIntArray;
import android.view.Surface;


/**
 * Abstract Picture Taking Service.
 *
 * @author hzitoun (zitoun.hamed@gmail.com)
 */
public abstract class APictureCapturingClass {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {

        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private final Activity activity;
    final Context context;
    final CameraManager manager;

    /***
     * constructor.
     *
     * @param activity the activity used to get display manager and the application context
     */
    APictureCapturingClass(final Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    /***
     * @return  orientation
     */
    int getOrientation() {
        final int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
        return ORIENTATIONS.get(2);
    }


    /**
     * starts pictures capturing process.
     *
     * @param listener picture capturing listener
     * @param fileName
     */
    public abstract void startCapturing(final PictureCapturingListener listener, String fileName);
}
