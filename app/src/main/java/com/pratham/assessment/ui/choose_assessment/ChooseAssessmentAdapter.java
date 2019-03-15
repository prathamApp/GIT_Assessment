package com.pratham.assessment.ui.choose_assessment;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ContentTable;

import java.util.List;

public class ChooseAssessmentAdapter extends RecyclerView.Adapter<ChooseAssessmentAdapter.MyViewHolder> {

    private Context mContext;
    private int lastPos = -1;
    private List<ContentTable> gamesViewList;
    ChoseAssessmentClicked assessmentClicked;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public ImageView iv_download;
        public LinearLayout game_card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.assessment_title);
            thumbnail = (ImageView) view.findViewById(R.id.assessment_thumbnail);
            iv_download = (ImageView) view.findViewById(R.id.iv_download_icon);
            game_card_view = (LinearLayout) view.findViewById(R.id.assessment_content_card);
        }
    }

    public ChooseAssessmentAdapter(Context mContext, List<ContentTable> gamesViewList, ChoseAssessmentClicked assessmentClicked) {
        this.mContext = mContext;
        this.gamesViewList = gamesViewList;
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
        final ContentTable gamesList = gamesViewList.get(position);

        Animation animation = null;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.item_fall_down);
        animation.setDuration(500);

        holder.title.setText(gamesList.getNodeTitle());

        holder.iv_download.setVisibility(View.GONE);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_warning);
        requestOptions.error(R.drawable.ic_warning);

        Glide.with(mContext).setDefaultRequestOptions(requestOptions)
                .load(gamesList.getNodeServerImage())
                .into(holder.thumbnail);


        holder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamesList.getNodeType() != null) {
                    assessmentClicked.assessmentClicked(position, gamesList.getNodeId());
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
        return gamesViewList.size();
    }
}