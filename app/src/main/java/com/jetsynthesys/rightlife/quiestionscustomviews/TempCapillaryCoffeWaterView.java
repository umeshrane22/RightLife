package com.jetsynthesys.rightlife.quiestionscustomviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class TempCapillaryCoffeWaterView extends View {
    private Paint backgroundPaint, fillPaint, markerPaint, thumbPaint;
    private int viewHeight, viewWidth;
    private float thumbY;
    private int[] markerPositions;
    private int minValue = 0, maxValue = 12000, stepValue = 1000;
    private float cornerRadiusFactor = 0.5f;
    private float thumbWidth = 80f;
    private float thumbHeight = 40f;
    private float thumbCornerRadius = 20f;
    private OnValueChangeListener listener;
    private HashMap<Integer, Integer> stepColorMap = new HashMap<>();

    public TempCapillaryCoffeWaterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.parseColor("#E8F0FE")); // Default soft blue
        backgroundPaint.setStyle(Paint.Style.FILL);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //fillPaint.setColor(Color.parseColor("#E35D5B")); // Default fill color
        fillPaint.setStyle(Paint.Style.FILL);

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(Color.TRANSPARENT);
        markerPaint.setStyle(Paint.Style.FILL);

        thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(Color.WHITE);
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setShadowLayer(10, 0, 0, Color.GRAY);
        setLayerType(LAYER_TYPE_SOFTWARE, thumbPaint);

        //setupDefaultStepColors();
    }

    public void setupDefaultStepColors(String color) {
        stepColorMap.clear();
        int stepCount = (maxValue - minValue) / stepValue + 1;
        for (int i = 0; i < stepCount; i++) {
            int step = i * stepValue;
            stepColorMap.put(step, Color.parseColor(color));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        int markerCount = (maxValue - minValue) / stepValue + 1;
        markerPositions = new int[markerCount];
        for (int i = 0; i < markerCount; i++) {
            markerPositions[i] = h - (i * h / (markerCount - 1));
        }
        thumbY = markerPositions[0];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = viewWidth / 2f;
        float barWidth = viewWidth / 2f;
        float cornerRadius = barWidth * cornerRadiusFactor;

        canvas.drawRoundRect(centerX - barWidth, 0, centerX + barWidth, viewHeight, cornerRadius, cornerRadius, backgroundPaint);
        canvas.drawRoundRect(centerX - barWidth, thumbY, centerX + barWidth, viewHeight, cornerRadius, cornerRadius, fillPaint);

        for (int pos : markerPositions) {
            canvas.drawCircle(centerX, pos, 10, markerPaint);
        }

        RectF thumbRect = new RectF(
                centerX - thumbWidth / 2f,
                thumbY - thumbHeight / 2f,
                centerX + thumbWidth / 2f,
                thumbY + thumbHeight / 2f
        );
        canvas.drawRoundRect(thumbRect, thumbCornerRadius, thumbCornerRadius, thumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
            thumbY = getClosestMarker(event.getY());
            invalidate();

            post(() -> {
                if (listener != null) {
                    int value = getValueFromPosition(thumbY);
                    if (stepColorMap.containsKey(value)) {
                        fillPaint.setColor(stepColorMap.get(value));
                    } else {
                       // fillPaint.setColor(Color.parseColor("#61A5C2"));
                    }
                    listener.onValueChanged(value);
                    invalidate();
                }
            });
            return true;
        }
        return super.onTouchEvent(event);
    }

    private float getClosestMarker(float y) {
        float closest = markerPositions[0];
        for (float pos : markerPositions) {
            if (Math.abs(y - pos) < Math.abs(y - closest)) {
                closest = pos;
            }
        }
        return closest;
    }

    private int getValueFromPosition(float y) {
        int closestIndex = 0;
        float closestDistance = Math.abs(y - markerPositions[0]);
        for (int i = 1; i < markerPositions.length; i++) {
            float distance = Math.abs(y - markerPositions[i]);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }
        Log.d("MarkerValue", "Closest Index: " + closestIndex + ", Value: " + (minValue + (closestIndex * stepValue)));
        return minValue + (closestIndex * stepValue);
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.listener = listener;
    }

    public void setCornerRadiusFactor(float factor) {
        this.cornerRadiusFactor = factor;
        invalidate();
    }

    public void setThumbSize(float width, float height) {
        this.thumbWidth = width;
        this.thumbHeight = height;
        invalidate();
    }

    public void setThumbCornerRadius(float radius) {
        this.thumbCornerRadius = radius;
        invalidate();
    }

    public void setStepColorMap(HashMap<Integer, Integer> customMap) {
        this.stepColorMap = customMap;

        if (markerPositions != null && markerPositions.length > 0) {
            int value = getValueFromPosition(thumbY);
            if (stepColorMap.containsKey(value)) {
                fillPaint.setColor(stepColorMap.get(value));
            }
        }

        invalidate();
    }

    // ✅ Dynamic background color
    public void setBackgroundColorInt(int color) {
        if (backgroundPaint != null) {
            backgroundPaint.setColor(color);
            invalidate();
        }
    }

    // ✅ Dynamic fill color
    public void setFillColorInt(int color) {
        if (fillPaint != null) {
            fillPaint.setColor(color);
            invalidate();
        }
    }

    public interface OnValueChangeListener {
        void onValueChanged(int value);
    }
}
