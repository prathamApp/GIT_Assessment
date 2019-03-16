package com.pratham.assessment.ui.certificate;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.CertificateModelClass;

import java.util.List;

/**
 * Created by Abc on 10-Jul-17.
 */

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.MyViewHolder>  {

    private Context mContext;
    private int lastPos = -1;
    //private List<ContentView> gamesViewList;
    private List<CertificateModelClass> certiViewList;
    CertificateClicked certificateClicked;
    public int quesIndex = 0;

    public void initializeIndex(){
        quesIndex = 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout certificate_card;
        public RatingBar ratingStars;

        public MyViewHolder(View view) {
            super(view);
            certificate_card = (RelativeLayout) view.findViewById(R.id.certificate_card_view);
            title = (TextView) view.findViewById(R.id.assess_data);
            ratingStars = (RatingBar) view.findViewById(R.id.ratingStars);
        }
    }

    public CertificateAdapter(Context mContext, List<CertificateModelClass> certiViewList, CertificateClicked certificateClicked) {
        this.mContext = mContext;
        this.certiViewList = certiViewList;
        this.certificateClicked = certificateClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.certi_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //final ContentView gamesList = gamesViewList.get(position);
        final CertificateModelClass certiList = certiViewList.get(position);

        Animation animation = null;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.item_fall_down);
        animation.setDuration(500);

        String ques="";

        if(certiList.getCodeCount()>1) {
            quesIndex += 1;
            ques=""+quesIndex+". ";
        }else {
//            quesIndex = 0;
            ques="";
        }

        if(!certiList.isAsessmentGiven()){
            holder.ratingStars.setVisibility(View.GONE);
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("English"))
                holder.title.setText(ques+certiList.getEnglishQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Hindi"))
                holder.title.setText(ques+certiList.getHindiQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Marathi"))
                holder.title.setText(ques+certiList.getMarathiQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Gujarati"))
                holder.title.setText(ques+certiList.getGujaratiQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Kannada"))
                holder.title.setText(ques+certiList.getKannadaQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Bengali"))
                holder.title.setText(ques+certiList.getBengaliQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Assamese"))
                holder.title.setText(ques+certiList.getAssameseQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Telugu"))
                holder.title.setText(ques+certiList.getTeluguQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Tamil"))
                holder.title.setText(ques+certiList.getTamilQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Odia"))
                holder.title.setText(ques+certiList.getOdiaQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Urdu"))
                holder.title.setText(ques+certiList.getUrduQues());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Punjabi"))
                holder.title.setText(ques+certiList.getPunjabiQues());

            holder.certificate_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    certificateClicked.onCertificateOpenGame(position,certiList.getNodeId());
                }
            });
        }else{
            holder.ratingStars.setVisibility(View.VISIBLE);
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("English"))
                holder.title.setText(ques+certiList.getEnglishAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Hindi"))
                holder.title.setText(ques+certiList.getHindiAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Marathi"))
                holder.title.setText(ques+certiList.getMarathiAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Gujarati"))
                holder.title.setText(ques+certiList.getGujaratiAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Kannada"))
                holder.title.setText(ques+certiList.getKannadaAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Bengali"))
                holder.title.setText(ques+certiList.getBengaliAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Assamese"))
                holder.title.setText(ques+certiList.getAssameseAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Telugu"))
                holder.title.setText(ques+certiList.getTeluguAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Tamil"))
                holder.title.setText(ques+certiList.getTamilAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Odia"))
                holder.title.setText(ques+certiList.getOdiaAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Urdu"))
                holder.title.setText(ques+certiList.getUrduAnsw());
            if(CertificateActivity.certificateLanguage.equalsIgnoreCase("Punjabi"))
                holder.title.setText(ques+certiList.getPunjabiAnsw());

            holder.ratingStars.setRating(certiList.getCertificateRating());

            holder.certificate_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //certificateClicked.onCertificateOpenGame(position,certiList.getNodeId());
                }
            });

        }


/*        holder.iv_download.setVisibility(View.GONE);

        if (gamesList.getNodeType().equalsIgnoreCase("Resource") && !gamesList.getIsDownloaded().equalsIgnoreCase("true")) {
            holder.iv_download.setVisibility(View.VISIBLE);
        }else if (gamesList.getNodeType().equalsIgnoreCase("Resource") && gamesList.getIsDownloaded().equalsIgnoreCase("true")) {
            holder.iv_download.setVisibility(View.GONE);
        }


        holder.game_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.game_card_view.setVisibility(View.GONE);
        setAnimations(holder.game_card_view, position);*/

    }

    private void setAnimations(final View content_card_view, final int position) {
        final Animation animation;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.item_fall_down);
        animation.setDuration(500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*                if (position > lastPos
                ) {*/
                content_card_view.setVisibility(View.VISIBLE);
                content_card_view.setAnimation(animation);
                lastPos = position;
//                }
            }
        }, (long) (20));

    }


    @Override
    public int getItemCount() {
        return certiViewList.size();
    }
}

/*private AppCompatActivity activity;
    private List<CertificatePercentModal> gameList;

    public CertificateAdapter(AppCompatActivity context,int resource, List<CertificatePercentModal> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.gameList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.certi_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.setTag(position);
        }

        final float growTo = 1f;
        final long duration = 2000;
        final AnimationSet growAndShrink;
        ScaleAnimation grow = new ScaleAnimation(0, growTo, 0, growTo, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        grow.setDuration(duration / 2);
*//*        ScaleAnimation shrink = new ScaleAnimation(growTo, 0, growTo, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(duration / 2);
        shrink.setStartOffset(duration / 2);*//*
        growAndShrink = new AnimationSet(true);
        growAndShrink.setInterpolator(new LinearInterpolator());
        growAndShrink.addAnimation(grow);
//        growAndShrink.addAnimation(shrink);

        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getStudentPercentage()*//*test.getStudentPercentage()*//*);
        holder.assessData.setText(getItem(position).getCertitext());
        holder.ratingBar.startAnimation(growAndShrink);

        grow.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                *//*float current = holder.ratingBar.getRating();
                ObjectAnimator anim = ObjectAnimator.ofFloat(holder.ratingBar, "rating", current, getItem(position).getStudentPercentage());
                anim.setDuration(1500);
                anim.start();*//*
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        private RatingBar ratingBar;
        private TextView assessData;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.RatingStars);
            assessData = (TextView) view.findViewById(R.id.assess_data);

        }
    }
}*/
