package com.example.iGraduate.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.example.iGraduate.R;
import com.example.iGraduate.UI.Main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManageAssessmentNoDeleteView extends AppCompatActivity {

    Switch manageAssessmentTypeSwitch;
    EditText manageAssessmentTitleText;
    EditText manageAssessmentStartDateText;
    EditText manageAssessmentEndDateText;
    CheckBox manageAssessmentStartReminderCheckbox;
    CheckBox manageAssessmentEndReminderCheckbox;

    DatePickerDialog.OnDateSetListener startingDate;
    DatePickerDialog.OnDateSetListener endingDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf;

    int assessmentId;
    String title;
    String startDate;
    String endDate;
    String type;
    Boolean shouldRemindStart = false;
    Boolean shouldRemindEnd = false;

    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_assessment_view);
        this.setTitle("Assessment Details");
        ActionBar actionBar = getSupportActionBar();

        repo = new Repository(getApplication());
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        manageAssessmentTypeSwitch = findViewById(R.id.manageAssessmentTypeSwitch);
        manageAssessmentTitleText = findViewById(R.id.manageAssessmentTitleText);
        manageAssessmentStartDateText = findViewById(R.id.manageAssessmentStartDateText);
        manageAssessmentEndDateText = findViewById(R.id.manageAssessmentEndDateText);
        manageAssessmentStartReminderCheckbox = findViewById(R.id.manageAssessmentStartReminderCheckbox);
        manageAssessmentEndReminderCheckbox = findViewById(R.id.manageAssessmentEndReminderCheckbox);

        type = (getIntent().getStringExtra("type"));

        System.out.println(type);

        if (type.equalsIgnoreCase("Performance")) {
            manageAssessmentTypeSwitch.setChecked(false);
        } else {
            manageAssessmentTypeSwitch.setChecked(true);
        }

        assessmentId = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        shouldRemindStart = getIntent().getBooleanExtra("shouldRemindStart", false);
        shouldRemindEnd = getIntent().getBooleanExtra("shouldRemindEnd", false);

        manageAssessmentTitleText.setText(title);
        manageAssessmentStartDateText.setText(startDate);
        manageAssessmentEndDateText.setText(endDate);
        manageAssessmentStartReminderCheckbox.setChecked(shouldRemindStart);
        manageAssessmentEndReminderCheckbox.setChecked(shouldRemindEnd);

        manageAssessmentStartDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = manageAssessmentStartDateText.getText().toString();
                if (info.equals("")) info = "04/20/22";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(com.example.iGraduate.UI.ManageAssessmentNoDeleteView.this, startingDate, myCalendarStart
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

        manageAssessmentEndDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = manageAssessmentEndDateText.getText().toString();
                if (info.equals("")) info = "04/20/22";
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(com.example.iGraduate.UI.ManageAssessmentNoDeleteView.this, endingDate, myCalendarEnd
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

    private void updateLabelStart() {
        manageAssessmentStartDateText.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        manageAssessmentEndDateText.setText(sdf.format(myCalendarEnd.getTime()));
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:

                if (validateInputs() == true) {
                    updateAssessment();

                    //REMINDERS
                    if (shouldRemindStart == true) {
                        String screenStartDate = manageAssessmentStartDateText.getText().toString();
                        System.out.println(screenStartDate);
                        if (screenStartDate.equals("")) screenStartDate = "04/20/22";
                        Date myStartDate = null;
                        try {
                            myStartDate = sdf.parse(screenStartDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long trigger = myStartDate.getTime();
                        Intent intent = new Intent(com.example.iGraduate.UI.ManageAssessmentNoDeleteView.this, MyReceiver.class);
                        intent.putExtra("key", manageAssessmentTitleText.getText().toString() + " starts today!");
                        PendingIntent sender = PendingIntent.getBroadcast(com.example.iGraduate.UI.ManageAssessmentNoDeleteView.this, MainActivity.numAlert++, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                    }

                    if (shouldRemindEnd == true) {
                        String screenEndDate = manageAssessmentEndDateText.getText().toString();
                        System.out.println(screenEndDate);
                        if (screenEndDate.equals("")) screenEndDate = "04/20/22";
                        Date myEndDate = null;
                        try {
                            myEndDate = sdf.parse(screenEndDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Long trigger1 = myEndDate.getTime();
                        Intent intent1 = new Intent(com.example.iGraduate.UI.ManageAssessmentNoDeleteView.this, MyReceiver.class);
                        intent1.putExtra("key", manageAssessmentTitleText.getText().toString() + " is due today!");
                        PendingIntent sender1 = PendingIntent.getBroadcast(com.example.iGraduate.UI.ManageAssessmentNoDeleteView.this, MainActivity.numAlert++, intent1, 0);
                        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager1.set(AlarmManager.RTC_WAKEUP, trigger1, sender1);
                    }

                    //Thread.sleep(1000);
                    Toast.makeText(this, manageAssessmentTitleText.getText().toString() + " was updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();

                }

                return true;
            case R.id.action_cancel:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public Boolean validateInputs() {

        Date startTestParse = null;
        Date endTestParse = null;
        try {
            startTestParse = sdf.parse(manageAssessmentStartDateText.getText().toString());
            endTestParse = sdf.parse(manageAssessmentEndDateText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        title = manageAssessmentTitleText.getText().toString();
        startDate = manageAssessmentStartDateText.getText().toString();
        endDate = manageAssessmentEndDateText.getText().toString();

        if (title.matches("") || startDate.matches("") || endDate.matches("")) {
            Toast.makeText(this, "Please ensure all fields have been filled out.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (startTestParse == null || endTestParse == null) {
            Toast.makeText(this, "Please ensure dates are in the following format (MM/dd/yy).", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!startTestParse.before(endTestParse) && !startTestParse.equals(endTestParse)) {
            Toast.makeText(this, "Please ensure the end date comes after the start date.", Toast.LENGTH_SHORT).show();            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_assessment_no_delete_menu, menu);
        return true;
    }

    public void handleTypeSwitch(View view) {
        if (manageAssessmentTypeSwitch.isChecked()) {
            type = "Objective";
        } else {
            type = "Performance";
        }
    }

    public void handleStartReminder(View view) {
        shouldRemindStart = !shouldRemindStart;
    }

    public void handleEndReminder(View view) {
        shouldRemindEnd = !shouldRemindEnd;
    }

    public void updateAssessment() {
        Assessment assessment = repo.findAssessment(assessmentId);
        assessment.setTitle(title);
        assessment.setStartDate(startDate);
        assessment.setEndDate(endDate);
        assessment.setType(type);
        assessment.setShouldRemindStart(shouldRemindStart);
        assessment.setShouldRemindEnd(shouldRemindEnd);
        repo.updateAssessment(assessment);
    }
}