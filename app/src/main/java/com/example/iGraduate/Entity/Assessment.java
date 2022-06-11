package com.example.iGraduate.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;

    private String title;
    private String startDate;
    private String endDate;
    private String type;

    private Boolean shouldRemindStart;
    private Boolean shouldRemindEnd;

    public Assessment() {

    }

    public Assessment(int assessmentId, String title, String startDate, String endDate, String type, Boolean shouldRemindStart, Boolean shouldRemindEnd) {
        this.assessmentId = assessmentId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.shouldRemindStart = shouldRemindStart;
        this.shouldRemindEnd = shouldRemindEnd;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getShouldRemindStart() {
        return shouldRemindStart;
    }

    public Boolean getShouldRemindEnd() {
        return shouldRemindEnd;
    }

    public void setShouldRemindStart(Boolean shouldRemindStart) {
        this.shouldRemindStart = shouldRemindStart;
    }

    public void setShouldRemindEnd(Boolean shouldRemindEnd) {
        this.shouldRemindEnd = shouldRemindEnd;
    }

    @Override
    public String toString() {
        return title;
    }
}
