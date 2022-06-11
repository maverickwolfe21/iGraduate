package com.example.iGraduate.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "courses")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int courseId;

    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String notes;
    private ArrayList<Integer> instructorIds;
    private ArrayList<Integer> assessmentIds;

    Boolean shouldRemindStart;
    Boolean shouldRemindEnd;

    public Course() {
    }

    public Course(String title, ArrayList<Integer> instructorIds, ArrayList<Integer> assessmentIds, String startDate, String endDate, Boolean shouldRemindStart, Boolean shouldRemindEnd, String status, String notes) {
        this.title = title;
        this.instructorIds = instructorIds;
        this.assessmentIds = assessmentIds;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.shouldRemindStart = shouldRemindStart;
        this.shouldRemindEnd = shouldRemindEnd;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Integer> getInstructorIds() {
        return instructorIds;
    }

    public void setInstructorIds(ArrayList<Integer> instructorIds) {
        this.instructorIds = instructorIds;
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

    public void setShouldRemindStart(Boolean shouldRemindStart) {
        this.shouldRemindStart = shouldRemindStart;
    }

    public void setShouldRemindEnd(Boolean shouldRemindEnd) {
        this.shouldRemindEnd = shouldRemindEnd;
    }

    public Boolean getShouldRemindStart() {
        return shouldRemindStart;
    }

    public Boolean getShouldRemindEnd() {
        return shouldRemindEnd;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<Integer> getAssessmentIds() {
        return assessmentIds;
    }

    public void setAssessmentIds(ArrayList<Integer> assessmentIds) {
        this.assessmentIds = assessmentIds;
    }

    @Override
    public String toString() {
        return title;
    }
}
