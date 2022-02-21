package com.example.piano;

public class KeyEvent {

    public int note;
    public float time;
    public boolean press;

    public KeyEvent(int note, float time, boolean press){
        this.note = note;
        this.time  = time;
        this.press = press;
    }
}
