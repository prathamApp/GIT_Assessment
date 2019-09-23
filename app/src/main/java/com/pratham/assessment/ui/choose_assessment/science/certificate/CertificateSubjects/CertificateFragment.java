package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CertificateFragment extends Fragment implements CertificateContract.CertificateView {


    @BindView(R.id.q1_label)
    TextView q1_label;
    @BindView(R.id.q2_label)
    TextView q2_label;
    @BindView(R.id.q3_label)
    TextView q3_label;

    @BindView(R.id.tv_name)
    TextView tv_studentName;
    @BindView(R.id.tv_correct_cnt)
    TextView tv_correct_cnt;
    @BindView(R.id.tv_wrong_cnt)
    TextView tv_wrong_cnt;
    @BindView(R.id.tv_skip_cnt)
    TextView tv_skip_cnt;
    @BindView(R.id.tv_total_time)
    TextView tv_total_time;
    @BindView(R.id.tv_time_taken)
    TextView tv_time_taken;
    @BindView(R.id.tv_total_marks)
    TextView tv_total_marks;
    @BindView(R.id.tv_student_marks)
    TextView tv_student_marks;
    @BindView(R.id.tv_total_cnt)
    TextView tv_total_cnt;
    @BindView(R.id.frame_fragment_certificate)
    FrameLayout frame_fragment_certificate;
    @BindView(R.id.q1_ratingStars)
    RatingBar q1_ratingStars;
    @BindView(R.id.q2_ratingStars)
    RatingBar q2_ratingStars;
    @BindView(R.id.q3_ratingStars)
    RatingBar q3_ratingStars;

    CertificatePresenterImpl presenter;
    AssessmentPaperForPush assessmentPaperForPush;

    public CertificateFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            assessmentPaperForPush = (AssessmentPaperForPush) bundle.getSerializable("assessmentPaperForPush");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_certificate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        frame_fragment_certificate.setBackground(getResources().getDrawable(Assessment_Utility.getRandomCertificateBackground(getActivity())));
        presenter = new CertificatePresenterImpl(getActivity(), this);
        generateCertificate();
//        presenter.getScoreData(assessmentPaperForPush.getPaperId());
    }

    private void generateCertificate() {
        try {
            presenter.getStudentName();
            setQuestion1Stars();
            String examId = assessmentPaperForPush.getExamId();
            JSONArray questionList = fetchQuestionsFromAssets("certificate_questions.json", "QuestionList");
            JSONArray topics = fetchQuestionsFromAssets("certificate_topics.json", "topicList");
            String topicName = AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPatternDetailsDao().getTopicNameByExamId(examId);
//            String langId = assessmentPaperForPush.getLanguageId();
            int langId = Integer.parseInt(assessmentPaperForPush.getLanguageId());
            for (int t = 0; t < topics.length(); t++) {
                JSONObject jsonObject = topics.getJSONObject(t);
                if (jsonObject.get("1").toString().equalsIgnoreCase(topicName)) {
                    topicName = jsonObject.get(langId + "").toString();
                }
            }

//            String examName = assessmentPaperForPush.getExamName();
//            JSONObject jsonObject = jsonArray.getJSONObject(langId);
            for (int i = 0; i < questionList.length(); i++) {
                JSONObject jsonObject = questionList.getJSONObject(i);
                if (jsonObject.get("langId").toString().equals(langId + "")) {
                    String q1 = jsonObject.get("question1") + topicName + jsonObject.get("end_question1");
                    q1_label.setText(q1);
                    String q2 = jsonObject.get("question2") + topicName + jsonObject.get("end_question2");
                    q2_label.setText(q2);
                    String q3 = jsonObject.get("question3") + topicName + jsonObject.get("end_question3");
                    q3_label.setText(q3);
                } else {
                    Log.d("##", jsonObject.get("langId") + "");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_correct_cnt.setText("" + assessmentPaperForPush.getCorrectCnt());
        int totalWrong = assessmentPaperForPush.getWrongCnt() + assessmentPaperForPush.getSkipCnt();
        tv_wrong_cnt.setText("" + totalWrong);
        tv_skip_cnt.setText("" + assessmentPaperForPush.getSkipCnt());
        tv_total_time.setText(assessmentPaperForPush.getExamTime() + " mins.");
        calculateTime(assessmentPaperForPush.getPaperStartTime(), assessmentPaperForPush.getPaperEndTime());
        tv_student_marks.setText(assessmentPaperForPush.getTotalMarks());
        tv_total_marks.setText(assessmentPaperForPush.getOutOfMarks());
        int totalCnt = assessmentPaperForPush.getCorrectCnt() + assessmentPaperForPush.getWrongCnt() + assessmentPaperForPush.getSkipCnt();
        tv_total_cnt.setText(totalCnt + "");
//        presenter.getPaper(assessmentPaperForPush.getExamId(), assessmentPaperForPush.getSubjectId());
    }

    private void setQuestion1Stars() {
        float q1Rating, q2Rating, q3Rating;
        String paperId = assessmentPaperForPush.getPaperId();
        q1Rating = presenter.getRating("0", paperId);
        q1_ratingStars.setRating(q1Rating);
        q2Rating = presenter.getRating("1", paperId);
        q2_ratingStars.setRating(q2Rating);
        q3Rating = presenter.getRating("2", paperId);
        q3_ratingStars.setRating(q3Rating);
    }

    public JSONArray fetchQuestionsFromAssets(String fileName, String nodeTitle) {
        JSONArray returnCodeList = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
//            InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/Game/CertificateData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            JSONObject jsonObj = new JSONObject(jsonStr);
            returnCodeList = jsonObj.getJSONArray(nodeTitle);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return returnCodeList;
    }


    @Override
    public void setStudentName(String name) {
        tv_studentName.setText(name);
    }

    private void calculateTime(String paperStartTime, String paperEndTime) {
        try {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(paperStartTime);
            Date date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(paperEndTime);

            long diff = date2.getTime() - date1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            String unit = "min.";
            String time = "";
            if (diffHours == 0 && diffMinutes == 0 && diffSeconds != 0) {
                unit = " secs.";
                time = diffSeconds + "";
            } else if (diffHours == 0 && diffMinutes != 0) {
                unit = " mins.";
                time = diffMinutes + " : " + diffSeconds;
            } else if (diffHours != 0) {
                unit = " hrs.";
                time = diffHours + " : " + diffMinutes + "  : " + diffSeconds;
            }
            tv_time_taken.setText(time + unit);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
