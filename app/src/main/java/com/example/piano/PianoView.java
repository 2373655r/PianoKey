package com.example.piano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PianoView extends View {

    public static final int NB = 14;
    private Paint black, yellow, white;
    private ArrayList<Key> whites = new ArrayList<>();
    private ArrayList<Key> blacks = new ArrayList<>();
    private int keyWidth, height;
    public AudioSoundPlayer soundPlayer;
    private Boolean[] keysPressed = new Boolean[25];
    private ArrayList<KeyEvent> events = new ArrayList<>();

    public ArrayList<KeyEvent> GetKeyEvents (){
        return events;
    }

    public void ResetKeyEvents(){
        events.clear();
    }

    public PianoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        black = new Paint();
        black.setColor(Color.BLACK);
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.FILL);
        soundPlayer = new AudioSoundPlayer(context);

        Arrays.fill(keysPressed, false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        keyWidth = w / NB;
        height = h;
        int count = 15;

        for (int i = 0; i < NB; i++) {
            int left = i * keyWidth;
            int right = left + keyWidth;

            if (i == NB - 1) {
                right = w;
            }

            RectF rect = new RectF(left, 0, right, h);
            whites.add(new Key(rect, i + 1));

            if (i != 0  &&   i != 3  &&  i != 7  &&  i != 10) {
                rect = new RectF((float) (i - 1) * keyWidth + 0.5f * keyWidth + 0.25f * keyWidth, 0,
                        (float) i * keyWidth + 0.25f * keyWidth, 0.67f * height);
                blacks.add(new Key(rect, count));
                count++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Key k : whites) {
            canvas.drawRect(k.rect, k.down ? yellow : white);
        }

        for (int i = 1; i < NB; i++) {
            canvas.drawLine(i * keyWidth, 0, i * keyWidth, height, black);
        }

        for (Key k : blacks) {
            canvas.drawRect(k.rect, k.down ? yellow : black);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        ArrayList<Key> PressedThisFrame = new ArrayList<>();

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {

            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            Key k = keyForCoords(x,y);

            if (k != null) {
                int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        // Action only fires for primary pointer
                        if (touchIndex == 0 && !k.down) {
                            k.down = true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!k.down) {
                            k.down = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Action only fires for primary pointer
                        if (touchIndex == 0 && k.down) {
                            k.down = false;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        if (pointerIndex == touchIndex) {
                            if (k.down) {
                                k.down = false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (pointerIndex == touchIndex && !k.down) {
                            k.down = true;
                        }
                        break;
                    default:
                        k.down = false;
                }
                if (k.down) {
                    PressedThisFrame.add(k);
                }
            }
        }

        ArrayList<Key> tmp = new ArrayList<>(whites);
        tmp.addAll(blacks);

        for (Key k : tmp) {

            if (!PressedThisFrame.contains(k)) {
                k.down = false;
            }

            if (!keysPressed[k.sound] && k.down) {
                Log.d("Key_Press", "key DOWN " + k.sound);
                //Create a key event and add it to the events list
                KeyEvent ke = new KeyEvent(k.sound, System.currentTimeMillis(),true);
                events.add(ke);

                keysPressed[k.sound] = true;
            } else if(keysPressed[k.sound] && !k.down) {
                Log.d("Key_Press", "key UP " + k.sound);
                //Create a key event and add it to the events list
                KeyEvent ke = new KeyEvent(k.sound, System.currentTimeMillis(),false);
                events.add(ke);

                keysPressed[k.sound] = false;
            }

            if (k.down) {

                if (!soundPlayer.isNotePlaying(k.sound)) {
                    soundPlayer.playNote(k.sound);
                    invalidate();
                } else {
                    releaseKey(k);
                }
            } else {
                soundPlayer.stopNote(k.sound);
                releaseKey(k);
            }
        }

        return true;
    }

    private Key keyForCoords(float x, float y) {
        for (Key k : blacks) {
            if (k.rect.contains(x,y)) {
                return k;
            }
        }

        for (Key k : whites) {
            if (k.rect.contains(x,y)) {
                return k;
            }
        }

        return null;
    }

    private void releaseKey(final Key k) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                k.down = false;
                handler.sendEmptyMessage(0);
            }
        }, 100);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };
}