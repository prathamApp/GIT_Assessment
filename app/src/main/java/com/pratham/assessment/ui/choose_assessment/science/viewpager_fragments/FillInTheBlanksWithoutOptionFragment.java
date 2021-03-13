package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.services.stt_service.ContinuousSpeechService;
import com.pratham.assessment.services.stt_service.STT_Result;
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
import java.util.Objects;

import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.viewpagerAdapter;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_fill_in_the_blanks_wo_option_row)
public class FillInTheBlanksWithoutOptionFragment extends Fragment implements STT_Result {

    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.et_answer)
    EditText etAnswer;
    @ViewById(R.id.ib_mic)
    ImageButton ib_mic;
    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;

    ContinuousSpeechService speechService;

    private float perc = 0;

    //    public static SpeechRecognizer speech = null;
    private static boolean voiceStart = false, correctArr[];
    public static Intent intent;
    private Intent recognizerIntent;

    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;
    AssessmentAnswerListener assessmentAnswerListener;
    private Context context;

    public FillInTheBlanksWithoutOptionFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
            context = getActivity();
            speechService = new ContinuousSpeechService(context, this);
            speechService.resetSpeechRecognizer();
        }
        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        setFillInTheBlanksQuestion();
    }

    public static FillInTheBlanksWithoutOptionFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        FillInTheBlanksWithoutOptionFragment_ fragment = new FillInTheBlanksWithoutOptionFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        fragment.setArguments(args);

        return fragment;
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
            context = getActivity();
            speechService = new ContinuousSpeechService(context, this);
            speechService.resetSpeechRecognizer();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fill_in_the_blanks_wo_option_row, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        question.setMovementMethod(new ScrollingMovementMethod());
        setFillInTheBlanksQuestion();
    }*/

    private void reInitCurrentItems() {
        if (viewpagerAdapter != null) {
            etAnswer = Objects.requireNonNull(viewpagerAdapter.getCurrentFragment().getView()).findViewById(R.id.et_answer);
            ib_mic = Objects.requireNonNull(viewpagerAdapter.getCurrentFragment().getView()).findViewById(R.id.ib_mic);
        }
//        ib_mic = viewpagerAdapter.getCurrentFragment().getView().findViewById(R.id.ib_mic);
    }

    public void setFillInTheBlanksQuestion() {
     /*   String para = "";
        if (scienceQuestion.isParaQuestion()) {
            para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
        }
        assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());
*/
        if (!AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
            if (!Assessment_Constants.SELECTED_LANGUAGE.equals("1") && !Assessment_Constants.SELECTED_LANGUAGE.equals("2")) {
                ib_mic.setVisibility(View.INVISIBLE);
            }

        etAnswer.setTextColor(Assessment_Utility.selectedColor);
        etAnswer.setText(Html.fromHtml(scienceQuestion.getUserAnswer()));
        question.setText(Html.fromHtml(scienceQuestion.getQname()));
        setOdiaFont(getActivity(), question);

        if (scienceQuestion.getPhotourl() != null && !scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {

            String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
            final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


            String path = scienceQuestion.getPhotourl();
            String[] imgPath = path.split("\\.");
            int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                try {
                    InputStream gif;
                    if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        Glide.with(getActivity()).asGif()
                                .load(path)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .placeholder(Drawable.createFromPath(localPath)))
                                .into(questionImage);
//                    zoomImg.setVisibility(View.VISIBLE);
                    } else {
                        gif = new FileInputStream(localPath);
                        questionImage.setVisibility(View.GONE);
                        questionGif.setVisibility(View.VISIBLE);
                        questionGif.setGifResource(gif);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Glide.with(getActivity())
                        .load(path)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(questionImage);
            }

            questionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
                }
            });
            questionGif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
                }
            });

         /*   } else {
                String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                questionImage.setImageBitmap(bitmap);
            }*/
        } else questionImage.setVisibility(View.GONE);


        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("@@@", "onTextChanged");


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("@@@", "beforeTextChanged");
//                onStop();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("@@@", "afterTextChanged");
                assessmentAnswerListener.setAnswerInActivity("", s.toString(), scienceQuestion.getQid(), null);
