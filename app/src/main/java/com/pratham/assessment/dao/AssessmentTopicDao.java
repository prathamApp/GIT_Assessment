package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentToipcsModal;

import java.util.List;

@Dao
public interface AssessmentTopicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTopic(AssessmentToipcsModal assessmentToipcsModal);

    @Query("DELETE FROM AssessmentTopic")
    public void deleteTopics();

    @Query("select * from AssessmentTopic")
    public List<AssessmentToipcsModal> getAllAssessmentToipcs();

    @Query("select topicid from AssessmentTopic where topicname=:topicName")
    public String getTopicIdByTopicName(String topicName);
/*

    @Query("select * from AssessmentTopic where sentFlag=0")
    public List<Modal_Log> getPushAllLogs();
*/

   /* @Query("select * from Logs where sentFlag=0 AND sessionId=:s_id")
    public List<Modal_Log> getAllLogs(String s_id);
*/
  /*  @Query("update Logs set sentFlag=1 where sentFlag=0")
    public void setSentFlag();*/
}