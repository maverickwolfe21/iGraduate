package com.example.iGraduate.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.iGraduate.Converters.Converters;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;

import java.util.List;

@Dao
@TypeConverters({Converters.class})
public interface CourseDAO {

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    Course findCourse(int courseId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void updateCourse(Course course);

    @Query("DELETE FROM courses WHERE courseId = :courseId")
    abstract void delete(int courseId);

    @Query("SELECT * FROM courses ORDER BY courseId ASC")
    List<Course> getAllCourses();

    @Query("DELETE FROM courses")
    abstract void deleteAll();

    @Query("DELETE FROM sqlite_sequence")
    void resetAutoIncrement();
}
