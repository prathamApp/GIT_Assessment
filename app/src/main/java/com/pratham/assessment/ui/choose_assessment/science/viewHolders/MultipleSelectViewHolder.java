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
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultipleSelectViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.tv_question_image)
    ImageView questionImage;
    @BindView(R.id.gl_multiselect)
    GridLayout gridLayout;
    ScienceQuestion scienceQuestion;
    Context context;

    public MultipleSelectViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.scienceQuestion = scienceQuestion;
    }

    public void setMultipleSelectQuestion(ScienceQuestion scienceQuestion, int pos) {
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
        }
        List<ScienceQuestionChoice> choices = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());

        gridLayout.setColumnCount(1);
        gridLayout.removeAllViews();
        for (int j = 0; j < choices.size(); j++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(choices.get(j).getChoicename());
            checkBox.setTextSize(25);
            GridLayout.LayoutParams paramGrid = new GridLayout.LayoutParams();
          /*  paramGrid.height = GridLayout.LayoutParams.WRAP_CONTENT;
            paramGrid.width = GridLayout.LayoutParams.WRAP_CONTENT;*/
            paramGrid.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(paramGrid);
            gridLayout.addView(checkBox);
        }
    }
}
