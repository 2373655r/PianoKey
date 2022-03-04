package com.example.piano;

import android.graphics.Color;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.Buffer;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

public class PianoActivity extends AppCompatActivity {

    public Button recordButton;
    public Button createButton;
    public PianoView pianoView;

    public TextView success;

    String hiddenPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pianoView = (PianoView) findViewById(R.id.pianoView);

        recordButton= (Button) findViewById(R.id.button);
        recordButton.setTag(1);
        recordButton.setText("Record");

        createButton= (Button) findViewById(R.id.button3);
        createButton.setTag(1);
        createButton.setText("Create");

        success= (TextView) findViewById(R.id.textView2);
        success.setText("Success");
        success.setTextColor(Color.BLUE);
        success.setVisibility(View.INVISIBLE);
    }

    public void OnClickRecordButton(View view){
        final int status =(Integer) recordButton.getTag();
        if(status == 1) {
            //Start Recording user input
            pianoView.ResetKeyEvents();
            recordButton.setText("Submit");
            recordButton.setTag(0); //pause

            //Disable create button
            createButton.setEnabled(false);
        } else {
            //Stop Recording user input
            recordButton.setText("Record");
            recordButton.setTag(1); //pause

            //Check password entered
            ValidatePassword(pianoView.GetKeyEvents());

            //Re-enable create button
            createButton.setEnabled(true);
        }
    }

    public void OnClickCreateButton(View view){
        final int status =(Integer) createButton.getTag();
        if(status == 1) {
            //Start Recording user input
            pianoView.ResetKeyEvents();
            createButton.setText("Save");
            createButton.setTag(0); //pause

            //Disable record button
            recordButton.setEnabled(false);
        } else {
            //Stop Recording user input
            createButton.setText("Create");
            createButton.setTag(1); //pause

            //Save password entered
            SavePassword(pianoView.GetKeyEvents());

            //Re-enable record button
            recordButton.setEnabled(true);
        }
    }

    private void SavePassword(ArrayList<KeyEvent> events){
        //String password = EventsToString(events);
        String password = "";
        if(password != null){
            if(password != ""){
                hiddenPassword = password;
                Log.d("Hidden Pass","" + hiddenPassword);
                DisplayMessage("Password Saved");
                return;
            }
        }
        //Something wrong with password
        Log.d("Hidden Pass","Invalid Password");
        DisplayMessage("Invalid Password");
    }

    private String EventsToString(ArrayList<KeyEvent> events){
        String curPassword = "";

        //Utils class handles turning events into chords
        for (Chord chord : Utils.normalise(events)) {
            curPassword += chord.toString() + "|";
        }
        return curPassword;
    }

    private void ValidatePassword(ArrayList<KeyEvent> events){
        //May add more checks but for now just check if empty
        if(events.size() == 0){
            Log.d("Login","No password entered");
        } else {
            String enteredPassword = EventsToString(events);

            //Log keys pressed
            Log.d("Login","Password:");
            for(KeyEvent event : events){
                Log.d("Login",event.note + "");
            }

            //Compare entered password to hidden password
            if(hiddenPassword == null){
                Log.d("Login","No hidden password");
            } else{
                Log.d("True Pass:",hiddenPassword);
                Log.d("Given Pass:",enteredPassword);
                if(hiddenPassword.equals(enteredPassword)){
                    Log.d("Login", "Login Success");
                    DisplayMessage("Success");
                    return;
                } else {
                    Log.d("Login", "Login Failure");
                }
            }
        }
        DisplayMessage("Wrong Password");
    }


    //Flashed text s on screen then hides it again
    public void DisplayMessage(String s){

        if(s == ""){
            return;
        }

        //Show user success
        success.setText(s);
        success.setVisibility(View.VISIBLE);

        //Timer to hide success text
        CountDownTimer timer = new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                success.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

}