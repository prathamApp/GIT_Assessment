package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.AssessmentTestModal;

import java.util.List;

@Dao
public interface AssessmentTestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTest(AssessmentTest assessmentTest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllTest(List<AssessmentTest> assessmentTest);

    @Query("DELETE FROM AssessmentTest")
    public void deleteTest();

    @Query("select * from AssessmentTest")
    public List<AssessmentTest> getAllAssessmentTests();

    @Query("select examname from AssessmentTest where examid=:examId")
    public String getExamNameById(String examId);
}