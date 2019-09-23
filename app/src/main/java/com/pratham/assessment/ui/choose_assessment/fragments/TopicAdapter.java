package com.pratham.assessment.ui.choose_assessment.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.ui.choose_assessment.ChoseAssessmentClicked;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> {
    Context context;
    private List<AssessmentTest> assessmentTests;
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

    public TopicAdapter(Context context, List<AssessmentTest> assessmentTests) {
        this.context = context;
        this.assessmentTests = assessmentTests;
        assessmentClicked = (ChoseAssessmentClicked) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.assessment_content_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final AssessmentTest assessmentTest = assessmentTests.get(i);

        myViewHolder.title.setText(assessmentTest.getExamname());
        myViewHolder.game_card_view.setCardBackgroundColor(Assessment_Utility.getRandomColorGradient());
        myViewHolder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Assessment_Utility.setSelectedColor(myViewHolder.game_card_view.getCardBackgroundColor().getDefaultColor());
                assessmentClicked.topicClicked(i, assessmentTest);

            }
        });
    }

    @Override
    public int getItemCount() {
        return assessmentTests.size();
    }


}
