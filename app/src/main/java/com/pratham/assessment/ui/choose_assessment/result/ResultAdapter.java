package com.pratham.assessment.ui.choose_assessment.result;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ResultModalClass;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ImageListDialog_;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AudioPlayerInterface;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.AudioUtil;

import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.constants.Assessment_Constants.ARRANGE_SEQUENCE;
import static com.pratham.assessment.constants.Assessment_Constants.AUDIO;
import static com.pratham.assessment.constants.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_AUDIO;
import static com.pratham.assessment.constants.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE;
import static com.pratham.assessment.constants.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA;
import static com.pratham.assessment.constants.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO;
import static com.pratham.assessment.constants.Assessment_Constants.FILL_IN_THE_BLANK;
import static com.pratham.assessment.constants.Assessment_Constants.FILL_IN_THE_BLANK_WITH_OPTION;
import static com.pratham.assessment.constants.Assessment_Constants.IMAGE_ANSWER;
import static com.pratham.assessment.constants.Assessment_Constants.KEYWORDS_QUESTION;
import static com.pratham.assessment.constants.Assessment_Constants.MATCHING_PAIR;
import static com.pratham.assessment.constants.Assessment_Constants.MULTIPLE_CHOICE;
import static com.pratham.assessment.constants.Assessment_Constants.MULTIPLE_SELECT;
import static com.pratham.assessment.constants.Assessment_Constants.TEXT_PARAGRAPH;
import static com.pratham.assessment.constants.Assessment_Constants.TRUE_FALSE;
import static com.pratham.assessment.constants.Assessment_Constants.VIDEO;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileExtension;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> implements AudioPlayerInterface {
    Context context;
    List<ResultModalClass> resultList;
    ResultListener resultListener;
    boolean isAudioPlaying = false, isQuestionAudioPlaying = false;
    View prevView, currentView;

    public ResultAdapter(Context context, List<ResultModalClass> resultList, ResultFragment resultFragment) {
        this.context = context;
        this.resultList = resultList;
        resultListener = resultFragment;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question, userAnswer, correctAnswer, correctAnsLabel, btnUserAnswer, btnCorrectAnswer, tv_you_answered_label;
        CardView cardView;
        ImageView questionImg;
        ImageView iv_correct_wrong_indicator, image_you_answered, image_correct_ans;
        LinearLayout ll_correct_ans, ll_user_ans, ll_ans;
        VideoView questionVideo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_result_question);
            tv_you_answered_label = itemView.findViewById(R.id.tv_you_answered_label);
            questionImg = itemView.findViewById(R.id.question_img);
            userAnswer = itemView.findViewById(R.id.tv_you_answered);
            correctAnswer = itemView.findViewById(R.id.tv_correct_answer);
            correctAnsLabel = itemView.findViewById(R.id.tv_correct_answer_label);
            btnUserAnswer = itemView.findViewById(R.id.btn_you_answered);
            btnCorrectAnswer = itemView.findViewById(R.id.btn_correct_Ans);
            cardView = itemView.findViewById(R.id.result_card_view);
            ll_correct_ans = itemView.findViewById(R.id.ll_correct_ans);
            ll_user_ans = itemView.findViewById(R.id.ll_user_ans);
            iv_correct_wrong_indicator = itemView.findViewById(R.id.iv_correct_wrong_indicator);
            image_correct_ans = itemView.findViewById(R.id.media_correct_Ans);
            image_you_answered = itemView.findViewById(R.id.media_you_answered);
            questionVideo = itemView.findViewById(R.id.question_video);
//            answerVideo = itemView.findViewById(R.id.video_you_answered);
            ll_ans = itemView.findViewById(R.id.ll_answers);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_result_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
//        myViewHolder.setIsRecyclable(false);

        setOdiaFont(context, myViewHolder.question);
        setOdiaFont(context, myViewHolder.userAnswer);
        setOdiaFont(context, myViewHolder.correctAnswer);
        setOdiaFont(context, myViewHolder.correctAnsLabel);
        setOdiaFont(context, myViewHolder.btnUserAnswer);
        setOdiaFont(context, myViewHolder.btnCorrectAnswer);
        setOdiaFont(context, myViewHolder.tv_you_answered_label);

        if (i == resultList.size() - 1) {
            resultListener.showDone(true);
        } else resultListener.showDone(false);


        final ResultModalClass result = resultList.get(i);
        myViewHolder.itemView.setTag(result.getqId());
        myViewHolder.questionImg.setVisibility(View.GONE);

        if (!result.getQuestionImg().equalsIgnoreCase("")) {
            myViewHolder.questionImg.setVisibility(View.VISIBLE);
            myViewHolder.questionVideo.setVisibility(View.GONE);
            String fileName = Assessment_Utility.getFileName(result.getqId(), result.getQuestionImg());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
            final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


//            if (wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            final String path = result.getQuestionImg();
            final String[] imgPath = path.split("\\.");
            final int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                Glide.with(context).asGif()
                        .load(path)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .placeholder(Drawable.createFromPath(localPath))
                        )
                        .into(myViewHolder.questionImg);
                myViewHolder.questionImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                        zoomImageDialog.show();*/
                        Assessment_Utility.showZoomDialog(context, path, localPath, "");

                    }
                });
            } else if (imgPath[len].equalsIgnoreCase("jpg") || imgPath[len].equalsIgnoreCase("jpeg") || imgPath[len].equalsIgnoreCase("png")) {
                Glide.with(context)
                        .load(path)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .placeholder(Drawable.createFromPath(localPath))
                        )
                        .into(myViewHolder.questionImg);
                myViewHolder.questionImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                        zoomImageDialog.show();*/
                        Assessment_Utility.showZoomDialog(context, path, localPath, "");

                    }
                });
            } else if (imgPath[len].equalsIgnoreCase("mp3")) {
                myViewHolder.questionImg.setImageResource(R.drawable.ic_play);
                myViewHolder.questionImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentView = v;
                        playMedia(path, localPath, v, imgPath[len]);
                    }
                });

            } else if (imgPath[len].equalsIgnoreCase("mp4") || imgPath[len].equalsIgnoreCase("3gp")) {
                myViewHolder.questionImg.setImageResource(R.drawable.ic_play);
                myViewHolder.questionImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myViewHolder.questionImg.setVisibility(View.GONE);
                        myViewHolder.questionVideo.setVisibility(View.VISIBLE);
                        playMedia(path, localPath, myViewHolder.questionVideo, imgPath[len]);
                    }
                });

            }
           /* myViewHolder.questionImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                    zoomImageDialog.show();
                }
            });*/
           /* } else {
               Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                myViewHolder.questionImg.setImageBitmap(bitmap);
                myViewHolder.questionImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, localPath,localPath);
                        zoomImageDialog.show();
                    }
                });
            }*/

        } else myViewHolder.questionImg.setVisibility(View.GONE);


        myViewHolder.question.setText(Html.fromHtml(result.getQuestion()));
        if (!result.getUserAnswer().equalsIgnoreCase(""))
            myViewHolder.userAnswer.setText(Html.fromHtml(result.getUserAnswer()));
       /* else if (!result.getUserAnswerId().equalsIgnoreCase("")) {
            showButtons(myViewHolder, true);
        }*/

        myViewHolder.btnCorrectAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultModalClass result = resultList.get(myViewHolder.getAdapterPosition());

                List<ScienceQuestionChoice> scienceQuestionChoice = AppDatabase.
                        getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(result.getqId());
                String corrImg = "", localPath = "";
                for (int i = 0; i < scienceQuestionChoice.size(); i++) {
                    if (scienceQuestionChoice.get(i).getCorrect().equalsIgnoreCase("true")) {
//                        if (wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        corrImg = scienceQuestionChoice.get(i).getChoiceurl();
//                        } else {
//                            String dirPath = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded";
                        String dirPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH;
                        String fileName = getFileName(scienceQuestionChoice.get(i).getQid(), scienceQuestionChoice.get(i).getChoiceurl());
                        localPath = dirPath + "/" + fileName;
//                        }
                        /*ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, corrImg, localPath);
                        zoomImageDialog.show();*/
                        Assessment_Utility.showZoomDialog(context, corrImg, localPath, "");

                    }
                }

            }
        });
        myViewHolder.btnUserAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultModalClass result = resultList.get(myViewHolder.getAdapterPosition());

                String dirPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH;
                String img = getImage(result.getUserAnswerId());
                if (img.equalsIgnoreCase("")) {
                   /* ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, result.getUserAnswer(), result.getUserAnswer());
                    zoomImageDialog.show();*/
                    Assessment_Utility.showZoomDialog(context, result.getUserAnswer(), result.getUserAnswer(), "");

                } else {
                    String fileName = getFileName(result.getqId(), img);
                    String localPath = dirPath + "/" + fileName;

                    /*ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, img, localPath);
                    zoomImageDialog.show();*/
                    Assessment_Utility.showZoomDialog(context, img, localPath, "");

                }
            }
        });


        if (!result.isAttempted()) {
            myViewHolder.userAnswer.setText(R.string.skipped);
            myViewHolder.userAnswer.setTextColor(context.getResources().getColor(R.color.colorProgress15));
        } else {
            int color = myViewHolder.tv_you_answered_label.getCurrentTextColor();
            myViewHolder.userAnswer.setTextColor(color);

        }
        myViewHolder.correctAnswer.setText(Html.fromHtml(result.getCorrectAnswer()));
        if (result.isCorrect()) {
            myViewHolder.iv_correct_wrong_indicator.setImageResource(R.drawable.ic_check_black);
            myViewHolder.iv_correct_wrong_indicator.setBackgroundColor(context.getResources().getColor(R.color.green));

            myViewHolder.cardView.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
            myViewHolder.ll_user_ans.setVisibility(View.GONE);
            ((TextView) myViewHolder.ll_correct_ans.getChildAt(0)).setText(R.string.answer);

        } else {
            myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
            myViewHolder.ll_correct_ans.setVisibility(View.VISIBLE);
            if (!myViewHolder.correctAnswer.getText().equals(""))
                myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
            myViewHolder.iv_correct_wrong_indicator.setImageResource(R.drawable.ic_close_black_24dp);
            myViewHolder.iv_correct_wrong_indicator.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            myViewHolder.cardView.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
        }

        final ScienceQuestion scienceQuestion = getQuestion(result.getqId());
        final List<ScienceQuestionChoice> scienceQuestionChoice = AppDatabase
                .getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(result.getqId());
        switch (scienceQuestion.getQtid()) {
            case ARRANGE_SEQUENCE:
            case MATCHING_PAIR:
                myViewHolder.image_you_answered.setVisibility(View.GONE);
                myViewHolder.image_correct_ans.setVisibility(View.GONE);

                showButtons(myViewHolder, result.isAttempted());


                if (result.isAttempted()) {
                    if (result.isCorrect()) {
                        myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
                        myViewHolder.btnUserAnswer.setVisibility(View.VISIBLE);

                    }
                }
                myViewHolder.btnCorrectAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowAnswerDialog showAnswerDialog = new ShowAnswerDialog(context, scienceQuestionChoice);
                        showAnswerDialog.show();
                    }
                });
                myViewHolder.btnUserAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ScienceQuestionChoice> userAns = result.getUserAnsList();
                        /*String ans = result.getUserAnswer();
                        String[] ansArr = ans.split(",");
                        for (int i = 0; i < ansArr.length; i++) {
                            for (int j = 0; j < scienceQuestionChoice.size(); j++) {
                                if (scienceQuestionChoice.get(j).getQcid().equalsIgnoreCase(ansArr[i])) {
                                    userAns.add(scienceQuestionChoice.get(j));
                                }
                            }
                        }*/
                        ShowAnswerDialog showAnswerDialog = new ShowAnswerDialog(context, userAns, scienceQuestionChoice);
                        showAnswerDialog.show();
                    }
                });
                if (!result.isAttempted()) {
                    myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                }
                break;
            case MULTIPLE_SELECT:
                myViewHolder.image_you_answered.setVisibility(View.GONE);
                myViewHolder.image_correct_ans.setVisibility(View.GONE);
                showButtons(myViewHolder, result.isAttempted());

                myViewHolder.btnCorrectAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ScienceQuestionChoice> correctAns = new ArrayList<>();

                        for (int i = 0; i < scienceQuestionChoice.size(); i++) {
                            if (scienceQuestionChoice.get(i).getCorrect().equalsIgnoreCase("true"))
                                correctAns.add(scienceQuestionChoice.get(i));
                        }
                        ShowAnswerDialog showAnswerDialog = new ShowAnswerDialog(context, correctAns, MULTIPLE_SELECT);
                        showAnswerDialog.show();
                    }
                });
                myViewHolder.btnUserAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ScienceQuestionChoice> userAns = new ArrayList<>();
                        String ans = result.getUserAnswer();
                        String[] ansArr = ans.split(",");
                        for (int a = 0; a < ansArr.length; a++) {
                            for (int j = 0; j < scienceQuestionChoice.size(); j++) {
                                if (scienceQuestionChoice.get(j).getQcid().equalsIgnoreCase(ansArr[a])) {
                                    userAns.add(scienceQuestionChoice.get(j));
                                }
                            }
                        }
                        ShowAnswerDialog showAnswerDialog = new ShowAnswerDialog(context, userAns, MULTIPLE_SELECT);
                        showAnswerDialog.show();
                    }
                });
                if (!result.isAttempted()) {
                    myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                }
                break;
          /*  case ARRANGE_SEQUENCE:
                showButtons(myViewHolder, result.isAttempted());
                myViewHolder.btnCorrectAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowAnswerDialog showAnswerDialog = new ShowAnswerDialog(context, scienceQuestionChoice);
                        showAnswerDialog.show();
                    }
                });
                break;*/
            case FILL_IN_THE_BLANK_WITH_OPTION:
            case MULTIPLE_CHOICE:
                myViewHolder.image_you_answered.setVisibility(View.GONE);
                myViewHolder.image_correct_ans.setVisibility(View.GONE);
                ScienceQuestionChoice correctAns = new ScienceQuestionChoice();
                ScienceQuestionChoice userAns = new ScienceQuestionChoice();
                for (int j = 0; j < scienceQuestionChoice.size(); j++) {
                    if (scienceQuestionChoice.get(j).getCorrect().equalsIgnoreCase("true")) {
                        correctAns = scienceQuestionChoice.get(j);
//                        userAns = scienceQuestionChoice.get(j);
                    }
                    if (scienceQuestionChoice.get(j).getQcid().equalsIgnoreCase(result.getUserAnswerId())) {
                        userAns = scienceQuestionChoice.get(j);
                    }
                }
