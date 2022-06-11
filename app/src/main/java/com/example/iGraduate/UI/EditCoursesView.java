package com.example.iGraduate.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class EditCoursesView extends AppCompatActivity {

    Repository repo;
    Boolean isChecked = false;
    int termId;
    Term term;

    ArrayList<String> courseIdsToAdd = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses_to_term_view);
        repo = new Repository(getApplication());

        setTitle("Select Courses");
        termId = getIntent().getIntExtra("termId", 0);
        //System.out.println("Post-intent termId: " + termId);
        System.out.println("Term Id: " + termId);
        term = repo.findTerm(termId);

        List<Course> courses = repo.getAllCourses();

        ListAdapter courseAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_checked, courses);
        ListView courseListView = (ListView) findViewById(R.id.addCoursesList);
        courseListView.setAdapter(courseAdapter);

        //Course IDs
        ArrayList<Integer> courseIds = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            courseIds.add(courses.get(i).getCourseId());
        }
        //System.out.println("CourseIds: " + courseIds);

        //Term IDs
        ArrayList<Integer> termCourseIds = getIntent().getIntegerArrayListExtra("courseIds");
        System.out.println("Term course Ids: " + termCourseIds);
        //System.out.println(courseIds);
        //System.out.println(termCourseIds);

        for (int i = 0; i < courseIds.size(); i++) {
            if (termCourseIds.contains(String.valueOf(courseIds.get(i)))) {
                courseListView.setItemChecked(i, true);
                courseIdsToAdd.add((String.valueOf(courseIds.get(i)))); // onSave
                System.out.println("match " + String.valueOf(courseIds.get(i)));
            }
        }


        courseListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Course course = (Course) parent.getItemAtPosition(position);

                        if (!courseIdsToAdd.contains(String.valueOf(course.getCourseId()))) {
                            courseIdsToAdd.add(String.valueOf(course.getCourseId()));
                        } else {
                            courseIdsToAdd.remove(String.valueOf(course.getCourseId()));
                        }
                        //System.out.println(courseIdsToAdd);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_courses_menu, menu);
        return true;
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:

                ArrayList<Integer> courseIds = new ArrayList<>();
                for (int i = 0; i < courseIdsToAdd.size(); i++) {
                    courseIds.add(Integer.parseInt(courseIdsToAdd.get(i)));
                }
                term.setCourseIds(courseIds);
                repo.updateTerm(term);
                Toast.makeText(this, "Courses updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.action_cancel:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
