package com.example.piano;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class PianoActivity extends AppCompatActivity {

    public Button recordButton;
    public Button createButton;
    public PianoView pianoView;

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
        String password = EventsToString(events);
        if(password != null){
            if(password != ""){
                hiddenPassword = password;
            }
        }
    }

    private String EventsToString(ArrayList<KeyEvent> events){
        String curPassword = "";
        //Gonna need to add support for multi key press and timing
        for(KeyEvent e : events){
            curPassword += e.sound;
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
                Log.d("Login",event.sound + "");
            }

            //Compare entered password to hidden password
            if(hiddenPassword == null){
                Log.d("Login","No hidden password");
            } else{
                Log.d("True Pass:",hiddenPassword);
                Log.d("Given Pass:",enteredPassword);
                if(hiddenPassword.equals(enteredPassword)){
                    Log.d("Login", "Login Success");
                } else {
                    Log.d("Login", "Login Failure");
                }
            }
        }
    }

}