//                for (int j = 0; j < scienceQuestionChoice.size(); j++) {
                if (!result.isAttempted()) {
                    if (correctAns.getChoiceurl().equalsIgnoreCase("")) {
                        myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                        myViewHolder.image_correct_ans.setVisibility(View.GONE);
                        myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                    } else {
                        setImage(correctAns, correctAns.getChoiceurl(), myViewHolder.image_correct_ans);
//                            setImage(userAns, getImage(result.getUserAnswerId()), myViewHolder.image_you_answered);
                        myViewHolder.ll_correct_ans.setVisibility(View.VISIBLE);
                        myViewHolder.correctAnswer.setVisibility(View.GONE);
//                                myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
                        myViewHolder.image_correct_ans.setVisibility(View.VISIBLE);
                    }

                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.image_you_answered.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.userAnswer.setText(R.string.skipped);
                    myViewHolder.userAnswer.setTextColor(context.getResources().getColor(R.color.colorProgress15));


                } else {
                    int color = myViewHolder.tv_you_answered_label.getCurrentTextColor();
                    myViewHolder.userAnswer.setTextColor(color);
                    if (result.getUserAnswerId().equalsIgnoreCase(userAns.getQcid()) ||
                            result.getUserAnswer().equalsIgnoreCase(userAns.getChoicename())) {
                        if (userAns.getChoiceurl().equalsIgnoreCase("")) {
                            myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                            myViewHolder.image_correct_ans.setVisibility(View.GONE);
                            myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                            myViewHolder.image_you_answered.setVisibility(View.GONE);
                            myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                            myViewHolder.tv_you_answered_label.setVisibility(View.VISIBLE);
                        } else {
                            setImage(correctAns, correctAns.getChoiceurl(), myViewHolder.image_correct_ans);
                            setImage(userAns, getImage(result.getUserAnswerId()), myViewHolder.image_you_answered);
//                                myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
                            myViewHolder.image_correct_ans.setVisibility(View.VISIBLE);
                            myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                            myViewHolder.image_you_answered.setVisibility(View.VISIBLE);
                            myViewHolder.userAnswer.setVisibility(View.GONE);

                            myViewHolder.ll_correct_ans.setVisibility(View.VISIBLE);
                            myViewHolder.correctAnsLabel.setVisibility(View.VISIBLE);

                        }
                    }
                }
                if (!result.isCorrect() && result.isAttempted()) {
                    if (userAns.getCorrect().equalsIgnoreCase("true")) {
                        if (!userAns.getChoiceurl().equalsIgnoreCase("")) {
                            myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                            myViewHolder.correctAnswer.setVisibility(View.GONE);
                            myViewHolder.image_correct_ans.setVisibility(View.VISIBLE);
                            setImage(correctAns, correctAns.getChoiceurl(), myViewHolder.image_correct_ans);
                            setImage(userAns, getImage(result.getUserAnswerId()), myViewHolder.image_you_answered);
                        }
                        if (!userAns.getChoicename().equalsIgnoreCase("")) {
                            myViewHolder.ll_correct_ans.setVisibility(View.VISIBLE);
                            myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                        }
                        if (!result.getUserAnswer().equalsIgnoreCase("")) {
                            myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                            myViewHolder.tv_you_answered_label.setVisibility(View.VISIBLE);
                        }
                    }
                }
