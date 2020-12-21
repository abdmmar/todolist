package com.abdmmar.todo_list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class EditDialog extends DialogFragment {
    private String text;
    private int id;
    private String editedText;
    private EditedTextListener listener;
    private EditText et_edit_todo;

    public EditDialog(String text, int id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_dialog, null);
        et_edit_todo = view.findViewById(R.id.et_edit_todo);
        et_edit_todo.setText(text);

        builder.setView(view)
                .setTitle("Edit Todo")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editedText = et_edit_todo.getText().toString();
                        listener.applyTexts(editedText, id);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditDialog.this.getDialog().cancel();
                    }
                });
        return  builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditedTextListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement EditTextListener");
        }
    }
    public interface EditedTextListener {
        void applyTexts(String editedText, int id);
    }
}
