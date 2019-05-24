package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.utilities.Assessment_Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrueFalseViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.rg_true_false)
    RadioGroup rg_true_false;
    @BindView(R.id.tv_question_image)
    ImageView questionImage;
    @BindView(R.id.rb_true)
    RadioButton radioButtonTrue;
    @BindView(R.id.rb_false)
    RadioButton radioButtonFalse;
    ScienceQuestion scienceQuestion;
    QuestionTypeListener questionTypeListener;

    Context context;

    public TrueFalseViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        questionTypeListener = scienceAdapter;
    }

    public void setTrueFalseQuestion(ScienceQuestion scienceQuestion1, int pos) {
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
        }else questionImage.setVisibility(View.GONE);
        rg_true_false.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonTrue.getId())
                    questionTypeListener.setAnswer("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);
                else if (checkedId == radioButtonFalse.getId())
                    questionTypeListener.setAnswer("", radioButtonFalse.getText().toString(), scienceQuestion.getQid(), null);
                else Toast.makeText(context, "Select Answer", Toast.LENGTH_SHORT).show();
            }
        });

        if (scienceQuestion.getUserAnswer().equalsIgnoreCase("true")) {
            radioButtonTrue.setChecked(true);
        } else if (scienceQuestion.getUserAnswer().equalsIgnoreCase("false")) {
            radioButtonFalse.setChecked(true);
        } else {
            radioButtonFalse.setChecked(false);
            radioButtonTrue.setChecked(false);
        }
    }
}
