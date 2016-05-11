package com.meten.ifuture.view.wave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meten.ifuture.R;


/**
 * Created by John on 2014/10/15.
 */
public class WaveView extends RelativeLayout {
    protected static final int LARGE = 1;
    protected static final int MIDDLE = 2;
    protected static final int LITTLE = 3;

    private int mAboveWaveColor;
    private int mProgress;

    private int mWaveToBottom;

    private Solid mSolid;
    private TextView tvProgress;

    private final int DEFAULT_ABOVE_WAVE_COLOR = Color.parseColor("#828182");
    private final int DEFAULT_PROGRESS = 80;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setOrientation(VERTICAL);
        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveView, R.attr.waveViewStyle, 0);
        mAboveWaveColor = attributes.getColor(R.styleable.WaveView_above_wave_color, DEFAULT_ABOVE_WAVE_COLOR);
        mProgress = attributes.getInt(R.styleable.WaveView_progress, DEFAULT_PROGRESS);
        attributes.recycle();

        mSolid = new Solid(context, null);
        mSolid.initializePainters(mAboveWaveColor);



        tvProgress = new TextView(context);
        tvProgress.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        tvProgress.setGravity(Gravity.CENTER);
        tvProgress.setTextColor(Color.WHITE);

        addView(mSolid);
        addView(tvProgress);

        setProgress(mProgress);
    }

    public void setProgress(int progress) {
        this.mProgress = progress > 100 ? 100 : progress;
        tvProgress.setText(mProgress+"%");
        computeWaveToBottom();
    }

    public int getProgress(){
        return mProgress;
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            computeWaveToBottom();
        }
    }


    private void computeWaveToBottom() {
        mWaveToBottom = (int) (getHeight() * ( mProgress / 100f));
        ViewGroup.LayoutParams params = mSolid.getLayoutParams();
        if (params != null) {
            ((LayoutParams) params).bottomMargin = mWaveToBottom;
        }
        mSolid.setLayoutParams(params);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.progress = mProgress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
    }

    private static class SavedState extends BaseSavedState {
        int progress;

        /**
         * Constructor called from {@link android.widget.ProgressBar#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
