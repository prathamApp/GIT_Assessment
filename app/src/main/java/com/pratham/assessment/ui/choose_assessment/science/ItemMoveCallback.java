package com.pratham.assessment.ui.choose_assessment.science;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.pratham.assessment.ui.choose_assessment.science.adapters.ArrangeSeqDragDropAdapter;
import com.pratham.assessment.ui.choose_assessment.science.adapters.MatchPairDragDropAdapter;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperContract mAdapter;

    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof MatchPairDragDropAdapter.MyViewHolder) {
                MatchPairDragDropAdapter.MyViewHolder myViewHolder =
                        (MatchPairDragDropAdapter.MyViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
            if (viewHolder instanceof ArrangeSeqDragDropAdapter.MyViewHolder) {
                ArrangeSeqDragDropAdapter.MyViewHolder myViewHolder =
                        (ArrangeSeqDragDropAdapter.MyViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof MatchPairDragDropAdapter.MyViewHolder) {
            MatchPairDragDropAdapter.MyViewHolder myViewHolder =
                    (MatchPairDragDropAdapter.MyViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
        if (viewHolder instanceof ArrangeSeqDragDropAdapter.MyViewHolder) {
            ArrangeSeqDragDropAdapter.MyViewHolder myViewHolder =
                    (ArrangeSeqDragDropAdapter.MyViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(MatchPairDragDropAdapter.MyViewHolder myViewHolder);

        void onRowSelected(ArrangeSeqDragDropAdapter.MyViewHolder myViewHolder);

        void onRowClear(MatchPairDragDropAdapter.MyViewHolder myViewHolder);

        void onRowClear(ArrangeSeqDragDropAdapter.MyViewHolder myViewHolder);

    }

}

