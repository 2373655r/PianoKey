package com.example.piano;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.io.InputStream;
import java.util.List;

public class AudioSoundPlayer {

    private SparseArray<PlayThread> threadMap = null;
    private Context context;
    private static final SparseArray<String> SOUND_MAP = new SparseArray<>();
    public static final int MAX_VOLUME = 100, CURRENT_VOLUME = 90;

    static {
        // white keys sounds
        SOUND_MAP.put(1, "c3");
        SOUND_MAP.put(2, "d3");
        SOUND_MAP.put(3, "e3");
        SOUND_MAP.put(4, "f3");
        SOUND_MAP.put(5, "g3");
        SOUND_MAP.put(6, "a4");
        SOUND_MAP.put(7, "b4");
        SOUND_MAP.put(8, "c4");
        SOUND_MAP.put(9, "d4");
        SOUND_MAP.put(10, "e4");
        SOUND_MAP.put(11, "f4");
        SOUND_MAP.put(12, "g4");
        SOUND_MAP.put(13, "a5");
        SOUND_MAP.put(14, "b5");
        // black keys sounds
        SOUND_MAP.put(15, "c-3");
        SOUND_MAP.put(16, "d-3");
        SOUND_MAP.put(17, "f-3");
        SOUND_MAP.put(18, "g-3");
        SOUND_MAP.put(19, "a-4");
        SOUND_MAP.put(20, "c-4");
        SOUND_MAP.put(21, "d-4");
        SOUND_MAP.put(22, "f-4");
        SOUND_MAP.put(23, "g-4");
        SOUND_MAP.put(24, "a-5");
    }

    public AudioSoundPlayer(Context context) {
        this.context = context;
        threadMap = new SparseArray<>();
    }

    public void playNote(int note) {
        if (!isNotePlaying(note)) {
            PlayThread thread = new PlayThread(note);
            thread.start();
            threadMap.put(note, thread);
        }
    }

    public void stopNote(int note) {
        PlayThread thread = threadMap.get(note);

        if (thread != null) {
            threadMap.remove(note);
        }
    }

    public boolean isNotePlaying(int note) {
        return threadMap.get(note) != null;
    }

    private class PlayThread extends Thread {
        int note;
        AudioTrack audioTrack;

        public PlayThread(int note) {
            this.note = note;
        }

        @Override
        public void run() {
            try {
                String path = SOUND_MAP.get(note) + ".wav";
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor ad = assetManager.openFd(path);
                long fileSize = ad.getLength();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

                audioTrack.play();
                InputStream audioStream = null;

                int headerOffset = 44; long bytesWritten = 0; int bytesRead = 0;

                audioStream = assetManager.open(path);
                audioStream.read(buffer, 0, headerOffset);

                while (bytesWritten < fileSize - headerOffset) {
                    bytesRead = audioStream.read(buffer, 0, bufferSize);
                    bytesWritten += Math.abs(audioTrack.write(buffer, 0, bytesRead));
                    System.out.println(bytesWritten);
                }

                System.out.println(bytesWritten);
                System.out.println(fileSize);


                audioTrack.stop();
                audioTrack.release();

            } catch (Exception e) {
                System.out.println("Sound Playing Error");
                e.printStackTrace();
            } finally {
                if (audioTrack != null) {
                    audioTrack.release();
                }
            }
        }
    }
}