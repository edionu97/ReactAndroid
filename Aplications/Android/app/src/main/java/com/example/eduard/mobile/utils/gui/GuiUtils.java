package com.example.eduard.mobile.utils.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.repository.room.entity.Contact;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GuiUtils {

    /**
     * Display an error dialog
     *
     * @param message: the message displayed in dialog
     * @param from:    the activity that is calling the dialog
     * @param title:   the title of the dialog
     */
    public static void displayErrorDialog(String message, Context from, String title) {

        AlertDialog.Builder builder = new AlertDialog
                .Builder(from)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.show();
    }

    public static void displayQuestionDialog(
            String message,
            Context from,
            String title,
            Runnable consumer) {

        AlertDialog.Builder builder = new AlertDialog
                .Builder(from)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Yes", (dialog, which) -> consumer.run());

        AlertDialog dialog = builder.create();

        dialog.setTitle(title);
        dialog.show();
    }

    public static void displayContentDialog(
            Context from,
            String title,
            BiConsumer<BaseAdapter, Integer> selected,
            BaseAdapter adapter) {

        AlertDialog.Builder builder = new AlertDialog
                .Builder(from)
                .setNegativeButton("CANCEL", ((dialog, which) -> dialog.dismiss()));

        builder.setSingleChoiceItems(
                adapter,
                0,
                (dialog, which) -> {
                    selected.accept(
                            adapter,
                            which
                    );
                    dialog.dismiss();
                }
        );

        AlertDialog dialog = builder.create();

        dialog.setTitle(title);
        dialog.show();
    }


    public static void showSnackBar(View view, String text){

        if(view == null){
            return;
        }

        Snackbar.make(
                view,
                text,
                Snackbar.LENGTH_LONG
        ).show();
    }
}
