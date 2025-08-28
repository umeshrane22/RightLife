package com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LineGrapghViewSteps extends View {
    private Paint linePaint; // Paint for the graph lines
    private Paint circlePaint; // Paint for the circles at the end of each line and intersections
    private Paint axisPaint; // Paint for the X-axis labels
    private Paint verticalLinePaint; // Paint for the vertical grey lines at labels
    private Paint maxValueLinePaint; // Paint for the vertical light grey line at max value
    private List<float[]> dataSets; // List of datasets (each dataset represents a line)
    private List<Integer> lineColors; // Colors for each line
    private Path path;

    public LineGrapghViewSteps(Context context) {
        super(context);
        init();
    }

    public LineGrapghViewSteps(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize line paint
        linePaint = new Paint();
        linePaint.setStrokeWidth(5f); // Thicker line to match the image
        linePaint.setStyle(Paint.Style.STROKE);

        // Initialize circle paint (for the dots at the end of each line and intersections)
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);

        // Initialize axis paint
        axisPaint = new Paint();
        axisPaint.setColor(0xFF000000); // Black color for the labels
        axisPaint.setTextSize(24f); // Reduced text size to fit all labels
        axisPaint.setTextAlign(Paint.Align.CENTER); // Default alignment (will adjust dynamically)

        // Initialize vertical line paint for labels
        verticalLinePaint = new Paint();
        verticalLinePaint.setColor(0xFFA7A7A7); // Grey color for vertical lines
        verticalLinePaint.setStrokeWidth(4f); // Thicker line
        verticalLinePaint.setStyle(Paint.Style.STROKE);
        // Solid line (no DashPathEffect)

        // Initialize paint for max value vertical line
        maxValueLinePaint = new Paint();
        maxValueLinePaint.setColor(0xFFD9D9D9); // Light grey color
        maxValueLinePaint.setStrokeWidth(3f); // Slightly thinner than graph lines
        maxValueLinePaint.setStyle(Paint.Style.STROKE);
        // Solid line (no DashPathEffect)

        // Initialize data sets and colors
        dataSets = new ArrayList<>();
        lineColors = new ArrayList<>();
        path = new Path();
    }

    // Method to add a dataset and its corresponding color
    public void addDataSet(float[] data, int color) {
        dataSets.add(data);
        lineColors.add(color);
        invalidate(); // Trigger a redraw after adding data
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get the width and height of the view
        float width = getWidth();
        float height = getHeight();

        // Log the view dimensions to debug
        Log.d("LineGrapghViewSteps", "View Width: " + width + ", View Height: " + height);

        if (dataSets.isEmpty()) {
            Log.w("LineGrapghViewSteps", "No datasets available, skipping draw.");
            return; // Exit if no data to draw
        }

        // Reserve space at the bottom for the X-axis labels
        float paddingBottom = 50f; // Space for X-axis labels
        float paddingTop = 10f; // Small padding at the top to avoid clipping
        float graphHeight = height - paddingBottom - paddingTop; // Adjusted graph height

        // Add padding at the start and end of the graph
        float paddingHorizontal = 30f; // Padding on both sides (start and end)
        float effectiveWidth = width - (2 * paddingHorizontal); // Effective width after padding

        // Find the global minimum, maximum values, and max value index
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;
        int maxValueIndex = 0; // Index of the max value
        for (int i = 0; i < dataSets.size(); i++) {
            float[] data = dataSets.get(i);
            if (data != null && data.length > 0) {
                for (int j = 0; j < data.length; j++) {
                    float value = data[j];
                    if (value < minValue) minValue = value;
                    if (value > maxValue) {
                        maxValue = value;
                        maxValueIndex = j; // Update index of max value
                    }
                }
            }
        }

        // Ensure minValue and maxValue are valid
        if (minValue == Float.MAX_VALUE || maxValue == Float.MIN_VALUE) {
            minValue = 0f; // Default min if no data
            maxValue = 100f; // Default max if no data
            Log.w("LineGrapghViewSteps", "No valid data range, using defaults (0-100).");
        }

        // Scale the data points to fit within the graph's height and effective width
        float scaleY = graphHeight / (maxValue - minValue);
        float scaleX = effectiveWidth / (dataSets.get(0).length - 1); // Safe to access now

        // Draw the vertical light grey line at the max value's X position
        float maxLineX = paddingHorizontal + (maxValueIndex * scaleX); // X-coordinate of max value
        canvas.drawLine(maxLineX, paddingTop, maxLineX, height - paddingBottom, maxValueLinePaint);
        Log.d("LineGrapghViewSteps", "Max value line at x: " + maxLineX + ", max value: " + maxValue + ", index: " + maxValueIndex);

        // Draw each dataset as a separate line
        for (int i = 0; i < dataSets.size(); i++) {
            float[] data = dataSets.get(i);
            int color = lineColors.get(i);

            // Set the line color
            linePaint.setColor(color);

            // Apply dotted effect for green (0xFF03B27B) and black (0xFF000000) lines
            if (color == 0xFF03B27B || color == 0xFFA7A7A7) {
                linePaint.setPathEffect(new DashPathEffect(new float[]{10f, 10f}, 0f)); // Dotted pattern: 10px dash, 10px gap
            } else {
                linePaint.setPathEffect(null); // Solid line for other colors
            }

            // Draw the line
            path.reset();
            float lastX = 0f;
            float lastY = 0f;
            if (data != null && data.length > 0) {
                // For the red line, draw only up to maxValueIndex
                int drawLimit = (color == 0xFFFD6967) ? Math.min(maxValueIndex + 1, data.length) : data.length;
                for (int j = 0; j < drawLimit; j++) {
                    // Adjust the x position to account for the start padding
                    float x = paddingHorizontal + (j * scaleX);
                    float y = (height - paddingBottom) - ((data[j] - minValue) * scaleY); // Adjusted for paddingTop
                    if (j == 0) {
                        path.moveTo(x, y); // Move to the first point
                    } else {
                        path.lineTo(x, y); // Draw line to the next point
                    }
                    // Store the last point for drawing the circle
                    if (j == drawLimit - 1) {
                        lastX = x;
                        lastY = y;
                    }
                }
                // Draw the line on the canvas
                canvas.drawPath(path, linePaint);

                // Draw a circle at the last point (only if not already at maxValueIndex for red line)
                if (color != 0xFFFD6967 || maxValueIndex != data.length - 1) {
                    circlePaint.setColor(color);
                    canvas.drawCircle(lastX, lastY, 6f, circlePaint); // 6f radius for the circle
                }

                // Draw a circle at the intersection with the max value line for red, grey, and green lines
                if (color == 0xFFFD6967 || color == 0xFF707070 || color == 0xFF03B27B) {
                    float y = (height - paddingBottom) - ((data[maxValueIndex] - minValue) * scaleY);
                    circlePaint.setColor(color);
                    canvas.drawCircle(maxLineX, y, 6f, circlePaint);
                    Log.d("LineGrapghViewSteps", "Intersection circle at x: " + maxLineX + ", y: " + y + ", color: " + Integer.toHexString(color));
                }
            }
        }

        // Draw X-axis labels (time in hours) and vertical solid grey lines
        String[] timeLabels = {"12 am", "6 am", "12 pm", "8 pm", "12 am"};
        float labelSpacing = effectiveWidth / (timeLabels.length - 1); // Space between labels based on effective width

        // Log the label spacing to debug
        Log.d("LineGrapghViewSteps", "Label Spacing: " + labelSpacing);

        for (int i = 0; i < timeLabels.length; i++) {
            float x;
            // Adjust the x position to account for the start padding
            if (i == 0) {
                // First label ("12 am"): Align left
                axisPaint.setTextAlign(Paint.Align.LEFT);
                x = paddingHorizontal; // Start at the padding
            } else if (i == timeLabels.length - 1) {
                // Last label ("12 am"): Align right
                axisPaint.setTextAlign(Paint.Align.RIGHT);
                x = width - paddingHorizontal; // End at the padding
            } else {
                // Middle labels ("6 am", "12 pm", "8 pm"): Align center
                axisPaint.setTextAlign(Paint.Align.CENTER);
                x = paddingHorizontal + (i * labelSpacing); // Center position for middle labels
            }

            // Draw the label
            float labelY = height - 10f; // Position labels closer to the bottom
            canvas.drawText(timeLabels[i], x, labelY, axisPaint);

            // Draw a taller and thicker vertical solid grey line at the label's X position
            float lineX = x; // X-coordinate aligned with the label
            float lineStartY = height - paddingBottom; // Start at the bottom of the graph
            float lineEndY = (height - paddingBottom) - 30f; // Extend 30px upward
            canvas.drawLine(lineX, lineStartY, lineX, lineEndY, verticalLinePaint);
        }
    }

    // Optional: Clear all datasets
    public void clear() {
        dataSets.clear();
        lineColors.clear();
        invalidate(); // Trigger a redraw
    }
}