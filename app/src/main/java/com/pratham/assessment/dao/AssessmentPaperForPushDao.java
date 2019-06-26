package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentPaperForPush;

import java.util.List;

@Dao
public interface AssessmentPaperForPushDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPaperForPush(AssessmentPaperForPush assessmentTest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllPapersForPush(List<AssessmentPaperForPush> assessmentPaperForPushes);

    @Query("DELETE FROM AssessmentPaperForPush")
    public void deletePapers();

    @Query("select * from AssessmentPaperForPush")
    public List<AssessmentPaperForPush> getAllAssessmentPapersForPush();

    @Query("select * from AssessmentPaperForPush where examid=:examId")
    public List<AssessmentPaperForPush> getAssessmentPapersByExamId(String examId);

   /* @Query("select subjectid from AssessmentTestModal where topicname=:topicName")
    public String getTopicIdByTopicName(String topicName);*/
}