package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pratham.assessment.domain.Assessment;
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



    @Query("select * from DownloadMedia where qId=:qid and paperId=:paperId")
    List<DownloadMedia> getMediaByQidAndPaperId(String qid,String paperId);

    @Query("DELETE FROM DownloadMedia")
    void deleteAllMedia();



    @Query("select * from DownloadMedia WHERE qId=:qid")
     List<DownloadMedia> getediaByQid(String qid);

  /*  @Query("update Assessment set sentFlag=1 where sentFlag=0")
    public void setSentFlag();*/
}
