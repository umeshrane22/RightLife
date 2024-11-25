package com.example.rlapp.ui.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.example.rlapp.R;

public class DmSansBoldTextview extends AppCompatTextView {

    public DmSansBoldTextview(@NonNull Context context) {
        super(context);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.fontfamily);
        if (typeface != null) {
            setTypeface(typeface);
        } else {
            Log.e("DmSansBoldTextview", "Failed to load font: R.font.fontfamily");
        }
    }
    public DmSansBoldTextview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Handle font attribute overrides if needed (optional)
    }

    public DmSansBoldTextview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Handle font attribute overrides if needed (optional)
    }
}