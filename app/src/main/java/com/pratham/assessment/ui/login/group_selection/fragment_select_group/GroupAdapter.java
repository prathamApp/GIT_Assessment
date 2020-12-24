package com.pratham.assessment.ui.login.group_selection.fragment_select_group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pratham.assessment.R;
import com.pratham.assessment.domain.Groups;

import java.util.ArrayList;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private ArrayList<Groups> datalist;
    private Context context;
    private ContractGroup contractGroup;

    public GroupAdapter(Context context, ArrayList<Groups> datalist, ContractGroup contractGroup) {
        this.context = context;
        this.datalist = datalist;
        this.contractGroup = contractGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_group, parent, false);
        return new GroupAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int pos) {
        pos = viewHolder.getAdapterPosition();
        viewHolder.group_name.setText(Html.fromHtml(datalist.get(pos).getGroupName()));
        if (datalist.get(pos).isSelected()) {

//            viewHolder.group_card.setCardBackgroundColor(context.getResources().getColor(R.color.colorGreen));
//            viewHolder.group_name.setTextColor(context.getResources().getColor(R.color.colorBlack));
            viewHolder.iv_tick.setVisibility(View.VISIBLE);
        } else {
//            viewHolder.group_card.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
//            viewHolder.group_name.setTextColor(context.getResources().getColor(R.color.colorBlack));
            viewHolder.iv_tick.setVisibility(View.INVISIBLE);

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractGroup.groupItemClicked(datalist.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
            }
        });
    }

    public void updateGroupItems(final ArrayList<Groups> newGroups) {
        datalist = newGroups;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.group_card)
        RelativeLayout group_card;
        //        @BindView(R.id.group_name)
        TextView group_name;
        //        @BindView(R.id.iv_tick)
        ImageView iv_tick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            group_card = itemView.findViewById(R.id.group_card);
            group_name = itemView.findViewById(R.id.group_name);
            iv_tick = itemView.findViewById(R.id.iv_tick);
        }
    }
}
