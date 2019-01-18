package com.example.bernard.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvwNotes;
    static ArrayList<String> notesList;
    SharedPreferences sharedPreferences;
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getApplicationContext());
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                intent.putExtra("Note", "");
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvwNotes = (ListView) findViewById(R.id.lvwNotes);

        sharedPreferences = getSharedPreferences("com.example.bernard.notesapp", MODE_PRIVATE);
        try {
            String listString = sharedPreferences.getString("list", ObjectSerializer.serialize(new ArrayList<String>()));
            notesList = (ArrayList<String>) ObjectSerializer.deserialize(listString);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList);
            lvwNotes.setAdapter(arrayAdapter);

            sharedPreferences.edit().putString("list", ObjectSerializer.serialize(notesList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lvwNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // delete selected item
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete note")
                        .setMessage("Are you sure you want to delete the note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesList.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                try {
                                    sharedPreferences.edit().putString("list", ObjectSerializer.serialize(notesList)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        lvwNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                intent.putExtra("Note", notesList.get(position));
                intent.putExtra("Position", position); // 1 - call from listview, 2 - call from menu item
                startActivity(intent);
            }
        });

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
