package com.example.iGraduate.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.Main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddCourseView extends AppCompatActivity {

    EditText title;
    EditText startDate;
    EditText endDate;
    EditText status;
    EditText notes;

    String titl;
    String start;
    String end;
    String stat;
    String note;

    Term term = null;

    Boolean shouldRemindStart = false;
    Boolean shouldRemindEnd = false;

    Repository repo;

    //Course course;
    int courseId;
    ArrayList<Integer> instructorIds;
    ArrayList<Integer> assessmentIds;

    DatePickerDialog.OnDateSetListener startingDate;
    DatePickerDialog.OnDateSetListener endingDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_view);

        Course course = new Course();

        courseId = course.getCourseId();
        instructorIds = course.getInstructorIds();
        assessmentIds = course.getAssessmentIds();

        repo = new Repository(getApplication());
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        this.setTitle("Add Course");
        ActionBar actionBar = getSupportActionBar();

        int termId = getIntent().getIntExtra("termId", 0);
        if (termId > 0) {
            term = repo.findTerm(termId);
        }

        title = findViewById(R.id.addCourseTitleEditText);
        startDate = findViewById(R.id.addCourseStartDateEditText);
        endDate = findViewById(R.id.addCourseEndDateEditText);
        status = findViewById(R.id.addCourseStatusEditText);
        notes = findViewById(R.id.addCourseNotesEditText);

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
                new DatePickerDialog(AddCourseView.this, startingDate, myCalendarStart
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
                new DatePickerDialog(AddCourseView.this, endingDate, myCalendarEnd
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
    }

/*    public void handleEditAssessments(View view) {
        Intent intent = new Intent(AddCourseView.this, EditAssessmentsView.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("assessmentIds", assessmentIds);
        startActivity(intent);
    }

    public void handleAddAssessment(View view) {
        Intent intent = new Intent(AddCourseView.this, AddAssessmentView.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("assessmentIds", assessmentIds);
        startActivity(intent);
    }

    public void handleAddInstructor(View view) {
        Intent intent = new Intent(AddCourseView.this, AddInstructorView.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("instructorIds", instructorIds);
        startActivity(intent);
    }

    public void handleEditInstructors(View view) {
        Intent intent = new Intent(AddCourseView.this, EditInstructorsView.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("instructorIds", instructorIds);
        startActivity(intent);
    }*/

    public void addCourseStartReminder(View view) {
        shouldRemindStart = !shouldRemindStart;
    }

    public void addCourseEndReminder(View view) {
        shouldRemindEnd = !shouldRemindEnd;
    }

    private void updateLabelStart() {
        startDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        endDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_course_menu, menu);
        return true;
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_cancel:
                finish();
                return true;
            case R.id.action_save:
                if (validateInputs() == true) {

                    ArrayList<Integer> instructorIds = new ArrayList<>();
                    ArrayList<Integer> assessmentIds = new ArrayList<>();
                    Course course = new Course(title.getText().toString(), instructorIds, assessmentIds, startDate.getText().toString(), endDate.getText().toString(), shouldRemindStart, shouldRemindEnd, status.getText().toString(), notes.getText().toString());
                    repo.insertCourse(course);

                    if (shouldRemindStart) {
                        String screenDate = startDate.getText().toString();
                        if (screenDate.equals("")) screenDate = "04/20/22";
                        Date myDate = null;
                        try {
                            myDate = sdf.parse(screenDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long trigger = myDate.getTime();
                        Intent intent = new Intent(AddCourseView.this, MyReceiver.class);
                        intent.putExtra("key", title.getText().toString() + " starts today!");
                        PendingIntent sender = PendingIntent.getBroadcast(AddCourseView.this, MainActivity.numAlert++, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                    }
                    if (shouldRemindEnd) {
                        String screenDate = endDate.getText().toString();
                        if (screenDate.equals("")) screenDate = "04/20/22";
                        Date myDate = null;
                        try {
                            myDate = sdf.parse(screenDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long trigger = myDate.getTime();
                        Intent intent = new Intent(AddCourseView.this, MyReceiver.class);
                        intent.putExtra("key", title.getText().toString() + " ends today!");
                        PendingIntent sender = PendingIntent.getBroadcast(AddCourseView.this, MainActivity.numAlert++, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                    }

                    if (term != null) {
                        ArrayList<Integer> termCourses = term.getCourseIds();
                        int id = repo.getAllCourses().size();
                        termCourses.add(id);
                        term.setCourseIds(termCourses);
                        repo.updateTerm(term);
                    }

                    Toast.makeText(this, title.getText().toString() + " was added successfully!", Toast.LENGTH_SHORT).show();

                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
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

        if (title.getText().toString().matches("") || startDate.getText().toString().matches("") || endDate.getText().toString().matches("") || status.getText().toString().matches("")) {
            Toast.makeText(this, "Please ensure all fields are filled out (notes are optional).", Toast.LENGTH_SHORT).show();
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

    public static void setListViewHeightBasedOnChildren
            (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}