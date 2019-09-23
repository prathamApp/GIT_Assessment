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

    @Query("select * from AssessmentPaperPattern")
    public List<AssessmentPaperPattern> getAllAssessmentPaperPatterns();

    @Query("select * from AssessmentPaperPattern where subjectid=:subId")
    public List<AssessmentPaperPattern> getAllAssessmentPaperPatternsBySubId(String subId);

    @Query("select * from AssessmentPaperPattern where examid=:examId")
    public AssessmentPaperPattern getAssessmentPaperPatternsByExamId(String examId);

    @Query("select examid from ASSESSMENTPAPERPATTERN where examname=:examname")
    public String getExamIdByExamName(String examname);

    @Query("select examname from ASSESSMENTPAPERPATTERN where examid=:examid")
    public String getExamNameById(String examid);


    @Query("select examname from ASSESSMENTPAPERPATTERN where subjectname=:subName")
    public String[] getExamNameBySubjectName(String subName);

    @Query("select subjectname from ASSESSMENTPAPERPATTERN where examid=:examid")
    public String getSubjectNameById(String examid);
}