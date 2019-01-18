package com.example.bernard.notesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {

    private String note;
    private Integer position;
    EditText editText;

    @Override
    public void onBackPressed() {
        if (editText.getText().toString().isEmpty()) {
            super.onBackPressed();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.bernard.notesapp", MODE_PRIVATE);

        try {
            if (position == null) {
                MainActivity.notesList.add(editText.getText().toString());
            } else {
                MainActivity.notesList.set(position, editText.getText().toString());
            }

            MainActivity.arrayAdapter.notifyDataSetChanged();

            sharedPreferences.edit().putString("list", ObjectSerializer.serialize(MainActivity.notesList));

            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent i = getIntent();

        note = i.getExtras().getString("Note");
        if (i.getExtras().containsKey("Position"))
            position = i.getExtras().getInt("Position");

        editText = (EditText) findViewById(R.id.editText2);
        editText.setText(note);
    }
}
