package com.pratham.assessment.ui.choose_assessment.science.adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ItemMoveCallback;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.StartDragListener;
import com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.MatchThePairFragment;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchPairDragDropAdapter extends RecyclerView.Adapter<MatchPairDragDropAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    List<ScienceQuestionChoice> draggedList = new ArrayList<>();
    private List<ScienceQuestionChoice> data;
    Context context;
    //    DragDropListener dragDropListener;
    //    QuestionTypeListener questionTypeListener;
    StartDragListener startDragListener;
    AssessmentAnswerListener assessmentAnswerListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        ImageView iv_choice_image, iv_zoom_eye;
        View rowView;
        RelativeLayout rl_img;

        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            mTitle = itemView.findViewById(R.id.tv_text);
            iv_choice_image = itemView.findViewById(R.id.iv_choice_image);
            iv_zoom_eye = itemView.findViewById(R.id.iv_zoom_eye);
            rl_img = itemView.findViewById(R.id.rl_img);
        }
    }

    public MatchPairDragDropAdapter(MatchThePairFragment fragment, List<ScienceQuestionChoice> data, Context context) {
        this.data = data;
        this.context = context;
//        questionTypeListener = scienceAdapter;
        startDragListener = fragment;
        assessmentAnswerListener = (ScienceAssessmentActivity) context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_simple_text_row_old, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.mTitle.setText(data.get(position));
//        if(data.get(position))
        draggedList.clear();
        if (data.size() > 0) {
            ScienceQuestionChoice scienceQuestionChoice = data.get(position);
            if (!scienceQuestionChoice.getMatchingurl().equalsIgnoreCase("")) {
                final String path = /*Assessment_Constants.loadOnlineImagePath +*/ scienceQuestionChoice.getMatchingurl();

                String fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getMatchingurl());
                final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


//                holder.iv_choice_image.setVisibility(View.VISIBLE);
                holder.rl_img.setVisibility(View.VISIBLE);
                holder.mTitle.setVisibility(View.GONE);
                holder.mTitle.setTextColor(Color.WHITE);
               /* Glide.with(context).asBitmap().
                        load(path).apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                        .into(holder.iv_choice_image);*/

                Glide.with(context)
                        .load(path)
//                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(holder.iv_choice_image);

//                holder.iv_choice_image.setOnClickListener(new View.OnClickListener() {
                holder.iv_zoom_eye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("QQQ", "choice clicked....");
                      /*  ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                        zoomImageDialog.show();*/
                        Assessment_Utility.showZoomDialog(context, path, localPath, "");
                    }
                });
            } else {
                holder.rl_img.setVisibility(View.GONE);
                if (scienceQuestionChoice.getMatchingname() != null && !scienceQuestionChoice.getMatchingname().equalsIgnoreCase(""))
                    holder.mTitle.setText(scienceQuestionChoice.getMatchingname());
            }

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

//            holder.iv_choice_image.setOnTouchListener(new View.OnTouchListener() {
            holder.rl_img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() ==
                            MotionEvent.ACTION_DOWN) {
                        startDragListener.requestDrag(holder);
                    }
                    return false;
                }
            });

            //todo add bubbleshowcase
       /* if (!Assessment_Constants.isShowcaseDisplayed)
            if (position == 0) {
                Assessment_Constants.isShowcaseDisplayed = true;
                new BubbleShowCaseBuilder((Activity) context)
                        .title("Note: ")
                        .description("swap to match the answer on the right to the word on the left")
                        .backgroundColor(ContextCompat.getColor(context, R.color.colorAccentDark))
                        .closeActionImage(ContextCompat.getDrawable(context, R.drawable.ic_close))
                        .targetView(holder.itemView).show();
            }*/
        }

    }

    /*public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.mTitle.setText(data.get(position));
//        if(data.get(position))
        Assessment_Utility.setOdiaFont(context, holder.mTitle);
        draggedList.clear();
        if (data.size() > 0) {
            ScienceQuestionChoice scienceQuestionChoice = data.get(position);
            if (!scienceQuestionChoice.getMatchingurl().equalsIgnoreCase("")) {
                final String path = *//*Assessment_Constants.loadOnlineImagePath +*//* scienceQuestionChoice.getMatchingurl();

                String fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getMatchingurl());
                final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


                holder.iv_choice_image.setVisibility(View.VISIBLE);
                holder.mTitle.setVisibility(View.GONE);
                holder.mTitle.setTextColor(Color.WHITE);
                Glide.with(context).asBitmap().
                        load(path).apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                        .into(holder.iv_choice_image);

                holder.iv_choice_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      *//*  ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                        zoomImageDialog.show();*//*
                        Assessment_Utility.showZoomDialog(context, path, localPath);
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

            //todo add bubbleshowcase
       *//* if (!Assessment_Constants.isShowcaseDisplayed)
            if (position == 0) {
                Assessment_Constants.isShowcaseDisplayed = true;
                new BubbleShowCaseBuilder((Activity) context)
                        .title("Note: ")
                        .description("swap to match the answer on the right to the word on the left")
                        .backgroundColor(ContextCompat.getColor(context, R.color.colorAccentDark))
                        .closeActionImage(ContextCompat.getDrawable(context, R.drawable.ic_close))
                        .targetView(holder.itemView).show();
            }*//*
        }

    }*/


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        try {

            Log.d("QQQ", fromPosition + " " + toPosition);
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Log.d("QQQfor1", fromPosition + " " + toPosition);
                    Collections.swap(data, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Log.d("QQQfor2", fromPosition + " " + toPosition);
                    Collections.swap(data, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            draggedList = data;
            Log.d("sss", draggedList.toString());
//        dragDropListener.setList(draggedList, data.get(0).getQid());
//        questionTypeListener.setAnswer("", "", data.get(0).getQid(), draggedList);
            assessmentAnswerListener.setAnswerInActivity("", "", data.get(0).getQid(), draggedList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRowSelected(MatchPairDragDropAdapter.MyViewHolder myViewHolder) {
//       myViewHolder.rowView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ripple_rectangle));
        /*  myViewHolder.mTitle.setTextColor(Assessment_Utility.selectedColor);
         */
        myViewHolder.rowView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gradient_selector));
        myViewHolder.mTitle.setTextColor(Assessment_Utility.selectedColor);
    }

    @Override
    public void onRowSelected(ArrangeSeqDragDropAdapter.MyViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(MatchPairDragDropAdapter.MyViewHolder myViewHolder) {
//        myViewHolder.rowView.setBackgroundColor(Color.WHITE);
       /* myViewHolder.rowView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gradient_selector));
        myViewHolder.mTitle.setTextColor(Assessment_Utility.selectedColor);*/

    }

    @Override
    public void onRowClear(ArrangeSeqDragDropAdapter.MyViewHolder myViewHolder) {

    }
}


