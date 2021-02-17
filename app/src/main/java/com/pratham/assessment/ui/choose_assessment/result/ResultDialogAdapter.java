package com.pratham.assessment.ui.choose_assessment.result;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.List;

public class ResultDialogAdapter extends RecyclerView.Adapter<ResultDialogAdapter.MyViewHolder> {
    Context context;
    List<ScienceQuestionChoice> scienceQuestionChoices;
    String type = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView image, iv_zoom_eye;
        RelativeLayout rl_img, rl_root;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_text);
            image = itemView.findViewById(R.id.iv_choice_image);
            iv_zoom_eye = itemView.findViewById(R.id.iv_zoom_eye);
            rl_img = itemView.findViewById(R.id.rl_img);
            rl_root = itemView.findViewById(R.id.rl_root);
        }
    }


    public ResultDialogAdapter(Context context, List<ScienceQuestionChoice> scienceQuestionChoices, String type) {
        this.context = context;
        this.scienceQuestionChoices = scienceQuestionChoices;
        this.type = type;
    }

    @NonNull
    @Override
    public ResultDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_simple_text_row_old, viewGroup, false);
        return new ResultDialogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final ScienceQuestionChoice scienceQuestionChoice = scienceQuestionChoices.get(i);
        Assessment_Utility.setOdiaFont(context, myViewHolder.text);
        myViewHolder.text.setTextColor(Color.BLACK);
        if (type.equalsIgnoreCase("que")) {
            if (!scienceQuestionChoice.getChoiceurl().equalsIgnoreCase("")) {
                myViewHolder.rl_img.setVisibility(View.VISIBLE);
                myViewHolder.image.setVisibility(View.VISIBLE);
                myViewHolder.text.setVisibility(View.GONE);

                final String path = /*Assessment_Constants.loadOnlineImagePath +*/ scienceQuestionChoice.getChoiceurl();

                String fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getChoiceurl());
                final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


                Glide.with(context).asBitmap().
                        load(path).apply(new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(Drawable.createFromPath(localPath))
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                        .into(myViewHolder.image);

                myViewHolder.iv_zoom_eye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("QQQ", "choice clicked....");
                        String path = ""  /*Assessment_Constants.loadOnlineImagePath +*/;
                        String fileName = "";
                        String localPath = "";

                        if (type.equalsIgnoreCase("que")) {
                            path = scienceQuestionChoice.getChoiceurl();
                            fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getChoiceurl());
                            localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                        } else if (type.equalsIgnoreCase("ans")) {
                            path = scienceQuestionChoice.getMatchingurl();
                            fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getMatchingurl());
                            localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                        }
                        Assessment_Utility.showZoomDialog(context, path, localPath, "");
                    }
                });

            } else {
                myViewHolder.rl_img.setVisibility(View.GONE);
                myViewHolder.text.setText(Html.fromHtml(scienceQuestionChoice.getChoicename()));
            }
        } else {
            if (type.equalsIgnoreCase("ans")) {
               /* if (scienceQuestionChoice.getMyIscorrect().equalsIgnoreCase("true"))
                    myViewHolder.rl_root.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.green_bg));
                else myViewHolder.rl_root.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_bg));*/

                if (!scienceQuestionChoice.getMatchingurl().equalsIgnoreCase("")) {
                    myViewHolder.rl_img.setVisibility(View.VISIBLE);
                    myViewHolder.image.setVisibility(View.VISIBLE);
                    myViewHolder.text.setVisibility(View.GONE);

                    final String path = /*Assessment_Constants.loadOnlineImagePath +*/ scienceQuestionChoice.getMatchingurl();

                    String fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getMatchingurl());
                    final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


                    Glide.with(context).asBitmap().
                            load(path).apply(new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(Drawable.createFromPath(localPath))
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                            .into(myViewHolder.image);

                    myViewHolder.iv_zoom_eye.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("QQQ", "choice clicked....");
                            String path = ""  /*Assessment_Constants.loadOnlineImagePath +*/;
                            String fileName = "";
                            String localPath = "";

                            if (type.equalsIgnoreCase("que")) {
                                path = scienceQuestionChoice.getChoiceurl();
                                fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getChoiceurl());
                                localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                            } else if (type.equalsIgnoreCase("ans")) {
                                path = scienceQuestionChoice.getMatchingurl();
                                fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), scienceQuestionChoice.getMatchingurl());
                                localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                            }
                            Assessment_Utility.showZoomDialog(context, path, localPath, "");
                        }
                    });
                } else {
                    myViewHolder.rl_img.setVisibility(View.GONE);
                    myViewHolder.text.setText(Html.fromHtml(scienceQuestionChoice.getMatchingname()));
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return scienceQuestionChoices.size();
    }
}
