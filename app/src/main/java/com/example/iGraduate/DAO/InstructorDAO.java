package com.example.iGraduate.DAO;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.iGraduate.Converters.Converters;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;

import java.util.ArrayList;
import java.util.List;

@Dao
@TypeConverters({Converters.class})
public interface InstructorDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Instructor instructor);

    @Query("SELECT * FROM instructors WHERE instructorId = :instructorId")
    Instructor findInstructor(int instructorId);

    @Query("DELETE FROM instructors WHERE instructorId = :instructorId")
    abstract void delete(int instructorId);

    @Query("SELECT * FROM instructors ORDER BY instructorId ASC")
    List<Instructor> getAllInstructors();

    @Update
    void updateInstructor(Instructor instructor);

    @Query("DELETE FROM instructors")
    abstract void deleteAll();

/*    @Query("DELETE FROM sqlite_sequence")
    void resetAutoIncrement();*/
}