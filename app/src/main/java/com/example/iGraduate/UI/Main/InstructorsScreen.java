package com.example.iGraduate.UI.Main;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.AddInstructorView;
import com.example.iGraduate.UI.AddTermView;
import com.example.iGraduate.UI.DetailedTermView;
import com.example.iGraduate.UI.LoadingDialog;
import com.example.iGraduate.UI.ManageInstructorView;
import com.example.iGraduate.UI.ManageInstructorWithDeleteView;

import java.util.List;

public class InstructorsScreen extends AppCompatActivity {

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructors_screen);

        this.setTitle("Instructors");

        Repository repo = new Repository(getApplication());

        List<Instructor> instructors = repo.getAllInstructors();

        ListAdapter instructorAdapter = new ArrayAdapter<Instructor>(this, android.R.layout.simple_list_item_1, instructors);
        ListView instructorListView = (ListView) findViewById(R.id.instructorsListView);
        instructorListView.setAdapter(instructorAdapter);

        instructorListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

/*                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible*/

                        Instructor instructor = (Instructor) parent.getItemAtPosition(position);

                        Intent intent = new Intent(InstructorsScreen.this, ManageInstructorWithDeleteView.class);
                        intent.putExtra("instructorId", instructor.getInstructorId());
                        intent.putExtra("name", instructor.getName());
                        intent.putExtra("email", instructor.getEmail());
                        intent.putExtra("phone", instructor.getPhone());
                        startActivity(intent);
                    }
                }
        );

    }

/*    @Override
    public void onResume() {
        // this is called by your activity before it gets visible to the user, when you leave this activity 2 and come back to
        //activity 1, because activity 1 wasn't killed it is resumed from the backstack and this function is called
        // TODO Auto-generated method stub
        super.onResume();
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
        p.setVisibility(View.GONE);
    }*/

    public void handleAddInstructor(View view) {
        Intent intent = new Intent(InstructorsScreen.this, AddInstructorView.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.courses_menu, menu);
        return true;
    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();

                Repository repo = new Repository(getApplication());

                List<Instructor> instructors = repo.getAllInstructors();

                ListAdapter instructorAdapter = new ArrayAdapter<Instructor>(this, android.R.layout.simple_list_item_1, instructors);
                ListView instructorListView = (ListView) findViewById(R.id.instructorsListView);
                instructorListView.setAdapter(instructorAdapter);

                instructorListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

/*                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible*/

                                Instructor instructor = (Instructor) parent.getItemAtPosition(position);

                                Intent intent = new Intent(InstructorsScreen.this, ManageInstructorWithDeleteView.class);
                                intent.putExtra("instructorId", instructor.getInstructorId());
                                intent.putExtra("name", instructor.getName());
                                intent.putExtra("email", instructor.getEmail());
                                intent.putExtra("phone", instructor.getPhone());
                                startActivity(intent);
                            }
                        }
                );
                Toast.makeText(this, "Page refreshed successfully!", Toast.LENGTH_SHORT).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}