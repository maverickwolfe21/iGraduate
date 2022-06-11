package com.example.iGraduate.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class EditInstructorsView extends AppCompatActivity {

    Repository repo;
    Boolean isChecked = false;
    int courseId;
    Course course;

    ArrayList<String> instructorIdsToAdd = new ArrayList<>();
    ArrayList<Integer> previousInstructorIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructors_to_course_view);
        repo = new Repository(getApplication());

        setTitle("Select Instructors");
        courseId = getIntent().getIntExtra("courseId", 0);
        //System.out.println("Post-intent termId: " + termId);

        List<Instructor> instructors = repo.getAllInstructors();

        //Instructor IDs
        ArrayList<Integer> instructorIds = new ArrayList<>();
        for (int i = 0; i < instructors.size(); i++) {
            instructorIds.add(instructors.get(i).getInstructorId());
        }

        previousInstructorIds = getIntent().getIntegerArrayListExtra("instructorIds");

        ListAdapter instructorAdapter = new ArrayAdapter<Instructor>(this, R.layout.checklist, instructors);
        ListView instructorListView = (ListView) findViewById(R.id.addInstructorsList);
        instructorListView.setAdapter(instructorAdapter);

        for (int i = 0; i < instructorIds.size(); i++) {
            if (previousInstructorIds.contains(String.valueOf(instructorIds.get(i)))) {
                instructorListView.setItemChecked(i, true);
                instructorIdsToAdd.add((String.valueOf(instructorIds.get(i)))); // onSave
                System.out.println("match " + String.valueOf(instructorIds.get(i)));
            }
        }

        instructorListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Instructor instructor = (Instructor) parent.getItemAtPosition(position);

                        if (!instructorIdsToAdd.contains(String.valueOf(instructor.getInstructorId()))) {
                            instructorIdsToAdd.add(String.valueOf(instructor.getInstructorId()));
                        } else {
                            instructorIdsToAdd.remove(String.valueOf(instructor.getInstructorId()));
                        }
                        System.out.println(instructorIdsToAdd);
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

                Course course = repo.findCourse(courseId);

                ArrayList<Integer> instructorIds = new ArrayList<>();
                for (int i = 0; i < instructorIdsToAdd.size(); i++) {
                    instructorIds.add(Integer.parseInt(instructorIdsToAdd.get(i)));
                }
                course.setInstructorIds(instructorIds);
                repo.updateCourse(course);
                Toast.makeText(this, "Instructors updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.action_cancel:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}