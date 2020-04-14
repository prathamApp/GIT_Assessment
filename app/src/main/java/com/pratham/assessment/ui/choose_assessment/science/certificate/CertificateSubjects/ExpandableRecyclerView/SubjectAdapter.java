package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.CertificateFragment;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.CertificateFragment_;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.List;

public class SubjectAdapter extends ExpandableRecyclerAdapter<SubjectViewHolder, SubjectAdapter.ExamViewHolder> {

    private Context mContext;
    private LayoutInflater mInflator;

    public SubjectAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        this.mContext = context;
    }


    @Override
    public SubjectViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflator.inflate(R.layout.layout_subject_row, parentViewGroup, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public ExamViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflator.inflate(R.layout.layout_exam_row, childViewGroup, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(SubjectViewHolder subjectViewHolder, int position, ParentListItem parentListItem) {
        AssessmentSubjectsExpandable assessmentSubjects = (AssessmentSubjectsExpandable) parentListItem;
        subjectViewHolder.bind(assessmentSubjects);
    }

    @Override
    public void onBindChildViewHolder(ExamViewHolder examViewHolder, int position, Object childListItem) {
        AssessmentPaperForPush paper = (AssessmentPaperForPush) childListItem;
        examViewHolder.bind(paper);
    }


    class ExamViewHolder extends ChildViewHolder {

        private TextView examName;
        private TextView timeStamp;
        private ImageView imageView;

        ExamViewHolder(View itemView) {
            super(itemView);
            examName = itemView.findViewById(R.id.tv_exam);
            timeStamp = itemView.findViewById(R.id.tv_timestamp);
            imageView = itemView.findViewById(R.id.ib_view_certificate);
        }

        public void bind(final AssessmentPaperForPush assessmentPaperForPush) {
            examName.setText(assessmentPaperForPush.getExamName());
            timeStamp.setText(assessmentPaperForPush.getPaperEndTime());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("assessmentPaperForPush", assessmentPaperForPush);
                    Assessment_Utility.showFragment((Activity) mContext, new CertificateFragment_(), R.id.frame_certificate, bundle, CertificateFragment.class.getSimpleName());
                }
            });
        }

    }

}