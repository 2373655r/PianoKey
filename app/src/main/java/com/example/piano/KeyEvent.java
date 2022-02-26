package com.example.piano;

public class KeyEvent{

    public int note;
    public long time;
    public boolean press;

    public KeyEvent(int note, long time, boolean press) {
        this.note = note;
        this.time = time;
        this.press = press;
    }
}
