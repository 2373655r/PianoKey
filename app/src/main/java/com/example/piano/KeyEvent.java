package com.example.piano;

public class KeyEvent {

    public int sound;
    public float Time;
    public boolean press;

    public KeyEvent(int sound, float downTime, boolean press){
        this.sound =sound;
        this.Time = downTime;
        this.press = true;
    }
}
