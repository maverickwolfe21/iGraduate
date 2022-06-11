package com.example.iGraduate.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iGraduate.R;

public class LoadingDialog {
    private AppCompatActivity activity;
    private AlertDialog dialog;

    public LoadingDialog(AppCompatActivity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
