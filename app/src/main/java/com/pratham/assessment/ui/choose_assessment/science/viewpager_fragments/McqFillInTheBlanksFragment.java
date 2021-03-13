package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AudioPlayerInterface;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.AudioUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.utilities.Assessment_Utility.getFileExtension;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_mcq_fill_in_the_blanks_with_options_row)
public class McqFillInTheBlanksFragment extends Fragment implements AudioPlayerInterface {

    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_view_question_img)
    ImageView iv_view_question_img;
    @ViewById(R.id.rl_question_img)
    RelativeLayout rl_question_img;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.rg_mcq)
    RadioGroup radioGroupMcq;
    @ViewById(R.id.grid_mcq)
    GridLayout gridMcq;
    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;

    private AssessmentAnswerListener assessmentAnswerListener;
    private List<ScienceQuestionChoice> options;
    int clickedOption = 0;

    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int imgCnt = 0, textCnt = 0, audioCnt = 0;
    private ScienceQuestion scienceQuestion;

    boolean isQuestionPlaying = false;
    boolean isOptionPlaying = false;
    View prevView;
    View currentView;

    public McqFillInTheBlanksFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        if (getArguments() != null) {
//            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();

        }


        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        setMcqsQuestion();

    }

    public static McqFillInTheBlanksFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        McqFillInTheBlanksFragment_ fragmentFirst = new McqFillInTheBlanksFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_mcq_fill_in_the_blanks_with_options_row, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setMcqsQuestion();
    }*/

    private void setMcqsQuestion() {

        options = new ArrayList<>();
        question.setText(Html.fromHtml(scienceQuestion.getQname()));
        setOdiaFont(getActivity(), question);

        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        if (scienceQuestion.getPhotourl() != null && !scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            String questionExtension = getFileExtension(scienceQuestion.getPhotourl());
            String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
            final String localPath;
            if (scienceQuestion.getIsQuestionFromSDCard())
                localPath = scienceQuestion.getPhotourl();
            else
                localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


            final String path = scienceQuestion.getPhotourl();
            if (questionExtension.equalsIgnoreCase("gif") || questionExtension.equalsIgnoreCase("png") || questionExtension.equalsIgnoreCase("jpg") || questionExtension.equalsIgnoreCase("jpeg")) {
                questionImage.setVisibility(View.VISIBLE);
                rl_question_img.setVisibility(View.VISIBLE);
//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {


           /* String[] imgPath = path.split("\\.");
            int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;*/
                if (questionExtension.equalsIgnoreCase("gif")) {
                    try {
                        InputStream gif;
                        /*if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                            Glide.with(getActivity()).asGif()
                                    .load(path)
                                    .apply(new RequestOptions()
                                            .placeholder(Drawable.createFromPath(localPath)))
                                    .into(questionImage);
                        } else {*/
                        gif = new FileInputStream(localPath);
                        questionImage.setVisibility(View.GONE);
                        questionGif.setVisibility(View.VISIBLE);
                        questionGif.setGifResource(gif);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

           /*     Glide.with(getActivity()).asGif()
                        .load(path)
                        .apply(new RequestOptions()
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(questionImage);*/
//                    zoomImg.setVisibility(View.VISIBLE);
                } else {
                    Glide.with(getActivity())
                            .load(localPath)
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .placeholder(Drawable.createFromPath(localPath)))
                            .into(questionImage);
                }
                iv_view_question_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
                  /*  MaryPopup marypopup = MaryPopup.with(getActivity())
                            .cancellable(true)
                            .blackOverlayColor(Color.parseColor("#DD444444"))
                            .backgroundColor(Color.parseColor("#EFF4F5"))
                            .center(true)
                            .content(R.layout.popup_layout)
                            .from(questionImage);
                    marypopup.show();*/

                    }
                });
          /*  questionGif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath);
                }
            });*/
           /* } else {
                String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
                final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                setImage(questionImage, localPath);
                questionImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showZoomDialog(localPath);
                    }
                });
            }*/
            } else {
                questionImage.setVisibility(View.VISIBLE);
                iv_view_question_img.setVisibility(View.GONE);
                questionImage.setImageResource(R.drawable.ic_play_circle);
                questionImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isQuestionPlaying) {
                            isQuestionPlaying = false;
                            questionImage.setImageResource(R.drawable.ic_play_circle);
                            AudioUtil.stopPlayingAudio();
                            stopPlayer();

                        } else {
                            isQuestionPlaying = true;
                            questionImage.setImageResource(R.drawable.ic_pause);
                            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                                AudioUtil.playRecording(path, McqFillInTheBlanksFragment.this);
                            else
                                AudioUtil.playRecording(localPath, McqFillInTheBlanksFragment.this);
                        }
                    }
                });
            }
        } else {
            questionImage.setVisibility(View.GONE);
            rl_question_img.setVisibility(View.GONE);
        }

        options.clear();
        options = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        imgCnt = 0;
        textCnt = 0;
        if (options != null) {
            radioGroupMcq.removeAllViews();
            gridMcq.removeAllViews();

            for (int r = 0; r < options.size(); r++) {
                if (options.get(r).getChoiceurl() != null && !options.get(r).getChoiceurl().equalsIgnoreCase("")) {
                    String ansExtension = getFileExtension(options.get(r).getChoiceurl());
                    if (ansExtension.equalsIgnoreCase("gif") ||
                            ansExtension.equalsIgnoreCase("png") ||
                            ansExtension.equalsIgnoreCase("jpg") ||
                            ansExtension.equalsIgnoreCase("jpeg")) {
                        imgCnt++;
                    } else {
                        audioCnt++;
                    }
                }
                if (!options.get(r).getChoicename().equalsIgnoreCase("")) {
                    textCnt++;
                }

            }
            for (int r = 0; r < options.size(); r++) {

                String ans = "$";
                if (!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                    ans = scienceQuestion.getUserAnswer();
                String ansId = scienceQuestion.getUserAnswerId();

                //                if (textCnt == options.size() && imgCnt == 0) {
                if ((options.get(r).getChoiceurl() == null || options.get(r).getChoiceurl().trim().equalsIgnoreCase("")) && !options.get(r).getChoicename().trim().equalsIgnoreCase("")) {
                    radioGroupMcq.setVisibility(View.GONE);
                    gridMcq.setVisibility(View.VISIBLE);
//                    if (options.get(r).getChoiceurl().equalsIgnoreCase("")) {

                    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mcq_single_text_item, gridMcq, false);
                    final TextView textView = (TextView) view;
//                        textView.setElevation(3);
                    textView.setMovementMethod(new ScrollingMovementMethod());
                    textView.setText(Html.fromHtml(options.get(r).getChoicename()));

                    setOdiaFont(getActivity(), textView);

                    gridMcq.addView(textView);
                    if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
                        textView.setTextColor(Assessment_Utility.selectedColor);
                        textView.setBackground(getActivity().getResources().getDrawable(R.drawable.gradient_selector));
                    } else {
                        textView.setTextColor(Color.WHITE);
                        textView.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));

                    }
                    final int finalR2 = r;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setOnclickOnItem(v, options.get(finalR2));

                            textView.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_edit_text));
                            textView.setTextColor(Assessment_Utility.selectedColor);
                        }
                    });

