package com.pratham.assessment.ui.choose_assessment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ECEModel;

import java.util.List;

public class ECEAdapter extends RecyclerView.Adapter<ECEAdapter.MyViewHolder> {

    private Context mContext;
    private List<ECEModel> eceModelList;
    //GameClicked gameClicked;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_ece_question;
        public TextView tv_ece_instructions;
        public ImageView iv_play_video;
        public Button yes;
        public Button no;
        public RatingBar rb_rating;

        public MyViewHolder(View view) {
            super(view);
            tv_ece_question =  view.findViewById(R.id.tv_ece_question);
            tv_ece_instructions = view.findViewById(R.id.tv_ece_instructions);
            iv_play_video =  view.findViewById(R.id.iv_play_video);
            yes =  view.findViewById(R.id.btn_yes);
            no =  view.findViewById(R.id.btn_no);
            rb_rating =  view.findViewById(R.id.rb_rating);
        }
    }

    public ECEAdapter(Context mContext, List<ECEModel> eceModelList) {
        this.mContext = mContext;
       this.eceModelList = eceModelList;
        /* this.gameClicked = gameClicked;*/
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ece_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ECEModel eceModel = eceModelList.get(position);


        holder.tv_ece_question.setText(eceModel.getQuestion());
        holder.tv_ece_instructions.setText(eceModel.getInstructions());

       /* Glide.with(mContext).load(sdCardPathString+ "/StoryData/" +gamesList.getNodeImage()).into(holder.thumbnail);
        holder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClicked.onGameClicked(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
            return eceModelList.size();
    }
}