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

public class DeleteTermDialog extends AppCompatDialogFragment {

    private int termId;
    private String title;
    private Repository repo;
    private Term term;

    public static Boolean isOpen = true;
    public static Boolean shouldClose = false;

    AppCompatActivity activity;

    public DeleteTermDialog(int termId, String title, Repository repo, AppCompatActivity activity) {
        this.termId = termId;
        this.title = title;
        this.repo = repo;

        this.term = repo.findTerm(termId);
        this.activity = activity;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this term?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //System.out.println("Course ID " + courseId);
                        if (!term.getCourseIds().isEmpty()) {
                            Toast.makeText(getContext(), "Please remove all associated courses before attempting to delete a term.", Toast.LENGTH_LONG).show();
                        } else {
                            repo.deleteTerm(termId);
                            Toast.makeText(getContext(), title + " was successfully deleted!", Toast.LENGTH_LONG).show();
                            activity.finish();
                        }
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