//                    }
/*                        radioGroupMcq.setVisibility(View.GONE);
                        gridMcq.setVisibility(View.VISIBLE);
                        gridMcq.setColumnCount(2);
                        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mcq_single_text_item, radioGroupMcq, false);
                        final TextView textView = (TextView) view;
                        textView.setTextColor(Assessment_Utility.colorStateList);
                        textView.setId(r);
                        textView.setElevation(3);

                       *//* if (!options.get(r).getChoiceurl().equalsIgnoreCase("")) {
                            final String path = options.get(r).getChoiceurl();
                            radioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showZoomDialog(path);
                                }
                            });
                            radioGroupMcq.addView(radioButton);
//                            radioButton.setText("View Option " + (r + 1));
//                    radioButton.setText(options.get(r).getChoicename());
                            if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
                                radioButton.setChecked(true);
                                radioButton.setTextColor(Assessment_Utility.selectedColor);
                            } else {
                                radioButton.setChecked(false);
                                radioButton.setTextColor(Color.WHITE);
                            }
                        } else {*//*
                        textView.setText(options.get(r).getChoicename());
                        if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
//                            textView.setChecked(true);
                            textView.setTextColor(Assessment_Utility.selectedColor);
                        } else {
//                            textView.setChecked(false);
                            textView.setTextColor(Color.WHITE);
                        }
//                        radioGroupMcq.addView(textView);
                        gridMcq.addView(textView);

                        if (ans.equals(options.get(r).getChoicename())) {
//                            textView.setChecked(true);
                        } else {
//                            textView.setChecked(false);
                        }
//                        }
                    }*/
                } else /*if (imgCnt == options.size() && textCnt == 0) {*/
                    if (options.get(r).getChoiceurl() != null && !options.get(r).getChoiceurl().trim().equalsIgnoreCase("") &&
                            options.get(r).getChoicename().trim().equalsIgnoreCase("")) {
                        radioGroupMcq.setVisibility(View.GONE);
                        gridMcq.setVisibility(View.VISIBLE);
                        String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), options.get(r).getChoiceurl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                        String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;

                        String path = options.get(r).getChoiceurl();

                        String[] imgPath = path.split("\\.");
                        int len;
                        if (imgPath.length > 0)
                            len = imgPath.length - 1;
                        else len = 0;
                  /*  final GifView gifView;
                    ImageView imageView = null;*/

                        final String imageUrl = options.get(r).getChoiceurl();
                        final View view;
                        final RelativeLayout rl_mcq;
                        View viewRoot;
                        final ImageView tick;
                        final ImageView zoomImg;


                        if (imgPath[len].equalsIgnoreCase("gif")) {
                            viewRoot = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mcq_gif_item, gridMcq, false);
                            view = viewRoot.findViewById(R.id.mcq_gif);
                            rl_mcq = viewRoot.findViewById(R.id.rl_mcq);
                            tick = viewRoot.findViewById(R.id.iv_tick);
                            zoomImg = viewRoot.findViewById(R.id.iv_view_img);
                        /*  setImage(view, imageUrl, localPath);
                        gridMcq.addView(view);*/
                        } else {
                            viewRoot = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mcq_card_image_item, gridMcq, false);
                            view = viewRoot.findViewById(R.id.mcq_img);
                            rl_mcq = viewRoot.findViewById(R.id.rl_mcq);
                            tick = viewRoot.findViewById(R.id.iv_tick);
                            zoomImg = viewRoot.findViewById(R.id.iv_view_img);


                        }
                        final int finalR = r;
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int g = 0; g < gridMcq.getChildCount(); g++) {
                                    gridMcq.getChildAt(g).setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));
                                    if (options.get(g).getChoiceurl() != null && !options.get(g).getChoiceurl().equalsIgnoreCase(""))
                                        ((CardView) ((RelativeLayout) gridMcq.getChildAt(g)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                                    else
                                        ((TextView) gridMcq.getChildAt(g)).setTextColor(Color.WHITE);
                                }
                                rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_edit_text));
                                tick.setVisibility(View.VISIBLE);
                                List<ScienceQuestionChoice> ans = new ArrayList<>();
                                ans.add(options.get(finalR));
                                scienceQuestion.setMatchingNameList(ans);
                                assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), ans);
                            }
                        });

                        zoomImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), options.get(finalR).getChoiceurl());
                                String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                                showZoomDialog(getActivity(), options.get(finalR).getChoiceurl(), localPath, "");

                            }
                        });


                        if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
                            rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_edit_text));
                            tick.setVisibility(View.VISIBLE);

                        } else {
                            rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));
                            tick.setVisibility(View.GONE);

                        }
                        setImage(view, imageUrl, localPath);
                        gridMcq.addView(viewRoot);

                    } else if (options.get(r).getChoiceurl() != null && !options.get(r).getChoiceurl().trim().equalsIgnoreCase("")
                            && !options.get(r).getChoicename().trim().equalsIgnoreCase("")) {

                        radioGroupMcq.setVisibility(View.GONE);
                        gridMcq.setVisibility(View.VISIBLE);
                        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mcq_single_text_with_image_item, gridMcq, false);
                        final LinearLayout rl_mcq = view.findViewById(R.id.rl_mcq);
                        final TextView textView = view.findViewById(R.id.tv_text);
                        final ImageView viewImage = view.findViewById(R.id.iv_view_img);
                        viewImage.setVisibility(View.VISIBLE);
                        textView.setMovementMethod(new ScrollingMovementMethod());

                        final int finalR = r;
