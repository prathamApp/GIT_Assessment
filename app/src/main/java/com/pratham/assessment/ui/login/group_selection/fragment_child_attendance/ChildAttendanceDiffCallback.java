package com.pratham.assessment.ui.login.group_selection.fragment_child_attendance;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;


import com.pratham.assessment.domain.Student;

import java.util.ArrayList;

public class ChildAttendanceDiffCallback extends DiffUtil.Callback {
    private ArrayList<Student> oldLanguageList = new ArrayList<>();
    private ArrayList<Student> newLanguageList = new ArrayList<>();

    public ChildAttendanceDiffCallback(ArrayList<Student> oldLanguageList, ArrayList<Student> newLanguageList) {
        this.oldLanguageList = oldLanguageList;
        this.newLanguageList = newLanguageList;
    }

    @Override
    public int getOldListSize() {
        return oldLanguageList != null ? oldLanguageList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newLanguageList != null ? newLanguageList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newLanguageList.get(newItemPosition).compareTo(oldLanguageList.get(oldItemPosition));
        if (result == 0) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Student newLanguage = newLanguageList.get(newItemPosition);
        return newLanguage;
    }
}
