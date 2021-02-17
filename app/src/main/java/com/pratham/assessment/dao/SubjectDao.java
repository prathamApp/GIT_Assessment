package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentSubjects;

import java.util.List;

@Dao
public interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllSubjects(List<AssessmentSubjects> subjects);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSubject(AssessmentSubjects subject);

    @Query("DELETE FROM AssessmentSubjects")
    public void deleteAllSubjects();

    @Query("DELETE FROM AssessmentSubjects where languageid=:langId")
    public void deleteSubjectsByLangId(String langId);

    @Query("DELETE FROM AssessmentSubjects where subjectid=:subId and languageid=:langId")
    public void deleteSubjectsByLangIdSubId(String subId, String langId);

    @Query("SELECT * FROM AssessmentSubjects ")
    public List<AssessmentSubjects> getAllSubjects();

    @Query("SELECT * FROM AssessmentSubjects where languageid=:langId")
    public List<AssessmentSubjects> getAllSubjectsByLangId(String langId);
//    @Query("SELECT * FROM Groups WHERE VillageID=:vID ORDER BY GroupName ASC")
//    public List<Groups> GetGroups(int vID);

    @Query("select subjectid FROM AssessmentSubjects WHERE subjectname=:subName")
    public String getIdByName(String subName);

    @Query("select * FROM AssessmentSubjects WHERE subjectid=:subId")
    public AssessmentSubjects getSubjectById(String subId);

  /*  @Query("SELECT qname FROM ScienceQuestion WHERE qid=:qID")
    public String getQuestionNameByQID(String qID);

    @Query("SELECT * FROM ScienceQuestion WHERE qid=:qid")
    public ScienceQuestion getQuestionByQID(String qid);

    @Query("SELECT * FROM ScienceQuestion WHERE topicid=:topicId")
    public List<ScienceQuestion> getQuestionListByTopicId(String topicId);
*/
  /*  @Query("select * from Groups WHERE DeviceID = 'deleted'")
    public List<Groups> GetAllDeletedGroups();
*/
}