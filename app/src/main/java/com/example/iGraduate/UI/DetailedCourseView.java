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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.Main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailedCourseView extends AppCompatActivity {

    EditText title;
    EditText startDate;
    EditText endDate;
    EditText status;
    EditText notes;
    CheckBox remindStart;
    CheckBox remindEnd;

    Repository repo;

    DatePickerDialog.OnDateSetListener startingDate;
    DatePickerDialog.OnDateSetListener endingDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf;

    int courseId;
    String titl;
    String start;
    String end;
    String stat;
    String note;
    ArrayList<Integer> instructorIds;
    ArrayList<Integer> assessmentIds;

    Boolean shouldRemindStart = false;
    Boolean shouldRemindEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_course_view);
        ActionBar actionBar = getSupportActionBar();

        repo = new Repository(getApplication());
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        title = findViewById(R.id.detailedCourseTitleEditText);
        startDate = findViewById(R.id.detailedCourseStartDateEditText);
        endDate = findViewById(R.id.detailedCourseEndDateEditText);
        status = findViewById(R.id.detailedCourseStatusEditText);
        notes = findViewById(R.id.detailedCourseNotesEditText);
        remindStart = findViewById(R.id.detailedCourseStartReminderCheckbox);
        remindEnd = findViewById(R.id.detailedCourseEndReminderCheckbox);

        courseId = getIntent().getIntExtra("id", 0);
        titl = getIntent().getStringExtra("title");
        assessmentIds = (getIntent().getIntegerArrayListExtra("assessmentIds"));
        instructorIds = (getIntent().getIntegerArrayListExtra("instructorIds"));

        this.setTitle("Course Details");

        title.setText(titl);
        startDate.setText(getIntent().getStringExtra("startDate"));
        endDate.setText(getIntent().getStringExtra("endDate"));
        status.setText(getIntent().getStringExtra("status"));
        notes.setText(getIntent().getStringExtra("notes"));
        remindStart.setChecked(getIntent().getBooleanExtra("shouldRemindStart", false));
        remindEnd.setChecked(getIntent().getBooleanExtra("shouldRemindEnd", false));

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
                new DatePickerDialog(DetailedCourseView.this, startingDate, myCalendarStart
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
                new DatePickerDialog(DetailedCourseView.this, endingDate, myCalendarEnd
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

        ArrayList<Integer> instructorIds = (getIntent().getIntegerArrayListExtra("instructorIds"));
        ArrayList<Instructor> instructors = new ArrayList<>();

        //INSTRUCTORS
        for (int i = 0; i < instructorIds.size(); i++) {
            String instructorId = String.valueOf(instructorIds.get(i));
            instructors.add(repo.findInstructor(Integer.parseInt(instructorId)));
        }

        ArrayList<Integer> assessmentIds = (getIntent().getIntegerArrayListExtra("assessmentIds"));
        ArrayList<Assessment> assessments = new ArrayList<>();

        //ASSESSMENTS
        for (int i = 0; i < assessmentIds.size(); i++) {
            String assessmentId = String.valueOf(assessmentIds.get(i));
            assessments.add(repo.findAssessment(Integer.parseInt(assessmentId)));
        }

        ListAdapter instructorAdapter = new ArrayAdapter<Instructor>(this, android.R.layout.simple_list_item_1, instructors);
        ListView instructorListView = (ListView) findViewById(R.id.detailedCourseDetailedInstructorsListView);
        instructorListView.setAdapter(instructorAdapter);

        setListViewHeightBasedOnChildren(instructorListView);

        instructorListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

/*                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible*/

                        Instructor instructor = (Instructor) parent.getItemAtPosition(position);
                        // use putIntent and getIntent
                        Intent intent = new Intent(DetailedCourseView.this, ManageInstructorView.class);
                        intent.putExtra("instructorId", instructor.getInstructorId());
                        intent.putExtra("name", instructor.getName());
                        intent.putExtra("email", instructor.getEmail());
                        intent.putExtra("phone", instructor.getPhone());
                        startActivity(intent);
                    }
                }
        );

        ListAdapter assessmentAdapter = new ArrayAdapter<Assessment>(this, android.R.layout.simple_list_item_1, assessments);
        ListView assessmentListView = (ListView) findViewById(R.id.detailedCourseDetailedAssessmentsListView);
        assessmentListView.setAdapter(assessmentAdapter);

        setListViewHeightBasedOnChildren(assessmentListView);

        assessmentListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible

                        Assessment assessment = (Assessment) parent.getItemAtPosition(position);
                        // use putIntent and getIntent
                        Intent intent = new Intent(DetailedCourseView.this, ManageAssessmentNoDeleteView.class);
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
    public void onResume() {
        // this is called by your activity before it gets visible to the user, when you leave this activity 2 and come back to
        //activity 1, because activity 1 wasn't killed it is resumed from the backstack and this function is called
        // TODO Auto-generated method stub
        super.onResume();
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
        p.setVisibility(View.GONE);
    }

    public void handleEditAssessments(View view) {
        Intent intent = new Intent(DetailedCourseView.this, EditAssessmentsView.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("assessmentIds", assessmentIds);
        startActivity(intent);
    }

    public void handleAddAssessment(View view) {
        Intent intent = new Intent(DetailedCourseView.this, AddAssessmentView.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("assessmentIds", assessmentIds);
        startActivity(intent);
    }

    public void handleEditInstructors(View view) {
        Intent intent = new Intent(DetailedCourseView.this, EditInstructorsView.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("instructorIds", instructorIds);
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
        getMenuInflater().inflate(R.menu.detailed_course_menu, menu);
        return true;
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Course course = repo.findCourse(courseId);

        //handle presses on the action bar items
        switch (item.getItemId()) {

/*            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;*/
            case R.id.action_refresh:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();

                instructorIds = course.getInstructorIds();
                ArrayList<Instructor> instructors = new ArrayList<>();

                //INSTRUCTORS
                for (int i = 0; i < instructorIds.size(); i++) {
                    if (repo.findInstructor(Integer.parseInt(String.valueOf(instructorIds.get(i)))) != null) {

                        instructors.add(repo.findInstructor(Integer.parseInt(String.valueOf(instructorIds.get(i)))));
                    } else {
                        instructorIds.remove(instructorIds.get(i));
                    }
                }

                assessmentIds = course.getAssessmentIds();
                ArrayList<Assessment> assessments = new ArrayList<>();

                //ASSESSMENTS
                for (int i = 0; i < assessmentIds.size(); i++) {
                    if (repo.findAssessment(Integer.parseInt(String.valueOf(assessmentIds.get(i)))) != null) {
                        assessments.add(repo.findAssessment(Integer.parseInt(String.valueOf(assessmentIds.get(i)))));
                    } else {
                        assessmentIds.remove(assessmentIds.get(i));
                    }

                }

                ListAdapter instructorAdapter = new ArrayAdapter<Instructor>(this, android.R.layout.simple_list_item_1, instructors);
                ListView instructorListView = (ListView) findViewById(R.id.detailedCourseDetailedInstructorsListView);
                instructorListView.setAdapter(instructorAdapter);

                setListViewHeightBasedOnChildren(instructorListView);

                ListAdapter assessmentAdapter = new ArrayAdapter<Assessment>(this, android.R.layout.simple_list_item_1, assessments);
                ListView assessmentListView = (ListView) findViewById(R.id.detailedCourseDetailedAssessmentsListView);
                assessmentListView.setAdapter(assessmentAdapter);

                setListViewHeightBasedOnChildren(assessmentListView);

                assessmentListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Assessment assessment = (Assessment) parent.getItemAtPosition(position);
                                // use putIntent and getIntent
                                Intent intent = new Intent(DetailedCourseView.this, ManageAssessmentNoDeleteView.class);
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

            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, notes.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.action_save:

                if (validateInputs() == true) {


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
                        Intent intent = new Intent(DetailedCourseView.this, MyReceiver.class);
                        intent.putExtra("key", title.getText().toString() + " starts today!");
                        PendingIntent sender = PendingIntent.getBroadcast(DetailedCourseView.this, MainActivity.numAlert++, intent, 0);
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
                        Intent intent = new Intent(DetailedCourseView.this, MyReceiver.class);
                        intent.putExtra("key", title.getText().toString() + " ends today!");
                        PendingIntent sender = PendingIntent.getBroadcast(DetailedCourseView.this, MainActivity.numAlert++, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                    }

                    course.setTitle(title.getText().toString());
                    course.setStartDate(startDate.getText().toString());
                    course.setEndDate(endDate.getText().toString());
                    course.setStatus(status.getText().toString());
                    course.setNotes(notes.getText().toString());
                    course.setInstructorIds(instructorIds);
                    course.setAssessmentIds(assessmentIds);
                    course.setShouldRemindStart(shouldRemindStart);
                    course.setShouldRemindEnd(shouldRemindEnd);
                    repo.updateCourse(course);

                    Toast.makeText(this, title.getText().toString() + " was updated successfully!", Toast.LENGTH_SHORT).show();

                    finish();
                }
                return true;
            case R.id.action_delete:
                showDeleteDialog();
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
            Toast.makeText(this, "Please ensure all fields have been filled out (notes are optional).", Toast.LENGTH_SHORT).show();
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

    public void handleStartReminder(View view) {
        shouldRemindStart = !shouldRemindStart;
    }

    public void handleEndReminder(View view) {
        shouldRemindEnd = !shouldRemindEnd;
    }

    public void showDeleteDialog() {
        DeleteCourseDialog deleteDialog = new DeleteCourseDialog(courseId, titl, repo, this);
        deleteDialog.show(getSupportFragmentManager(), "deleteDialog");
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