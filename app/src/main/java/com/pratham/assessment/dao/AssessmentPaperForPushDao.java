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

    @Query("select distinct subjectId from AssessmentPaperForPush where languageId=:langId and studentId=:studId")
    public List<String> getAssessmentPapersByUniqueSubId(String langId, String studId);

    @Query("select distinct examId from AssessmentPaperForPush where languageId=:langId and studentId=:studId")
    public List<String> getAssessmentPapersByUniqueExamId(String langId, String studId);

    @Query("select distinct languageId from AssessmentPaperForPush where studentId=:studId and examId=:examId and (" +
            " question1Rating  !='null' " +
            "or question2Rating !='null' " +
            "or question3Rating !='null' " +
            "or question4Rating !='null' " +
            "or question5Rating !='null' " +
            "or question6Rating !='null' " +
            "or question7Rating !='null' " +
            "or question8Rating !='null' " +
            "or question9Rating !='null' " +
            "or question10Rating !='null')")
    public List<String> getAssessmentPapersByUniqueLangCertificatequestionsNotNull(String studId,String examId);

@Query("select distinct examId from AssessmentPaperForPush where studentId=:studId and" +
            " question1Rating  !='null' " +
            "or question2Rating !='null' " +
            "or question3Rating !='null' " +
            "or question4Rating !='null' " +
            "or question5Rating !='null' " +
            "or question6Rating !='null' " +
            "or question7Rating !='null' " +
            "or question8Rating !='null' " +
            "or question9Rating !='null' " +
            "or question10Rating !='null'")
    public List<String> getAssessmentPapersCertificatequestionsNotNull(String studId);

    @Query("select * from AssessmentPaperForPush where examid=:examId and subjectId=:subId and studentId=:studentId")
    public List<AssessmentPaperForPush> getAssessmentPapersByExamIdAndSubId(String examId, String subId, String studentId);

    @Query("select * from AssessmentPaperForPush where subjectId=:subId")
    public List<AssessmentPaperForPush> getAssessmentPaperBySubId(String subId);

    @Query("select * from AssessmentPaperForPush where subjectId=:subId and studentId=:studentId")
    public List<AssessmentPaperForPush> getAssessmentPaperBySubIdAndStudId(String subId, String studentId);

    @Query("select * from AssessmentPaperForPush where subjectId=:subId and studentId=:studentId and languageId=:langId")
    public List<AssessmentPaperForPush> getAssessmentPaperBySubIdAndLangId(String subId, String studentId, String langId);

    @Query("select * from AssessmentPaperForPush where subjectId=:subId and studentId=:studentId and languageId=:langId and examId=:examId")
    public List<AssessmentPaperForPush> getAssessmentPaperBySubIdAndLangIdExamId(String subId, String studentId, String langId, String examId);

    @Query("update AssessmentPaperForPush set sentFlag=1 where sentFlag=0")
    public void setSentFlag();

    @Query("update AssessmentPaperForPush set question1Rating=:question1Rating,question2Rating=:question2Rating," +
            "question3Rating=:question3Rating,question4Rating=:question4Rating ,question5Rating=:question5Rating where paperId=:paperId")
    public void setAllRatings(String question1Rating, String question2Rating, String question3Rating, String question4Rating, String question5Rating, String paperId);

    @Query("update AssessmentPaperForPush set languageId=:languageId , subjectId=:subjectId ," +
            " examId=:examId ,examName=:examName ,totalMarks=:totalMarks ,outOfMarks=:scoredMarks ," +
            "paperStartTime=:paperStartTime ,paperEndTime=:paperEndTime,examTime=:examDuration" +
            " where paperId=:paperId")
    public long updatePaper(String languageId, String subjectId, String examId, String examName
            , String paperId, String totalMarks, String scoredMarks, String paperStartTime
            , String paperEndTime, String examDuration);
}