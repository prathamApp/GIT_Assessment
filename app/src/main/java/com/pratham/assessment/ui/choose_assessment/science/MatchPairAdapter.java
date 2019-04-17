package com.pratham.assessment.ui.choose_assessment.science;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;

public class MatchPairAdapter extends RecyclerView.Adapter<MatchPairAdapter.MyViewHolder> {
    List<ScienceQuestionChoice> pairList;
    Context context;

    public MatchPairAdapter(List<ScienceQuestionChoice> pairList, Context context) {
        this.pairList = pairList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_text);
            imageView = itemView.findViewById(R.id.iv_choice_image);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_simple_text_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        ScienceQuestionChoice scienceQuestionChoice = pairList.get(i);
        if (!scienceQuestionChoice.getChoiceurl().equalsIgnoreCase("")) {
            myViewHolder.imageView.setVisibility(View.VISIBLE);
            myViewHolder.text.setVisibility(View.GONE);

            Glide.with(context).asBitmap().
                    load(Assessment_Constants.loadOnlineImagePath + scienceQuestionChoice.getChoiceurl()).apply(new RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                    .into(myViewHolder.imageView);
        } else myViewHolder.text.setText(scienceQuestionChoice.getChoicename());

    }

    @Override
    public int getItemCount() {
        return pairList.size();
    }
}
