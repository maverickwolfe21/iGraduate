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

//import com.example.iGraduate.Adapters.TermAdapter;
import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Term;
import com.example.iGraduate.R;
import com.example.iGraduate.UI.AddTermView;
import com.example.iGraduate.UI.DetailedTermView;
import com.example.iGraduate.UI.LoadingDialog;

import java.util.List;

public class TermsScreen extends AppCompatActivity {

    //private ArrayList<Term> terms = new ArrayList<Term>();
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        this.setTitle("Terms");

        Repository repo = new Repository(getApplication());

        List<Term> terms = repo.getAllTerms();

        ListAdapter termAdapter = new ArrayAdapter<Term>(this, android.R.layout.simple_list_item_1, terms);
        ListView termListView = (ListView) findViewById(R.id.termsListView);
        termListView.setAdapter(termAdapter);

        termListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
                        p.setVisibility(View.VISIBLE); // if not set it to visible

                        Term term = (Term) parent.getItemAtPosition(position);

                        Intent intent = new Intent(TermsScreen.this, DetailedTermView.class);
                        intent.putExtra("id", term.getTermId());
                        intent.putExtra("title", term.getTitle());
                        intent.putExtra("startDate", term.getStartDate());
                        intent.putExtra("endDate", term.getEndDate());
                        intent.putExtra("courseIds", term.getCourseIds());
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

    public void handleAddTerm(View view) {
        Intent intent = new Intent(TermsScreen.this, AddTermView.class);
        //putIntent
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

                List<Term> terms = repo.getAllTerms();

                ListAdapter termAdapter = new ArrayAdapter<Term>(this, android.R.layout.simple_list_item_1, terms);
                ListView termListView = (ListView) findViewById(R.id.termsListView);
                termListView.setAdapter(termAdapter);

                termListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Term term = (Term) parent.getItemAtPosition(position);
                                Intent intent = new Intent(TermsScreen.this, DetailedTermView.class);
                                intent.putExtra("id", term.getTermId());
                                intent.putExtra("title", term.getTitle());
                                intent.putExtra("startDate", term.getStartDate());
                                intent.putExtra("endDate", term.getEndDate());
                                intent.putExtra("courseIds", term.getCourseIds());
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