package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class ScienceQuestionChoice {

    @NonNull
    @PrimaryKey
    private String qcid;
    private String correct;

    private String choicename;

    private String qid;

    private String matchingname;

    public String getCorrect ()
    {
        return correct;
    }

    public void setCorrect (String correct)
    {
        this.correct = correct;
    }

    public String getQcid ()
    {
        return qcid;
    }

    public void setQcid (String qcid)
    {
        this.qcid = qcid;
    }

    public String getChoicename ()
    {
        return choicename;
    }

    public void setChoicename (String choicename)
    {
        this.choicename = choicename;
    }

    public String getQid ()
    {
        return qid;
    }

    public void setQid (String qid)
    {
        this.qid = qid;
    }

    public String getMatchingname ()
    {
        return matchingname;
    }

    public void setMatchingname (String matchingname)
    {
        this.matchingname = matchingname;
    }

}