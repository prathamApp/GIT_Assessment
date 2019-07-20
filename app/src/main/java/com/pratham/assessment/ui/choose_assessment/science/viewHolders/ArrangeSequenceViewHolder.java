package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.adapters.DragDropAdapter;
import com.pratham.assessment.ui.choose_assessment.science.ItemMoveCallback;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.StartDragListener;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArrangeSequenceViewHolder extends RecyclerView.ViewHolder  implements StartDragListener {
    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;
    @BindView(R.id.rl_arrange_seq)
    RecyclerView recyclerArrangeSeq;
    ScienceQuestion scienceQuestion;
    Context context;
    ScienceAdapter scienceAdapter;
    ItemTouchHelper touchHelper;


    public ArrangeSequenceViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.scienceAdapter= scienceAdapter;

    }

    public void setArrangeSeqQuestion(ScienceQuestion scienceQuestion1, int pos) {
        this.scienceQuestion = scienceQuestion1;
        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().
                    load(Assessment_Constants.loadOnlineImagePath + scienceQuestion.getPhotourl()).apply(new RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable bd = new BitmapDrawable(resource);
                            questionImage.setImageDrawable(bd);
                        }
                    });


        }else questionImage.setVisibility(View.GONE);
        List list1 = new ArrayList();

        List<ScienceQuestionChoice> pairList = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        Log.d("wwwwwwwwwww", pairList.size() + "");
        if (!pairList.isEmpty()) {
            for (int p = 0; p < pairList.size(); p++) {
                list1.add(pairList.get(p).getChoicename());
            }

            DragDropAdapter dragDropAdapter = new DragDropAdapter(this, list1, context, scienceAdapter);
            ItemTouchHelper.Callback callback =
                    new ItemMoveCallback(dragDropAdapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(null);
            touchHelper.attachToRecyclerView(recyclerArrangeSeq);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context.getApplicationContext());
            recyclerArrangeSeq.setLayoutManager(linearLayoutManager1);
            recyclerArrangeSeq.setAdapter(dragDropAdapter);

        }
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);

    }
}
