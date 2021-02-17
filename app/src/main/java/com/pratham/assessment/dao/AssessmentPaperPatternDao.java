package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentPaperPattern;

import java.util.List;

@Dao
public interface AssessmentPaperPatternDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPaperPattern(AssessmentPaperPattern assessmentPaperPattern);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllPapersPatterns(List<AssessmentPaperPattern> assessmentPaperPatterns);

    @Query("DELETE FROM AssessmentPaperPattern")
    public void deletePaperPatterns();

    @Query("DELETE FROM AssessmentPaperPattern where examid=:examId")
    public void deletePaperPatternByExamId(String examId);

    @Query("select * from AssessmentPaperPattern")
    public List<AssessmentPaperPattern> getAllAssessmentPaperPatterns();

    @Query("select * from AssessmentPaperPattern where subjectid=:subId and (" +
            " certificateQuestion1  !='null'  " +
            "or certificateQuestion2!='null'  " +
            "or certificateQuestion3!='null'  " +
            "or certificateQuestion4!='null'  " +
            "or certificateQuestion5!='null'  " +
            "or certificateQuestion6!='null'  " +
            "or certificateQuestion7!='null'  " +
            "or certificateQuestion8!='null'  " +
            "or certificateQuestion9!='null'  " +
            "or certificateQuestion10 !='null') and" +
            "( certificateQuestion1  !=''  " +
            "or certificateQuestion2!=''  " +
            "or certificateQuestion3!=''  " +
            "or certificateQuestion4!=''  " +
            "or certificateQuestion5!=''  " +
            "or certificateQuestion6!=''  " +
            "or certificateQuestion7!=''  " +
            "or certificateQuestion8!=''  " +
            "or certificateQuestion9!=''  " +
            "or certificateQuestion10 !='')")
    public List<AssessmentPaperPattern> getAllAssessmentPaperPatternsBySubId(String subId);

    @Query("select * from AssessmentPaperPattern where subjectid=:subId" )
    public List<AssessmentPaperPattern> getAllAssessmentPaperPatternsBySubjectId(String subId);

    @Query("select * from AssessmentPaperPattern where subjectid=:subId and noofcertificateq!=0")
    public List<AssessmentPaperPattern> getAllAssessmentPaperPatternsBySubIdNoCertificates(String subId);

    @Query("select * from AssessmentPaperPattern where subjectid=:subId and examid=:examid")
    public AssessmentPaperPattern getAllAssessmentPaperPatternsBySubIdAndExamId(String subId, String examid);

    @Query("select * from AssessmentPaperPattern where examid=:examId")
    public AssessmentPaperPattern getAssessmentPaperPatternsByExamId(String examId);


    @Query("select * from AssessmentPaperPattern where examid=:examId and (" +
            " certificateQuestion1  !='null'  " +
            "or certificateQuestion2!='null'  " +
            "or certificateQuestion3!='null'  " +
            "or certificateQuestion4!='null'  " +
            "or certificateQuestion5!='null'  " +
            "or certificateQuestion6!='null'  " +
            "or certificateQuestion7!='null'  " +
            "or certificateQuestion8!='null'  " +
            "or certificateQuestion9!='null'  " +
            "or certificateQuestion10 !='null') and" +
            "( certificateQuestion1  !=''  " +
            "or certificateQuestion2!=''  " +
            "or certificateQuestion3!=''  " +
            "or certificateQuestion4!=''  " +
            "or certificateQuestion5!=''  " +
            "or certificateQuestion6!=''  " +
            "or certificateQuestion7!=''  " +
            "or certificateQuestion8!=''  " +
            "or certificateQuestion9!=''  " +
            "or certificateQuestion10 !='')")
    public AssessmentPaperPattern getAssessmentPaperPatternsByExamIdQNotNull(String examId);

    @Query("select examid from ASSESSMENTPAPERPATTERN where examname=:examname")
    public String getExamIdByExamName(String examname);

    @Query("select examname from ASSESSMENTPAPERPATTERN where examid=:examid")
    public String getExamNameById(String examid);


    @Query("select examname from ASSESSMENTPAPERPATTERN where subjectname=:subName")
    public String[] getExamNameBySubjectName(String subName);

    @Query("select subjectname from ASSESSMENTPAPERPATTERN where examid=:examid")
    public String getSubjectNameById(String examid);
}