//                speechService.resetSpeechRecognizer();

            }
        });

    }


    @Click(R.id.ib_mic)
    public void onMicClicked() {
        callSTT();
    }

    public void callSTT() {
        if (!voiceStart) {
            voiceStart = true;
            micPressed(1);
            speechService.startSpeechInput();
        } else {
            voiceStart = false;
            micPressed(0);
            speechService.stopSpeechInput();
        }
    }


    public void micPressed(int micPressed) {
        if (ib_mic != null) {
            if (micPressed == 0) {
                ib_mic.setImageResource(R.drawable.ic_mic_24dp);
            } else if (micPressed == 1) {
                ib_mic.setImageResource(R.drawable.ic_stop_black_24dp);
            }
            ib_mic.setElevation(5);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
       /* if (speech != null) {
            speech.stopListening();*/
        if (speechService != null)
            speechService.stopSpeechInput();
        micPressed(0);
        voiceStart = false;
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (speechService != null) {
            speechService.stopSpeechInput();
            speechService.resetSpeechRecognizer();
        }
        micPressed(0);
        voiceStart = false;
       /* if (speech != null) {
            speech.stopListening();
        }*/
        if (speechService != null)
            speechService.stopSpeechInput();

    }

/*    @Override
    public void onResume() {
        super.onResume();
        speechService.resetSpeechRecognizer();
       *//* if (speech != null) {
            micPressed(0);
            voiceStart = false;
        }*//*
    }*/

    @Override
    public void Stt_onResult(ArrayList<String> matches) {
        micPressed(0);
//        ib_mic.stopRecording();
        System.out.println("LogTag" + " onResults");
//        ArrayList<String> matches = results;

        String sttResult = "";
//        String sttResult = matchses.get(0);
        String sttQuestion;
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("LogTag" + " onResults :  " + matches.get(i));

            if (matches.get(i).equalsIgnoreCase(scienceQuestion.getAnswer()))
                sttResult = matches.get(i);
            else sttResult = matches.get(0);
        }
        sttQuestion = scienceQuestion.getAnswer();
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
        perc = ((float) correctCnt / (float) correctArr.length) * 100;
        Log.d("Punctu", "onResults: " + perc);
        if (perc >= 75) {

            for (int i = 0; i < splitQues.length; i++) {
                //((TextView) readChatFlow.getChildAt(i)).setTextColor(getResources().getColor(R.color.readingGreen));
                correctArr[i] = true;
            }

//            scrollView.setBackgroundResource(R.drawable.convo_correct_bg);

        }
        reInitCurrentItems();

        etAnswer.append(" " + sttResult);
        voiceStart = false;
        micPressed(0);

    }

    @Override
    public void Stt_onError() {

    }

   /* @Override
    public void Stt_onPartialResult(String sttResult) {

    }*/

   /* @Override
    public void silenceDetected() {
        if (voiceStart) {
            speechService.resetHandler(true);
           *//* silence_outer_layout.setVisibility(View.VISIBLE);
            silenceViewHandler = new Handler();
            silence_iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_continuous_shake));
            AnimateTextView(context, silence_main_layout);*//*
        }
    }*/

  /*  @Override
    public void stoppedPressed() {

    }

    @Override
    public void sttEngineReady() {

    }
*/

    /*@Override
    public void Stt_onError() {
        voiceStart = false;
        micPressed(0);
    }*/

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
        speechService.resetSpeechRecognizer();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE
        String para = "";
        if (scienceQuestion != null) {
            ScienceQuestion scienceQuestion = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getQuestionByQID(this.scienceQuestion.getQid());
            if (scienceQuestion.isParaQuestion()) {
                btn_view_hint.setVisibility(View.VISIBLE);
//                para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
            } else btn_view_hint.setVisibility(View.GONE);
//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        } else {
            btn_view_hint.setVisibility(View.GONE);

//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        }


    }

    @Click(R.id.btn_view_hint)
    public void showPara() {
        if (scienceQuestion != null) {
            if (scienceQuestion.isParaQuestion()) {
                String para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
                showZoomDialog(getActivity(), "", "", para);
            }
        }
    }
}
