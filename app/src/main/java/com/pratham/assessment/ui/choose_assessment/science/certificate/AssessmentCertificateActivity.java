package com.pratham.assessment.ui.choose_assessment.science.certificate;

import android.os.Bundle;
import android.view.WindowManager;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.CertificateSubjectsFragment;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.CertificateSubjectsFragment_;
import com.pratham.assessment.utilities.Assessment_Utility;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/
public class AssessmentCertificateActivity extends BaseActivity {
    /*@BindView(R.id.spinner_sub)
    Spinner spinnerSubject;
    @BindView(R.id.spinner_paper)
    Spinner spinnerPaper;

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
    List<AssessmentPaperPattern> paperPatterns;
//    AssessmentCertificateContract.CertificatePresenter presenter;
    String selectedSub, selectedTopic;
*/
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_certificate);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ButterKnife.bind(this);

        Assessment_Utility.showFragment(this, new CertificateSubjectsFragment_(),
                R.id.frame_certificate, null, CertificateSubjectsFragment.class.getSimpleName());

     /*   presenter = new AssessmentCertificatePresenterImpl(this);
        presenter.getStudent(Assessment_Constants.currentStudentID);
        presenter.getSubjectData();*/


//        String subName = presenter.getSubjectName(examId);
//        String topicName = presenter.getTopicName(examId);
//        tv_topic.setText(topicName);
//        tv_subject.setText(subName);


    }

    /* @Override
     public void setStudentName(String studentName) {
         tv_studentName.setText(studentName);
     }
    /* @Override
     public void setSubjectSpinner(String[] sub) {
         if (sub.length <= 0) {
             sub = new String[1];
             sub[0] = "No subjects";
         }
         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, sub);
         dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinnerSubject.setAdapter(dataAdapter);
         spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedSub = spinnerSubject.getSelectedItem().toString();
                 generateCertificateData();

                 //presenter.getTopicData(selectedSub);
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
     }

     @Override
     public void setTopicSpinner(String[] topics) {
        *//* if (topics.length <= 0) {
            topics = new String[1];
            topics[0] = "No topics";
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, topics);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(dataAdapter);
*//*
     *//* spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTopic = spinnerTopic.getSelectedItem().toString();
                if (selectedTopic.equalsIgnoreCase("No topics")) {
                } else if (selectedTopic.equalsIgnoreCase("")) {
                    Toast.makeText(AssessmentCertificateActivity.this, "Select topic", Toast.LENGTH_SHORT).show();
                } else {
                    generateCertificateData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*//*

    }

    @Override
    public void showCertificate(AssessmentPaperForPush paper, List<Score> scoreData) {

        tv_correct_cnt.setText("" + paper.getCorrectCnt());
        tv_wrong_cnt.setText("" + paper.getWrongCnt());
        tv_skip_cnt.setText("" + paper.getSkipCnt());
        tv_total_time.setText("Total time  : " + 00 + " : " + paper.getExamTime() + "  : " + 00);
        calculateTime(paper.getPaperStartTime(), paper.getPaperEndTime());
        tv_student_marks.setText(paper.getTotalMarks());
        tv_total_marks.setText(paper.getOutOfMarks());
        tv_total_cnt.setText("" + scoreData.size());
    }

    @Override
    public void showNothing() {
        tv_correct_cnt.setText("0");
        tv_wrong_cnt.setText("0");
        tv_skip_cnt.setText("0");
        tv_time_taken.setText("Time taken : " + 0 + " : " + 0 + "  : " + 0);
        tv_total_time.setText("Total time  : " + 0 + " : " + 0 + "  : " + 0);
        tv_student_marks.setText("0");
        tv_total_marks.setText("0");
    }

    @Override
    public void showPaperList(final List<AssessmentPaperForPush> paperList) {
        String[] paperIds = new String[paperList.size()];
        for (int i = 0; i < paperList.size(); i++) {
            paperIds[i] = "" + paperList.get(i).getExamName();
        }
        if (paperIds.length <= 0) {
            paperIds = new String[1];
            paperIds[0] = "No papers";
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, paperIds);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaper.setAdapter(dataAdapter);
        spinnerPaper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (paperList.size() > 0) {
                    AssessmentPaperForPush paper = paperList.get(position);
                    presenter.getScoreData(paper);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
*/
   /* private void calculateTime(String paperStartTime, String paperEndTime) {
        try {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(paperStartTime);
            Date date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(paperEndTime);

            long diff = date2.getTime() - date1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);

            tv_time_taken.setText(getString(R.string.time_taken)+" " + diffHours + " : " + diffMinutes + "  : " + diffSeconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    /*private void generateCertificateData() {
        presenter.generateCertificate(selectedSub);
    }
*/
    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments > 1) {
            super.onBackPressed();
        } else finish();
    }
}
