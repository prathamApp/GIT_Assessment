package com.pratham.assessment.ui.choose_assessment.result;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ResultModalClass;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ZoomImageDialog;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.AssessmentApplication.wiseF;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.ARRANGE_SEQUENCE;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.FILL_IN_THE_BLANK;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.KEYWORDS_QUESTION;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.MATCHING_PAIR;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.MULTIPLE_CHOICE;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.MULTIPLE_SELECT;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    Context context;
    List<ResultModalClass> resultList;
    ResultListener resultListener;

    public ResultAdapter(Context context, List<ResultModalClass> resultList, ResultFragment resultFragment) {
        this.context = context;
        this.resultList = resultList;
        resultListener = resultFragment;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question, userAnswer, correctAnswer, btnUserAnswer, btnCorrectAnswer, tv_you_answered_label;
        CardView cardView;
        ImageView questionImg;
        ImageView iv_correct_wrong_indicator;
        LinearLayout ll_correct_ans, ll_user_ans;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_result_question);
            tv_you_answered_label = itemView.findViewById(R.id.tv_you_answered_label);
            questionImg = itemView.findViewById(R.id.question_img);
            userAnswer = itemView.findViewById(R.id.tv_you_answered);
            correctAnswer = itemView.findViewById(R.id.tv_correct_answer);
            btnUserAnswer = itemView.findViewById(R.id.btn_you_answered);
            btnCorrectAnswer = itemView.findViewById(R.id.btn_correct_Ans);
            cardView = itemView.findViewById(R.id.result_card_view);
            ll_correct_ans = itemView.findViewById(R.id.ll_correct_ans);
            ll_user_ans = itemView.findViewById(R.id.ll_user_ans);
            iv_correct_wrong_indicator = itemView.findViewById(R.id.iv_correct_wrong_indicator);
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

        if (i == resultList.size() - 1) {
            resultListener.showDone(true);
        } else resultListener.showDone(false);


        final ResultModalClass result = resultList.get(i);
        myViewHolder.itemView.setTag(result.getqId());
        if (!result.getQuestionImg().equalsIgnoreCase("")) {
            myViewHolder.questionImg.setVisibility(View.VISIBLE);

            String fileName = Assessment_Utility.getFileName(result.getqId(), result.getQuestionImg());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
            final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


//            if (wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            final String path = result.getQuestionImg();
            String[] imgPath = path.split("\\.");
            int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                Glide.with(context).asGif()
                        .load(path)
                        .apply(new RequestOptions()
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(myViewHolder.questionImg);
            } else {
                Glide.with(context)
                        .load(path)
                        .apply(new RequestOptions()
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(myViewHolder.questionImg);
            }
            myViewHolder.questionImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, localPath);
                    zoomImageDialog.show();
                }
            });
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


        myViewHolder.question.setText(result.getQuestion());
        if (!result.getUserAnswer().equalsIgnoreCase(""))
            myViewHolder.userAnswer.setText(result.getUserAnswer());
        else if (!result.getUserAnswerId().equalsIgnoreCase("")) {
            showButtons(myViewHolder, true);
        }

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
                        ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, corrImg, localPath);
                        zoomImageDialog.show();
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

                String fileName = getFileName(result.getqId(), img);
                String localPath = dirPath + "/" + fileName;

                ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, img, localPath);
                zoomImageDialog.show();
            }
        });


        if (!result.isAttempted()) {
            myViewHolder.userAnswer.setText("Skipped");
            myViewHolder.userAnswer.setTextColor(context.getResources().getColor(R.color.colorProgress15));
        } else {
            int color = myViewHolder.tv_you_answered_label.getCurrentTextColor();
            myViewHolder.userAnswer.setTextColor(color);

        }
        myViewHolder.correctAnswer.setText(result.getCorrectAnswer());
        if (result.isCorrect()) {
            myViewHolder.iv_correct_wrong_indicator.setImageResource(R.drawable.ic_check_black);
            myViewHolder.iv_correct_wrong_indicator.setBackgroundColor(context.getResources().getColor(R.color.green));

            myViewHolder.cardView.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
            myViewHolder.ll_user_ans.setVisibility(View.GONE);
            ((TextView) myViewHolder.ll_correct_ans.getChildAt(0)).setText("Answer");

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
            case MATCHING_PAIR:
                showButtons(myViewHolder, result.isAttempted());
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
                        List<ScienceQuestionChoice> userAns = new ArrayList<>();
                        String ans = result.getUserAnswer();
                        String[] ansArr = ans.split(",");
                        for (int i = 0; i < ansArr.length; i++) {
                            for (int j = 0; j < scienceQuestionChoice.size(); j++) {
                                if (scienceQuestionChoice.get(j).getQcid().equalsIgnoreCase(ansArr[i])) {
                                    userAns.add(scienceQuestionChoice.get(j));
                                }
                            }
                        }
                        ShowAnswerDialog showAnswerDialog = new ShowAnswerDialog(context, userAns, scienceQuestionChoice);
                        showAnswerDialog.show();
                    }
                });
                break;
            case MULTIPLE_SELECT:
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

                break;
            case ARRANGE_SEQUENCE:
                showButtons(myViewHolder, result.isAttempted());
                break;
            case MULTIPLE_CHOICE:
                for (int j = 0; j < scienceQuestionChoice.size(); j++) {
                    if (!result.isAttempted()) {
                        if (scienceQuestionChoice.get(j).getCorrect().equalsIgnoreCase("true"))
                            if (scienceQuestionChoice.get(j).getChoiceurl().equalsIgnoreCase("")) {
                                myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                                myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                            } else {
                                myViewHolder.correctAnswer.setVisibility(View.GONE);
                                myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
                            }

                        myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                        myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                        myViewHolder.userAnswer.setText("Skipped");
                        myViewHolder.userAnswer.setTextColor(context.getResources().getColor(R.color.colorProgress15));


                    } else {
                        int color = myViewHolder.tv_you_answered_label.getCurrentTextColor();
                        myViewHolder.userAnswer.setTextColor(color);
                        if (result.getUserAnswerId().equalsIgnoreCase(scienceQuestionChoice.get(j).getQcid()) ||
                                result.getUserAnswer().equalsIgnoreCase(scienceQuestionChoice.get(j).getChoicename())) {
                            if (scienceQuestionChoice.get(j).getChoiceurl().equalsIgnoreCase("")) {
                                myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                                myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                                myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                            } else {
                                myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
                                myViewHolder.btnUserAnswer.setVisibility(View.VISIBLE);
                                myViewHolder.userAnswer.setVisibility(View.GONE);
                            }
                        }
                    }
                    if (!result.isCorrect()) {
                        if (scienceQuestionChoice.get(j).getCorrect().equalsIgnoreCase("true")) {
                            if (!scienceQuestionChoice.get(j).getChoiceurl().equalsIgnoreCase(""))
                                myViewHolder.btnCorrectAnswer.setVisibility(View.VISIBLE);
                            if (!scienceQuestionChoice.get(j).getChoicename().equalsIgnoreCase(""))
                                myViewHolder.correctAnswer.setVisibility(View.VISIBLE);

                            if (!result.getUserAnswer().equalsIgnoreCase(""))
                                myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (!myViewHolder.correctAnswer.getText().equals(""))
                    myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                else myViewHolder.correctAnswer.setVisibility(View.GONE);


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
            case KEYWORDS_QUESTION:
            case FILL_IN_THE_BLANK:
                myViewHolder.btnCorrectAnswer.setVisibility(View.GONE);
                myViewHolder.btnUserAnswer.setVisibility(View.GONE);
                if (result.isCorrect()) {
                    myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.userAnswer.setVisibility(View.GONE);
                } else {
                    myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
                    myViewHolder.userAnswer.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private String getImage(String userAnswerId) {
        String img = AppDatabase.getDatabaseInstance(context)
                .getScienceQuestionChoicesDao().getImageByQcID(userAnswerId);
        return img;
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
            myViewHolder.userAnswer.setVisibility(View.VISIBLE);
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


}
