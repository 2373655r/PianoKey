package com.example.piano;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class KeyDuration {
    int   note;
    float start;
    float end;
    float midpoint;
    float duration;

    KeyDuration(int note, float start, float end) {
        this.note     = note;
        this.start    = start;
        this.end      = end;
        this.duration = end - start;
        this.midpoint = this.start + (this.duration/2);
    }
}

class Chord {
    boolean notes[];
    Chord() {
        this.notes = new boolean[25];
    }
    void setNote(int note) {
        this.notes[note] = true;
    }
    void unsetNote(int note) {
        this.notes[note] = false;
    }

    public String toString() {
        int n = 0;
        for (boolean note : this.notes) {
            n = (n << 1) + (note ? 1 : 0);
        }
        return Integer.toString(n);
    }
}
public class Utils {

    public static float getOverlap(KeyDuration a, KeyDuration b) {
        float total   = Math.max(a.end, b.end) - Math.min(a.start, b.start);
        float overlap = Math.min(a.end, b.end) - Math.max(a.start, b.start);
        return overlap > 0 ? overlap/total : 0f;
    }
    public static ArrayList<KeyDuration> getDurations(ArrayList<KeyEvent> input) {
        float keys[] = new float[25];
        Arrays.fill(keys, -1);
        ArrayList<KeyDuration> durations = new ArrayList<>();

        for (KeyEvent event : input) {
            if (event.press  && keys[event.note] < 0) {
                keys[event.note] = event.time;
            } else if (!event.press && keys[event.note] > 0) {
                durations.add(new KeyDuration(event.note, event.time, keys[event.note]));
            }
        }
        Collections.sort(durations, (KeyDuration d1, KeyDuration d2) ->
                Float.compare(d1.start, d2.start)
        );
        return durations;
    }

    static float THRESHOLD = 0.5f;

    public static ArrayList<Chord> normalise(ArrayList<KeyEvent> input) {
        ArrayList<Chord> chords = new ArrayList<>();
        ArrayList<KeyDuration> durations = getDurations(input);
        for (int i = 0; i < durations.size(); i++) {
            KeyDuration current = durations.get(i);
            Chord chord = new Chord();
            chord.setNote(current.note);
            for (int j = i+1; j < durations.size(); j++) {
                KeyDuration next = durations.get(j);
                if (next.start > current.end) { break; }
                if (getOverlap(current, next) > THRESHOLD) {
                    chord.setNote(next.note);
                }
            }
            chords.add(chord);
        }
        return chords;
    }
}
