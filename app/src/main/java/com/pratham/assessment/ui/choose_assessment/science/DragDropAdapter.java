package com.pratham.assessment.ui.choose_assessment.science;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.Collections;
import java.util.List;

public class DragDropAdapter extends RecyclerView.Adapter<DragDropAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private List<ScienceQuestionChoice> data;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        ImageView iv_choice_image;
        View rowView;

        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            mTitle = itemView.findViewById(R.id.tv_text);
            iv_choice_image = itemView.findViewById(R.id.iv_choice_image);
        }
    }

    public DragDropAdapter(List<ScienceQuestionChoice> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_simple_text_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.mTitle.setText(data.get(position));
//        if(data.get(position))

        ScienceQuestionChoice scienceQuestionChoice = data.get(position);
        if (!scienceQuestionChoice.getMatchingurl().equalsIgnoreCase("")) {
            holder.iv_choice_image.setVisibility(View.VISIBLE);
            holder.mTitle.setVisibility(View.GONE);
            Glide.with(context).asBitmap().
                    load(Assessment_Constants.loadOnlineImagePath + scienceQuestionChoice.getMatchingurl()).apply(new RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                    .into(holder.iv_choice_image);
        } else holder.mTitle.setText(scienceQuestionChoice.getMatchingname());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }
}


