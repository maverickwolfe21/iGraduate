package com.example.iGraduate.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.iGraduate.Converters.Converters;
import com.example.iGraduate.DAO.AssessmentDAO;
import com.example.iGraduate.DAO.CourseDAO;
import com.example.iGraduate.DAO.InstructorDAO;
import com.example.iGraduate.DAO.TermDAO;
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.Entity.Term;

// change/increment version to everytime you change anything (dataType, etc.)
@Database(entities = {Course.class, Instructor.class, Assessment.class, Term.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DatabaseBuilder extends RoomDatabase {
    public abstract CourseDAO courseDAO();

    public abstract InstructorDAO instructorDAO();

    public abstract AssessmentDAO assessmentDAO();

    public abstract TermDAO termDAO();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "ScheduleDatabase.db")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
