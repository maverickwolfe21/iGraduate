package com.example.iGraduate.UI.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.AddCourseView;
import com.example.iGraduate.UI.DetailedCourseView;

import java.util.List;

public class CoursesScreen extends AppCompatActivity {

    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        repo = new Repository(getApplication());

        this.setTitle("Courses");

        List<Course> courses = repo.getAllCourses();

        ListAdapter courseAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_1, courses);
        ListView courseListView = (ListView) findViewById(R.id.coursesListView);
        courseListView.setAdapter(courseAdapter);

        courseListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible

                        Course course = (Course) parent.getItemAtPosition(position);
                        Intent intent = new Intent(CoursesScreen.this, DetailedCourseView.class);
                        intent.putExtra("id", course.getCourseId());
                        intent.putExtra("title", course.getTitle());
                        intent.putExtra("startDate", course.getStartDate());
                        intent.putExtra("endDate", course.getEndDate());
                        intent.putExtra("status", course.getStatus());
                        intent.putExtra("notes", course.getNotes());
                        intent.putExtra("instructorIds", course.getInstructorIds());
                        intent.putExtra("assessmentIds", course.getAssessmentIds());

                        startActivity(intent);

                    }
                }
        );
    }

    @Override
    public void onResume() {
        // this is called by your activity before it gets visible to the user, when you leave this activity 2 and come back to
        //activity 1, because activity 1 wasn't killed it is resumed from the backstack and this function is called
        // TODO Auto-generated method stub
        super.onResume();
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
        p.setVisibility(View.GONE);
    }

    public void handleAddCourse(View view) {
        Intent intent = new Intent(CoursesScreen.this, AddCourseView.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.courses_menu, menu);
        return true;
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();

                List<Course> courses = repo.getAllCourses();

                ListAdapter courseAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_1, courses);
                ListView courseListView = (ListView) findViewById(R.id.coursesListView);
                courseListView.setAdapter(courseAdapter);

                courseListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Course course = (Course) parent.getItemAtPosition(position);
                                Intent intent = new Intent(CoursesScreen.this, DetailedCourseView.class);
                                intent.putExtra("id", course.getCourseId());
                                intent.putExtra("title", course.getTitle());
                                intent.putExtra("startDate", course.getStartDate());
                                intent.putExtra("endDate", course.getEndDate());
                                intent.putExtra("shouldRemindStart", course.getShouldRemindStart());
                                intent.putExtra("shouldRemindEnd", course.getShouldRemindEnd());
                                intent.putExtra("status", course.getStatus());
                                intent.putExtra("notes", course.getNotes());
                                intent.putExtra("instructorIds", course.getInstructorIds());
                                intent.putExtra("assessmentIds", course.getAssessmentIds());
                                startActivity(intent);
                            }
                        }
                );

                Toast.makeText(this, "Page refreshed successfully!", Toast.LENGTH_SHORT).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}