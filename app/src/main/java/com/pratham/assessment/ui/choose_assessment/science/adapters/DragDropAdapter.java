package com.pratham.assessment.ui.choose_assessment.science.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ItemMoveCallback;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ZoomImageDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.DragDropListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.StartDragListener;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.ArrangeSequenceViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.MatchThePairViewHolder;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragDropAdapter extends RecyclerView.Adapter<DragDropAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    List<ScienceQuestionChoice> draggedList = new ArrayList<>();
    private List<ScienceQuestionChoice> data;
    Context context;
    DragDropListener dragDropListener;
    QuestionTypeListener questionTypeListener;
    StartDragListener startDragListener;

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

    public DragDropAdapter(MatchThePairViewHolder matchThePairViewHolder, List<ScienceQuestionChoice> data, Context context, ScienceAdapter scienceAdapter) {
        this.data = data;
        this.context = context;
        questionTypeListener = scienceAdapter;
        startDragListener = matchThePairViewHolder;
    }

    public DragDropAdapter(ArrangeSequenceViewHolder arrangeSequenceViewHolder, List<ScienceQuestionChoice> data, Context context, ScienceAdapter scienceAdapter) {
        this.data = data;
        this.context = context;
        questionTypeListener = scienceAdapter;
        startDragListener = arrangeSequenceViewHolder;
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
        draggedList.clear();
        ScienceQuestionChoice scienceQuestionChoice = data.get(position);
        if (!scienceQuestionChoice.getMatchingurl().equalsIgnoreCase("")) {
            final String path=Assessment_Constants.loadOnlineImagePath + scienceQuestionChoice.getMatchingurl();
            holder.iv_choice_image.setVisibility(View.VISIBLE);
            holder.mTitle.setVisibility(View.GONE);
            Glide.with(context).asBitmap().
                    load(path).apply(new RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                    .into(holder.iv_choice_image);

            holder.iv_choice_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZoomImageDialog zoomImageDialog=new ZoomImageDialog(context,path);
                    zoomImageDialog.show();
                }
            });
        } else holder.mTitle.setText(scienceQuestionChoice.getMatchingname());

        holder.mTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    startDragListener.requestDrag(holder);
                }
                return false;
            }
        });

        holder.iv_choice_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    startDragListener.requestDrag(holder);
                }
                return false;
            }
        });

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
        draggedList = data;
        Log.d("sss", draggedList.toString());
//        dragDropListener.setList(draggedList, data.get(0).getQid());
        questionTypeListener.setAnswer("", "", data.get(0).getQid(), draggedList);

    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }
}


