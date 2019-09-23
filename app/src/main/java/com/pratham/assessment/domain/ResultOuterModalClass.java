package com.pratham.assessment.domain;

import java.io.Serializable;
import java.util.List;

public class ResultOuterModalClass implements Serializable {

    String outOfMarks, marksObtained, studentId, examStartTime, examEndTime, examId, subjectId, paperId;
    List<ResultModalClass> resultList;

    public String getOutOfMarks() {
        return outOfMarks;
    }

    public void setOutOfMarks(String outOfMarks) {
        this.outOfMarks = outOfMarks;
    }

    public String getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(String marksObtained) {
        this.marksObtained = marksObtained;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(String examStartTime) {
        this.examStartTime = examStartTime;
    }

    public String getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(String examEndTime) {
        this.examEndTime = examEndTime;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public List<ResultModalClass> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultModalClass> resultList) {
        this.resultList = resultList;
    }
}
