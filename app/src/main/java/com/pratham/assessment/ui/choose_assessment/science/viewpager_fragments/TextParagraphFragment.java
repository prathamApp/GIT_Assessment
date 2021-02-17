package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nex3z.flowlayout.FlowLayout;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.services.stt_service_new.ContinuousSpeechService_New;
import com.pratham.assessment.services.stt_service_new.STT_Result_New;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static com.pratham.assessment.constants.Assessment_Constants.LANGUAGE;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(resName = "layout_text_paragraph_item")
public class TextParagraphFragment extends Fragment implements STT_Result_New.sttView {
    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.ib_mic)
    ImageButton btn_Mic;
    @ViewById(R.id.ib_stop)
    ImageButton btn_Stop;
    @ViewById(R.id.myflowlayout)
    FlowLayout wordFlowLayout;
    @ViewById(R.id.myScrollView)
    ScrollView myScrollView;
/*    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;*/

    AssessmentAnswerListener assessmentAnswerListener;
    Context context;

    ContinuousSpeechService_New continuousSpeechService;
    boolean voiceStart = false, flgPerMarked = false, onSdCard;
    static boolean[] correctArr;
    static boolean[] testCorrectArr;
    public Handler handler, audioHandler, soundStopHandler, colorChangeHandler,
            startReadingHandler, quesReadHandler, endhandler;
    //    public static MediaPlayer mp, mPlayer;
    List<String> splitWords = new ArrayList<String>();
    List<String> splitWordsPunct = new ArrayList<String>();
    List<String> wordsDurationList = new ArrayList<String>();
    List<String> wordsResIdList = new ArrayList<String>();
    boolean playFlg = false, mediaPauseFlag = false, pauseFlg = false, playHideFlg = false;
    int wordCounter = 0, totalPages = 0, correctAnswerCount, pageNo = 1, quesNo = 0, quesPgNo = 0;
    Map<String, Integer> wordCount;

    String answer;
    StringBuffer sttResult = new StringBuffer();
    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;

    public TextParagraphFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        context = getActivity();
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();

        }
        continuousSpeechService = new ContinuousSpeechService_New(context, TextParagraphFragment.this, "");
        continuousSpeechService.resetSpeechRecognizer();
        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        setTextPara();

    }

    public static TextParagraphFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        TextParagraphFragment_ textParagraphFragment = new TextParagraphFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        textParagraphFragment.setArguments(args);
        return textParagraphFragment;
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_true_false_row, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setTrueFalseQuestion();
    }
