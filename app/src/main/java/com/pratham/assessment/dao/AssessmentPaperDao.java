package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentPaper;

import java.util.List;

@Dao
public interface AssessmentPaperDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPaper(AssessmentPaper assessmentTest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllPapers(List<AssessmentPaper> assessmentPapers);

    @Query("DELETE FROM AssessmentPaper")
    public void deletePapers();

    @Query("select * from AssessmentPaper")
    public List<AssessmentPaper> getAllAssessmentPapers();

    @Query("select * from AssessmentPaper where examid=:examId")
    public List<AssessmentPaper> getAssessmentPapersByExamId(String examId);

   /* @Query("select subjectid from AssessmentTestModal where topicname=:topicName")
    public String getTopicIdByTopicName(String topicName);*/
}