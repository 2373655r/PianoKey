package com.example.piano;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class KeyDuration {
    int  note;
    long start;
    long end;
    long midpoint;
    long duration;

    KeyDuration(int note, long start, long end) {
        this.note     = note;
        this.start    = start;
        this.end      = end;
        this.duration = end - start;
        this.midpoint = this.start + (this.duration/2);
    }

    public String toString() {
        return this.note + "@" + this.duration;
    }
}

class Chord {
    boolean notes[];

    Chord() {
        this.notes = new boolean[25];
    }

    Chord(boolean[] notes) {
        this.notes = notes;
    }

    Chord(String strChord) {
        this.notes = new boolean[25];
        int intChord = Integer.parseInt(strChord);
        for (int i = 0; i < notes.length; i++) {
            int j = notes.length - 1 - i;
            notes[i] = ((intChord >> j) & 1) == 1;
        }
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

    public static double getOverlap(KeyDuration a, KeyDuration b) {
        double total   = Math.max(a.end, b.end) - Math.min(a.start, b.start);
        double overlap = Math.min(a.end, b.end) - Math.max(a.start, b.start);
        Log.d("Total for "   + a + " + " + b, Double.toString(total));
        Log.d("Overlap for " + a + " + " + b, Double.toString(overlap));
        return overlap > 0 ? overlap/total : 0f;
    }
    public static ArrayList<KeyDuration> getDurations(ArrayList<KeyEvent> input) {
        long keys[] = new long[25];
        Arrays.fill(keys, -1);
        ArrayList<KeyDuration> durations = new ArrayList<>();

        for (KeyEvent event : input) {
            Log.d("Key event", event.note + "@" + event.time + (event.press ? "D" : "U"));
            if (event.press  && keys[event.note] < 0) {
                keys[event.note] = event.time;
            } else if (!event.press && keys[event.note] >= 0) {
                durations.add(new KeyDuration(event.note, keys[event.note], event.time));
                keys[event.note] = -1;
            }
        }
        Collections.sort(durations, (KeyDuration d1, KeyDuration d2) ->
                Float.compare(d1.start, d2.start)
        );
        return durations;
    }

    static double THRESHOLD = 0.5;

    public static ArrayList<Chord> normalise(ArrayList<KeyEvent> input) {
        ArrayList<Chord> chords = new ArrayList<>();
        ArrayList<KeyDuration> durations = getDurations(input);

        //for (int i = 0; i < durations.size(); i++) {
        int i = 0;
        while (i < durations.size()) {
            KeyDuration current = durations.get(i);
            Log.d("KeyDuration", current.toString());
            Chord chord = new Chord();
            chord.setNote(current.note);

            //for (int j = i+1; j < durations.size(); j++) {
            int j = i+1;
            while (j < durations.size()) {
                KeyDuration next = durations.get(j);
                if (next.start > current.end) { break; }
                if (getOverlap(current, next) > THRESHOLD) {
                    chord.setNote(next.note);
                    durations.remove(next);
                } else { j++; }
            }

            chords.add(chord);
            i++;
        }
        return chords;
    }

    public static ArrayList<Chord> makeMelody(String melodyStr) {
        ArrayList<Chord> chords = new ArrayList<>();
        for (String chord : melodyStr.split("\\|")) {
            chords.add(new Chord(chord));
        }
        return chords;
    }
}
