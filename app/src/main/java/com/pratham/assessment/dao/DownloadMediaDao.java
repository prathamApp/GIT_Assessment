package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pratham.assessment.domain.DownloadMedia;

import java.util.List;


@Dao
public interface DownloadMediaDao {

    @Insert
    long insert(DownloadMedia downloadMedia);

    @Insert
    long[] insertAll(DownloadMedia... downloadMedia);

    @Insert
    long[] insertAllMedia(List<DownloadMedia> downloadMediaList);

    @Update
    int update(DownloadMedia downloadMedia);

    @Delete
    void delete(DownloadMedia downloadMedia);

    @Delete
    void deleteAll(DownloadMedia... downloadMedia);


    @Query("select * from DownloadMedia where qId=:qid and paperId=:paperId and mediaType=:type and sentFlag=0")
    DownloadMedia getMediaByQidAndPaperId(String qid, String paperId, String type);

    @Query("select * from DownloadMedia where mediaType=:type and paperId=:paperId and sentFlag=0")
    DownloadMedia getMediaByTypeAndPaperId(String type, String paperId);

    @Query("DELETE FROM DownloadMedia")
    void deleteAllMedia();

    @Query("DELETE FROM DownloadMedia where paperId=:paperId and qId=:qid")
    int deleteByPaperIdAndQid(String paperId, String qid);

    @Query("DELETE FROM DownloadMedia where paperId=:paperId and qtId=:qtid")
    int deleteByPaperIdAndQtid(String paperId, String qtid);

    @Query("DELETE FROM DownloadMedia where qId=:qId and qtId=:qtid")
    int deleteByQIdAndQtid(String qId, String qtid);


    @Query("select * from DownloadMedia WHERE qId=:qid")
    DownloadMedia getMediaByQid(String qid);

    @Query("select * from DownloadMedia WHERE sentFlag=0 AND mediaType=:type and photoUrl!=''")
    List<DownloadMedia> getMediaByTypeForPush(String type);

    @Query("update DownloadMedia set sentFlag=1 where sentFlag=0 AND mediaType=:type")
    public int setSentFlag(String type);
}
