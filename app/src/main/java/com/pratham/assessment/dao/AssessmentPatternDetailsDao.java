package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.domain.AssessmentPatternDetails;

import java.util.List;

@Dao
public interface AssessmentPatternDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPatternDetails(AssessmentPatternDetails assessmentPatternDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllPatternDetails(List<AssessmentPatternDetails> assessmentPatternDetails);

    @Query("DELETE FROM AssessmentPatternDetails")
    public void deletePatternDetails();

    @Query("DELETE FROM AssessmentPatternDetails where examId=:examId")
    public void deletePatternDetailsByExamId(String examId);

    @Query("select * from AssessmentPatternDetails")
    public List<AssessmentPatternDetails> getAllAssessmentPatternDetails();

    @Query("select * from AssessmentPatternDetails where examId=:examId")
    public List<AssessmentPatternDetails> getAssessmentPatternDetailsByExamId(String examId);

    @Query("select distinct topicid from AssessmentPatternDetails where examId=:examId")
    public List<String> getTopicsByExamId(String examId);


    @Query("select distinct topicid from AssessmentPatternDetails")
    public List<String> getDistinctTopicIds();

    @Query("select topicname from AssessmentPatternDetails where examId=:examId")
    public String getTopicNameByExamId(String examId);


  /*  @Query("select * from AssessmentPatternDetails where topicid=:topicId and examId=:examId")
    public List<AssessmentPatternDetails> getTopicIdByTopicIdExamId(String topicId, String examId);*/
}