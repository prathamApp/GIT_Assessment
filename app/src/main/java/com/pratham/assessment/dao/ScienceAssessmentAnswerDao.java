package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.ScienceAssessmentAnswer;

import java.util.List;

@Dao
public interface ScienceAssessmentAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAnswer(ScienceAssessmentAnswer assessmentAnswer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllAnswers(List<ScienceAssessmentAnswer> assessmentAnswers);

    @Query("DELETE FROM ScienceAssessmentAnswer")
    public void deleteAnswer();

    @Query("select * from ScienceAssessmentAnswer")
    public List<ScienceAssessmentAnswer> getAllAssessmentAnswers();

    @Query("select * from ScienceAssessmentAnswer where sentFlag=0")
    public List<ScienceAssessmentAnswer> getAllAssessmentAnswersForPush();

   /* @Query("select subjectid from AssessmentTestModal where topicname=:topicName")
    public String getTopicIdByTopicName(String topicName);*/
}