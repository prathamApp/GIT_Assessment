package com.pratham.assessment.ui.choose_assessment.result;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ResultModalClass;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ZoomImageDialog;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.ARRANGE_SEQUENCE;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.MATCHING_PAIR;
import static com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity.MULTIPLE_SELECT;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    Context context;
    List<ResultModalClass> resultList;

    public ResultAdapter(Context context, List<ResultModalClass> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question, userAnswer, correctAnswer, btnUserAnswer, btnCorrectAnswer;
        CardView cardView;
        ImageView iv_correct_wrong_indicator;
        LinearLayout ll_correct_ans, ll_user_ans;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_result_question);
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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final ResultModalClass result = resultList.get(i);
        myViewHolder.question.setText(result.getQuestion());
        if (!result.getUserAnswer().equalsIgnoreCase(""))
            myViewHolder.userAnswer.setText(result.getUserAnswer());
        else if (!result.getUserAnswerId().equalsIgnoreCase("")) {
            showButtons(myViewHolder, true);

            myViewHolder.btnCorrectAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ScienceQuestionChoice> scienceQuestionChoice = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(result.getqId());
                    for (int i = 0; i < scienceQuestionChoice.size(); i++) {
                        if (scienceQuestionChoice.get(i).getCorrect().equalsIgnoreCase("true")) {
                            String corrImg = scienceQuestionChoice.get(i).getChoiceurl();
                            ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, Assessment_Constants.loadOnlineImagePath + corrImg);
                            zoomImageDialog.show();
                        }
                    }

                }
            });
            myViewHolder.btnUserAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String img = getImage(result.getUserAnswerId());
                    ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, Assessment_Constants.loadOnlineImagePath + img);
                    zoomImageDialog.show();
                }
            });

        }
        if (!result.isAttempted()) {
            myViewHolder.userAnswer.setText("Skipped");
            myViewHolder.userAnswer.setTextColor(context.getResources().getColor(R.color.colorProgress15));
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
            myViewHolder.correctAnswer.setVisibility(View.VISIBLE);
            myViewHolder.iv_correct_wrong_indicator.setImageResource(R.drawable.ic_close_black_24dp);
            myViewHolder.iv_correct_wrong_indicator.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            myViewHolder.cardView.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
        }

        final ScienceQuestion scienceQuestion = getQuestion(result.getqId());
        final List<ScienceQuestionChoice> scienceQuestionChoice = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(result.getqId());
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

        }

    }

    private String getImage(String userAnswerId) {
        String img = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getImageByQcID(userAnswerId);
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
        ScienceQuestion scienceQuestion = AppDatabase.getDatabaseInstance(context).getScienceQuestionDao().getQuestionByQID(qId);
        return scienceQuestion;
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}