*/

    public void setTextPara() {
     /*   String para = "";
        if (scienceQuestion.isParaQuestion()) {
            para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
        }
        assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());
*/
        setWords();
        setOdiaFont(getActivity(), question);
//        question.setText(scienceQuestion.getQname());
        if (scienceQuestion.getPhotourl() != null && !scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {

            String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
            final String localPath;
            if (scienceQuestion.getIsQuestionFromSDCard())
                localPath = scienceQuestion.getPhotourl();
            else
                localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


            String path = scienceQuestion.getPhotourl();
            String[] imgPath = path.split("\\.");
            int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                try {
                    InputStream gif;
                    /*if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        Glide.with(getActivity()).asGif()
                                .load(path)
                                .apply(new RequestOptions()
                                        .placeholder(Drawable.createFromPath(localPath)))
                                .into(questionImage);
//                    zoomImg.setVisibility(View.VISIBLE);
                    } else {*/
                        gif = new FileInputStream(localPath);
                        questionImage.setVisibility(View.GONE);
                        questionGif.setVisibility(View.VISIBLE);
                        questionGif.setGifResource(gif);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Glide.with(getActivity())
                        .load(localPath)
                        .apply(new RequestOptions()
                                .placeholder(Drawable.createFromPath(localPath))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(questionImage);
            }
        } else questionImage.setVisibility(View.GONE);

    }

    @Click({R.id.iv_question_image, R.id.iv_question_gif})
    public void questionImageClicked() {
        String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
        final String localPath = AssessmentApplication.assessPath
                + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH
                + "/" + fileName;
        showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
    }

    private void setWords() {
        splitWords = Arrays.asList(scienceQuestion.getQname().split(" "));

        wordCount = new HashMap<String, Integer>();
        StringTokenizer para = new StringTokenizer(scienceQuestion.getQname().toLowerCase());
        int tokenCount = para.countTokens();
        while (para.hasMoreTokens()) {
            String word = para.nextToken();
            Integer count = wordCount.get(word);
            if (count == null) { //this means the word was encountered the first time
                wordCount.put(word, 1);
            } else { //word was already encountered we need to increment the count
                wordCount.put(word, count + 1);
            }
        }
        for (String words : wordCount.keySet()) {
            Log.d("Word : " + words, " has count :" + wordCount.get(words));
        }


        correctArr = new boolean[splitWords.size()];
        for (int i = 0; i < splitWords.size(); i++) {
            correctArr[i] = false;
//            HighlightWords highlightWords = new HighlightWords();
//            highlightWords.setPosition(i);
//            highlightWords.setWord(splitWords.get(i));
//            highlightWords.setHighlighted(false);
//            highlightWordsList.add(highlightWords);
//            splitWordsPunct.add(splitWords.get(i).replaceAll(STT_REGEX_2, ""));
            String myString = Assessment_Utility.removeSpecialCharacters(splitWords.get(i));
            splitWordsPunct.add(myString);
            Log.d("setWords", "setWords: " + myString);
        }
        setWordTOLayout();
    }

    public void setWordTOLayout() {
        for (int i = 0; i < splitWords.size(); i++) {
            if (splitWords.get(i).equalsIgnoreCase("#")) {
                final TextView myTextView = new TextView(context);
                myTextView.setWidth(2000);
                wordFlowLayout.addView(myTextView);
            } else {
                final TextView myTextView = new TextView(context);
                myTextView.setText(Html.fromHtml(splitWords.get(i)));
                myTextView.setId(i);
                myTextView.setTextSize(22);
                myTextView.setTypeface(ResourcesCompat.getFont(context, R.font.quicksand_medium));
                myTextView.setTextColor(getResources().getColor(R.color.white));
                final int finalI = i;
                myTextView.setOnClickListener(v -> {
//                    if (!FastSave.getInstance().getString(FC_Constants.CURRENT_FOLDER_NAME, "").equalsIgnoreCase("maths"))
                    if ((!playFlg || pauseFlg) && !voiceStart) {
//                            setMute(0);
                        myTextView.setTextColor(getResources().getColor(R.color.colorRed));
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                        myTextView.startAnimation(animation);
                        if (colorChangeHandler == null)
                            colorChangeHandler = new Handler();
                        colorChangeHandler.postDelayed(() -> {
                            myTextView.setTextColor(getResources().getColor(R.color.white));
                            Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.zoom_out_new);
                            myTextView.startAnimation(animation1);
                        }, 350);
                         /*   if (!storyAudio.equalsIgnoreCase("NA"))
                                playClickedWord(finalI);*/
//                        ttsService.play("" + linesStringList[finalI]);
                    }
                });
                wordFlowLayout.addView(myTextView);
            }
        }
    }


    @Click(R.id.ib_mic)
    public void onMicClick() {
        voiceStart = true;
        flgPerMarked = false;
//        showLoader();
        btn_Mic.setVisibility(View.GONE);
//        btn_Play.setVisibility(View.GONE);
        btn_Stop.setVisibility(View.VISIBLE);
        try {
            if (quesReadHandler != null) {
                quesReadHandler.removeCallbacksAndMessages(null);
               /* try {
                    if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                        mPlayer.reset();
                        mPlayer.release();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        continuousSpeechService.startSpeechInput();
//        startStoryReading(wordCounter);
    }

    @Override
    public void Stt_onResult(ArrayList<String> sttResult) {
        flgPerMarked = false;
//        if(AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
//            for (String str : sttResult)
//                this.sttResult.append(" " + str);
//        }else {
//
//        }
        sttResultProcess(sttResult, splitWordsPunct, wordsResIdList);

    }

    /*private void getAccurateResult(ArrayList<String> result) {
        String sttResult = "";
//        String sttResult = matches.get(0);
        String sttQuestion;
        for (int i = 0; i < result.size(); i++) {
            System.out.println("LogTag" + " onResults :  " + result.get(i));

            if (result.get(i).equalsIgnoreCase(scienceQuestion.getAnswer()))
                sttResult = result.get(i);
            else sttResult = result.get(0);
        }
        sttQuestion = scienceQuestion.getQname();
        String regex = "[\\-+.\"^?!@#%&*,:]";
        String quesFinal = sttQuestion.replaceAll(regex, "");


        String[] splitQues = quesFinal.split(" ");
        String[] splitRes = sttResult.split(" ");

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
       float perc = ((float) correctCnt / (float) correctArr.length) * 100;
        Log.d("Punctu", "onResults: " + perc);
        if (perc >= 75) {

            for (int i = 0; i < splitQues.length; i++) {
                //((TextView) readChatFlow.getChildAt(i)).setTextColor(getResources().getColor(R.color.readingGreen));
                correctArr[i] = true;
            }

//            scrollView.setBackgroundResource(R.drawable.convo_correct_bg);

        }
    }*/
    @Override
    public void Stt_onPartialResult(String sttResult) {
        startHighlighting(sttResult);
    }

    private void startHighlighting(String sttResult) {
        List<String> splittedResult = Arrays.asList(sttResult.split(" "));
        int len = splittedResult.size();

  /*      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < wordFlowLayout.getChildCount(); j++) {
                    wordFlowLayout.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
            }
        },300);*/


        if (wordFlowLayout != null) {
//            if (scienceQuestion.getQname().toLowerCase().contains(sttResult.toLowerCase())) {
            for (int j = 0; j < wordFlowLayout.getChildCount(); j++) {
                for (int i = 0; i < len; i++) {
                    String curWord = ((TextView) wordFlowLayout.getChildAt(j)).getText().toString();
                    String newWord = Assessment_Utility.removeSpecialCharacters(curWord);
                    if (!newWord.equalsIgnoreCase(""))
                        if (splittedResult.get(i).equalsIgnoreCase(newWord))
//                            if (getHighlightedWord(j, splittedResult.get(i))) {
                            wordFlowLayout.getChildAt(j).setBackgroundColor(Color.BLUE);

                    //                            }
                }
            }
//            }
            new Handler().postDelayed(this::removeHighlight, 2500);
        }
    }

    private void removeHighlight() {
        for (int j = 0; j < wordFlowLayout.getChildCount(); j++) {
            wordFlowLayout.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
        }
    }


    @Click(R.id.ib_stop)
    public void onStopClicked() {
        if (voiceStart) {
            voiceStart = false;
            btn_Stop.setVisibility(View.GONE);
//            if (!FastSave.getInstance().getString(APP_SECTION, "").equalsIgnoreCase(sec_Test) && !playHideFlg)
//                btn_Play.setVisibility(View.VISIBLE);
            btn_Mic.setVisibility(View.VISIBLE);
            continuousSpeechService.stopSpeechInput();
        } else if (playFlg || pauseFlg) {
            wordCounter = 0;
            btn_Stop.setVisibility(View.GONE);
//            btn_Play.setVisibility(View.VISIBLE);
    /*        if (!contentType.equalsIgnoreCase(FC_Constants.RHYME_RESOURCE))
                btn_Mic.setVisibility(View.VISIBLE);
            btn_Play.setImageResource(R.drawable.ic_play_arrow_black);
            startPlayBack = Float.parseFloat(modalPagesList.get(currentPage).getReadList().get(0).getWordFrom());
*/
            try {
                playFlg = false;
                pauseFlg = true;
              /*  try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.reset();
                        mp.release();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                if (startReadingHandler != null)
                    startReadingHandler.removeCallbacksAndMessages(null);
                if (soundStopHandler != null)
                    soundStopHandler.removeCallbacksAndMessages(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setCorrectViewColor();
//        if (getPercentage() > 35) scienceQuestion.setIsCorrect(true);

        assessmentAnswerListener.setAnswerInActivity("" + calculateMarks(), sttResult.toString(), scienceQuestion.getQid(), null);

    }


    @Override
    public void silenceDetected() {
        if (voiceStart) {
            continuousSpeechService.resetHandler(true);
           /* silence_outer_layout.setVisibility(View.VISIBLE);
            silenceViewHandler = new Handler();
            silence_iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_continuous_shake));
            AnimateTextView(context, silence_main_layout);*/
        }
    }

    @Override
    public void stoppedPressed() {

    }

    @Override
    public void sttEngineReady() {

    }

    /*@Override
    public void Stt_onError() {

    }*/


    public void sttResultProcess
            (ArrayList<String> sttResult, List<String> splitWordsPunct, List<String> wordsResIdList) {

        String sttRes = "";
        for (int i = 0; i < sttResult.size(); i++) {
            System.out.println("LogTag" + " onResults :  " + sttResult.get(i));

            if (sttResult.get(i).equalsIgnoreCase(scienceQuestion.getAnswer()))
                sttRes = sttResult.get(i);
            else sttRes = sttResult.get(0);
        }
        String[] splitRes = sttRes.split(" ");
        answer = " ";
//        addSttResultDB(sttResult);

        for (int j = 0; j < splitRes.length; j++) {
            if (FastSave.getInstance().getString(LANGUAGE, "1").equalsIgnoreCase("1"))
                splitRes[j].replaceAll("[^a-zA-Z ]", "");
            else
                splitRes[j] = Assessment_Utility.removeSpecialCharacters(splitRes[j]);
            for (int i = 0; i < splitWordsPunct.size(); i++) {
                if ((splitRes[j].equalsIgnoreCase(splitWordsPunct.get(i))) && !correctArr[i]) {
                    correctArr[i] = true;
                    answer = answer + " " + splitWordsPunct.get(i) /*+ "(" + wordsResIdList.get(i) + "),"*/;
                    break;
                }
            }
        }
//        getPercentage();
//        int correctWordCount = getCorrectCounter();
        String wordTime = Assessment_Utility.getCurrentDateTime();
//        addLearntWords(splitWordsPunct, wordsResIdList);
//        addScore(0, "Words:" + word, correctWordCount, correctArr.length, wordTime, " ");
        this.sttResult.append(" ").append(sttRes);
        assessmentAnswerListener.setAnswerInActivity("" + calculateMarks(), this.sttResult.toString(), scienceQuestion.getQid(), null);

    }

    private int calculateMarks() {
        int marks = (getPercentage() * Integer.parseInt(scienceQuestion.getOutofmarks())) / 100;
        return marks;
    }

    public int getPercentage() {
        int corrCnt = getCorrectCounter();
        int perc = (corrCnt * 100) / splitWords.size();
        Log.d("$$$", "getPercentage: " + getCorrectCounter() + " " + splitWords.size() + " " + perc);
        return perc;
    }


    public int getCorrectCounter() {
        int counter = 0;

        for (int x = 0; x < correctArr.length; x++)
            if (correctArr[x])
                counter++;
        return counter;
    }


    public void setCorrectViewColor() {
        try {
            for (int x = 0; x < correctArr.length; x++) {
                if (correctArr[x]) {
                    (wordFlowLayout.getChildAt(x)).setBackgroundColor(getResources().getColor(R.color.transparent));
                    ((TextView) wordFlowLayout.getChildAt(x)).setTextColor(getResources().getColor(R.color.green));
                }
            }
            getPercentage();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (voiceStart)
//            sttMethod();
    }


    /*public void startStoryReading(final int index) {
        float wordDuration = 1;
        handler = new Handler();
        colorChangeHandler = new Handler();
//        mp.start();
        TextView myNextView = null;

        if (index < wordsDurationList.size()) {
            wordDuration = Float.parseFloat(wordsDurationList.get(index));
            final TextView myView = (TextView) wordFlowLayout.getChildAt(index);
            if (index < wordFlowLayout.getChildCount())
                myNextView = (TextView) wordFlowLayout.getChildAt(index + 1);

            if (myNextView != null)
                isScrollBelowVisible(myNextView);
            myView.setTextColor(getResources().getColor(R.color.colorRed));
            myView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.reading_zoom_in);
//            myView.startAnimation(animation);
//            wordPopUp(this, myView);
            colorChangeHandler.postDelayed(() -> {
                myView.setTextColor(getResources().getColor(R.color.white));
                myView.setBackgroundColor(getResources().getColor(R.color.transparent));
//                    wordPopDown(ReadingStoryActivity.this, myView);
//                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.reading_zoom_out);
//                myView.startAnimation(animation1);
            }, 350);
            if (index == wordsDurationList.size() - 1) {
                try {
                    handler.postDelayed(() -> {
                        try {
                            playFlg = false;
                            pauseFlg = true;
                           *//* if (!contentType.equalsIgnoreCase(FC_Constants.RHYME_RESOURCE)) {
                                btn_Mic.setVisibility(View.VISIBLE);
                                btn_Stop.setVisibility(View.GONE);
                                wordCounter = 0;
                                quesReadHandler = new Handler();
                                quesReadHandler.postDelayed(() -> {
                                    Collections.shuffle(readSounds);
                                    mPlayer = MediaPlayer.create(context, readSounds.get(0));
                                    mPlayer.start();
                                }, (long) (5000));
                            }*//*
//                            btn_Stop.performClick();
//                            layout_mic_ripple.startRippleAnimation();
//                            layout_ripplepulse_right.startRippleAnimation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, (long) (wordDuration * 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPauseFlag = true;
            }
        } else
            wordDuration = 1;

        handler.postDelayed(() -> {
            if (playFlg && !pauseFlg) {
                if (index < wordFlowLayout.getChildCount()) {
                    wordCounter += 1;
                    if (!pauseFlg) {
                        startStoryReading(wordCounter);
                    }
                } else {
                    for (int i = 0; i < wordsDurationList.size(); i++) {
                        TextView myView = (TextView) wordFlowLayout.getChildAt(i);
                        myView.setBackgroundColor(Color.TRANSPARENT);
                        myView.setTextColor(getResources().getColor(R.color.white));
                    }
                    wordCounter = 0;
                }
            }
        }, (long) (wordDuration * 1000));

    }
*/

    /*private void addSttResultDB(ArrayList<String> stt_Result) {
     *//* String deviceId = AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("DeviceId");
        StringBuilder strWord = new StringBuilder("STT_ALL_RESULT - ");
        for(int i =0 ; i<stt_Result.size(); i++) {
            strWord.append(stt_Result.get(i)).append(" - ");
            stt_Result.size();
            if(i > 0 && i < 3)
                remainingResult.add(stt_Result.get(i));
        }
        try {
            Score score = new Score();
            score.setSessionID(FastSave.getInstance().getString(Assessment_Constants.CURRENT_SESSION, ""));
            score.setResourceID(resId);
            score.setQuestionId(0);
            score.setScoredMarks(0);
            score.setTotalMarks(0);
            score.setStudentID(FastSave.getInstance().getString(FC_Constants.CURRENT_STUDENT_ID, ""));
            score.setStartDateTime(resStartTime);
            score.setDeviceID(deviceId.equals(null) ? "0000" : deviceId);
            score.setEndDateTime(FC_Utility.getCurrentDateTime());
            score.setLevel(0);
            score.setLabel(""+strWord);
            score.setSentFlag(0);
            appDatabase.getScoreDao().insert(score);
            BackupDatabase.backup(context);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
    }*/

    /*   public void addScore(int wID, String Word, int scoredMarks, int totalMarks, String
               resStartTime, String Label) {
          *//* try {
            String deviceId = appDatabase.getStatusDao().getValue("DeviceId");
            Score score = new Score();
            score.setSessionID(FastSave.getInstance().getString(FC_Constants.CURRENT_SESSION, ""));
            score.setResourceID(resId);
            score.setQuestionId(wID);
            score.setScoredMarks(scoredMarks);
            score.setTotalMarks(totalMarks);
            score.setStudentID(FastSave.getInstance().getString(FC_Constants.CURRENT_STUDENT_ID, ""));
            score.setStartDateTime(resStartTime);
            score.setDeviceID(deviceId.equals(null) ? "0000" : deviceId);
            score.setEndDateTime(FC_Utility.getCurrentDateTime());
            score.setLevel(0);
            score.setLabel(Word + " - " + Label);
            score.setSentFlag(0);
            appDatabase.getScoreDao().insert(score);

            if (FastSave.getInstance().getString(APP_SECTION,"").equalsIgnoreCase(sec_Test)) {
                Assessment assessment = new Assessment();
                assessment.setResourceIDa(resId);
                assessment.setSessionIDa(FastSave.getInstance().getString(FC_Constants.ASSESSMENT_SESSION, ""));
                assessment.setSessionIDm(FastSave.getInstance().getString(FC_Constants.CURRENT_SESSION, ""));
                assessment.setQuestionIda(wID);
                assessment.setScoredMarksa(scoredMarks);
                assessment.setTotalMarksa(totalMarks);
                assessment.setStudentIDa(FastSave.getInstance().getString(FC_Constants.CURRENT_ASSESSMENT_STUDENT_ID, ""));
                assessment.setStartDateTimea(resStartTime);
                assessment.setDeviceIDa(deviceId.equals(null) ? "0000" : deviceId);
                assessment.setEndDateTime(FC_Utility.getCurrentDateTime());
                assessment.setLevela(FC_Constants.currentLevel);
                assessment.setLabel("test: " + Label);
                assessment.setSentFlag(0);
                appDatabase.getAssessmentDao().insert(assessment);
            }

            BackupDatabase.backup(context);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
    }
*/
    private void isScrollBelowVisible(View view) {
        Rect scrollBounds = new Rect();
        myScrollView.getDrawingRect(scrollBounds);

        float top = view.getY();
        float bottom = top + view.getHeight();

        if (!(scrollBounds.top < top) || !(scrollBounds.bottom > bottom))
            view.getParent().requestChildFocus(view, view);
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE
      /*  String para = "";
        if (scienceQuestion != null) {
            if (scienceQuestion.isParaQuestion()) {
                btn_view_hint.setVisibility(View.VISIBLE);
//                para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
            } else btn_view_hint.setVisibility(View.GONE);
//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        } else {
            btn_view_hint.setVisibility(View.GONE);

//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        }*/


    }

   /* @Click(R.id.btn_view_hint)
    public void showPara() {
        if (scienceQuestion != null) {
            if (scienceQuestion.isParaQuestion()) {
                String para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
                showZoomDialog(getActivity(), "", "", para);
            }
        }
    }*/
}
