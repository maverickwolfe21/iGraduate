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
import com.example.iGraduate.Entity.Course;
import com.example.iGraduate.Entity.Instructor;
import com.example.iGraduate.Entity.Term;

import java.util.ArrayList;
import java.util.List;

public class DeleteInstructorDialog extends AppCompatDialogFragment {

    private int instructorId;
    private String name;
    private Repository repo;
    private Instructor instructor;

    AppCompatActivity activity;

    public DeleteInstructorDialog(int instructorId, String name, Repository repo, AppCompatActivity activity) {
        this.instructorId = instructorId;
        this.name = name;
        this.repo = repo;

        this.instructor = repo.findInstructor(instructorId);
        this.activity = activity;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this instructor?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //remove from courses

                        List<Course> courses = repo.getAllCourses();

                        for (int c = 0; c < courses.size(); c++) {
                            Course currentCourse = courses.get(c);
                            ArrayList<Integer> instructorIds = currentCourse.getInstructorIds();
                            for (int n = 0; n < instructorIds.size(); n++) {
                                int courseInstructorId = Integer.parseInt(String.valueOf(instructorIds.get(n)));
                                if (courseInstructorId == instructorId) {
                                    //System.out.println("match!");
                                    int index = n;
                                    instructorIds.remove(index);
                                    currentCourse.setInstructorIds(instructorIds);
                                    repo.updateCourse(currentCourse);
                                }
                            }

                        }

                        repo.deleteInstructor(instructorId);
                        Toast.makeText(getContext(), name + " was successfully deleted!", Toast.LENGTH_LONG).show();
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
