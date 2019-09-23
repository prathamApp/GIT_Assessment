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

    @Insert
    long[] insertAll(Score... scores);

    @Insert
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

    @Query("select * from Score where sentFlag = 0 AND SessionID=:session_id")
    List<Score> getAllNewScores(String session_id);

    @Query("select count(*) from Score where Level =:level AND paperId=:paperId")
    int getLevelCnt(String level, String paperId);

    @Query("select count(*) from Score where Level =:level AND isCorrect=:isCorrect AND paperId=:paperId")
    int getLevelCorrectCnt(String level, String paperId,boolean isCorrect);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addScoreList(List<Score> contentList);

    @Query("update Score set sentFlag=1 where sentFlag=0")
    public void setSentFlag();


    @Query("select * from Score where paperId=:paperId")
    List<Score> getScoreByPaperId(String paperId);


}
