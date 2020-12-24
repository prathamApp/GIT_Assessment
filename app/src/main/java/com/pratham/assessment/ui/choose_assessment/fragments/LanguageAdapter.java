package com.pratham.assessment.ui.choose_assessment.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.ui.choose_assessment.ChoseAssessmentClicked;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {
    Context context;
    private List<AssessmentLanguages> assessmentLanguages;
    ChoseAssessmentClicked assessmentClicked;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public ImageView iv_download;
        public MaterialCardView game_card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.assessment_title);
            thumbnail = (ImageView) view.findViewById(R.id.assessment_thumbnail);
            iv_download = (ImageView) view.findViewById(R.id.iv_download_icon);
            game_card_view = (MaterialCardView) view.findViewById(R.id.assessment_content_card);
        }

    }

    public LanguageAdapter(Context context, List<AssessmentLanguages> assessmentLanguages) {
        this.context = context;
        this.assessmentLanguages = assessmentLanguages;
        assessmentClicked = (ChoseAssessmentClicked) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.assessment_content_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final AssessmentLanguages languages = assessmentLanguages.get(i);
        String lang = FastSave.getInstance().getString(Assessment_Constants.LANGUAGE, "1");
        if (lang.equals(languages.getLanguageid())) {
            myViewHolder.game_card_view.setCardBackgroundColor(Assessment_Utility.getRandomColorGradient());
            myViewHolder.title.setTextColor(Color.WHITE);
        } else {
            myViewHolder.game_card_view.setCardBackgroundColor(Color.WHITE);
            myViewHolder.title.setTextColor(Color.BLACK);
        }
        myViewHolder.title.setText(languages.getLanguagename());
//        myViewHolder.game_card_view.setCardBackgroundColor(Assessment_Utility.getRandomColorGradient());

        myViewHolder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentClicked.languageClicked(i, languages);

            }
        });
    }

    @Override
    public int getItemCount() {
        return assessmentLanguages.size();
    }


}
