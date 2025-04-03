package com.jetsynthesys.rightlife.ui.mindaudit.questions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MindAuditAssessmentQuestions {

    @SerializedName("assessmentQuestionnaires")
    @Expose
    private AssessmentQuestionnaires assessmentQuestionnaires;

    public AssessmentQuestionnaires getAssessmentQuestionnaires() {
        return assessmentQuestionnaires;
    }

    public void setAssessmentQuestionnaires(AssessmentQuestionnaires assessmentQuestionnaires) {
        this.assessmentQuestionnaires = assessmentQuestionnaires;
    }

}