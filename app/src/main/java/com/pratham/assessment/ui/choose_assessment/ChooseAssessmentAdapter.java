package com.pratham.assessment.ui.choose_assessment;

import android.content.Context;
import android.os.Handler;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;

public class ChooseAssessmentAdapter extends RecyclerView.Adapter<ChooseAssessmentAdapter.MyViewHolder> {

    private Context mContext;
    private int lastPos = -1;
    private List<ContentTable> assessmentViewList;
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

    public ChooseAssessmentAdapter(Context mContext, List<ContentTable> assessmentViewList, ChoseAssessmentClicked assessmentClicked) {
        this.mContext = mContext;
        this.assessmentViewList = assessmentViewList;
        this.assessmentClicked = assessmentClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_content_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //final ContentView gamesList = gamesViewList.get(position);
        final ContentTable assessList = assessmentViewList.get(position);

        Animation animation = null;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.item_fall_down);
        animation.setDuration(500);

        if (assessList.getNodeTitle().equalsIgnoreCase("test"))
            holder.title.setText("English");
        else
            holder.title.setText(assessList.getNodeTitle());

        holder.iv_download.setVisibility(View.GONE);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_warning);
        requestOptions.error(R.drawable.ic_warning);


        if ( (assessList.getIsDownloaded().equalsIgnoreCase("true") || assessList.getIsDownloaded().equalsIgnoreCase("1")) && !Assessment_Constants.SD_CARD_Content){
            Glide.with(mContext).setDefaultRequestOptions(requestOptions)
                    .load(AssessmentApplication.assessPath + Assessment_Constants.THUMBS_PATH + assessList.getNodeImage())
                    .into(holder.thumbnail);
        }
        else if ( (assessList.getIsDownloaded().equalsIgnoreCase("true") || assessList.getIsDownloaded().equalsIgnoreCase("1")) && Assessment_Constants.SD_CARD_Content) {
            Glide.with(mContext).setDefaultRequestOptions(requestOptions)
                    .load(Assessment_Constants.ext_path + Assessment_Constants.THUMBS_PATH + assessList.getNodeImage())
                    .into(holder.thumbnail);
        }else
            Glide.with(mContext).setDefaultRequestOptions(requestOptions)
                    .load(assessList.getNodeServerImage())
                    .into(holder.thumbnail);

        holder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assessList.getNodeType() != null) {
                    assessmentClicked.assessmentClicked(position, assessList.getNodeId());
                }
            }
        });
        holder.game_card_view.setVisibility(View.GONE);
        setAnimations(holder.game_card_view, position);

    }

    private void setAnimations(final View content_card_view, final int position) {
        final Animation animation;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.item_fall_down);
        animation.setDuration(500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                content_card_view.setVisibility(View.VISIBLE);
                content_card_view.setAnimation(animation);
                lastPos = position;
            }
        }, (long) (20));

    }


    @Override
    public int getItemCount() {
        return assessmentViewList.size();
    }
}