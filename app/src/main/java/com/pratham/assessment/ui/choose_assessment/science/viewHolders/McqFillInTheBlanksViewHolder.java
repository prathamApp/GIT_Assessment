package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ZoomImageDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class McqFillInTheBlanksViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;
    @BindView(R.id.rg_mcq)
    RadioGroup radioGroupMcq;
    @BindView(R.id.iv_zoom_img)
    ImageView zoomImg;
    private ScienceQuestion scienceQuestion;
    private Context context;
    private QuestionTypeListener questionTypeListener;
    AssessmentAnswerListener assessmentAnswerListener;

    public McqFillInTheBlanksViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        questionTypeListener = scienceAdapter;
        assessmentAnswerListener=(ScienceAssessmentActivity)context;
    }

    public void setMcqsQuestion(ScienceQuestion scienceQuestion1, int pos) {
        this.scienceQuestion = scienceQuestion1;

        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().
                    load(/*Assessment_Constants.loadOnlineImagePath +*/ scienceQuestion.getPhotourl()).apply(new RequestOptions()
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

        final List<ScienceQuestionChoice> options = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        if (options != null) {
            radioGroupMcq.removeAllViews();

            for (int r = 0; r < options.size(); r++) {
//                final RadioButton radioButton = new RadioButton(context);
                final View view = LayoutInflater.from(context).inflate(R.layout.layout_mcq_radio_item, radioGroupMcq, false);
                final RadioButton radioButton = (RadioButton) view;
                radioButton.setButtonTintList(Assessment_Utility.colorStateList);

                radioButton.setId(r);
                radioButton.setElevation(3);

//                radioButton.setButtonDrawable(android.R.color.transparent);
                RadioGroup.LayoutParams layoutParams;
                String ans = scienceQuestion1.getUserAnswer();
                String ansId = scienceQuestion1.getUserAnswer();

//                radioButton.setLayoutParams(layoutParams);


//                final RadioButton tempRadio = (RadioButton) radioButton;
                if (!options.get(r).getChoiceurl().equalsIgnoreCase("")) {
                    layoutParams = new RadioGroup.LayoutParams(MATCH_PARENT, getDp(150), 1);
                    radioButton.setLayoutParams(layoutParams);
                    final int finalR = r;
                    final String path = /*Assessment_Constants.loadOnlineImagePath +*/ options.get(r).getChoiceurl();
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path);
                            zoomImageDialog.show();*/
                        }
                    });
                    Glide.with(context).asBitmap().
                            load(path)
                            .apply(new RequestOptions()
                                    .fitCenter()
                                    .format(DecodeFormat.PREFER_ARGB_8888)
                                    .override(getDp(200), getDp(300))).into(new SimpleTarget<Bitmap>(MATCH_PARENT, MATCH_PARENT) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable bd = new BitmapDrawable(resource);
                            radioButton.setButtonDrawable(bd);
                        }
                    });
                    layoutParams.setMargins(15, 10, 10, 10);
                    radioButton.setLayoutParams(layoutParams);
                    radioGroupMcq.addView(radioButton);
                    radioButton.setText(options.get(r).getChoicename());
                    if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
                        radioButton.setChecked(true);
                    } else radioButton.setChecked(false);


                } else {
                    radioButton.setText(options.get(r).getChoicename());
                    if (scienceQuestion.getUserAnswerId().equalsIgnoreCase(options.get(r).getQcid())) {
                        radioButton.setChecked(true);
                        radioButton.setTextColor(Assessment_Utility.selectedColor);
                    } else {
                        radioButton.setChecked(false);
                        radioButton.setTextColor(Color.WHITE);
                    }


                    layoutParams = new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
                    layoutParams.setMargins(15, 10, 10, 10);
                    radioButton.setLayoutParams(layoutParams);
                    radioGroupMcq.addView(radioButton);
                    if (ans.equals(options.get(r).getChoicename())) {
                        radioButton.setChecked(true);
                    } else {
                        radioButton.setChecked(false);
                    }

                }

            }

          /*  if (!scienceQuestion.getUserAnswerId().equalsIgnoreCase("")) {
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getQcid().equalsIgnoreCase(scienceQuestion.getUserAnswerId())) {
                        ((RadioButton) radioGroupMcq.getChildAt(i)).setChecked(true);
                    }
                }
            }*/
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
//                        String answer = ((RadioButton) group.getChildAt(i)).getText().toString();
//                        String ansId = options.get(i).getQcid();
                        assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), ans);
//                        questionTypeListener.setAnswer("", "", scienceQuestion.getQid(), ans);
                    } else {
                        ((RadioButton) group.getChildAt(i)).setTextColor(context.getResources().getColor(R.color.white));

                    }
                }
            }
        });


    }


    private int getDp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

}

