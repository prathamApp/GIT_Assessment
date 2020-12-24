package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentPatternDetails;
import com.pratham.assessment.domain.CertificateTopicList;

import java.util.List;

@Dao
public interface CertificateTopicListDao {
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCertificateTopicQuestion(CertificateTopicList certificateTopic);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllCertificateTopicQuestions(List<CertificateTopicList> certificateTopicList);

    @Query("DELETE FROM CertificateTopicList")
    public void deleteCertificateTopicList();


    @Query("DELETE FROM CertificateTopicList where topicid=:topicid")
    public void deleteQuestionByTopicId(String topicid);

    @Query("select * from CertificateTopicList")
    public List<CertificateTopicList> getAllCertificateQuestions();

    @Query("select distinct topicid from CertificateTopicList")
    public List<String> getDistinctTopicIds();
*/

}