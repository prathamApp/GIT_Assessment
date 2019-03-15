package com.pratham.assessment.ui.login.group_selection.fragment_child_attendance;

import android.view.View;

import com.pratham.assessment.domain.Student;


public interface ContractChildAttendance {
    interface attendanceView {
        void childItemClicked(Student student, int position);

        void moveToDashboardOnChildClick(Student student, int position, View v);
    }
}
