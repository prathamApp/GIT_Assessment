package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratham.assessment.R;


class SubjectViewHolder extends ParentViewHolder {
    private TextView subjectTextView;
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private final ImageView mArrowExpandImageView;
    private final LinearLayout ll_sub_row;

    SubjectViewHolder(@NonNull View itemView) {
        super(itemView);
        subjectTextView = itemView.findViewById(R.id.tv_sub);
        mArrowExpandImageView = itemView.findViewById(R.id.iv_arrow_expand);
        ll_sub_row = itemView.findViewById(R.id.ll_sub_row);
    }


    public void bind(AssessmentSubjectsExpandable assessmentSubjects) {
        if (assessmentSubjects.getChildItemList().size() > 0)
            subjectTextView.setText(assessmentSubjects.getSubjectname());
        else {
            ll_sub_row.setVisibility(View.GONE);
        }
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if (expanded) {
            mArrowExpandImageView.setRotation(ROTATED_POSITION);
        } else {
            mArrowExpandImageView.setRotation(INITIAL_POSITION);
        }

    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        mArrowExpandImageView.startAnimation(rotateAnimation);

    }
}
