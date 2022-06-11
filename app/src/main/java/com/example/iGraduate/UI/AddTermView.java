package com.example.iGraduate.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTermView extends AppCompatActivity {

    EditText titleText;
    EditText startDateText;
    EditText endDateText;

    String title;
    String startDate;
    String endDate;

    Repository repo;

    DatePickerDialog.OnDateSetListener startingDate;
    DatePickerDialog.OnDateSetListener endingDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term_view);

        this.setTitle("Add Term");
        ActionBar actionBar = getSupportActionBar();

        repo = new Repository(getApplication());
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        titleText = findViewById(R.id.editAddTermTitle);
        startDateText = findViewById(R.id.editAddTermStartDate);
        endDateText = findViewById(R.id.editAddTermEndDate);

        startDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = startDateText.getText().toString();
                if (info.equals("")) info = "04/19/22";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddTermView.this, startingDate, myCalendarStart
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

        endDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = endDateText.getText().toString();
                if (info.equals("")) info = "04/19/22";
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddTermView.this, endingDate, myCalendarEnd
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
        startDateText.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        endDateText.setText(sdf.format(myCalendarEnd.getTime()));
    }


    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                if (validateInputs() == true) {

                    ArrayList<Integer> courseIds = new ArrayList<>();
                    Term term = new Term();
                    term.setTitle(titleText.getText().toString());
                    term.setStartDate(startDateText.getText().toString());
                    term.setEndDate(endDateText.getText().toString());
                    term.setCourseIds(courseIds);
                    repo.insertTerm(term);
                    finish();
                    Toast.makeText(this, titleText.getText().toString() + " was added successfully!", Toast.LENGTH_SHORT).show();
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
            startTestParse = sdf.parse(startDateText.getText().toString());
            endTestParse = sdf.parse(endDateText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (titleText.getText().toString().matches("") || startDateText.getText().toString().matches("") || endDateText.getText().toString().matches("")) {
            Toast.makeText(this, "Please ensure all fields are filled out.", Toast.LENGTH_SHORT).show();
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
}