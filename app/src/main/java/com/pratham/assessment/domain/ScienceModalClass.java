package com.pratham.assessment.domain;

public class ScienceModalClass
{
    private String questionId;

    private String question;

    private String[] options;

    private String type;

    private String[] correctAns;

    private String mandatory;

    public String getQuestionId ()
    {
        return questionId;
    }

    public void setQuestionId (String questionId)
    {
        this.questionId = questionId;
    }

    public String getQuestion ()
    {
        return question;
    }

    public void setQuestion (String question)
    {
        this.question = question;
    }

    public String[] getOptions ()
    {
        return options;
    }

    public void setOptions (String[] options)
    {
        this.options = options;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String[] getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String[] correctAns) {
        this.correctAns = correctAns;
    }

    public String getMandatory ()
    {
        return mandatory;
    }

    public void setMandatory (String mandatory)
    {
        this.mandatory = mandatory;
    }


}
