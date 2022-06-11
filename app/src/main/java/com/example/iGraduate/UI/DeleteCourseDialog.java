package com.example.iGraduate.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.iGraduate.Database.Repository;
import com.example.iGraduate.Entity.Term;

import java.util.ArrayList;
import java.util.List;

public class DeleteCourseDialog extends AppCompatDialogFragment {

    int courseId;
    String title;
    Repository repo;
    AppCompatActivity activity;

    public DeleteCourseDialog(int courseId, String title, Repository repo, AppCompatActivity activity) {
        this.courseId = courseId;
        this.title = title;
        this.repo = repo;
        this.activity = activity;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this course?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //remove courseId from term's courseIds before deleting

                        List<Term> terms = repo.getAllTerms();

                        for (int t = 0; t < terms.size(); t++) {
                            Term term = terms.get(t);
                            ArrayList<Integer> coursesIds = terms.get(t).getCourseIds();
                            for (int c = 0; c < coursesIds.size(); c++) {

                                int termCourseId = Integer.parseInt(String.valueOf(coursesIds.get(c)));
                                if (termCourseId == courseId) {
                                    int index = c;
                                    coursesIds.remove(index);
                                    term.setCourseIds(coursesIds);
                                    repo.updateTerm(term);
                                }
                            }

                        }

                        repo.deleteCourse(courseId);
                        Toast.makeText(getContext(), title + " was successfully deleted!", Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

}
