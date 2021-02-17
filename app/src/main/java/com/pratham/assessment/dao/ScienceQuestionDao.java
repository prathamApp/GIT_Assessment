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

    @Query("UPDATE ScienceQuestion SET qname = replace( qname, '\n', '<br/>' ) WHERE qname LIKE '%\n%'")
    public void replaceNewLineForQuestions();

@Query("UPDATE ScienceQuestion SET qname = replace( qname, '/n', '<br/>' ) WHERE qname LIKE '%/n%'")
    public void replaceNewLineForQuestions2();


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
    public List<ScienceQuestion> getParaQuestionByQidAndLevel(String qid, String level);

    @Query("SELECT * FROM ScienceQuestion WHERE qtid=:qtid  and languageid=:selectedLanguage and subjectid=:subjectId and topicid=:topicid and qlevel=:level and ansdesc like :keyword")
    public ScienceQuestion getParagraphs(String selectedLanguage, String subjectId, String topicid, String level, String qtid, String keyword);
 @Query("SELECT * FROM ScienceQuestion WHERE qtid=:qtid  and languageid=:selectedLanguage and subjectid=:subjectId and topicid=:topicid and qlevel=:level")
    public ScienceQuestion getParagraphsWithoutKeywords(String selectedLanguage, String subjectId, String topicid, String level, String qtid);

    @Query("SELECT * FROM ScienceQuestion WHERE qtid=:qtid  and languageid=:selectedLanguage and subjectid=:subjectId and topicid=:topicid and qlevel=:level and ansdesc like :keyword order by random() limit 1")
    public ScienceQuestion getParagraphsRandomly(String selectedLanguage, String subjectId, String topicid, String level, String qtid, String keyword);

    @Query("SELECT * FROM ScienceQuestion WHERE qtid=:qtid  and languageid=:selectedLanguage and subjectid=:subjectId and topicid=:topicid and qlevel=:level order by random() limit 1")
    public ScienceQuestion getParagraphsRandomlyWithoutKeywords(String selectedLanguage, String subjectId, String topicid, String level, String qtid);

    @Query("SELECT * FROM ScienceQuestion WHERE ansdesc like :keyword")
    public List<ScienceQuestion> getQuestionByKeyWords(String keyword);

    @Query("SELECT qname FROM ScienceQuestion WHERE qid=:refId ")
    public String getParabyRefId(String refId);

    @Query("SELECT * FROM ScienceQuestion WHERE topicid=:topicId and languageid=:langId and subjectid=:subId and examid=:examId")
    public List<ScienceQuestion> getQuestionListByLangIdSubIdTopicIdExamId(String topicId, String langId, String subId, String examId);

    @Query("SELECT * FROM ScienceQuestion WHERE topicid=:topicId and languageid=:langId and subjectid=:subId")
    public List<ScienceQuestion> getQuestionListByLangIdSubIdTopicId(String topicId, String langId, String subId);

    @Query("DELETE FROM ScienceQuestion WHERE topicid=:topicId and languageid=:langId and subjectid=:subId")
    public void deleteByLangIdSubIdTopicId(String topicId, String langId, String subId);

    @Query("select * from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId and qlevel=:qlevel and IsParaQuestion=0 and ansdesc like :keyword and qtid!='14' order by random() limit :noOfQues")
    public List<ScienceQuestion> getQuestionListByPatternRandomly(String langId, String subId, String topicId, String qtid, String qlevel, int noOfQues, String keyword);
@Query("select * from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId and qlevel=:qlevel and IsParaQuestion=0 and qtid!='14' order by random() limit :noOfQues")
    public List<ScienceQuestion> getQuestionListByPatternRandomlyWithoutKeywords(String langId, String subId, String topicId, String qtid, String qlevel, int noOfQues);

    @Query("select * from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId and qlevel=:qlevel and IsParaQuestion=0 and ansdesc like :keyword and qtid!='14' limit :noOfQues")
    public List<ScienceQuestion> getQuestionListByPattern(String langId, String subId, String topicId, String qtid, String qlevel, int noOfQues, String keyword);
@Query("select * from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId and qlevel=:qlevel and IsParaQuestion=0  and qtid!='14' limit :noOfQues")
    public List<ScienceQuestion> getQuestionListByPatternWithoutKeywords(String langId, String subId, String topicId, String qtid, String qlevel, int noOfQues);

    @Query("delete from ScienceQuestion where topicid=:topicId and subjectid=:subId and languageid=:langId")
    public int deleteQuestionList(String langId, String subId, String topicId);

    @Query("select distinct qid from ScienceQuestion where qtId=:qtid and topicid=:topicId and subjectid=:subId and languageid=:langId order by random() limit :noOfQues")
    public String getQuestionListByPatternOld(String langId, String subId, String topicId, String qtid, int noOfQues);

      /*  @Query("select * from Groups WHERE DeviceID = 'deleted'")
        public List<Groups> GetAllDeletedGroups();
    */
}