package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.CertificateTopicList;

import java.util.List;

@Dao
public interface CertificateTopicListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCertificateTopicQuestion(CertificateTopicList certificateTopic);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllCertificateTopicQuestions(List<CertificateTopicList> certificateTopicList);

    @Query("DELETE FROM CertificateTopicList")
    public void deleteCertificateTopicList();

//
    @Query("DELETE FROM CertificateTopicList where subjectid=:subjectId and examid=:examId")
    public void deleteQuestionByExamIdSubId(String subjectId, String examId);

    @Query("select * from CertificateTopicList")
    public List<CertificateTopicList> getAllCertificateQuestions();

    @Query("select * from CertificateTopicList where examId=:examId and subjectid=:subjectid")
    public List<CertificateTopicList> getQuestionsByExamIdSubId(String examId,String subjectid);

//    @Query("select distinct topicid from CertificateTopicList")
//    public List<String> getDistinctTopicIds();

}