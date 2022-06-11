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
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.R;

import java.util.ArrayList;
import java.util.List;

public class EditAssessmentsView extends AppCompatActivity {

    Repository repo;
    Boolean isChecked = false;
    int courseId;
    Course course;

    ArrayList<String> assessmentIdsToAdd = new ArrayList<>();
    ArrayList<Integer> previousAssessmentIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessments_view);
        repo = new Repository(getApplication());

        setTitle("Select Assessments");
        courseId = getIntent().getIntExtra("courseId", 0);
        //System.out.println("Post-intent termId: " + termId);

        List<Assessment> assessments = repo.getAllAssessments();
        previousAssessmentIds = getIntent().getIntegerArrayListExtra("assessmentIds");

        ListAdapter assessmentAdapter = new ArrayAdapter<Assessment>(this, R.layout.checklist, assessments);
        ListView assessmentListView = (ListView) findViewById(R.id.assessmentsList);
        assessmentListView.setAdapter(assessmentAdapter);

        for (int i = 0; i < previousAssessmentIds.size(); i++) {
            String id = String.valueOf(previousAssessmentIds.get(i));
            assessmentIdsToAdd.add(id);
            assessmentListView.setItemChecked(i, true);
        }

        assessmentListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Assessment assessment = (Assessment) parent.getItemAtPosition(position);

                        if (!assessmentIdsToAdd.contains(String.valueOf(assessment.getAssessmentId()))) {
                            assessmentIdsToAdd.add(String.valueOf(assessment.getAssessmentId()));
                        } else {
                            assessmentIdsToAdd.remove(String.valueOf(assessment.getAssessmentId()));
                        }
                        System.out.println(assessmentIdsToAdd);
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

                ArrayList<Integer> assessmentIds = new ArrayList<>();
                for (int i = 0; i < assessmentIdsToAdd.size(); i++) {
                    assessmentIds.add(Integer.parseInt(assessmentIdsToAdd.get(i)));
                }
                course.setAssessmentIds(assessmentIds);
                repo.updateCourse(course);
                Toast.makeText(this, "Assessments updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.action_cancel:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