//                }
                if (!myViewHolder.correctAnswer.getText().equals("")) {
//                    if (userAns.getChoiceurl().equalsIgnoreCase(""))
                    myViewHolder.ll_correct_ans.setVisibility(View.VISIBLE);
                    myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.correctAnsLabel.setVisibility(View.VISIBLE);

                } else {
                    myViewHolder.correctAnswer.setVisibility(View.GONE);
//                    myViewHolder.correctAnsLabel.setVisibility(View.GONE);

                }

             /*   if (!result.isAttempted()) {
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.userAnswer.setText("Skipped");
                    myViewHolder.userAnswer.setTextColor(context.getResources().getColor(R.color.colorProgress15));
                } else {
                    int color = myViewHolder.tv_you_answered_label.getCurrentTextColor();
                    myViewHolder.userAnswer.setTextColor(color);

                }*/
                break;
            case TEXT_PARAGRAPH:
            case KEYWORDS_QUESTION:
                myViewHolder.image_you_answered.setVisibility(View.GONE);
                myViewHolder.image_correct_ans.setVisibility(View.GONE);
                myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                myViewHolder.btnUserAnswer.setVisibility(View.GONE);
//                if (result.isCorrect()) {
                myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                myViewHolder.tv_you_answered_label.setVisibility(View.VISIBLE);
               /* } else {
                    myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                }*/
                break;
            case FILL_IN_THE_BLANK:
                myViewHolder.image_you_answered.setVisibility(View.GONE);
                myViewHolder.image_correct_ans.setVisibility(View.GONE);
                myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                if (result.isCorrect()) {
                    if (!result.getCorrectAnswer().equalsIgnoreCase("")) {
                        myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                        myViewHolder.userAnswer.setVisibility(View.GONE);
                    } else {
                        myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                        myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                        myViewHolder.correctAnswer.setVisibility(View.VISIBLE);

                    }
                } else {
                    if (!result.getCorrectAnswer().equalsIgnoreCase("")) {

                        myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                        myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                        myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case AUDIO:
            case VIDEO:
            case IMAGE_ANSWER:
                myViewHolder.image_you_answered.setVisibility(View.GONE);
                myViewHolder.image_correct_ans.setVisibility(View.GONE);
                if (result.isAttempted()) {
                    myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                    myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
//                    myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                    myViewHolder.btnUserAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.userAnswer.setVisibility(View.GONE);
//                    myViewHolder.correctAnswer.setVisibility(View.GONE);
                    myViewHolder.tv_you_answered_label.setVisibility(View.GONE);

                } else {
//                    myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.tv_you_answered_label.setVisibility(View.GONE);
//                    myViewHolder.correctAnswer.setVisibility(View.GONE);
                    myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                }

                myViewHolder.btnUserAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (result.getUserAnsList() != null && result.getUserAnsList().size() > 0) {
                            List imageList = new ArrayList();
                            for (int j = 0; j < result.getUserAnsList().size(); j++) {
                                Uri uri = Uri.parse(result.getUserAnsList().get(j).getQcid());
                                Log.d("Uri", "onClick: " + uri);
                                // TODO: 13-Mar-21 check this ri issue
                                imageList.add(result.getUserAnsList().get(j).getQcid());
                            }
//                            List imgs = Arrays.asList(result.getUserAnswer().split(","));
//                            imageList.addAll(imgs);
                            Intent intent = new Intent(context, ImageListDialog_.class);
                            intent.putParcelableArrayListExtra("imageList", (ArrayList<? extends Parcelable>) imageList);
                            if (scienceQuestion.getQtid().equalsIgnoreCase(VIDEO))
                                intent.putExtra(DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA, DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO);
                            else if (scienceQuestion.getQtid().equalsIgnoreCase(AUDIO))
                                intent.putExtra(DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA, DOWNLOAD_MEDIA_TYPE_ANSWER_AUDIO);
                            else if (scienceQuestion.getQtid().equalsIgnoreCase(IMAGE_ANSWER))
                                intent.putExtra(DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA, DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE);
                            intent.putExtra("showDeleteButton", false);
                            context.startActivity(intent);
                        }
                    }
                });
                break;
           /* case VIDEO:
                if (result.getQuestion().equalsIgnoreCase(""))
                    myViewHolder.question.setText(R.string.video);
                if (result.isAttempted()) {
                    myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                    myViewHolder.question.setVisibility(View.VISIBLE);

                    myViewHolder.userAnswer.setVisibility(View.GONE);
                    myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.image_you_answered.setVisibility(View.VISIBLE);
                    myViewHolder.image_you_answered.setImageResource(R.drawable.ic_play);
                    myViewHolder.image_you_answered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            myViewHolder.image_you_answered.setVisibility(View.GONE);
//                            myViewHolder.answerVideo.setVisibility(View.VISIBLE);
                            showZoomDialog(context, result.getUserAnswer(), result.getUserAnswer(), "");
//                            playMedia(result.getUserAnswer(), result.getUserAnswer(), myViewHolder.answerVideo, "mp4");
                        }
                    });

                } else {
                    myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                }
                break;
            case AUDIO:
                if (result.getQuestion().equalsIgnoreCase(""))
                    myViewHolder.question.setText(R.string.audio);
                if (result.isAttempted()) {
                    myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                    myViewHolder.question.setVisibility(View.VISIBLE);

                    myViewHolder.userAnswer.setVisibility(View.GONE);
                    myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.image_you_answered.setVisibility(View.VISIBLE);
                    myViewHolder.image_you_answered.setImageResource(R.drawable.ic_play);
                    isAudioPlaying = false;
                    myViewHolder.image_you_answered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (prevView != null)
                                ((ImageView) prevView).setImageResource(R.drawable.ic_play);
                            currentView = view;
                            if (prevView != null && view != prevView && isAudioPlaying) {
                                ((ImageView) prevView).setImageResource(R.drawable.ic_play);
                                isAudioPlaying = false;
                                AudioUtil.stopPlayingAudio();
                                stopPlayer();
                            }
                            prevView = view;
                            if (isAudioPlaying) {
                                isAudioPlaying = false;
                                ((ImageView) view).setImageResource(R.drawable.ic_play);
                                AudioUtil.stopPlayingAudio();
                                stopPlayer();

                            } else {
                                isAudioPlaying = true;
                                ((ImageView) view).setImageResource(R.drawable.ic_pause);
                                if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                                    AudioUtil.playRecording(result.getUserAnswer(), ResultAdapter.this);
                                else
                                    AudioUtil.playRecording(result.getUserAnswer(), ResultAdapter.this);
                            }
                        }
                    });

                } else {
                    myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);
                    myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.ll_correct_ans.setVisibility(View.GONE);
                }
                break;
*/
            case TRUE_FALSE:
                myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                myViewHolder.image_you_answered.setVisibility(View.GONE);
                myViewHolder.image_correct_ans.setVisibility(View.GONE);
