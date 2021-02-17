package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

@Dao
public interface ScienceQuestionChoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllQuestionChoices(List<ScienceQuestionChoice> choiceList);

    @Query("UPDATE ScienceQuestionChoice SET choicename  = replace( choicename, '\n', '<br/>' ) WHERE choicename LIKE '%\n%'")
    public void replaceNewLineForQuestionOptionChoiceNames();

    @Query("UPDATE ScienceQuestionChoice SET matchingname  = replace( matchingname, '\n', '<br/>' ) WHERE matchingname LIKE '%\n%'")
    public void replaceNewLineForQuestionOptionMatchingNames();

    @Query("DELETE FROM ScienceQuestionChoice")
    public void deleteAllQuestionChoices();

    @Query("SELECT * FROM ScienceQuestionChoice ")
    public List<ScienceQuestionChoice> getAllQuestionChoices();

//    @Query("SELECT * FROM Groups WHERE VillageID=:vID ORDER BY GroupName ASC")
//    public List<Groups> GetGroups(int vID);

    @Query("DELETE FROM ScienceQuestionChoice WHERE qid=:qid")
    public void deleteQuestionChoicesByQID(String qid);

    @Query("SELECT * FROM ScienceQuestionChoice WHERE qid=:qID")
    public List<ScienceQuestionChoice> getQuestionChoicesByQID(String qID);

    @Query("SELECT * FROM ScienceQuestionChoice WHERE qcid=:qcid")
    public ScienceQuestionChoice getQuestionChoicesByQcID(String qcid);

    @Query("SELECT choiceurl FROM ScienceQuestionChoice WHERE qcid=:qcid")
    public String getImageByQcID(String qcid);

  /*  @Query("select * from Groups WHERE DeviceID = 'deleted'")
    public List<Groups> GetAllDeletedGroups();
*/
}