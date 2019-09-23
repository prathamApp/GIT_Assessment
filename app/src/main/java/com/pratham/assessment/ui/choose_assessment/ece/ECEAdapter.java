package com.pratham.assessment.ui.choose_assessment.ece;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ECEModel;

import java.util.List;

public class ECEAdapter extends RecyclerView.Adapter<ECEAdapter.MyViewHolder> {

    private Context mContext;
    private List<ECEModel> eceModelList;
    AnswerClickedListener answerClickedListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_ece_question;
        public TextView tv_ece_question_title;
        //        public TextView tv_ece_instructions;
//        public ImageView iv_play_video;
        public Button canDo;
        public Button needHelp;
        //        public RatingBar rb_rating;
        public LinearLayout ll_yes_no, ll_rating;

        public MyViewHolder(View view) {
            super(view);
            tv_ece_question = view.findViewById(R.id.tv_ece_question);
            tv_ece_question_title = view.findViewById(R.id.tv_science_question_title);
//            tv_ece_instructions = view.findViewById(R.id.tv_ece_instructions);
//            iv_play_video =  view.findViewById(R.id.iv_play_video);
            canDo = view.findViewById(R.id.btn_yes);
            needHelp = view.findViewById(R.id.btn_no);
//            rb_rating =  view.findViewById(R.id.rb_rating);
            ll_yes_no = view.findViewById(R.id.ll_yes_no);
//            ll_rating =  view.findViewById(R.id.ll_rating);
        }
    }

    public ECEAdapter(Context mContext, List<ECEModel> eceModelList) {
        this.mContext = mContext;
        this.eceModelList = eceModelList;
        this.answerClickedListener = (AnswerClickedListener) mContext;
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
        if (eceModel.getIsSelected() == 1) {
//            holder.canDo.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccentDark));
            holder.canDo.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_effect));
            holder.canDo.setTextColor(mContext.getResources().getColor(R.color.colorWhiteLight));
            holder.needHelp.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_rectangle));
            holder.needHelp.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

        } else if (eceModel.getIsSelected() == 2) {
//            holder.needHelp.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccentDark));
            holder.needHelp.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_effect));
            holder.needHelp.setTextColor(mContext.getResources().getColor(R.color.colorWhiteLight));

            holder.canDo.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_rectangle));
            holder.canDo.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

        } else {

            holder.canDo.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_rectangle));
            holder.needHelp.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_rectangle));
            holder.canDo.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
            holder.needHelp.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

        }

        holder.tv_ece_question.setText(eceModel.getQuestion());
        holder.tv_ece_question_title.setText(eceModel.getTitle());
        holder.canDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eceModel.setIsSelected(1);
                answerClickedListener.onAnswerClicked(position, 1);
//                holder.canDo.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccentDark));
                holder.canDo.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_effect));
                holder.canDo.setTextColor(mContext.getResources().getColor(R.color.colorWhiteLight));
                holder.needHelp.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_rectangle));
                holder.needHelp.setTextColor(mContext.getResources().getColor(R.color.colorBlack));


            }
        });
        holder.needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eceModel.setIsSelected(2);
                answerClickedListener.onAnswerClicked(position, 2);
//                holder.needHelp.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccentDark));
                holder.needHelp.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_effect));
                holder.needHelp.setTextColor(mContext.getResources().getColor(R.color.colorWhiteLight));
                holder.canDo.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_rectangle));
                holder.canDo.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

            }
        });

//        holder.tv_ece_instructions.setText(eceModel.getInstructions());
        /*if(eceModel.getRating().equalsIgnoreCase("true")){
            holder.ll_yes_no.setVisibility(View.GONE);
            holder.ll_rating.setVisibility(View.VISIBLE);
        }else {
            holder.ll_yes_no.setVisibility(View.VISIBLE);
            holder.ll_rating.setVisibility(View.GONE);
        }*/

       /* Glide.with(mContext).load(sdCardPathString+ "/StoryData/" +gamesList.getNodeImage()).into(holder.thumbnail);
        holder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClicked.onGameClicked(position);
            }
        });*/
    }

   /* public interface OnItemChangedListener<T extends MyViewHolder> {
        *//**
         * Called when new item is selected. It is similar to the onScrollEnd of ScrollStateChangeListener, except that it is * also called when currently selected item appears on the screen for the first time. * viewHolder will be null, if data set becomes empty
         *//*
          answerClickedListener.onAnswerClicked(position, 2);
    }*/

    @Override
    public int getItemCount() {
        return eceModelList.size();
    }
}