package com.creatpixel.happynote;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //Key for intent
    public static final String MSG = "com.creatpixel.happynote.MSG";
    int tag = 0;
    int counting = 0;

    public void whenOpen(){
        getCounting();
        for(int i = 0; i < counting; i++){
            noteTitleBlock();
        }

        Toast.makeText(this, "Open", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        whenOpen();
    }
    //Creating menu when create app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //When call this we open note activity for writing notes
    public void getInTheNote(View view, String tag){
        //Intent works //Adding class in intent
        Intent intent = new Intent(this, notepad.class);

        intent.putExtra(MSG, tag);
        //Start activity, Calling intent because he helps us to connect component each others
        startActivity(intent);
    }

    //When we click on ad note in menu we call this for adding new note
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addnote:
                counting++;
                noteTitleBlock();
                save();

                Log.i("akki", "getCounting value: " + counting);
                Toast.makeText(this, "Note: " + tag, Toast.LENGTH_SHORT).show();

            break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Create notesTitleBlocks
    @SuppressLint("SetTextI18n")
    public void noteTitleBlock(){
        tag++;
        //This is a layout where we are creating our new notes
        LinearLayout layout = findViewById(R.id.backround);
        //TypeFace class for fonts setting
        Typeface typeface = ResourcesCompat.getFont(this, R.font.alataregular);

        //Setting Up textview #####################################################################
        TextView note = new TextView(MainActivity.this);
        note.setText("Note "+ tag);
        //note.setBackgroundColor(Color.rgb(92, 107, 115));
        note.setTextSize(26);
        note.setTextColor(Color.rgb(224, 251, 252));
        note.setTag(tag);
        note.setPadding(0,5,0,5);
        note.setTypeface(typeface);
        note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                alert(view);
                Log.i("akki", "Long clicked");
                return false;
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check current tag of note
                String noteTag = view.getTag().toString();
                //When click on note title, We get in the note means 2nd Activity
                getInTheNote(view, noteTag);
                Toast.makeText(MainActivity.this, "Note number " + noteTag, Toast.LENGTH_SHORT).show();
            }
        });
        //Setting Up textview #####################################################################
        layout.addView(note);

        getTitleData();
    }

    public void save(){
        SharedPreferences countingStorage = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = countingStorage.edit();

        String countingString = Integer.toString(counting);
        editor.putString("Counting", countingString);

        Log.i("akki", "saved counting " + counting);
        editor.apply();
    }

    public void getCounting(){
        SharedPreferences countingStorage = getSharedPreferences("save", MODE_PRIVATE);
        int nCounting = Integer.parseInt((Objects.requireNonNull(countingStorage.getString("Counting", "404"))));

        //#################################################################################
        if(nCounting != 404){
            counting = nCounting;

            Log.i("akki", "No error in getCounting");
        }
        else{
            Log.i("akki", "404 Error");
        }
    }

    public void alert(final View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you really want to delete note")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(view);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void delete(View view){
        TextView noteTitle = (TextView) view;
        //Delete TextView
        noteTitle.setVisibility(View.GONE);
        //Minus tag
        tag--;
        //Minus counting from storage
        counting--;
        save();
        //Delete note data according to tag
        SharedPreferences noteData = getSharedPreferences("noteData", MODE_PRIVATE);
        SharedPreferences.Editor editor = noteData.edit();
        String tag = noteTitle.getTag().toString();
        editor.putString(tag, "");
        editor.putString(tag+"title","");
        editor.apply();

        restart();
        Log.i("akki", "Deleted!");
    }

    public void restart(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public void getTitleData(){
        SharedPreferences noteData = getSharedPreferences("noteData", MODE_PRIVATE);

        LinearLayout ll = findViewById(R.id.backround);
        TextView textView;
        int childCount = ll.getChildCount();

        for(int i = 0; i < childCount; i++){
            textView = (TextView) ll.getChildAt(i);
            int tagValue = Integer.parseInt(textView.getTag().toString());

            String titleTxt = noteData.getString(tagValue+"title", "Note "+ tagValue);
            assert titleTxt != null;
            if(!titleTxt.isEmpty()){
                textView.setText(titleTxt);
            }
            else{
                textView.setText("Note "+ tagValue);
            }
        }
    }
//    @SuppressLint("SetTextI18n")
//    public void shiftNotes(View view){
//        LinearLayout ll = findViewById(R.id.backround);
//        TextView v;
//
//        int deletedTag = Integer.parseInt(view.getTag().toString());
//        int count = counting+1;
//        int loop = 0;
//
//        for(int i = 0; i < count; i++){
//            loop++;
//            v = (TextView)ll.getChildAt(i);
//            v.setVisibility(View.GONE);
//
//            Log.i("loop", "Loop "+ loop);
//        }
////        counting = 0;
////        tag = 0;
//        //whenOpen();
//        Log.i("akki", "deleted tag "+ deletedTag);
//    }
}