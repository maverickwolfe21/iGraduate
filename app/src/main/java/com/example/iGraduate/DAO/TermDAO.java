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
import com.example.iGraduate.Entity.Term;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Query("SELECT * FROM terms WHERE termId = :termId")
    Term findTerm(int termId);

    @Update
    void updateTerm(Term term);

    @Query("DELETE FROM terms WHERE termId = :termId")
    abstract void delete(int termId);

    @Query("SELECT * FROM terms ORDER BY termId ASC")
    List<Term> getAllTerms();

    @Query("DELETE FROM terms")
    abstract void deleteAll();

/*    @Query("DELETE FROM sqlite_sequence")
    void resetAutoIncrement();*/
}