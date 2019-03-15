package com.pratham.assessment.services.stt_service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import java.util.ArrayList;
import static com.pratham.assessment.BaseActivity.setMute;

/**
 * Created by Ameya on 12-03-2019.
 */

public class ContinuousSpeechService implements RecognitionListener {
    private static ContinuousSpeechService instance = null;
    private static Intent recognizerIntent;
    private static SpeechRecognizer speech = null;
    public Context context;
    boolean voiceStart = false;
    STT_Result stt_result;
    String LOG_TAG="ContinuousSpeechService : ", sttResult;

    public ContinuousSpeechService(Context context, STT_Result stt_result) {
        this.context = context;
        this.stt_result = stt_result;
        resetSpeechRecognizer();
    }

    public void setRecogniserIntent() {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
    }

    public void resetSpeechRecognizer() {
        if (speech != null)
            speech.destroy();
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(context));
        if (SpeechRecognizer.isRecognitionAvailable(context))
            speech.setRecognitionListener(this);
    }

    public void stopSpeechInput() {
        voiceStart = false;
        setMute(0);
        speech.stopListening();
    }

    public void startSpeechInput() {
        voiceStart = true;
        setRecogniserIntent();
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) { }

    @Override
    public void onBeginningOfSpeech() {
        setMute(1);
        Log.d(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) { }

    @Override
    public void onBufferReceived(byte[] buffer) { }

    @Override
    public void onEndOfSpeech() {
        speech.stopListening();
    }

    @Override
    public void onError(int error) {
        if (voiceStart) {
            resetSpeechRecognizer();
            speech.startListening(recognizerIntent);
        }
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        sttResult = matches.get(0);
        Log.d("STT-Res", "\n");

        stt_result.Stt_onResult(sttResult);

        if (!voiceStart) {
            resetSpeechRecognizer();
        } else
            speech.startListening(recognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle partialResults) { }

    @Override
    public void onEvent(int eventType, Bundle params) { }
}