//                myViewHolder.answerVideo.setVisibility(View.GONE);
                if (result.isAttempted()) {
                    if (result.isCorrect()) {
                        myViewHolder.ll_correct_ans.setVisibility(View.VISIBLE);
                        myViewHolder.ll_user_ans.setVisibility(View.GONE);
                    } else {
                        myViewHolder.ll_correct_ans.setVisibility(View.VISIBLE);
                        myViewHolder.ll_user_ans.setVisibility(View.VISIBLE);

                    }
                }
                break;
        }

    }

    private void playMedia(String path, String localPath, View view, String extension) {
        if (extension.equalsIgnoreCase("mp3")) {
            if (isQuestionAudioPlaying) {
                isQuestionAudioPlaying = false;
                ((ImageView) view).setImageResource(R.drawable.ic_play);
                AudioUtil.stopPlayingAudio();
                stopPlayer();

            } else {
                isQuestionAudioPlaying = true;
                ((ImageView) view).setImageResource(R.drawable.ic_pause);
                if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                    AudioUtil.playRecording(path, ResultAdapter.this);
                else AudioUtil.playRecording(localPath, ResultAdapter.this);
            }
        } else if (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("3gp")) {
            MediaController mediaController = new MediaController(context);
            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                ((VideoView) view).setVideoPath(path);
            else ((VideoView) view).setVideoPath(localPath);

            ((VideoView) view).setMediaController(mediaController);
            mediaController.setAnchorView(view);
            ((VideoView) view).setZOrderOnTop(true);
            ((VideoView) view).setZOrderMediaOverlay(true);
            ((VideoView) view).start();
        }
    }

    private void setImage(ScienceQuestionChoice scienceQuestionChoice, String answer, ImageView view) {
        final String path = answer;
        String extension = getFileExtension(path);
        String fileName = Assessment_Utility.getFileName(scienceQuestionChoice.getQid(), answer);
        final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;

        if (extension.equalsIgnoreCase("gif")) {
            prevView = view;
            Glide.with(context).asGif()
                    .load(path)
                    .apply(new RequestOptions()
                            .placeholder(Drawable.createFromPath(localPath))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                    zoomImageDialog.show();*/
                    Assessment_Utility.showZoomDialog(context, path, localPath, "");

                }
            });
        } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png")) {
            prevView = view;
            Glide.with(context)
                    .load(path)
                    .apply(new RequestOptions()
                            .placeholder(Drawable.createFromPath(localPath))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                    zoomImageDialog.show();*/
                    Assessment_Utility.showZoomDialog(context, path, localPath, "");
                    /**/
                }
            });
        } else if (extension.equalsIgnoreCase("mp3")) {
            prevView = view;
            view.setImageResource(R.drawable.ic_play);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ImageView) prevView).setImageResource(R.drawable.ic_play);
                    currentView = view;
                    if (prevView != null && view != prevView && isAudioPlaying) {
                        ((ImageView) prevView).setImageResource(R.drawable.ic_play);
                        isAudioPlaying = false;
                        AudioUtil.stopPlayingAudio();
                        stopPlayer();
                    }
                    prevView = view;
                    if (isAudioPlaying) {
                        isAudioPlaying = false;
                        ((ImageView) view).setImageResource(R.drawable.ic_play);
                        AudioUtil.stopPlayingAudio();
                        stopPlayer();

                    } else {
                        isAudioPlaying = true;
                        ((ImageView) view).setImageResource(R.drawable.ic_pause);
                        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                            AudioUtil.playRecording(path, ResultAdapter.this);
                        else AudioUtil.playRecording(localPath, ResultAdapter.this);
                    }
                }
            });
        } else if (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("3gp")) {
        }

    }

    private String getImage(String userAnswerId) {
        String img = AppDatabase.getDatabaseInstance(context)
                .getScienceQuestionChoicesDao().getImageByQcID(userAnswerId);
        if (img != null)
            return img;
        else return "";
    }

    private void showButtons(MyViewHolder myViewHolder, boolean attempted) {
        if (attempted) {
            myViewHolder.btnUserAnswer.setVisibility(View.VISIBLE);
            myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
            myViewHolder.correctAnswer.setVisibility(View.GONE);
            myViewHolder.userAnswer.setVisibility(View.GONE);
        } else {
            myViewHolder.btnUserAnswer.setVisibility(View.GONE);
            myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
            myViewHolder.correctAnswer.setVisibility(View.GONE);
            myViewHolder.userAnswer.setVisibility(View.GONE);
        }

    }

    private ScienceQuestion getQuestion(String qId) {
        ScienceQuestion scienceQuestion = AppDatabase.
                getDatabaseInstance(context).getScienceQuestionDao().getQuestionByQID(qId);
        return scienceQuestion;
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    @Override
    public void stopPlayer() {
        if (prevView != null)
            ((ImageView) prevView).setImageResource(R.drawable.ic_play);
        if (currentView != null)
            ((ImageView) currentView).setImageResource(R.drawable.ic_play);
        if (isAudioPlaying) {
            isAudioPlaying = false;
        }
        if (isQuestionAudioPlaying) {
            isQuestionAudioPlaying = false;
        }
    }

}
