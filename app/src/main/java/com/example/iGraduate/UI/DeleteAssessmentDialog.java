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
import com.example.iGraduate.Entity.Assessment;
import com.example.iGraduate.Entity.Course;

import java.util.ArrayList;
import java.util.List;

public class DeleteAssessmentDialog extends AppCompatDialogFragment {


    private int assessmentId;
    private String title;
    private Repository repo;
    private Assessment assessment;

    public static Boolean isOpen = true;
    public static Boolean shouldClose = false;

    AppCompatActivity activity;

    public DeleteAssessmentDialog(int assessmentId, String title, Repository repo, AppCompatActivity activity) {
        this.assessmentId = assessmentId;
        this.title = title;
        this.repo = repo;

        this.assessment = repo.findAssessment(assessmentId);
        this.activity = activity;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this assessment?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        List<Course> courses = repo.getAllCourses();

                        for (int c = 0; c < courses.size(); c++) {
                            Course currentCourse = courses.get(c);
                            ArrayList<Integer> assessmentIds = courses.get(c).getAssessmentIds();
                            for (int a = 0; a < assessmentIds.size(); a++) {

                                int courseAssessmentId = Integer.parseInt(String.valueOf(assessmentIds.get(a)));

                                if (courseAssessmentId == assessmentId) {

                                    int index = a;
                                    assessmentIds.remove(index);
                                    currentCourse.setAssessmentIds(assessmentIds);
                                    repo.updateCourse(currentCourse);
                                }
                            }

                        }

                        repo.deleteAssessment(assessmentId);
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

