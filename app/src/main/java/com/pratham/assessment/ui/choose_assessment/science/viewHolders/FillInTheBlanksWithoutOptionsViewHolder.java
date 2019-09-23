package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.voice_ripple.Renderer;
import com.pratham.assessment.custom.voice_ripple.TimerCircleRippleRenderer;
import com.pratham.assessment.custom.voice_ripple.VoiceRippleView;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FillInTheBlanksWithoutOptionsViewHolder extends RecyclerView.ViewHolder implements RecognitionListener {
    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;
    @BindView(R.id.et_answer)
    EditText etAnswer;
    @BindView(R.id.ib_mic)
    VoiceRippleView ib_mic;
    ScienceQuestion scienceQuestion;
    QuestionTypeListener questionTypeListener;
    AssessmentAnswerListener assessmentAnswerListener;
    public static SpeechRecognizer speech = null;
    private static boolean voiceStart = false, correctArr[];
    public static Intent intent;
    private Intent recognizerIntent;
    Context context;
    private float perc = 0;

    Renderer currentRenderer;
    public FillInTheBlanksWithoutOptionsViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        questionTypeListener = scienceAdapter;
        assessmentAnswerListener = (ScienceAssessmentActivity) context;
        ib_mic.setRecordDrawable(context.getResources().getDrawable(R.drawable.ic_mic_black_24dp),
                context.getResources().getDrawable(R.drawable.ic_hearing_black_24dp));

        currentRenderer = new TimerCircleRippleRenderer(getDefaultRipplePaint(), getDefaultRippleBackgroundPaint(), getButtonPaint(), getArcPaint(), 10000.0, 0.0);
        if (currentRenderer instanceof TimerCircleRippleRenderer) {
            ((TimerCircleRippleRenderer) currentRenderer).setStrokeWidth(20);
        }
        ib_mic.setRenderer(currentRenderer);
        resetSpeechRecognizer();

    }

    public void setFillInTheBlanksQuestion(ScienceQuestion scienceQuestion1, int pos) {
        this.scienceQuestion = scienceQuestion1;
        etAnswer.setTextColor(Assessment_Utility.selectedColor);
        etAnswer.setText(scienceQuestion.getUserAnswer());
        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().
                    load(/*Assessment_Constants.loadOnlineImagePath +*/ scienceQuestion.getPhotourl())
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable bd = new BitmapDrawable(resource);
                            questionImage.setImageDrawable(bd);
                        }
                    });
        } else questionImage.setVisibility(View.GONE);

        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                questionTypeListener.setAnswer("", etAnswer.getText().toString(), scienceQuestion.getQid(), null);
                assessmentAnswerListener.setAnswerInActivity("", etAnswer.getText().toString(), scienceQuestion.getQid(), null);
            }
        });

    }

    @OnClick(R.id.ib_mic)
    public void onMicClicked() {
        if (ib_mic.isRecording()) ib_mic.stopRecording();
        else
            ib_mic.startRecording();
        callSTT();
    }

    public void callSTT() {
        if (!voiceStart) {
            voiceStart = true;
            // btn_reading.setImageResource(R.drawable.ic_stop_black_24dp);
//            micPressed(1);
            startSpeechInput();
        } else {
            voiceStart = false;
//            micPressed(0);
            stopSpeechInput();
        }
    }


    public void micPressed(int micPressed) {
        if (micPressed == 0) {
            ib_mic.setRecordDrawable(context.getResources().getDrawable(R.drawable.ic_mic_24dp), context.getResources().getDrawable(R.drawable.ic_mic_24dp));
            // disableWordsSentences(true);

        }// mic.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
        else if (micPressed == 1) {
            //mic.setBackground(getResources().getDrawable(R.drawable.ripple_effect_selected));
//            ib_mic.setImageResource(R.drawable.ic_stop_black_24dp);
            ib_mic.setRecordDrawable(context.getResources().getDrawable(R.drawable.ic_mic_24dp), context.getResources().getDrawable(R.drawable.ic_mic_24dp));

            //disableWordsSentences(false);

        }

    }

    private void stopSpeechInput() {
        speech.stopListening();
    }

    private void startSpeechInput() {
        setRecogniserIntent();
        speech.startListening(recognizerIntent);
    }

    private void setRecogniserIntent() {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "hi");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");
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
        Log.d("FillInTheBlanks", "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(context));
        if (SpeechRecognizer.isRecognitionAvailable(context))
            speech.setRecognitionListener(this);
        else {//finish();
        }
    }


    @Override
    public void onReadyForSpeech(Bundle params) {

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
        voiceStart = false;
//        micPressed(0);
        ib_mic.stopRecording();
    }

    @Override
    public void onResults(Bundle results) {
//        micPressed(0);
        ib_mic.stopRecording();
        System.out.println("LogTag" + " onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String sttResult = "";
//        String sttResult = matches.get(0);
        String sttQuestion = "";
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).equalsIgnoreCase(scienceQuestion.getAnswer()))
                sttResult = matches.get(i);
            else sttResult = matches.get(0);
        }
        sttQuestion = scienceQuestion.getAnswer();
        String quesFinal = sttQuestion.replaceAll("[\\-\\+\\.\\^\\?\\'\\!:,]", "");


        String splitQues[] = quesFinal.split(" ");
        String splitRes[] = sttResult.split(" ");

        if (splitQues.length < splitRes.length)
            correctArr = new boolean[splitRes.length];
        else correctArr = new boolean[splitQues.length];


        for (int j = 0; j < splitRes.length; j++) {
            for (int i = 0; i < splitQues.length; i++) {
                if (splitRes[j].equalsIgnoreCase(splitQues[i])) {
                    // ((TextView) readChatFlow.getChildAt(i)).setTextColor(getResources().getColor(R.color.readingGreen));
                    correctArr[i] = true;
                    //sendClikChanger(1);
                    break;
                }
            }
        }


        int correctCnt = 0;
        for (int x = 0; x < correctArr.length; x++) {
            if (correctArr[x])
                correctCnt++;
        }
        perc = ((float) correctCnt / (float) correctArr.length) * 100;
        Log.d("Punctu", "onResults: " + perc);
        if (perc >= 75) {

            for (int i = 0; i < splitQues.length; i++) {
                //((TextView) readChatFlow.getChildAt(i)).setTextColor(getResources().getColor(R.color.readingGreen));
                correctArr[i] = true;
            }

//            scrollView.setBackgroundResource(R.drawable.convo_correct_bg);

        }
        etAnswer.setText(sttResult);
        voiceStart = false;

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    private Paint getArcPaint() {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }
    private Paint getDefaultRipplePaint() {
        Paint ripplePaint = new Paint();
        ripplePaint.setStyle(Paint.Style.FILL);
        ripplePaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        ripplePaint.setAntiAlias(true);

        return ripplePaint;
    }

    private Paint getDefaultRippleBackgroundPaint() {
        Paint rippleBackgroundPaint = new Paint();
        rippleBackgroundPaint.setStyle(Paint.Style.FILL);
        rippleBackgroundPaint.setColor((ContextCompat.getColor(context, R.color.colorPrimary) & 0x00FFFFFF) | 0x40000000);
        rippleBackgroundPaint.setAntiAlias(true);

        return rippleBackgroundPaint;
    }

    private Paint getButtonPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }
}
