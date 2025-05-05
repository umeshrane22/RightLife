package com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class LineGraphView extends View {

    private final Paint linePaint;
    private final Paint circlePaint;
    private final Path path;
    private float[] dataPoints;
    private final float padding = 20f;

    public LineGraphView(Context context) {
        this(context, null);
    }

    public LineGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Initialize paints and path
        linePaint = new Paint();
        linePaint.setColor(0xFFB11414);
        linePaint.setStrokeWidth(3f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(0xFFB11414);
        circlePaint.setStrokeWidth(3f);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);

        path = new Path();

        // Initialize dataPoints with 7 zeros
        dataPoints = new float[7];
        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = 0f;
        }
    }

    public void setDataPoints(float[] points) {
        this.dataPoints = points != null ? points : new float[7]; // Fallback to 7 zeros if null
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth() - 2 * padding;
        float height = getHeight() - 2 * padding;

        // Find min and max values
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;
        for (float value : dataPoints) {
            if (value < minValue) minValue = value;
            if (value > maxValue) maxValue = value;
        }
        // Handle edge case where all values are the same
        if (maxValue == minValue) {
            minValue = 0f;
            maxValue = 100f;
        }

        float range = maxValue - minValue;
        float scaleY = height / range;
        float scaleX = dataPoints.length > 1 ? width / (dataPoints.length - 1) : 0f;

        path.reset();
        for (int i = 0; i < dataPoints.length; i++) {
            float x = padding + i * scaleX;
            float y = padding + height - ((dataPoints[i] - minValue) * scaleY);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            canvas.drawCircle(x, y, 5f, circlePaint);
        }

        canvas.drawPath(path, linePaint);
    }
}