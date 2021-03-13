package com.pratham.assessment.utilities;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;

import com.pratham.assessment.ui.choose_assessment.science.interfaces.AudioPlayerInterface;

import java.io.IOException;
import java.net.URI;

public class AudioUtil {

    private static MediaRecorder mRecorder;
    private static MediaPlayer mPlayer;
    public static AudioPlayerInterface audioPlayerInterface;

    public AudioUtil() {
    }

    public static void startRecording(String filePath) {
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setOutputFile(filePath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRecording() {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
    }

    public static void playRecording(Uri filePath, final AudioPlayerInterface activity, Context context) {
        try {
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.stop();

            mPlayer = new MediaPlayer();
            audioPlayerInterface = activity;
            mPlayer.setDataSource(context,filePath);
            mPlayer.prepare();
            mPlayer.start();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayingAudio();
                 /*   if (activity instanceof LanguageActivity)
                        ((LanguageActivity) activity).audioStopped();
                    if (activity instanceof EnglishActivity)
                        ((EnglishActivity) activity).audioStopped();
                    if (activity instanceof MathActivity)
                        ((MathActivity) activity).audioStopped();*/
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public static void playRecording(String filePath, final AudioPlayerInterface activity) {
        try {
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.stop();

            mPlayer = new MediaPlayer();
            audioPlayerInterface = activity;
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayingAudio();
                 /*   if (activity instanceof LanguageActivity)
                        ((LanguageActivity) activity).audioStopped();
                    if (activity instanceof EnglishActivity)
                        ((EnglishActivity) activity).audioStopped();
                    if (activity instanceof MathActivity)
                        ((MathActivity) activity).audioStopped();*/
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopPlayingAudio() {
        audioPlayerInterface.stopPlayer();

        try {
            if (mPlayer != null)
                mPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer = null;
    }


}