//                    final ImageView finalImageView = imageView;
                        textView.setMovementMethod(new ScrollingMovementMethod());
                        textView.setText(Html.fromHtml(options.get(r).getChoicename()));

                        setOdiaFont(getActivity(), textView);

                        if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
                            textView.setTextColor(Assessment_Utility.selectedColor);
                            rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.gradient_selector));
                        } else {
                            textView.setTextColor(Color.WHITE);
                            rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));

                        }
                        final int finalR2 = r;
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setOnclickOnItem(v, options.get(finalR2));

                                rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_edit_text));
                                textView.setTextColor(Assessment_Utility.selectedColor);
                            }
                        });


                        viewImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), options.get(finalR).getChoiceurl());
                                String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                                showZoomDialog(getActivity(), options.get(finalR).getChoiceurl(), localPath, "");

                            }
                        });


                        gridMcq.addView(view);
                    } else if (audioCnt == options.size()) {
                        radioGroupMcq.setVisibility(View.GONE);
                        gridMcq.setVisibility(View.VISIBLE);
                        String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), options.get(r).getChoiceurl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                        final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;

//                    String path = options.get(r).getChoiceurl();
                        String pathExtension = getFileExtension(options.get(r).getChoiceurl());
                  /*  String[] imgPath = path.split("\\.");
                    int len;
                    if (imgPath.length > 0)
                        len = imgPath.length - 1;
                    else len = 0;*/
                  /*  final GifView gifView;
                    ImageView imageView = null;*/

                        final String imageUrl = options.get(r).getChoiceurl();

                        final ImageView audioImage;
                        final RelativeLayout rl_mcq;
                        View viewRoot;
                        final ImageView tick;
                        final TextView audioText;


