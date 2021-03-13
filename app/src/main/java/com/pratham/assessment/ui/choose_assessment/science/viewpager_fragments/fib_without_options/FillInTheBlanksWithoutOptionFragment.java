package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_without_options;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.viewpagerAdapter;
import static com.pratham.assessment.constants.Assessment_Constants.STT_REGEX_3;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_fill_in_the_blanks_wo_option_row)
public class FillInTheBlanksWithoutOptionFragment extends Fragment
        implements FIB_WithoutOption_Contract.FIB_WithoutOption_View,
        STT_Result {

    @Bean(FIB_WithoutOption_Presenter.class)
    FIB_WithoutOption_Contract.FIB_WithoutOptionPresenter presenter;

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

    private static boolean voiceStart = false;
    public static boolean[] correctArr;
    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";
    private ScienceQuestion scienceQuestion;
    AssessmentAnswerListener assessmentAnswerListener;

    @AfterViews
    public void init() {
        if (getArguments() != null) {
            int pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
            speechService = new ContinuousSpeechService(getActivity(), this);
            speechService.resetSpeechRecognizer();
        }
        presenter.setView(FillInTheBlanksWithoutOptionFragment.this);
        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        setFillInTheBlanksQuestion();
    }

    //TODO MAYBE UTILITY
    public static FillInTheBlanksWithoutOptionFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        FillInTheBlanksWithoutOptionFragment_ fragment = new FillInTheBlanksWithoutOptionFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        fragment.setArguments(args);

        return fragment;
    }

    @UiThread
    @Override
    public void reInitCurrentItems() {
        etAnswer = Objects.requireNonNull(viewpagerAdapter.getCurrentFragment().getView()).findViewById(R.id.et_answer);
        ib_mic = viewpagerAdapter.getCurrentFragment().getView().findViewById(R.id.ib_mic);
        micPressed(0);
    }

    @Override
    @UiThread
    public void appendText(String sttResultStr) {
        etAnswer.append(" " + sttResultStr);
    }

    public void setFillInTheBlanksQuestion() {

        if (!AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
            if (!Assessment_Constants.SELECTED_LANGUAGE.equals("1") && !Assessment_Constants.SELECTED_LANGUAGE.equals("2")) {
                ib_mic.setVisibility(View.INVISIBLE);
            }

        etAnswer.setTextColor(Assessment_Utility.selectedColor);
        etAnswer.setText(Html.fromHtml(scienceQuestion.getUserAnswer()));
        question.setText(Html.fromHtml(scienceQuestion.getQname()));

        setOdiaFont(getActivity(), question);

        correctArr = new boolean[scienceQuestion.getAnswer().
                replaceAll(STT_REGEX_3, "")
                .split(" ")
                .length];

        if (scienceQuestion.getPhotourl() != null &&  !scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
            String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
            final String localPath;
            if (scienceQuestion.getIsQuestionFromSDCard())
                localPath = scienceQuestion.getPhotourl();
            else
                localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;

            String path = scienceQuestion.getPhotourl();
            String[] imgPath = path.split("\\.");
            int len = 0;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                try {
                    InputStream gif;
 /*                   if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        Glide.with(Objects.requireNonNull(getActivity())).asGif()
                                .load(path)
                                .apply(new RequestOptions()
                                        .placeholder(Drawable.createFromPath(localPath)))
                                .into(questionImage);
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
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(localPath)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(questionImage);
            }
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
//                Log.d("@@@"+scienceQuestion.getQid(), s+"");
                assessmentAnswerListener.setAnswerInActivity("", s.toString(), scienceQuestion.getQid(), null);
            }
        });
    }


    @Click({R.id.iv_question_image, R.id.iv_question_gif})
    public void questionImageClicked() {
        String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
        final String localPath = AssessmentApplication.assessPath
                + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH
                + "/" + fileName;
        showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
    }

    @Click(R.id.ib_mic)
    public void onMicClicked() {
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
            if (micPressed == 0)
                ib_mic.setImageResource(R.drawable.ic_mic_24dp);
            else
                ib_mic.setImageResource(R.drawable.ic_stop_black_24dp);

            ib_mic.setElevation(5);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (speechService != null) {
            speechService.stopSpeechInput();
            micPressed(0);
            voiceStart = false;
        }
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
    }

    @Override
    public void Stt_onResult(ArrayList<String> sttResult) {
//        micPressed(0);
        System.out.println("LogTag" + " onResults");
        presenter.processSTT_Result(scienceQuestion.getAnswer(), sttResult);
    }

    @Override
    public void Stt_onError() {
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
            } else btn_view_hint.setVisibility(View.GONE);

        } else {
            btn_view_hint.setVisibility(View.GONE);
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
