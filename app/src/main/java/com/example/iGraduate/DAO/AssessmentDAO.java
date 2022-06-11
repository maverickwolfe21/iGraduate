package com.example.iGraduate.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.iGraduate.Converters.Converters;
import com.example.iGraduate.Entity.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Query("SELECT * FROM assessments WHERE assessmentId = :assessmentId")
    Assessment findAssessment(int assessmentId);

    @Update
    void updateAssessment(Assessment assessment);

    @Query("DELETE FROM assessments WHERE assessmentId = :assessmentId")
    abstract void delete(int assessmentId);

    @Query("SELECT * FROM assessments ORDER BY assessmentId ASC")
    List<Assessment> getAllAssessments();

    @Query("DELETE FROM assessments")
    abstract void deleteAll();

/*    @Query("DELETE FROM sqlite_sequence")
    void resetAutoIncrement();*/
}