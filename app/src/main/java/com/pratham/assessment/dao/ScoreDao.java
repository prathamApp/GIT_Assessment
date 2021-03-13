package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.pratham.assessment.domain.Score;

import java.util.List;


@Dao
public interface ScoreDao {

    @Insert
    long insert(Score score);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Score... scores);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllScores(List<Score> scores);

    @Update
    int update(Score score);

    @Delete
    void delete(Score score);

    @Delete
    void deleteAll(Score... scores);

    @Query("select * from Score")
    List<Score> getAllScores();

    @Query("select * from Score where sentFlag=0 and examId=:ece_assessment")
    List<Score> getAllPushScores(String ece_assessment);


    @Query("DELETE FROM Score")
    void deleteAllScores();

    @Query("select * from Score where sentFlag = 0 AND paperId=:paperId AND SessionID=:sessionId")
    List<Score> getAllNewScores(String paperId, String sessionId);

    @Query("select count(*) from Score where Level =:level AND paperId=:paperId")
    int getLevelCnt(String level, String paperId);

    @Query("select count(*) from Score where Level =:level AND isCorrect=:isCorrect AND paperId=:paperId")
    int getLevelCorrectCnt(String level, String paperId, boolean isCorrect);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addScoreList(List<Score> contentList);

    @Query("update Score set sentFlag=1 where sentFlag=0")
    public void setSentFlag();


    @Query("select * from Score where paperId=:paperId")
    List<Score> getScoreByPaperId(String paperId);

    @Query("select * from Score where sentFlag = 0 AND SessionID=:sessionID")
    List<Score> getAllNewScoresBySession(String sessionID);


    @Query("Select count(distinct REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(substr(startdatetime,1,instr(startdatetime,' ')),'01','1'),'02','2'),'03','3'),'04','4'),'05','5'),'06','6'),'07','7'),'08','8'),'09','9')) as dates from Score sc where length(startdatetime)>5")
    int getTotalActiveDeviceDays();

}
