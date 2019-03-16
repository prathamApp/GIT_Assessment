package com.pratham.assessment.ui.display_english_list;

import android.content.Context;
import android.os.Handler;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;


public class TestDisplayAdapter extends RecyclerView.Adapter<TestDisplayAdapter.MyViewHolder> {

    private Context mContext;
    private List<ContentTable> gamesViewList;
    TestClicked TestClicked;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public ImageView iv_download;
        public MaterialCardView game_card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.level_title);
            thumbnail = (ImageView) view.findViewById(R.id.level_thumbnail);
            iv_download = (ImageView) view.findViewById(R.id.iv_download_icon);
            game_card_view = (MaterialCardView) view.findViewById(R.id.level_card_view);
        }
    }

    public TestDisplayAdapter(Context mContext, List<ContentTable> gamesViewList, TestClicked TestClicked) {
        this.mContext = mContext;
        this.gamesViewList = gamesViewList;
        this.TestClicked = TestClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.english_level_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ContentTable gamesList = gamesViewList.get(position);
//        Log.d("sd", "onBindViewHolder: " + sdCardPath + "LLA_Thumbs/" + gamesList.getNodeServerImage());
        holder.title.setText(""+gamesList.getNodeTitle());

        holder.iv_download.setVisibility(View.GONE);
        if (!gamesList.getIsDownloaded().equalsIgnoreCase("true")) {
            holder.iv_download.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(gamesList.getNodeServerImage()).into(holder.thumbnail);
        }
        else if (gamesList.getIsDownloaded().equalsIgnoreCase("true") && !Assessment_Constants.SD_CARD_Content) {
            Glide.with(mContext)
                    .load(AssessmentApplication.assessPath + Assessment_Constants.THUMBS_PATH+gamesList.getNodeImage())
                    .into(holder.thumbnail);
        } else if (gamesList.getIsDownloaded().equalsIgnoreCase("true") && Assessment_Constants.SD_CARD_Content) {
            Glide.with(mContext)
                    .load(Assessment_Constants.ext_path + Assessment_Constants.THUMBS_PATH +gamesList.getNodeImage())
                    .into(holder.thumbnail);
        }

/*        if (gamesList.getNodeType().equalsIgnoreCase("topic")) {
            holder.iv_download.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(""+gamesList.getNodeServerImage()).into(holder.thumbnail);
            if (gamesList.getIsDownloaded().equalsIgnoreCase("true")) {
                holder.iv_download.setVisibility(View.GONE);
                Glide.with(mContext).load(COSApplication.pradigiPath + "/LLA_Thumbs"+ gamesList.getNodeImage()).into(holder.thumbnail);
            }
        }*/

        holder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gamesList.getIsDownloaded().equalsIgnoreCase("true"))
                    TestClicked.onTestClicked(position, gamesList.getNodeId());
                else
                    TestClicked.onTestDownloadClicked(position, gamesList.getNodeId(),gamesList.getNodeTitle());
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
            }
        }, (long) (20));

    }

    @Override
    public int getItemCount() {
        return gamesViewList.size();
    }
}