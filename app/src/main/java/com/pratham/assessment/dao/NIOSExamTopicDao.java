package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentToipcsModal;
import com.pratham.assessment.domain.NIOSExamTopics;

import java.util.List;

@Dao
public interface NIOSExamTopicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTopic(NIOSExamTopics assessmentToipcsModal);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllTopics(List<NIOSExamTopics> assessmentToipcsModal);

    @Query("DELETE FROM NIOSExamTopics")
    public void deleteTopics();

    @Query("select * from NIOSExamTopics")
    public List<AssessmentToipcsModal> getAllAssessmentToipcs();

    @Query("select * from NIOSExamTopics where examid=:examId")
    public List<NIOSExamTopics> getTopicIdByExamId(String examId);

    @Query("select * from NIOSExamTopics where subjectid=:subjectId and languageid=:LangId")
    public List<NIOSExamTopics> getTopicIdBySubIdLangId(String subjectId,String LangId);
/*

    @Query("select * from NIOSExamTopics where sentFlag=0")
    public List<Modal_Log> getPushAllLogs();
*/


  /*  @Query("update Logs set sentFlag=1 where sentFlag=0")
    public void setSentFlag();*/
}