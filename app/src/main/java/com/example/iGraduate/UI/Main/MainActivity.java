package com.example.iGraduate.UI.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public static int numAlert;
    Repository repo;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repo = new Repository(getApplication());

/*
        //test data
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                loadingDialog = new LoadingDialog(MainActivity.this);
                loadingDialog.startLoadingDialog();

                Looper.loop();
            }
        }).start();


        repo.deleteAll();
        repo.resetAutoIncrements();

        Instructor instructor1 = new Instructor("Bobby Jones", "bobbyjones@wgu.edu", "602-258-8888"); //, courseIds[]
        Instructor instructor2 = new Instructor("Judy B. Jones", "judybjones@wgu.edu", "602-258-8764");
        Instructor instructor3 = new Instructor("Tony B. Jones", "tonybjones@wgu.edu", "602-258-8743");
        Instructor instructor4 = new Instructor("Johnny B. Jones", "Johnnybjones@wgu.edu", "602-258-8746");
        repo.insertInstructor(instructor1);
        repo.insertInstructor(instructor2);
        repo.insertInstructor(instructor3);
        repo.insertInstructor(instructor4);

        Assessment assessment1 = new Assessment(1, "ABM2 — ABM2 TASK 1: MOBILE APPLICATION DEVELOPMENT", "01/23/22", "01/23/22", "Objective", false, false);
        Assessment assessment2 = new Assessment(2, "ABM2 — ABM2 TASK 2: WEBSITE DEVELOPMENT", "01/23/22", "01/23/22", "Performance", false, false);
        repo.insertAssessment(assessment1);
        repo.insertAssessment(assessment2);

        ArrayList<Integer> instructorIds = new ArrayList<Integer>();
        Collections.addAll(instructorIds, 1, 2, 3, 4);

        ArrayList<Integer> assessmentIds = new ArrayList<Integer>();
        Collections.addAll(assessmentIds, 1, 2); //append everytime assessment is created

        ArrayList<Integer> courseIds = new ArrayList<Integer>();
        Collections.addAll(courseIds, 1); //append everytime assessment is created
        Course algebra1 = new Course("C123 - Course 1", instructorIds, assessmentIds,
                "04/19/22", "04/19/22", false, false, "Finished", "Best math class ever!");
        Course algebra2 = new Course("C194 - Course 2", instructorIds, assessmentIds,
                "04/19/22", "04/19/22", false, false, "In-Progress", "Second best math class ever!");
        repo.insertCourse(algebra1);
        repo.insertCourse(algebra2);

        Term winter = new Term(1, "Term 1", "01/01/22", "06/01/22", courseIds);
        Term summer = new Term(2, "Term 2", "06/01/22", "12/31/22", courseIds);
        repo.insertTerm(winter);
        repo.insertTerm(summer);

        loadingDialog.dismissDialog();*/
    }

    public void viewCourses(View view) {
        Intent intent = new Intent(MainActivity.this, CoursesScreen.class);
        startActivity(intent);
    }

    public void viewAssessments(View view) {
        Intent intent = new Intent(MainActivity.this, AssessmentsScreen.class);
        startActivity(intent);
    }

    // mentor
    public void viewTerms(View view) {
        Intent intent = new Intent(MainActivity.this, TermsScreen.class);
        startActivity(intent);
    }

    public void viewInstructors(View view) {
        Intent intent = new Intent(MainActivity.this, InstructorsScreen.class);
        startActivity(intent);
    }
}