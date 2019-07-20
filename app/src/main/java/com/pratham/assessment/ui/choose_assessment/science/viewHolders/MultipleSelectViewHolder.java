package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultipleSelectViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;
    @BindView(R.id.gl_multiselect)
    GridLayout gridLayout;
    ScienceQuestion scienceQuestion;
    QuestionTypeListener questionTypeListener;

    Context context;

    public MultipleSelectViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        questionTypeListener = scienceAdapter;

    }

    public void setMultipleSelectQuestion(ScienceQuestion scienceQuestion1, int pos) {
        this.scienceQuestion = scienceQuestion1;

        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().
                    load(Assessment_Constants.loadOnlineImagePath + scienceQuestion.getPhotourl()).apply(new RequestOptions()
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
        final List<ScienceQuestionChoice> choices = scienceQuestion.getLstquestionchoice();

        gridLayout.setColumnCount(1);
        gridLayout.removeAllViews();
        for (int j = 0; j < choices.size(); j++) {
            final CheckBox checkBox = new CheckBox(context);
            checkBox.setText(choices.get(j).getChoicename());
            checkBox.setTag(choices.get(j).getQcid());
            if (choices.get(j).getMyIscorrect().equalsIgnoreCase("TRUE")) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setTextSize(25);
            checkBox.setPadding(5, 5, 5, 5);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Toast.makeText(context, "" + checkBox.getText(), Toast.LENGTH_SHORT).show();
                    String mQcID = buttonView.getTag().toString();
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

                    questionTypeListener.setAnswer("", "", scienceQuestion.getQid(), choices);
                }
            });
            GridLayout.LayoutParams paramGrid = new GridLayout.LayoutParams();
          /*  paramGrid.height = GridLayout.LayoutParams.WRAP_CONTENT;
            paramGrid.width = GridLayout.LayoutParams.WRAP_CONTENT;*/
            paramGrid.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(paramGrid);
            gridLayout.addView(checkBox);
        }

        for (int j = 0; j < choices.size(); j++) {


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



