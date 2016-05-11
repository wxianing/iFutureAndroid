package com.meten.ifuture.view.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by John on 2014/10/15.
 */
class Solid extends View {

    private Paint aboveWavePaint;
    public final int DEFAULT_ABOVE_ALPHA = 80;

    public Solid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Solid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        setLayoutParams(params);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(getLeft(), 0, getRight(), getBottom(), aboveWavePaint);
    }

    public void initializePainters(int mAboveWaveColor) {
        aboveWavePaint = new Paint();
        aboveWavePaint.setColor(mAboveWaveColor);
        aboveWavePaint.setAlpha(DEFAULT_ABOVE_ALPHA);
        aboveWavePaint.setStyle(Paint.Style.FILL);
        aboveWavePaint.setAntiAlias(true);
    }

}
