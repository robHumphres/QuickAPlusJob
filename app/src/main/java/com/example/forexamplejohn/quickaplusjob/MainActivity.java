package com.example.forexamplejohn.quickaplusjob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button clearDataButton, sendEmailButton,startTimeButton, stopTimeButton;
    private TextView startTimeText, stopTimeText, jobName, jobNotes;
    private static final String
            PREFS_NAME = "MEMORY",
            jobNameString = "jobName",
            startTimeString = "startTime",
            stopTimeString = "stopTime",
            jobNoteString = "jobNote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clearDataButton = findViewById(R.id.button_clearData);
        clearDataButton.setOnClickListener(this);

        sendEmailButton = findViewById(R.id.button_sendEmail);
        sendEmailButton.setOnClickListener(this);

        startTimeButton = findViewById(R.id.button_startTimeButton);
        startTimeButton.setOnClickListener(this);

        stopTimeButton = findViewById(R.id.button_stopTimeButton);
        stopTimeButton.setOnClickListener(this);

        //Textviews
        startTimeText = findViewById(R.id.textView_startTime);
        stopTimeText = findViewById(R.id.textView_stopTime);
        jobName = findViewById(R.id.editText_jobName);
        jobNotes = findViewById(R.id.editText_jobNotes);

        tryFillingData();

    }

    private void tryFillingData(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        String temp;


        if(settings.getString("jobName",null)!= null){
            temp = settings.getString(jobNameString,"blank");
            jobName.setText(temp);
        }

        if(settings.getString(startTimeString,null)!= null){
            temp = settings.getString(startTimeString,null);
            startTimeText.setText(temp);

        }

        if(settings.getString(stopTimeString,null)!= null){
            temp = settings.getString(stopTimeString,null);
            stopTimeText.setText(temp);

        }

        if(settings.getString(jobNoteString,null)!= null){
            temp = settings.getString(jobNoteString,null);
            jobNotes.setText(temp);

        }

    }//end of tryingfilldata

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setData();
//    }

    @Override
    public void onClick(View view) {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        switch (view.getId()){
            case R.id.button_clearData:
                clearData();
                break;
            case R.id.button_sendEmail:
                sendEmail();
                break;
            case R.id.button_startTimeButton:
                startTimeText.setText(date);
                break;
            case R.id.button_stopTimeButton:
                stopTimeText.setText(date);
                break;
        }//end of switch

        //Saves to Preferences after any click
        setData();
    }

    public void sendEmail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"robhumphres@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Quick Job " + jobName.getText());
        i.putExtra(Intent.EXTRA_TEXT   ,
                  "Hours of job: \n" +
                        "Start Time: " + startTimeText.getText() +
                        "\nStop Time: " + stopTimeText.getText() +
                        "\n\nNotes from the job " + jobNotes.getText())
        ;
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setData(){
        setPreferenceData(jobName.getText(),startTimeText.getText(),stopTimeText.getText(),jobNotes.getText());
        Toast.makeText(this, "Save Data", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        tryFillingData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setData();
    }

    public void setPreferenceData(CharSequence s, CharSequence s2, CharSequence s3, CharSequence s4){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(jobNameString, s.toString());
        editor.putString(startTimeString,s2.toString());
        editor.putString(stopTimeString,s3.toString());
        editor.putString(jobNoteString,s4.toString());
        editor.commit();

//        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
//        String temp;



    }

    public void clearData(){
        startTimeText.setText("Start Time");
        stopTimeText.setText("Stop Time");
        jobNotes.setText("");
        jobName.setText("");

        setPreferenceData("","","","");
    }
}
