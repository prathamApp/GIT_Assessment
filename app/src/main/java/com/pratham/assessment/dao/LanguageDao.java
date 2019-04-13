package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.ScienceQuestion;

import java.util.List;

@Dao
public interface LanguageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllLanguages(List<AssessmentLanguages> languages);

    @Query("DELETE FROM AssessmentLanguages")
    public void deleteAllLangs();

    @Query("SELECT * FROM AssessmentLanguages ")
    public List<AssessmentLanguages> getAllLangs();

//    @Query("SELECT * FROM Groups WHERE VillageID=:vID ORDER BY GroupName ASC")
//    public List<Groups> GetGroups(int vID);

   /* @Query("DELETE FROM ScienceQuestion WHERE qid=:qid")
    public void deleteQuestionByQID(String qid);
*/
    @Query("SELECT languageid FROM AssessmentLanguages WHERE languagename=:lname")
    public String getLangNameById(String lname);

    /*@Query("SELECT * FROM ScienceQuestion WHERE qid=:qid")
    public ScienceQuestion getQuestionByQID(String qid);

    @Query("SELECT * FROM ScienceQuestion WHERE topicid=:topicId")
    public List<ScienceQuestion> getQuestionListByTopicId(String topicId);
*/
  /*  @Query("select * from Groups WHERE DeviceID = 'deleted'")
    public List<Groups> GetAllDeletedGroups();
*/
}