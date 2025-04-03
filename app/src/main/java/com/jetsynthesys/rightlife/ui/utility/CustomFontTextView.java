package com.jetsynthesys.rightlife.ui.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.jetsynthesys.rightlife.R;

public class CustomFontTextView extends AppCompatTextView {

    public CustomFontTextView(Context context) {
        super(context);
        init();
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Load your custom font from res/font
        Typeface customFont = ResourcesCompat.getFont(getContext(), R.font.dmsans_bold);
        setTypeface(customFont);
    }
  /*  private void init() {
        // Load your custom font from assets
        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/dmsans_bold.ttf");
        setTypeface(customFont);
    }*/
}