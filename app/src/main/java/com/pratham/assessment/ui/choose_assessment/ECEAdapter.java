package com.pratham.assessment.ui.choose_assessment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.assessment.R;

import java.util.List;

public class ECEAdapter extends RecyclerView.Adapter<ECEAdapter.MyViewHolder> {

    private Context mContext;
    //private List<ModalGames> gamesViewList;
    //GameClicked gameClicked;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public CardView game_card_view;

        public MyViewHolder(View view) {
            super(view);
            /*title = (TextView) view.findViewById(R.id.game_title);
            thumbnail = (ImageView) view.findViewById(R.id.game_thumbnail);
            game_card_view = (CardView) view.findViewById(R.id.game_card_view);*/
        }
    }

    public ECEAdapter(Context mContext) {
        this.mContext = mContext;
       /* this.gamesViewList = gamesViewList;
        this.gameClicked = gameClicked;*/
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ece_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       // final ModalGames gamesList = gamesViewList.get(position);


   /*     holder.title.setText(gamesList.getNodeTitle());
        ex_path = new SdCardPath(mContext);
        sdCardPathString = ex_path.getSdCardPath();
*/

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
            return 0; /*gamesViewList.size();*/
    }
}