//                    if (pathExtension.equalsIgnoreCase("gif")) {
                       /* viewRoot = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mcq_gif_item, gridMcq, false);
                        view = viewRoot.findViewById(R.id.mcq_gif);
                        rl_mcq = viewRoot.findViewById(R.id.rl_mcq);
                        tick = viewRoot.findViewById(R.id.iv_tick);
                        zoomImg = viewRoot.findViewById(R.id.iv_view_img);*/
                        /*  setImage(view, imageUrl, localPath);
                        gridMcq.addView(view);*/
//                    } else {
                        viewRoot = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mcq_audio_item, gridMcq, false);
                        audioImage = viewRoot.findViewById(R.id.mcq_audio);
                        rl_mcq = viewRoot.findViewById(R.id.rl_mcq);
                        tick = viewRoot.findViewById(R.id.iv_tick);
                        audioText = viewRoot.findViewById(R.id.txt_audio_title);


//                    }
                        final int finalR = r;
                        audioText.setText(getString(R.string.audio) + (r + 1));
                        setOdiaFont(getActivity(), audioText);

                        rl_mcq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int g = 0; g < gridMcq.getChildCount(); g++) {
//                                gridMcq.getChildAt(g).setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));
                                    ((CardView) ((RelativeLayout) gridMcq.getChildAt(g)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
//                                ((ImageView) ((LinearLayout) ((CardView) ((RelativeLayout) gridMcq.getChildAt(g)).getChildAt(0)).getChildAt(0)).getChildAt(1)).setImageResource(R.drawable.ic_play);
                                }
                                tick.setVisibility(View.VISIBLE);
                                List<ScienceQuestionChoice> ans = new ArrayList<>();
                                ans.add(options.get(finalR));
                                scienceQuestion.setMatchingNameList(ans);
                                assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), ans);

                            }
                        });
//                    final ImageView finalImageView = imageView;
                        audioImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int g = 0; g < gridMcq.getChildCount(); g++) {
//                                gridMcq.getChildAt(g).setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));
//                                ((CardView) ((RelativeLayout) gridMcq.getChildAt(g)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                                    ((ImageView) ((LinearLayout) ((CardView) ((RelativeLayout) gridMcq.getChildAt(g)).getChildAt(0)).getChildAt(0)).getChildAt(1)).setImageResource(R.drawable.ic_play);

                                }
                                currentView = v;
                                setAudioToOption(v, imageUrl, localPath);
//                            rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_edit_text));

                            }
                        });
//                    zoomImg.setVisibility(View.GONE);
                        if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
//                        rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_edit_text));
                            tick.setVisibility(View.VISIBLE);

                        } else {
//                        rl_mcq.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));
                            tick.setVisibility(View.GONE);

                        }
