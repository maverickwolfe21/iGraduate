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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.Main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAssessmentView extends AppCompatActivity {

    Repository repo;

    EditText title;
    EditText startDate;
    EditText endDate;
    Switch type;
    CheckBox startReminder;
    CheckBox endReminder;

    DatePickerDialog.OnDateSetListener startingDate;
    DatePickerDialog.OnDateSetListener endingDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf;

    String titl;
    String start;
    String end;
    Boolean shouldRemindStart = false;
    Boolean shouldRemindEnd = false;
    String tipe;

    int courseId;
    ArrayList<Integer> assessmentIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment_view);
        this.setTitle("Add Assessment");
        ActionBar actionBar = getSupportActionBar();

        sdf = new SimpleDateFormat(myFormat, Locale.US);
        repo = new Repository(getApplication());

        courseId = getIntent().getIntExtra("courseId", 0);
        assessmentIds = (getIntent().getIntegerArrayListExtra("assessmentIds"));

        title = findViewById(R.id.editAddAssessmentTitle);
        startDate = findViewById(R.id.editAddAssessmentStartDate);
        endDate = findViewById(R.id.editAddAssessmentEndDate);
        type = findViewById(R.id.addAssessmentTypeSwitch);
        startReminder = findViewById(R.id.editStartCheckbox);
        endReminder = findViewById(R.id.editEndCheckbox);

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = startDate.getText().toString();
                if (info.equals("")) info = "04/20/22";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddAssessmentView.this, startingDate, myCalendarStart
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
                if (info.equals("")) info = "04/20/22";
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddAssessmentView.this, endingDate, myCalendarEnd
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

    public void addAssessmentStartReminder(View view) {
        shouldRemindStart = !shouldRemindStart;
        //System.out.println("ShouldRemind: " + shouldRemindStart);
    }

    public void addAssessmentEndReminder(View view) {
        shouldRemindEnd = !shouldRemindEnd;
    }

    private void updateLabelStart() {
        startDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        endDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:

                if (validateInputs() == true) {

                    if (type.isChecked()) {
                        tipe = "Objective";
                    } else {
                        tipe = "Performance";
                    }

                    int assessmentId = -1;
                    List<Assessment> allAssessments = repo.getAllAssessments();
                    if (!allAssessments.isEmpty()) {
                        assessmentId = allAssessments.size() + 1;
                    } else {
                        assessmentId = 1;
                    }
                    Assessment assessment = new Assessment();
                    assessment.setAssessmentId(assessmentId);
                    assessment.setTitle(title.getText().toString());
                    assessment.setStartDate(startDate.getText().toString());
                    assessment.setEndDate(endDate.getText().toString());
                    assessment.setType(tipe);
                    assessment.setShouldRemindStart(shouldRemindStart);
                    assessment.setShouldRemindEnd(shouldRemindEnd);

                    repo.insertAssessment(assessment);

                    if (courseId != -1) {

                        //add to list of course's assessments
                        assessmentIds.add(assessment.getAssessmentId());

                        Course course = repo.findCourse(courseId);
                        course.setAssessmentIds(assessmentIds);
                        repo.updateCourse(course);
                    }

                    if (shouldRemindStart == true) {
                        String screenStartDate = startDate.getText().toString();
                        if (screenStartDate.equals("")) screenStartDate = "04/20/22";
                        Date myStartDate = null;
                        try {
                            myStartDate = sdf.parse(screenStartDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long trigger = myStartDate.getTime();
                        Intent intent = new Intent(AddAssessmentView.this, MyReceiver.class);
                        intent.putExtra("key", title.getText().toString() + " starts today!");
                        PendingIntent sender = PendingIntent.getBroadcast(AddAssessmentView.this, MainActivity.numAlert++, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                    }

                    if (shouldRemindEnd == true) {
                        String screenEndDate = endDate.getText().toString();
                        if (screenEndDate.equals("")) screenEndDate = "04/20/22";
                        Date myEndDate = null;
                        try {
                            myEndDate = sdf.parse(screenEndDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long trigger1 = myEndDate.getTime();
                        Intent intent1 = new Intent(AddAssessmentView.this, MyReceiver.class);
                        intent1.putExtra("key", title.getText().toString() + " is due today!");
                        PendingIntent sender1 = PendingIntent.getBroadcast(AddAssessmentView.this, MainActivity.numAlert++, intent1, 0);
                        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager1.set(AlarmManager.RTC_WAKEUP, trigger1, sender1);
                    }
                    Toast.makeText(this, title.getText().toString() + " was added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            case R.id.action_cancel:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_assessment_menu, menu);
        return true;
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
        }
        return true;
    }
}