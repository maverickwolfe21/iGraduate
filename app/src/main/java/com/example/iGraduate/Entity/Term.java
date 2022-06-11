package com.example.iGraduate.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termId;

    private String title;
    private String startDate;
    private String endDate;
    private ArrayList<Integer> courseIds;

    public Term() {
    }

    public Term(int termId, String title, String startDate, String endDate, ArrayList<Integer> courseIds) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseIds = courseIds;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
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

    public ArrayList<Integer> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(ArrayList<Integer> courseIds) {
        this.courseIds = courseIds;
    }

    @Override
    public String toString() {
        return title + " (" + startDate + " - " + endDate + ")";
    }
}
