package com.example.iGraduate.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddInstructorView extends AppCompatActivity {

    Repository repo;

    int courseId;
    ArrayList<Integer> instructorIds;

    EditText name;
    EditText email;
    EditText phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructor_view);

        repo = new Repository(getApplication());

        courseId = getIntent().getIntExtra("courseId", 0);
        instructorIds = (getIntent().getIntegerArrayListExtra("instructorIds"));

        name = findViewById(R.id.addInstructorNameText);
        email = findViewById(R.id.addInstructorEmailText);
        phone = findViewById(R.id.addInstructorPhoneText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_instructor_menu, menu);
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

                    String name = this.name.getText().toString();
                    String email = this.email.getText().toString();
                    String phone = this.phone.getText().toString();

                    int instructorId = -1;
                    List<Instructor> allInstructors = repo.getAllInstructors();
                    if (!allInstructors.isEmpty()) {
                        instructorId = allInstructors.size() + 1;
                    } else {
                        instructorId = 1;
                    }
                    Instructor instructor = new Instructor();
                    instructor.setInstructorId(instructorId);
                    instructor.setName(name);
                    instructor.setEmail(email);
                    instructor.setPhone(phone);

                    repo.insertInstructor(instructor);

                    if (courseId > 0) {
                        //add to list of course's instructors
                        instructorIds.add(instructor.getInstructorId());

                        Course course = repo.findCourse(courseId);
                        course.setInstructorIds(instructorIds);
                        repo.updateCourse(course);
                    }

                    Toast.makeText(this, name + " was added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean validateInputs() {

        if (name.getText().toString().matches("") || email.getText().toString().matches("") || phone.getText().toString().matches("")) {
            Toast.makeText(this, "Please ensure all fields are filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        String regex = "^(1-)?\\d{3}-\\d{3}-\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone.getText().toString());
        if (matcher.matches() == false) {
            Toast.makeText(this, "Please ensure phone number is in the correct format (XXX-XXX-XXXX).", Toast.LENGTH_SHORT).show();
            return false;
        }
        String emailAddress = email.getText().toString();
        String regexPattern = "^(.+)@(\\S+)$";
        Toast.makeText(this, "Please ensure email is in the correct format.", Toast.LENGTH_SHORT).show();
        return (patternMatches(emailAddress, regexPattern));
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}