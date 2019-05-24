package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ZoomImageDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.ArrayList;
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
    @BindView(R.id.iv_zoom_img)
    ImageView zoomImg;
    private ScienceQuestion scienceQuestion;
    private Context context;
    private QuestionTypeListener questionTypeListener;

    public McqFillInTheBlanksViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        questionTypeListener = scienceAdapter;
    }

    public void setMcqsQuestion(ScienceQuestion scienceQuestion1, int pos) {
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

        final List<ScienceQuestionChoice> options = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        if (options != null) {
            radioGroupMcq.removeAllViews();

            for (int r = 0; r < options.size(); r++) {
                final RadioButton radioButton = new RadioButton(context);
                radioButton.setId(r);
                radioButton.setPadding(25, 25, 25, 25);
                radioButton.setElevation(3);
//                radioButton.setButtonDrawable(android.R.color.transparent);
                radioButton.setBackground(context.getResources().getDrawable(R.drawable.custom_radio_button));
                RadioGroup.LayoutParams layoutParams;

//                radioButton.setLayoutParams(layoutParams);
                radioButton.setTextSize(23);


                final RadioButton tempRadio = radioButton;
                if (!options.get(r).getChoiceurl().equalsIgnoreCase("")) {
                    layoutParams = new RadioGroup.LayoutParams(MATCH_PARENT, getDp(150), 1);
                    radioButton.setLayoutParams(layoutParams);
                    final int finalR = r;
                    final String path=Assessment_Constants.loadOnlineImagePath + options.get(r).getChoiceurl();
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ZoomImageDialog zoomImageDialog=new ZoomImageDialog(context,path);
                            zoomImageDialog.show();
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
                            tempRadio.setButtonDrawable(bd);
                        }
                    });
                } else {
                    radioButton.setText(options.get(r).getChoicename());
                    layoutParams = new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
                }
                layoutParams.setMargins(15, 10, 10, 10);
                radioButton.setLayoutParams(layoutParams);
                radioGroupMcq.addView(radioButton);
            }

            if (!scienceQuestion.getUserAnswerId().equalsIgnoreCase("")) {
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getQcid().equalsIgnoreCase(scienceQuestion.getUserAnswerId())) {
                        ((RadioButton) radioGroupMcq.getChildAt(i)).setChecked(true);
                    }
                }
            }
        }

        radioGroupMcq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ((RadioButton) radioGroupMcq.getChildAt(checkedId)).setChecked(true);

                for (int i = 0; i < group.getChildCount(); i++) {
                    if ((group.getChildAt(i)).getId() == checkedId) {
                        List<ScienceQuestionChoice> ans = new ArrayList<>();
                        ans.add(options.get(i));
//                        String answer = ((RadioButton) group.getChildAt(i)).getText().toString();
//                        String ansId = options.get(i).getQcid();
                        questionTypeListener.setAnswer("", "", scienceQuestion.getQid(), ans);
                    }
                }
            }
        });


    }


    private int getDp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

}

