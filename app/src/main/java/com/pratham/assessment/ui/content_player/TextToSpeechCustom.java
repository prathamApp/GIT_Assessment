package com.pratham.assessment.ui.content_player;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Abc on 27-Apr-17.
 */

public class TextToSpeechCustom {
    public static TextToSpeech textToSpeech;
    public static Context mContext;
    HashMap<String, String> map;

    public TextToSpeechCustom(Context mContext, float spRate) {
        super();
        this.mContext = mContext;
        try {
            textToSpeech = new TextToSpeech(mContext, new TtsListener(spRate));
            textToSpeech.setOnUtteranceProgressListener(new TtsUtteranceListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ttsFunction(final String toSpeak, String lang, float sprate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(toSpeak, lang,sprate);
        } else {
            ttsUnder20(toSpeak, lang,sprate);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text, String lang,float sprate) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");

        if (lang.equals("hin"))
            textToSpeech.setLanguage(new Locale("hi", "IN"));
        else
            textToSpeech.setLanguage(new Locale("en", "IN"));

        textToSpeech.setSpeechRate(sprate);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text, String lang, float sprate) {
        String utteranceId = this.hashCode() + "";

        if (lang.equals("hin"))
            textToSpeech.setLanguage(new Locale("hi", "IN"));
        else
            textToSpeech.setLanguage(new Locale("en", "IN"));

        textToSpeech.setSpeechRate(sprate);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    public static void speechRate(float rate){
        textToSpeech.setSpeechRate(rate);
    }

    public void stopSpeaker() {
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
            textToSpeech.shutdown();
        }
/*
        if (textToSpeechEng.isSpeaking())
            textToSpeechEng.stop();
*/

    }

    public void stopSpeakerDuringJS() {
        if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
    }

}