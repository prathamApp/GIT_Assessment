package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class McqFillInTheBlanksViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.tv_question_image)
    ImageView questionImage;
    @BindView(R.id.rg_mcq)
    RadioGroup radioGroupMcq;
    ScienceQuestion scienceQuestion;
    Context context;

    public McqFillInTheBlanksViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.scienceQuestion = scienceQuestion;
    }

    public void setMcqsQuestion(ScienceQuestion scienceQuestion, int pos) {
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
        List<ScienceQuestionChoice> options = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        if (options != null) {
            radioGroupMcq.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
            radioGroupMcq.removeAllViews();
            for (int r = 0; r < options.size(); r++) {
                final RadioButton radioButton = new RadioButton(context);
                radioButton.setId(r);
                radioButton.setTextSize(25);
                if (!options.get(r).getChoiceurl().equalsIgnoreCase("")) {

                    int myWidth = MATCH_PARENT;
                    int myHeight = MATCH_PARENT;
                    Glide.with(context).asBitmap().
                            load(Assessment_Constants.loadOnlineImagePath + options.get(r).getChoiceurl())
                            .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL)).into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable bd = new BitmapDrawable(resource);
                            radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, bd, null);
                        }
                    });
                } else
                    radioButton.setText(options.get(r).getChoicename());
                radioGroupMcq.addView(radioButton);
            }
        }

    }
}