//                    setImage(view, imageUrl, localPath);
                        gridMcq.addView(viewRoot);

                    }

            }
        }
        radioGroupMcq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //((RadioButton) radioGroupMcq.getChildAt(checkedId)).setChecked(true);
                RadioButton rb = group.findViewById(checkedId);
                if (rb != null) {
                    rb.setChecked(true);
                    rb.setTextColor(Assessment_Utility.selectedColor);
                }

                for (int i = 0; i < group.getChildCount(); i++) {
                    if ((group.getChildAt(i)).getId() == checkedId) {
                        ((RadioButton) group.getChildAt(i)).setTextColor(Assessment_Utility.selectedColor);

                        List<ScienceQuestionChoice> ans = new ArrayList<>();
                        ans.add(options.get(i));
                        scienceQuestion.setMatchingNameList(ans);
                        assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), ans);
                    } else {
                        ((RadioButton) group.getChildAt(i)).setTextColor(getActivity().getResources().getColor(R.color.white));
                    }
                }
            }
        });
    }

    private void setAudioToOption(View v, String onlinePath, String localPath) {
        if (prevView != null && v != prevView) {
            isOptionPlaying = false;
            AudioUtil.stopPlayingAudio();
            stopPlayer();
        }
        prevView = v;
        if (isOptionPlaying) {
            isOptionPlaying = false;
            ((ImageView) v).setImageResource(R.drawable.ic_play);
            AudioUtil.stopPlayingAudio();
            stopPlayer();

        } else {
            isOptionPlaying = true;
            ((ImageView) v).setImageResource(R.drawable.ic_pause);
            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                AudioUtil.playRecording(onlinePath, this);
            else AudioUtil.playRecording(localPath, this);
        }
    }


    private void setOnclickOnItem(View v, ScienceQuestionChoice scienceQuestionChoice) {
        for (int g = 0; g < gridMcq.getChildCount(); g++) {
            gridMcq.getChildAt(g).setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.custom_radio_button));
            View view = gridMcq.getChildAt(g);
            if (view instanceof TextView)
                ((TextView) view).setTextColor(Color.WHITE);
            if (view instanceof RelativeLayout) {
                ((CardView) ((RelativeLayout) view).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            }
            if (view instanceof LinearLayout) {
                ((TextView) ((CardView) ((LinearLayout) view).getChildAt(0)).getChildAt(0)).setTextColor(Color.WHITE);
            }

        }

        List<ScienceQuestionChoice> ans = new ArrayList<>();
        ans.add(scienceQuestionChoice);
        scienceQuestion.setMatchingNameList(ans);
        assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), ans);

    }

    private void setImage(View view, final String choiceurl, String placeholder) {
//        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
        String path = choiceurl;
        String[] imgPath = path.split("\\.");
        int len;
        if (imgPath.length > 0)
            len = imgPath.length - 1;
        else len = 0;


        if (imgPath[len].equalsIgnoreCase("gif")) {

            try {
                GifView gifView = (GifView) view;
                InputStream gif = new FileInputStream(placeholder);
                gifView.setGifResource(gif);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    /*        Glide.with(getActivity()).asGif()
                    .load(path)
                    .apply(new RequestOptions()
                            .placeholder(Drawable.createFromPath(placeholder)))
                    .into(imageView);*/
        } else {
            ImageView imageView = (ImageView) view;
            Glide.with(getActivity())
                    .load(path)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(Drawable.createFromPath(placeholder)))
                    .into(imageView);
        }


//        }
    }


    @Override
    public void onPause() {
        super.onPause();
//        if(audioPlayerInterface!=null)
        if (isOptionPlaying || isQuestionPlaying)
            AudioUtil.stopPlayingAudio();
        stopPlayer();
    }

    @Override
    public void stopPlayer() {
        if (isQuestionPlaying) {
            questionImage.setImageResource(R.drawable.ic_play_circle);
            isQuestionPlaying = false;
        }
        if (isOptionPlaying) {
            ((ImageView) currentView).setImageResource(R.drawable.ic_play);
            isOptionPlaying = false;

        }
    }

   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
//            if (scienceQuestion != null) {
                String para = "";
                if (isVisibleToUser) {
                    if (scienceQuestion.isParaQuestion()) {
                        para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
                    }
                    assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

                } else {
                    assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

//                }
            }
        }
    }*/
   /* @Override
    public void onResume() {
        super.onResume();
        String para = "";
        if (scienceQuestion != null) {
            if (scienceQuestion.isParaQuestion()) {
                para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
            }
            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        } else {
            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        }
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

