package com.pratham.assessment.ui.content_player;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioPlayer extends Thread {
    MediaRecorder mediaRecorder;
    String mediaFilePath;
    String internalStoragePath;

    /**
     * Give the thread high priority so that it's not canceled unexpectedly, and start it
     */
    public AudioPlayer(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
    }

    @Override
    public void run() {
        try {
            try {
                internalStoragePath = Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/Recordings/";
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setAudioEncodingBitRate(96000);
                mediaRecorder.setAudioSamplingRate(44100);
                mediaRecorder.setOutputFile(internalStoragePath+mediaFilePath);
                mediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaRecorder.start();

        } catch (Throwable x) {
            Log.w("Audio", "Error reading voice audio", x);
        }
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */
    public void close() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}