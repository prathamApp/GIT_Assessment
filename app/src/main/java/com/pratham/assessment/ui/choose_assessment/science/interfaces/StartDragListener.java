package com.pratham.assessment.ui.choose_assessment.science.interfaces;

import android.support.v7.widget.RecyclerView;

import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

public interface StartDragListener {
    void requestDrag(RecyclerView.ViewHolder viewHolder);
    void onItemDragged(List<ScienceQuestionChoice> draggedList);

}
