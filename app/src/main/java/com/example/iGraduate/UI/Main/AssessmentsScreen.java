package com.example.iGraduate.UI.Main;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.AddAssessmentView;
import com.example.iGraduate.UI.ManageAssessmentView;

import java.util.List;

public class AssessmentsScreen extends AppCompatActivity {

    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        this.setTitle("Assessments");

        repo = new Repository(getApplication());

        List<Assessment> assessments = repo.getAllAssessments();

        ListAdapter assessmentAdapter = new ArrayAdapter<Assessment>(this, android.R.layout.simple_list_item_1, assessments);
        ListView assessmentListView = (ListView) findViewById(R.id.assessmentsListView);
        assessmentListView.setAdapter(assessmentAdapter);

        assessmentListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible

                        Assessment assessment = (Assessment) parent.getItemAtPosition(position);
                        Intent intent = new Intent(AssessmentsScreen.this, ManageAssessmentView.class);
                        intent.putExtra("id", assessment.getAssessmentId());
                        intent.putExtra("title", assessment.getTitle());
                        intent.putExtra("startDate", assessment.getStartDate());
                        intent.putExtra("endDate", assessment.getEndDate());
                        intent.putExtra("type", assessment.getType());
                        intent.putExtra("shouldRemindStart", assessment.getShouldRemindStart());
                        intent.putExtra("shouldRemindEnd", assessment.getShouldRemindEnd());
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessments_menu, menu);
        return true;
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

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
                List<Assessment> assessments = repo.getAllAssessments();

                ListAdapter assessmentAdapter = new ArrayAdapter<Assessment>(this, android.R.layout.simple_list_item_1, assessments);
                ListView assessmentListView = (ListView) findViewById(R.id.assessmentsListView);
                assessmentListView.setAdapter(assessmentAdapter);

                assessmentListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Assessment assessment = (Assessment) parent.getItemAtPosition(position);
                                Intent intent = new Intent(AssessmentsScreen.this, ManageAssessmentView.class);
                                intent.putExtra("id", assessment.getAssessmentId());
                                intent.putExtra("title", assessment.getTitle());
                                intent.putExtra("startDate", assessment.getStartDate());
                                intent.putExtra("endDate", assessment.getEndDate());
                                intent.putExtra("type", assessment.getType());
                                intent.putExtra("shouldRemindStart", assessment.getShouldRemindStart());
                                intent.putExtra("shouldRemindEnd", assessment.getShouldRemindEnd());
                                startActivity(intent);
                            }
                        }
                );
                Toast.makeText(this, "Page refreshed successfully!", Toast.LENGTH_SHORT).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleAddAssessment(View view) {
        Intent intent = new Intent(AssessmentsScreen.this, AddAssessmentView.class);
        intent.putExtra("courseId", -1);
        startActivity(intent);
    }
}