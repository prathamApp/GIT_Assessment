package com.pratham.assessment.services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.pratham.assessment.interfaces.SpeechResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by HP on 11-04-2018.
 */

public class STTService {
    private static STTService instance = null;
    private SpeechRecognizer mSpeechRecognizer;
    private SpeechResult mSpeechResult;
    private boolean mIsListening = false;
    private final List<String> mPartialData = new ArrayList<>();
    private String mUnstableData;
    private Context mContext;
    private Locale mLocale = Locale.getDefault();
    private long mStopListeningDelayInMs = 4000;
    private long mTransitionMinimumDelay = 1200;
    private List<String> mLastPartialResults = null;
    private static Intent intent;

    private STTService(final Context context) {
        initSpeechRecognizer(context);
    }

    /**
     * Initializes speech recognition.
     *
     * @param context application context
     * @return speech instance
     */
    public static STTService init(final Context context) {
        if (instance == null) {
            instance = new STTService(context);
        }

         intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
//                .putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, /*mLocale.getLanguage()*/"en-IN")
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        }

        return instance;

    }

    /**
     * Initializes speech interface callback.
     */
    public void initCallback(final SpeechResult speechResult) {
        this.mSpeechResult = speechResult;
    }

    private final RecognitionListener mListener = new RecognitionListener() {

        @Override
        public void onReadyForSpeech(final Bundle bundle) {
            mPartialData.clear();
            mUnstableData = null;
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            returnPartialResultsAndRecreateSpeechRecognizer();
        }

        @Override
        public void onResults(Bundle bundle) {
            final List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String result;
            if (results != null && !results.isEmpty()
                    && results.get(0) != null && !results.get(0).isEmpty()) {
                result = results.get(0);
            } else {
                Log.d(STTService.class.getSimpleName(), "No speech results, getting partial");
                result = getPartialResultsAsString();
            }            mIsListening = false;
            mSpeechResult.onResult(result);
            mSpeechResult.onResult(results);
            initSpeechRecognizer(mContext);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            final List<String> partialResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final List<String> unstableData = bundle.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
            if (partialResults != null && !partialResults.isEmpty()) {
                mPartialData.clear();
                mPartialData.addAll(partialResults);
                mUnstableData = unstableData != null && !unstableData.isEmpty()
                        ? unstableData.get(0) : null;
                try {
                    if (mLastPartialResults == null || !mLastPartialResults.equals(partialResults)) {
                        mLastPartialResults = partialResults;
                    }
                } catch (final Throwable exc) {
                    Log.d(STTService.class.getSimpleName(),
                            "Unhandled exception in delegate onSpeechPartialResults", exc);
                }
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

    };

    private void initSpeechRecognizer(final Context context) {
        if (context == null)
            throw new IllegalArgumentException("context must be defined!");
        mContext = context;
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            if (mSpeechRecognizer != null) {
                try {
                    mSpeechRecognizer.destroy();
                } catch (final Throwable exc) {
                    Log.d(STTService.class.getSimpleName(),
                            "Non-Fatal error while destroying speech. " + exc.getMessage());
                } finally {
                    mSpeechRecognizer = null;
                }
            }
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mSpeechRecognizer.setRecognitionListener(mListener);
        } else {
            mSpeechRecognizer = null;
        }

        mPartialData.clear();
        mUnstableData = null;
    }

    /**
     * Must be called inside Activity's onDestroy.
     */
    public synchronized void shutdown() {
        if (mSpeechRecognizer != null) {
            try {
                mSpeechRecognizer.stopListening();
            } catch (final Exception exc) {
                Log.d(getClass().getSimpleName(), "Warning while de-initing speech recognizer", exc);
            }
        }
        instance = null;
    }

    /**
     * Gets speech recognition instance.
     *
     * @return SpeechRecognition instance
     */
    public static STTService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Speech recognition has not been initialized! call init method first!");
        }
        return instance;
    }


    /**
     * Starts voice recognition.
     */
    public void startListening() {
        if (mIsListening) return;
        if (mSpeechRecognizer == null)
            throw new IllegalStateException("Speech recognition has not been initialized! call init method first!");

        try {
            mSpeechRecognizer.startListening(intent);
        } catch (final SecurityException exc) {
            Log.d(STTService.class.getSimpleName(), "startListening: not able to listen");
        }
        mIsListening = true;
    }

    /**
     * Stops voice recognition listening.
     * This method does nothing if voice listening is not active
     */
    public void stopListening() {
        if (!mIsListening) return;

        mIsListening = false;
        returnPartialResultsAndRecreateSpeechRecognizer();
    }

    private String getPartialResultsAsString() {
        final StringBuilder out = new StringBuilder("");
        for (final String partial : mPartialData) {
            out.append(partial).append(" ");
        }
        if (mUnstableData != null && !mUnstableData.isEmpty())
            out.append(mUnstableData);
        return out.toString().trim();
    }

    private void returnPartialResultsAndRecreateSpeechRecognizer() {
        mIsListening = false;
        // recreate the speech recognizer
        initSpeechRecognizer(mContext);
    }

    /**
     * Check if voice recognition is currently active.
     *
     * @return true if the voice recognition is on, false otherwise
     */
    public boolean isListening() {
        return mIsListening;
    }

    /**
     * Sets text to speech and recognition language.
     * Defaults to device language setting.
     *
     * @param locale new locale
     * @return speech instance
     */
    public STTService setLocale(final Locale locale) {
        mLocale = locale;
        return this;
    }
}
