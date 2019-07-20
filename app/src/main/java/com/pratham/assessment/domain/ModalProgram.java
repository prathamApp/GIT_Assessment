package com.pratham.assessment.domain;

import com.google.gson.annotations.SerializedName;


public class ModalProgram {
    @SerializedName("ProgramId")
    private
    String ProgramId;
    @SerializedName("ProgramName")
    private
    String ProgramName;
    @SerializedName("programId")
    private
    String kolibriProgramId;
    @SerializedName("programName")
    private
    String kolibriProgramName;

    public String getKolibriProgramId() {
        return kolibriProgramId;
    }

    public void setKolibriProgramId(String kolibriProgramId) {
        this.kolibriProgramId = kolibriProgramId;
    }

    public String getKolibriProgramName() {
        return kolibriProgramName;
    }

    public void setKolibriProgramName(String kolibriProgramName) {
        this.kolibriProgramName = kolibriProgramName;
    }

    public String getProgramId() {
        return ProgramId;
    }

    public void setProgramId(String programId) {
        ProgramId = programId;
    }

    public String getProgramName() {
        return ProgramName;
    }

    public void setProgramName(String programName) {
        ProgramName = programName;
    }
}