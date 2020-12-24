package com.pratham.assessment.ui.choose_assessment.ece;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.custom.discrete_view.DSVOrientation;
import com.pratham.assessment.custom.discrete_view.DiscreteScrollView;
import com.pratham.assessment.custom.discrete_view.ScaleTransformer;
import com.pratham.assessment.domain.ECEModel;
import com.pratham.assessment.domain.Score;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_ece)
public class ECEActivity extends BaseActivity implements DiscreteScrollView.OnItemChangedListener, AnswerClickedListener {
    @ViewById(R.id.attendance_recycler_view)
    DiscreteScrollView discreteScrollView;

    @ViewById(R.id.ll_progress)
    LinearLayout ll_progress;

    @ViewById(R.id.btn_submit)
    Button submit;
    List<ECEModel> eceModelList;
    String eceStartTime = "";
    String resId;
    String crlId;
    int[] idArr = {R.id.step_1, R.id.step_2, R.id.step_3, R.id.step_4, R.id.step_5, R.id.step_6, R.id.step_7, R.id.step_8, R.id.step_9, R.id.step_10, R.id.step_11, R.id.step_12, R.id.step_13, R.id.step_14, R.id.step_15, R.id.step_16, R.id.step_17};

    @AfterViews
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        resId = getIntent().getStringExtra("resId");
        crlId = getIntent().getStringExtra("crlId");

        /*Drawable drawable = ContextCompat.getDrawable(this,R.drawable.ic_submit_assessment);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*2),
                (int)(drawable.getIntrinsicHeight()*2));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 0, 0);
        submit.setCompoundDrawables(sd.getDrawable(), null, null, null);
        */
        JSONArray jsonArray = fetchJson("ece.json");
        eceModelList = parseJsonArray(jsonArray);
        //insertJsonToDB(jsonArray);


        eceStartTime = AssessmentApplication.getCurrentDateTime();

        ECEAdapter eceAdapter = new ECEAdapter(this, eceModelList);
        discreteScrollView.setOrientation(DSVOrientation.HORIZONTAL);
        discreteScrollView.addOnItemChangedListener(this);
        discreteScrollView.setItemTransitionTimeMillis(200);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.5f)
                .build());
        discreteScrollView.setAdapter(eceAdapter);
        eceAdapter.notifyDataSetChanged();
        discreteScrollView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                for (int i = 0; i < eceModelList.size(); i++) {
                    if (eceModelList.get(i).getIsSelected() > 0) {
                        ImageView view = findViewById(idArr[i]);
//                        view.setBackgroundColor(getResources().getColor(R.color.catcho_primary));
                        view.setBackground(getResources().getDrawable(R.drawable.answered_ece_card));

                    } else {
                        ImageView view = findViewById(idArr[i]);
                        view.setBackground(getResources().getDrawable(R.drawable.ece_top_bg));
//                        view.setBackgroundColor(getResources().getColor(R.color.colorRed));

                    }
                }
                ImageView view = findViewById(idArr[adapterPosition]);
//                view.setBackgroundColor(getResources().getColor(R.color.color_bg));
                view.setBackground(getResources().getDrawable(R.drawable.current_ece_card));

            }
        });
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ece);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        resId = getIntent().getStringExtra("resId");
        crlId = getIntent().getStringExtra("crlId");

        *//*Drawable drawable = ContextCompat.getDrawable(this,R.drawable.ic_submit_assessment);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*2),
                (int)(drawable.getIntrinsicHeight()*2));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 0, 0);
        submit.setCompoundDrawables(sd.getDrawable(), null, null, null);
        *//*
        JSONArray jsonArray = fetchJson("ece.json");
        eceModelList = parseJsonArray(jsonArray);


        eceStartTime = AssessmentApplication.getCurrentDateTime();

        ECEAdapter eceAdapter = new ECEAdapter(this, eceModelList);
        discreteScrollView.setOrientation(DSVOrientation.HORIZONTAL);
        discreteScrollView.addOnItemChangedListener(this);
        discreteScrollView.setItemTransitionTimeMillis(200);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.5f)
                .build());
        discreteScrollView.setAdapter(eceAdapter);
        eceAdapter.notifyDataSetChanged();
        discreteScrollView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                for (int i = 0; i < eceModelList.size(); i++) {
                    if (eceModelList.get(i).getIsSelected() > 0) {
                        ImageView view = findViewById(idArr[i]);
                        view.setBackground(getResources().getDrawable(R.drawable.answered_ece_card));

                    } else {
                        ImageView view = findViewById(idArr[i]);
                        view.setBackground(getResources().getDrawable(R.drawable.ece_top_bg));

                    }
                }
                ImageView view = findViewById(idArr[adapterPosition]);
                view.setBackground(getResources().getDrawable(R.drawable.current_ece_card));

            }
        });
    }
