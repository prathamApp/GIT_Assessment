package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.custom.sparkbutton.SparkButton;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static android.graphics.Color.GREEN;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_multiple_select_row)
public class MultipleSelectFragment extends Fragment {

    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.gl_multiselect)
    GridLayout gridLayout;
    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;
    AssessmentAnswerListener assessmentAnswerListener;


    public MultipleSelectFragment() {
        // Required empty public constructor
    }

    public static MultipleSelectFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        MultipleSelectFragment_ multipleSelectFragment = new MultipleSelectFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        multipleSelectFragment.setArguments(args);
        return multipleSelectFragment;
    }

    @AfterViews
    public void init() {
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
        }
        setMultipleSelectQuestion();

    }


/*    @Override
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
        return inflater.inflate(R.layout.layout_multiple_select_row, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setMultipleSelectQuestion();
    }*/

    public void setMultipleSelectQuestion() {
        setOdiaFont(getActivity(), question);

        question.setText(scienceQuestion.getQname());
        final String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
        final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;

        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {


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
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(questionImage);
            }
           /* } else {
                String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                questionImage.setImageBitmap(bitmap);
            }*/
        } else questionImage.setVisibility(View.GONE);

        questionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath);
            }
        });
        questionGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath);
            }
        });

        final List<ScienceQuestionChoice> choices = scienceQuestion.getLstquestionchoice();

        gridLayout.setColumnCount(1);
        gridLayout.removeAllViews();
        for (int j = 0; j < choices.size(); j++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_multiple_select_item, gridLayout, false);
//            final SparkButton sparkButton = (SparkButton) view;
//            final CheckBox checkBox = (CheckBox) sparkButton.getChildAt(3);
            final CheckBox checkBox = (CheckBox) view;
            checkBox.setButtonTintList(Assessment_Utility.colorStateList);
            checkBox.setTextColor(getActivity().getResources().getColor(R.color.white));
            setOdiaFont(getActivity(), checkBox);
            if (!choices.get(j).getChoicename().equalsIgnoreCase(""))
                checkBox.setText(choices.get(j).getChoicename());
            else if (!choices.get(j).getChoiceurl().equalsIgnoreCase("")) {


                final String path = choices.get(j).getChoiceurl();

                String fileNameChoice = Assessment_Utility.getFileName(scienceQuestion.getQid(), choices.get(j).getChoiceurl());
                final String localPathChoice = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileNameChoice;


                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Assessment_Utility.showZoomDialog(getActivity(), path, localPathChoice);

                    }
                });
              /*  Glide.with(getActivity()).asBitmap().
                        load(path)
                        .apply(new RequestOptions()
                                .fitCenter()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .override(getDp(200), getDp(300))).into(new SimpleTarget<Bitmap>(MATCH_PARENT, MATCH_PARENT) {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Drawable bd = new BitmapDrawable(resource);
                        checkBox.setButtonDrawable(bd);
                    }
                });*/
                checkBox.setText("View Option " + (j + 1));

            }
            checkBox.setTag(choices.get(j).getQcid());
           /* if (Assessment_Constants.isPracticeModeOn) {
                if (scienceQuestion.getIsAttempted()) {
                    if (choices.get(j).getMyIscorrect().equalsIgnoreCase("TRUE")) {
                        checkBox.setChecked(true);
                        checkBox.setTextColor(Assessment_Utility.selectedColor);
                    } else {
                        checkBox.setChecked(false);
                        checkBox.setTextColor(getActivity().getResources().getColor(R.color.white));

                    }
                    if (choices.get(j).getCorrect().equalsIgnoreCase("TRUE")) {
                        sparkButton.setActiveImage(R.drawable.correct_bg);
//                        checkBox.setBackgroundColor(GREEN);
                    }
                }
            } else {*/
                if (scienceQuestion.getIsAttempted()) {
                    if (choices.get(j).getMyIscorrect().equalsIgnoreCase("TRUE")) {
                        checkBox.setChecked(true);
                        checkBox.setTextColor(Assessment_Utility.selectedColor);
                    } else {
                        checkBox.setChecked(false);
                        checkBox.setTextColor(getActivity().getResources().getColor(R.color.white));

                    }
//                }
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Toast.makeText(context, "" + checkBox.getText(), Toast.LENGTH_SHORT).show();
                    String mQcID = buttonView.getTag().toString();
//                    buttonView.setTextColor(Assessment_Utility.selectedColor);
                    ScienceQuestionChoice mScienceQuestionChoice = null;
                    for (ScienceQuestionChoice scienceQuestionChoice : choices) {
                        if (scienceQuestionChoice.getQcid().equals(mQcID)) {
                            mScienceQuestionChoice = scienceQuestionChoice;
                            break;
                        }
                    }
                    if (isChecked) {
                        if (mScienceQuestionChoice != null)
                            mScienceQuestionChoice.setMyIscorrect("true");
                    } else {
                        if (mScienceQuestionChoice != null)
                            mScienceQuestionChoice.setMyIscorrect("false");
                    }

                    for (int i = 0; i < gridLayout.getRowCount(); i++) {
                        if (((CheckBox) gridLayout.getChildAt(i)).isChecked()) {
                            ((CheckBox) gridLayout.getChildAt(i)).setTextColor(Assessment_Utility.selectedColor);
                        } else {
                            ((CheckBox) gridLayout.getChildAt(i)).setTextColor(Color.WHITE);
                        }
                    }


//                    questionTypeListener.setAnswer("", "", scienceQuestion.getQid(), choices);
                    assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), choices);
                }
            });
            GridLayout.LayoutParams paramGrid = new GridLayout.LayoutParams();
//            paramGrid.height = GridLayout.LayoutParams.WRAP_CONTENT;
            paramGrid.width = GridLayout.LayoutParams.WRAP_CONTENT;
            paramGrid.setGravity(Gravity.FILL_HORIZONTAL);
            paramGrid.setMargins(10, 10, 10, 10);
            /*sparkButton.setLayoutParams(paramGrid);
            gridLayout.addView(sparkButton);*/
            checkBox.setLayoutParams(paramGrid);
            gridLayout.addView(checkBox);
        }




     /*   for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final CheckBox checkBox = (CheckBox) gridLayout.getChildAt(i);
            final int finalI = i;
           *//* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(context, "" + checkBox.getText(), Toast.LENGTH_SHORT).show();
                    int index=finalI + 1;
                    if (isChecked) {
                        if (!checkedAnswers.contains(index+"")) {
//                            int index = finalI + 1;
                            checkedAnswers.add(index+"");
                        }
                    } else {
                        if (checkedAnswers.contains(index+"")) {
//                            int index = finalI + 1;
                           checkedAnswers.remove(index+"");
                        }
                    }
                    Collections.sort(checkedAnswers);
                    List<ScienceQuestionChoice> ans=new ArrayList<>();
                    ans.add(choices.get(index-1));
                    questionTypeListener.setAnswer("","", scienceQuestion.getQid(),ans );
                }*//*

        });*/

    }


}
