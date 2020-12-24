package com.pratham.assessment.services.stt_service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.constants.Assessment_Constants;

import java.util.ArrayList;

import static com.pratham.assessment.constants.Assessment_Constants.LANGUAGE;

/**
 * Created by Ameya on 12-03-2019.
 */

public class ContinuousSpeechService implements RecognitionListener {
    private static ContinuousSpeechService instance = null;
    private static Intent recognizerIntent;
    private static SpeechRecognizer speech = null;
    public Context context;
    STT_Result stt_result;
    boolean voiceStart = false;

    String LOG_TAG = "ContinuousSpeechService : ", sttResult;

    public ContinuousSpeechService(Context context, STT_Result stt_result) {
        this.context = context;
        this.stt_result = stt_result;
        resetSpeechRecognizer();
    }

    public void setRecogniserIntent() {
        String lang_pref = getSelectedLanguageCode();
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, lang_pref);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang_pref + "-IN");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    private String getSelectedLanguageCode() {
        String lang_code = "en";
        Assessment_Constants.SELECTED_LANGUAGE = FastSave.getInstance().getString(LANGUAGE, "1");
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.ENGLISH_ID))
            lang_code = Assessment_Constants.ENGLISH_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.HINDI_ID))
            lang_code = Assessment_Constants.HINDI_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.MARATHI_ID))
            lang_code = Assessment_Constants.MARATHI_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.GUJARATI_ID))
            lang_code = Assessment_Constants.GUJARATI_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.KANNADA_ID))
            lang_code = Assessment_Constants.KANNADA_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.ASSAMESE_ID))
            lang_code = Assessment_Constants.ASSAMESE_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.BENGALI_ID))
            lang_code = Assessment_Constants.BENGALI_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.PUNJABI_ID))
            lang_code = Assessment_Constants.PUNJABI_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.ODIA_ID))
            lang_code = Assessment_Constants.ODIA_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.TAMIL_ID))
            lang_code = Assessment_Constants.TAMIL_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.TELUGU_ID))
            lang_code = Assessment_Constants.TELUGU_CODE;
        if (Assessment_Constants.SELECTED_LANGUAGE.equalsIgnoreCase(Assessment_Constants.URDU_ID))
            lang_code = Assessment_Constants.URDU_CODE;

        return lang_code;
    }


    public void resetSpeechRecognizer() {
        if (speech != null)
            try {
                speech.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(context));
        if (SpeechRecognizer.isRecognitionAvailable(context))
            speech.setRecognitionListener(this);
    }

    public void stopSpeechInput() {
        voiceStart = false;
        speech.stopListening();
    }

    public void startSpeechInput() {
        setRecogniserIntent();
        voiceStart = true;
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {
//        setMute(1);
        Log.d(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
        speech.stopListening();
    }

    @Override
    public void onError(int error) {
        if (voiceStart) {
            resetSpeechRecognizer();
            speech.startListening(recognizerIntent);

            String message = "";
            if (error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) message = "network timeout";
            else if (error == SpeechRecognizer.ERROR_NETWORK) message = "network";
            else if (error == SpeechRecognizer.ERROR_AUDIO) message = "audio";
            else if (error == SpeechRecognizer.ERROR_SERVER) message = "server";
            else if (error == SpeechRecognizer.ERROR_CLIENT) message = "client";
            else if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) message = "speech timeout";
            else if (error == SpeechRecognizer.ERROR_NO_MATCH) message = "no match found";
            else if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) message = "recognizer busy";
            else if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS)
                message = "insufficient permissions";
            Log.d("error ", message);
            stt_result.Stt_onError();
        }
    }

    @Override
    public void onResults(Bundle results) {
        voiceStart = false;
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

//        sttResult = matches.get(0);
        Log.d("STT-Res", "\n");

        stt_result.Stt_onResult(matches);
        if (!voiceStart) {
            resetSpeechRecognizer();
        } else
            speech.startListening(recognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
}