*/
    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView title = dialog.findViewById(R.id.dia_title);
        Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);

        title.setText(R.string.do_you_want_to_leave_assessment);
        restart_btn.setText(R.string.no);
        exit_btn.setText(R.string.yes);
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ECEActivity.super.onBackPressed();
                endTestSession();
                finish();
            }
        });

        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void endTestSession() {
        try {
            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        String assessmentSession = FastSave.getInstance().getString("assessmentSession", "");

//                        String toDateTemp = appDatabase.getSessionDao().getToDate(Assessment_Constants.assessmentSession);
                        String toDateTemp = appDatabase.getSessionDao().getToDate(assessmentSession);

                        if (toDateTemp.equalsIgnoreCase("na")) {
//                            appDatabase.getSessionDao().UpdateToDate(Assessment_Constants.assessmentSession, AssessmentApplication.getCurrentDateTime());
                            appDatabase.getSessionDao().UpdateToDate(assessmentSession, AssessmentApplication.getCurrentDateTime());
                        }
                        BackupDatabase.backup(ECEActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ECEModel> parseJsonArray(JSONArray jsonArray) {
        List<ECEModel> eceList = new ArrayList<>();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                ECEModel eceModel = new ECEModel();
                eceModel.setQuestionId(jsonArray.getJSONObject(i).getString("questionId"));
                eceModel.setQuestion(jsonArray.getJSONObject(i).getString("question"));
                eceModel.setType(jsonArray.getJSONObject(i).getString("type"));
                eceModel.setTitle(jsonArray.getJSONObject(i).getString("title"));
                eceModel.setInstructions(jsonArray.getJSONObject(i).getString("instructions"));
                eceModel.setVideo(jsonArray.getJSONObject(i).getString("video"));
                eceModel.setRating(jsonArray.getJSONObject(i).getString("rating"));
                eceList.add(eceModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eceList;
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    public JSONArray fetchJson(String jasonName) {
        JSONArray jsonArr = null;
        try {
            //InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/RC/" + jasonName);
            InputStream is = this.getAssets().open("" + jasonName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            jsonArr = new JSONArray(jsonStr);
            //returnStoryNavigate = jsonObj.getJSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArr;
    }

    @Click(R.id.btn_submit)
    public void onSubmit() {
        int cnt = 0;
        for (int i = 0; i < eceModelList.size(); i++) {
            if (eceModelList.get(i).getIsSelected() != -1) {
                cnt++;
            } else {
                Toast.makeText(this, "Please complete all questions...", Toast.LENGTH_SHORT).show();
                discreteScrollView.scrollToPosition(i);
                break;
            }
        }
        if (cnt == eceModelList.size()) {
            showConfirmationDialog();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to save this assessment?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveAssessmentToDB();
                endTestSession();
                finish();
            }
        });
        builder.setNegativeButton("Review", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void saveAssessmentToDB() {
        List<Score> scoreList = new ArrayList<>();

        for (int i = 0; i < eceModelList.size(); i++) {
            Score score = new Score();
            score.setResourceID(resId);
            String assessmentSession = FastSave.getInstance().getString("assessmentSession", "");

//            score.setSessionID(Assessment_Constants.assessmentSession);
            score.setSessionID(assessmentSession);
//            score.setSessionIDm(Assessment_Constants.currentSession);
            score.setQuestionId(0);
            if (eceModelList.get(i).getIsSelected() == 1)
                score.setScoredMarks(10);
            else if (eceModelList.get(i).getIsSelected() == 2)
                score.setScoredMarks(5);
            score.setTotalMarks(10);
            String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//            score.setStudentID(Assessment_Constants.currentStudentID);
            score.setStudentID(currentStudentID);
            score.setStartDateTime(eceStartTime);
            score.setEndDateTime(AssessmentApplication.getCurrentDateTime());
            score.setDeviceID(crlId);
            score.setLevel(eceModelList.get(i).getIsSelected());
            score.setLabel("ECE-" + eceModelList.get(i).getQuestion());
            score.setSentFlag(0);
            score.setExamId("ece_assessment");
            if (eceModelList.get(i).getIsSelected() == 1)
                score.setUserAnswer("Can do");
            else if (eceModelList.get(i).getIsSelected() == 2)
                score.setUserAnswer("Need help");
            scoreList.add(score);
        }
        AppDatabase.getDatabaseInstance(this).getScoreDao().insertAllScores(scoreList);
        BackupDatabase.backup(this);
    }
/*
 private void saveAssessmentToDB() {
        List<Assessment> assessmentList = new ArrayList<>();

        for (int i = 0; i < eceModelList.size(); i++) {
            Assessment assessment = new Assessment();
            assessment.setResourceIDa(resId);
            assessment.setSessionIDa(Assessment_Constants.assessmentSession);
            assessment.setSessionIDm(Assessment_Constants.currentSession);
            assessment.setQuestionIda(0);
            if (eceModelList.get(i).getIsSelected() == 1)
                assessment.setScoredMarksa(10);
            else if (eceModelList.get(i).getIsSelected() == 2)
                assessment.setScoredMarksa(5);
            assessment.setTotalMarksa(10);
            assessment.setStudentIDa(Assessment_Constants.currentStudentID);
            assessment.setStartDateTimea(eceStartTime);
            assessment.setEndDateTime(AssessmentApplication.getCurrentDateTime());
            assessment.setDeviceIDa(crlId);
            assessment.setLevela(eceModelList.get(i).getIsSelected());
            assessment.setLabel(eceModelList.get(i).getQuestion());
            assessment.setSentFlag(0);
            assessmentList.add(assessment);
        }
        AppDatabase.getDatabaseInstance(this).getAssessmentDao().insertAllAssessments(assessmentList);
        BackupDatabase.backup(this);
    }
*/

    @Override
    public void onAnswerClicked(int position, int answer) {
        eceModelList.get(position).setIsSelected(answer);
        ImageView view = findViewById(idArr[position]);
//        view.setBackgroundColor(getResources().getColor(R.color.catcho_primary));
        view.setBackground(getResources().getDrawable(R.drawable.answered_ece_card));

    }
}
