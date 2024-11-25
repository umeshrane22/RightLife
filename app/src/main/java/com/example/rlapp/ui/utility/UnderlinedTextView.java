package com.example.rlapp.ui.utility;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

public class UnderlinedTextView extends androidx.appcompat.widget.AppCompatTextView {

    public UnderlinedTextView(Context context) {
        super(context);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}

