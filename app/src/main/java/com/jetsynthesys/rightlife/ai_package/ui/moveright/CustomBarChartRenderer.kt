package com.jetsynthesys.rightlife.ai_package.ui.moveright

import androidx.media3.common.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomBarChartRenderer(
    chart: BarChart,
    animator: com.github.mikephil.charting.animation.ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val labels: List<String>,
    private val labelsDate: List<String>
) : BarChartRenderer(chart, animator, viewPortHandler) {

    init {
        // Initialize custom XAxisRenderer using reflection
        try {
            val xAxisRendererField = BarChartRenderer::class.java.getDeclaredField("mXAxisRenderer")
            xAxisRendererField.isAccessible = true
            val barChart = mChart as BarChart // Typecast to access xAxis
          /*  xAxisRendererField.set(this, CustomXAxisRenderer(
                viewPortHandler,
                barChart.xAxis,
                barChart.getTransformer(YAxis.AxisDependency.LEFT), // Use LEFT dependency
                labels,
                labelsDate
            ))*/
            xAxisRendererField.isAccessible = false
        } catch (e: Exception) {
           // Log.e("CustomBarChartRenderer", "Failed to set XAxisRenderer: ${e.message}", e)
        }
    }
}