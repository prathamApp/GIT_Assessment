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

    @Query("select * from AssessmentPaperForPush where sentFlag=0")
    public List<AssessmentPaperForPush> getAllAssessmentPapersForPush();

    @Query("select * from AssessmentPaperForPush where examid=:examId")
    public List<AssessmentPaperForPush> getAssessmentPapersByExamId(String examId);

    @Query("select * from AssessmentPaperForPush where paperId=:paperId")
    public AssessmentPaperForPush getAssessmentPapersByPaperId(String paperId);

    @Query("select distinct subjectId from AssessmentPaperForPush where languageId=:langId")
    public List<String> getAssessmentPapersByUniqueSubId(String langId);

    @Query("select distinct languageId from AssessmentPaperForPush")
    public List<String> getAssessmentPapersByUniqueLang();

    @Query("select * from AssessmentPaperForPush where examid=:examId and subjectId=:subId and studentId=:studentId")
    public List<AssessmentPaperForPush> getAssessmentPapersByExamIdAndSubId(String examId, String subId, String studentId);

    @Query("select * from AssessmentPaperForPush where subjectId=:subId and studentId=:studentId")
    public List<AssessmentPaperForPush> getAssessmentPaperBySubId(String subId, String studentId);

    @Query("select * from AssessmentPaperForPush where subjectId=:subId and studentId=:studentId and languageId=:langId")
    public List<AssessmentPaperForPush> getAssessmentPaperBySubIdAndLangId(String subId, String studentId, String langId);

    @Query("update AssessmentPaperForPush set sentFlag=1 where sentFlag=0")
    public void setSentFlag();


   /* @Query("select subjectid from AssessmentTestModal where topicname=:topicName")
    public String getTopicIdByTopicName(String topicName);*/
}