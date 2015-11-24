package com.oreilly.demo.android.pa.uidemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;

import com.oreilly.demo.android.pa.uidemo.model.Dot;
import com.oreilly.demo.android.pa.uidemo.model.Dots;
import com.oreilly.demo.android.pa.uidemo.view.DotView;


/**
 * Android UI demo program
 */
public class TouchMe extends Activity {
    /** Dot diameter */
    public static final int DOT_DIAMETER = 6;

    /** Listen for taps. */
    private static final class TrackingTouchListener implements View.OnTouchListener {
        private final Dots mDots;
        private List<Integer> tracks = new ArrayList<>();

        TrackingTouchListener(final Dots dots) { mDots = dots; }

        @Override public boolean onTouch(final View v, final MotionEvent evt) {
            final int action = evt.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    final int idx1 = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    tracks.add(evt.getPointerId(idx1));
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    final int idx2 = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    tracks.remove(Integer.valueOf(evt.getPointerId(idx2)));
                    break;

                case MotionEvent.ACTION_MOVE:
                    final int n = evt.getHistorySize();
                    for (Integer i: tracks) {
                        final int idx = evt.findPointerIndex(i);
                        for (int j = 0; j < n; j++) {
                            addDot(
                                mDots,
                                evt.getHistoricalX(idx, j),
                                evt.getHistoricalY(idx, j),
                                evt.getHistoricalPressure(idx, j),
                                evt.getHistoricalSize(idx, j));
                        }
                    }
                    break;


                default:
                    return false;
            }

            for (final Integer i: tracks) {
                final int idx = evt.findPointerIndex(i);
                addDot(
                    mDots,
                    evt.getX(idx),
                    evt.getY(idx),
                    evt.getPressure(idx),
                    evt.getSize(idx));
            }

            return true;
        }

        private void addDot(
                final Dots dots,
                final float x,
                final float y,
                final float p,
                final float s) {
            dots.addDot(x, y, Color.CYAN, (int) ((p + 0.5) * (s + 0.5) * DOT_DIAMETER));
        }
    }

    /** Generate new dots, one per second. */
    private final class DotGenerator implements Runnable {
        final Dots dots;
        final DotView view;
        final int color;

        private final Handler hdlr = new Handler();
        private final Runnable makeDots;

        private volatile boolean done;

        DotGenerator(final Dots dots, final DotView view, final int color) {
            this.dots = dots;
            this.view = view;
            this.color = color;
            makeDots = () -> makeDot(this.dots, this.view, this.color);
        }

        public void done() { done = true; }

        @Override
        public void run() {
            while (!done) {
                hdlr.post(makeDots);
                try { Thread.sleep(2000); } catch (InterruptedException e) { return; }
            }
        }
    }

    private final Random rand = new Random();

    /** The application model */
    final Dots dotModel = new Dots();

    /** The application view */
    DotView dotView;

    /** The dot generator */
    DotGenerator dotGenerator;

    /** Called when the activity is first created. */
    @Override public void onCreate(final Bundle state) {
        super.onCreate(state);

        // install the view
        setContentView(R.layout.main);

        // find the dots view
        dotView = (DotView) findViewById(R.id.dots);
        dotView.setDots(dotModel);

        dotView.setOnCreateContextMenuListener(this);
        dotView.setOnTouchListener(new TrackingTouchListener(dotModel));

        dotView.setOnKeyListener((final View v, final int keyCode, final KeyEvent event) -> {
            if (KeyEvent.ACTION_DOWN != event.getAction()) {
                return false;
            }

            int color;
            switch (keyCode) {
                case KeyEvent.KEYCODE_SPACE:
                    color = Color.MAGENTA;
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    color = Color.BLUE;
                    break;
                default:
                    return false;
            }

            makeDot(dotModel, dotView, color);

            return true;
        });


        dotView.setOnFocusChangeListener((final View v, final boolean hasFocus) -> {
            if (!hasFocus && (null != dotGenerator)) {
                dotGenerator.done();
                dotGenerator = null;
            }
            else if (hasFocus && (null == dotGenerator)) {
                dotGenerator = new DotGenerator(dotModel, dotView, Color.BLACK);
                new Thread(dotGenerator).start();
            }
        });

        // wire up the controller
        findViewById(R.id.button1).setOnClickListener((final View v) ->
            makeDot(dotModel, dotView, Color.RED)
        );
        findViewById(R.id.button2).setOnClickListener((final View v) ->
            makeDot(dotModel, dotView, Color.GREEN)
        );

        final EditText tb1 = (EditText) findViewById(R.id.text1);
        final EditText tb2 = (EditText) findViewById(R.id.text2);
        dotModel.setDotsChangeListener((Dots dots) -> {
            Dot d = dots.getLastDot();
            // This code makes the UI unacceptably unresponsive.
            // ... investigating - in March, 2014, this was not a problem
            tb1.setText((null == d) ? "" : String.valueOf(d.getX())); // uncommented
            tb2.setText((null == d) ? "" : String.valueOf(d.getY())); // uncommented
            dotView.invalidate();
        });
    }

    /** Install an options menu. */
    @Override public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    /** Respond to an options menu selection. */
    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                dotModel.clearDots();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Install a context menu. */
    @Override public void onCreateContextMenu(
            final ContextMenu menu,
            final View v,
            final ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Clear").setAlphabeticShortcut('x');
    }

    /** Respond to a context menu selection. */
    @Override public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                dotModel.clearDots();
                return true;
            default:
        }
        return false;
    }

    /**
     * @param dots the dots we're drawing
     * @param view the view in which we're drawing dots
     * @param color the color of the dot
     */
    void makeDot(final Dots dots, final DotView view, final int color) {
        final int pad = (DOT_DIAMETER + 2) * 2;
        dots.addDot(
            DOT_DIAMETER + (rand.nextFloat() * (view.getWidth() - pad)),
            DOT_DIAMETER + (rand.nextFloat() * (view.getHeight() - pad)),
            color,
            DOT_DIAMETER);
    }
}