package com.oreilly.demo.android.pa.uidemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.oreilly.demo.android.pa.uidemo.R;
import com.oreilly.demo.android.pa.uidemo.model.Monster;
import com.oreilly.demo.android.pa.uidemo.model.Monsters;

public class MonsterView extends GridView {

    Monsters monsters;

    public MonsterView(Context context) {
        super(context);
        setFocusableInTouchMode(true);


    }


    /**
     * @param context
     * @param attrs
     */
    public MonsterView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setFocusableInTouchMode(true);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MonsterView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setFocusableInTouchMode(true);
    }


    public void setParams(Context context, Monsters _monsters) {

        monsters = _monsters;
    }

    @Override protected void onDraw(final Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);

        for (int i = 0; i < monsters.monsters.size(); i++) {
            Monster monster = monsters.monsters.get(i);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(monster.state == Monster.State.vulnerble? Color.YELLOW: Color.GREEN);
            canvas.drawCircle(monster.x, monster.y, monster.r, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);
            canvas.drawRect(monster.x - monster.r - 25, monster.y - monster.r - 25, monster.x + monster.r + 25, monster.y + monster.r + 25, paint);

        }
    }
}