package com.example.iGraduate.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailedTermView extends AppCompatActivity {

    EditText title;
    EditText startDate;
    EditText endDate;

    String termTitle;

    Repository repo;
    int termId;
    Term term;

    DatePickerDialog.OnDateSetListener startingDate;
    DatePickerDialog.OnDateSetListener endingDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf;

    ListAdapter courseAdapter;
    ListView courseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term_view);

        repo = new Repository(getApplication());
        termId = getIntent().getIntExtra("id", 0);
        term = repo.findTerm(termId);

        termTitle = getIntent().getStringExtra("title");
        this.setTitle("Term Details");
        ActionBar actionBar = getSupportActionBar();

        title = findViewById(R.id.editDetailedTermTitle);
        startDate = findViewById(R.id.editDetailedTermStartDate);
        endDate = findViewById(R.id.editDetailedTermEndDate);

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = startDate.getText().toString();
                if (info.equals("")) info = "04/19/22";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(DetailedTermView.this, startingDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startingDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = endDate.getText().toString();
                if (info.equals("")) info = "04/19/22";
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(DetailedTermView.this, endingDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endingDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };

        title.setText(getIntent().getStringExtra("title"));
        startDate.setText(getIntent().getStringExtra("startDate"));
        endDate.setText(getIntent().getStringExtra("endDate"));

        sdf = new SimpleDateFormat(myFormat, Locale.US);

        //get term course IDs
        ArrayList<Integer> termCourseIds = (getIntent().getIntegerArrayListExtra("courseIds"));
        ArrayList<Course> courses = new ArrayList<>();

        //get term courses
        for (int i = 0; i < termCourseIds.size(); i++) {
            int courseId = Integer.parseInt(String.valueOf(termCourseIds.get(i)));
            courses.add(repo.findCourse(courseId));
        }
        courseAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_1, courses);
        courseListView = (ListView) findViewById(R.id.termCoursesListView);
        courseListView.setAdapter(courseAdapter);

        courseListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible

                        Course course = (Course) parent.getItemAtPosition(position);
                        System.out.println("Start date: " + course.getStartDate());
                        System.out.println("End date: " + course.getEndDate());

                        Intent intent = new Intent(DetailedTermView.this, DetailedCourseNoDeleteView.class);
                        intent.putExtra("id", course.getCourseId());
                        intent.putExtra("title", course.getTitle());
                        intent.putExtra("startDate", course.getStartDate());
                        intent.putExtra("endDate", course.getEndDate());
                        intent.putExtra("instructorIds", course.getInstructorIds());
                        intent.putExtra("assessmentIds", course.getAssessmentIds());
                        intent.putExtra("status", course.getStatus());
                        intent.putExtra("notes", course.getNotes());
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

    public void handleEditCourses(View view) {
        Intent intent = new Intent(DetailedTermView.this, EditCoursesView.class);
        intent.putExtra("termId", termId);
        ArrayList<Integer> courseIds = term.getCourseIds();
        intent.putExtra("courseIds", courseIds);
        startActivity(intent);
    }

    private void updateLabelStart() {
        startDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        endDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_term_menu, menu);
        return true;
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();

                term = repo.findTerm(termId);
                ArrayList<Integer> termCourseIDs = term.getCourseIds();
                ArrayList<Course> courses = new ArrayList<>();

                //get term courses
                for (int i = 0; i < termCourseIDs.size(); i++) {
                    int courseId = Integer.parseInt(String.valueOf(termCourseIDs.get(i)));
                    courses.add(repo.findCourse(courseId));
                }
                courseAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_1, courses);
                courseListView = (ListView) findViewById(R.id.termCoursesListView);
                courseListView.setAdapter(courseAdapter);

                courseListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Course course = (Course) parent.getItemAtPosition(position);

                                Intent intent = new Intent(DetailedTermView.this, DetailedCourseView.class);
                                intent.putExtra("id", course.getCourseId());
                                intent.putExtra("title", course.getTitle());
                                intent.putExtra("startDate", course.getStartDate());
                                intent.putExtra("endDate", course.getEndDate());
                                intent.putExtra("instructorIds", course.getInstructorIds());
                                intent.putExtra("assessmentIds", course.getAssessmentIds());
                                intent.putExtra("status", course.getStatus());
                                intent.putExtra("notes", course.getNotes());
                                startActivity(intent);
                            }
                        }
                );
                Toast.makeText(this, "Page refreshed successfully!", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_save:

                if (validateInputs() == true) {
                    Term term = repo.findTerm(termId);
                    term.setTitle(title.getText().toString());
                    term.setStartDate(startDate.getText().toString());
                    term.setEndDate(endDate.getText().toString());
                    repo.updateTerm(term);
                    Toast.makeText(this, title.getText().toString() + " was updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    System.out.println(false);
                }

                return true;
            case R.id.action_delete:
                showDeleteDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDeleteDialog() {

        DeleteTermDialog deleteDialog = new DeleteTermDialog(termId, termTitle, repo, this);
        deleteDialog.show(getSupportFragmentManager(), "deleteDialog");
    }

    public boolean validateInputs() {

        Date startTestParse = null;
        Date endTestParse = null;
        try {
            startTestParse = sdf.parse(startDate.getText().toString());
            endTestParse = sdf.parse(endDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (title.getText().toString().matches("") || startDate.getText().toString().matches("") || endDate.getText().toString().matches("")) {
            Toast.makeText(this, "Please ensure all fields have been filled out.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (startTestParse == null || endTestParse == null) {
            Toast.makeText(this, "Please ensure dates are in the following format (MM/dd/yy).", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!startTestParse.before(endTestParse) && !startTestParse.equals(endTestParse)) {
            Toast.makeText(this, "Please ensure the end date comes after the start date.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}