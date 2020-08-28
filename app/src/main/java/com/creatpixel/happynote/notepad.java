package com.creatpixel.happynote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class notepad extends AppCompatActivity {
    String tag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        EditText editTextNote = findViewById(R.id.editTextNote);

        //Getting mainactivity message into 2nd activity using getIntent object and storing in string var
        Intent intent = getIntent();
        tag = intent.getStringExtra(MainActivity.MSG);

        //When open this activity get note data into view
        getData();
    }

    @Override
    public void onBackPressed() {
        saveData(tag);
        restart();

        Log.i("akki","Back pressed");
        super.onBackPressed();
    }

    @Override
    protected void onUserLeaveHint() {
        saveData(tag);

        Log.i("akki", "Home pressed");
        super.onUserLeaveHint();
    }

    //Save data, Note storage
    public void saveData(String tag){
        SharedPreferences noteData = getSharedPreferences("noteData", MODE_PRIVATE);
        SharedPreferences.Editor editor = noteData.edit();

        EditText editTextNote = findViewById(R.id.editTextNote);
        EditText editTextTitle = findViewById(R.id.titleText);
        String noteText = editTextNote.getText().toString();
        String titleText = editTextTitle.getText().toString();

        editor.putString(tag, noteText);
        editor.putString(tag+"title", titleText);
        editor.apply();
    }

    //GetData from storage
    public void getData(){
        SharedPreferences noteData = getSharedPreferences("noteData", MODE_PRIVATE);
        String note = noteData.getString(tag, "");
        String title = noteData.getString(tag+"title", "");

        EditText editTextNote = findViewById(R.id.editTextNote);
        EditText editTextTitle = findViewById(R.id.titleText);
        editTextNote.setText(note);
        editTextTitle.setText(title);
    }
    //For updating titles in start window
    public void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}