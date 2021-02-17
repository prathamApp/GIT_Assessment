package com.pratham.assessment.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity(primaryKeys = {"qid","paperId"})
public class TempScienceQuestion implements Serializable {

    private String ansdesc;
    private String updatedby;
    private String qlevel;
    private String addedby;
    private String languageid;
    private String active;
    private String lessonid;
    @Embedded
    private ArrayList<ScienceQuestionChoice> lstquestionchoice;
    @Ignore
    List<ScienceQuestionChoice> matchingNameList = null;
    private String qtid;
    @NonNull
    private String qid;
    private String subjectid;
    private String addedtime;
    private String updatedtime;
    private String photourl;
    private String examtime;
    private String topicid;
    private String answer;
    private String outofmarks;
    private String qname;
    private String hint;
    private String examid;
    private String pdid;
    private String startTime;

    private String endTime;
    private String marksPerQuestion = "0";
    private String userAnswerId = "";
    private String userAnswer = "";
    private boolean isAttempted;
    private boolean isCorrect;
    public boolean IsParaQuestion;
    private String RefParaID;
    private boolean IsQuestionFromSDCard;

    private String SessionID;
    private String StudentID;
    private String DeviceID;
    private int ScoredMarks;
    private int paperTotalMarks;
    private String paperStartDateTime;
    private String paperEndDateTime;
    private int Level;
    private String Label;
    private int sentFlag;

    @NonNull
    private String paperId;


    public String getAnsdesc() {
        return ansdesc;
    }

    public void setAnsdesc(String ansdesc) {
        this.ansdesc = ansdesc;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public String getQlevel() {
        return qlevel;
    }

    public void setQlevel(String qlevel) {
        this.qlevel = qlevel;
    }

    public String getAddedby() {
        return addedby;
    }

    public void setAddedby(String addedby) {
        this.addedby = addedby;
    }

    public String getLanguageid() {
        return languageid;
    }

    public void setLanguageid(String languageid) {
        this.languageid = languageid;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

    public ArrayList<ScienceQuestionChoice> getLstquestionchoice() {
        return lstquestionchoice;
    }

    public void setLstquestionchoice(ArrayList<ScienceQuestionChoice> lstquestionchoice) {
        this.lstquestionchoice = lstquestionchoice;
    }

    public List<ScienceQuestionChoice> getMatchingNameList() {
        return matchingNameList;
    }

    public void setMatchingNameList(List<ScienceQuestionChoice> matchingNameList) {
        this.matchingNameList = matchingNameList;
    }

    public String getQtid() {
        return qtid;
    }

    public void setQtid(String qtid) {
        this.qtid = qtid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getAddedtime() {
        return addedtime;
    }

    public void setAddedtime(String addedtime) {
        this.addedtime = addedtime;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getExamtime() {
        return examtime;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOutofmarks() {
        return outofmarks;
    }

    public void setOutofmarks(String outofmarks) {
        this.outofmarks = outofmarks;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public String getPdid() {
        return pdid;
    }

    public void setPdid(String pdid) {
        this.pdid = pdid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }



    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMarksPerQuestion() {
        return marksPerQuestion;
    }

    public void setMarksPerQuestion(String marksPerQuestion) {
        this.marksPerQuestion = marksPerQuestion;
    }

    public String getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(String userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isAttempted() {
        return isAttempted;
    }

    public void setAttempted(boolean attempted) {
        isAttempted = attempted;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isParaQuestion() {
        return IsParaQuestion;
    }

    public void setParaQuestion(boolean paraQuestion) {
        IsParaQuestion = paraQuestion;
    }

    public String getRefParaID() {
        return RefParaID;
    }

    public void setRefParaID(String refParaID) {
        RefParaID = refParaID;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public int getScoredMarks() {
        return ScoredMarks;
    }

    public void setScoredMarks(int scoredMarks) {
        ScoredMarks = scoredMarks;
    }

    public int getPaperTotalMarks() {
        return paperTotalMarks;
    }

    public void setPaperTotalMarks(int paperTotalMarks) {
        this.paperTotalMarks = paperTotalMarks;
    }

    public String getPaperStartDateTime() {
        return paperStartDateTime;
    }

    public void setPaperStartDateTime(String paperStartDateTime) {
        this.paperStartDateTime = paperStartDateTime;
    }

    public String getPaperEndDateTime() {
        return paperEndDateTime;
    }

    public void setPaperEndDateTime(String paperEndDateTime) {
        this.paperEndDateTime = paperEndDateTime;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    @NonNull
    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(@NonNull String paperId) {
        this.paperId = paperId;
    }

    public boolean getIsQuestionFromSDCard() {
        return IsQuestionFromSDCard;
    }

    public void setIsQuestionFromSDCard(boolean questionFromSDCard) {
        IsQuestionFromSDCard = questionFromSDCard;
    }
}
