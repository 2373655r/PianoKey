package com.example.piano;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PianoActivity extends AppCompatActivity {

    public Button testButton;
    public PianoView pianoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testButton = (Button) findViewById(R.id.button);
        pianoView = (PianoView) findViewById(R.id.pianoView);
        testButton.setTag(1);
        testButton.setText("Record");
    }

    public void OnClickRecordButton(View view){
        final int status =(Integer) testButton.getTag();
        if(status == 1) {
            //Start Recording user input
            pianoView.soundPlayer.ResetPassword();
            testButton.setText("Stop");
            testButton.setTag(0); //pause
        } else {
            //Stop Recording user input
            testButton.setText("Record");
            testButton.setTag(1); //pause
            //Check password entered
            ValidatePassword(pianoView.soundPlayer.getPassword());
        }
    }

    public void ValidatePassword(String password){
        //May add more checks but for now just check if empty
        if(password == ""){
            System.out.println("No password entered");
        } else {
            System.out.println("Password: " + password);
        }
    }

}