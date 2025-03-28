package com.example.rlapp.ai_package.ui.moveright.graphs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LineGraphViewWorkout extends View {

    private Paint linePaint; // Paint for the line
    private Path path;
    private List<Float> dataPoints; // Dynamic data points

    public LineGraphViewWorkout(Context context) {
        super(context);
        init();
    }

    public LineGraphViewWorkout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize line paint
        linePaint = new Paint();
        linePaint.setColor(0xFFB11414); // Line color
        linePaint.setStrokeWidth(3f); // Thinner line for small view
        linePaint.setStyle(Paint.Style.STROKE);

        path = new Path();
        dataPoints = new ArrayList<>(); // Initialize as empty list
    }

    // Method to update the heart rate data dynamically
    public void updateData(List<Float> points) {
        System.out.println("Updating data in LineGraphViewWorkout: " + points);
        this.dataPoints = points;
        invalidate(); // Redraw the graph with new data
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("Drawing LineGraphViewWorkout with dataPoints: " + dataPoints + ", width: " + getWidth() + ", height: " + getHeight());

        if (dataPoints == null || dataPoints.isEmpty()) {
            System.out.println("No data to draw");
            return;
        }

        // Get the width and height of the view
        float width = getWidth();
        float height = getHeight();

        // Find the minimum and maximum values in the data points
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;
        for (float value : dataPoints) {
            if (value < minValue) minValue = value;
            if (value > maxValue) maxValue = value;
        }

        // Avoid division by zero
        if (maxValue == minValue) {
            maxValue = minValue + 1; // Add a small offset to avoid division by zero
        }

        // Scale the data points to fit within the view's height
        float scaleY = height / (maxValue - minValue);
        float scaleX = width / (dataPoints.size() - 1);

        // Draw the line
        path.reset();
        for (int i = 0; i < dataPoints.size(); i++) {
            float x = i * scaleX;
            float y = height - ((dataPoints.get(i) - minValue) * scaleY);

            if (i == 0) {
                path.moveTo(x, y); // Move to the first point
            } else {
                path.lineTo(x, y); // Draw line to the next point
            }
        }

        // Draw the line
        canvas.drawPath(path, linePaint);
    }
}