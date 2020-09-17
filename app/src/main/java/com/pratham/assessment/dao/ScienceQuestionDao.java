package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.ScienceQuestion;

import java.util.List;

@Dao
public interface ScienceQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllQuestions(List<ScienceQuestion> questionList);

    @Query("DELETE FROM ScienceQuestion")
    public void deleteAllQuestions();

    @Query("SELECT * FROM ScienceQuestion ")
    public List<ScienceQuestion> getAllQuestions();

    //    @Query("SELECT * FROM Groups WHERE VillageID=:vID ORDER BY GroupName ASC")
    //    public List<Groups> GetGroups(int vID);

    @Query("DELETE FROM ScienceQuestion WHERE qid=:qid")
    public void deleteQuestionByQID(String qid);

    @Query("DELETE FROM ScienceQuestion WHERE examid=:examId")
    public void deleteQuestionByExamId(String examId);

    @Query("SELECT qname FROM ScienceQuestion WHERE qid=:qID")
    public String getQuestionNameByQID(String qID);

    @Query("SELECT answer FROM ScienceQuestion WHERE qid=:qID")
    public String getAnswerNameByQID(String qID);

    @Query("SELECT * FROM ScienceQuestion WHERE qid=:qid")
    public ScienceQuestion getQuestionByQID(String qid);


    @Query("SELECT * FROM ScienceQuestion WHERE RefParaID=:qid and IsParaQuestion and qlevel=:level")
    public List<ScienceQuestion> getParaQuestionByQidAndLevel(String qid,String level);

    @Query("SELECT * FROM ScienceQuestion WHERE qtid='14' and languageid=:selectedLanguage and subjectid=:subjectId and topicid=:topicid and qlevel=:level")
    public ScienceQuestion getParagraphs(String selectedLanguage, String subjectId, String topicid,String level);

    @Query("SELECT qname FROM ScienceQuestion WHERE qid=:refId ")
    public String getParabyRefId(String refId);

    @Query("SELECT * FROM ScienceQuestion WHERE topicid=:topicId and languageid=:langId and subjectid=:subId and examid=:examId")
    public List<ScienceQuestion> getQuestionListByLangIdSubIdTopicIdExamId(String topicId, String langId, String subId, String examId);

    @Query("SELECT * FROM ScienceQuestion WHERE topicid=:topicId and languageid=:langId and subjectid=:subId")
    public List<ScienceQuestion> getQuestionListByLangIdSubIdTopicId(String topicId, String langId, String subId);

    @Query("DELETE FROM ScienceQuestion WHERE topicid=:topicId and languageid=:langId and subjectid=:subId")
    public void deleteByLangIdSubIdTopicId(String topicId, String langId, String subId);

    @Query("select * from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId and qlevel=:qlevel and IsParaQuestion=0 and qtid!='14' order by random() limit :noOfQues")
    public List<ScienceQuestion> getQuestionListByPattern(String langId, String subId, String topicId, String qtid, String qlevel, int noOfQues);

    @Query("select * from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId and qlevel=:qlevel and IsParaQuestion=0 and qtid!='14' limit :noOfQues")
    public List<ScienceQuestion> getQuestionListByPatternRandomly(String langId, String subId, String topicId, String qtid, String qlevel, int noOfQues);

    @Query("select distinct qid from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId order by random() limit :noOfQues")
    public String getQuestionListByPatternOld(String langId, String subId, String topicId, String qtid, int noOfQues);

      /*  @Query("select * from Groups WHERE DeviceID = 'deleted'")
        public List<Groups> GetAllDeletedGroups();
    */
}