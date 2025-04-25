package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.SleepIdealActualResponse
import com.jetsynthesys.rightlife.databinding.FragmentIdealActualSleepTimeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.github.mikephil.charting.data.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class SleepIdealActualFragment : BaseFragment<FragmentIdealActualSleepTimeBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentIdealActualSleepTimeBinding
        get() = FragmentIdealActualSleepTimeBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var sixMonthGraph: SixMonthGraphView
    private lateinit var lineChart:LineChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var progressDialog: ProgressDialog
    private lateinit var idealActualResponse: SleepIdealActualResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundResource(R.drawable.sleep_stages_bg)

        lineChart = view.findViewById(R.id.idealActualChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        sixMonthGraph = view.findViewById(R.id.sixMonthGraph)

        val backBtn = view.findViewById<ImageView>(R.id.img_back)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

        backBtn.setOnClickListener {
            navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")
        }

        // Show Week data by default
        fetchSleepData()

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek ->{
                    lineChart.visibility = View.VISIBLE
                    sixMonthGraph.visibility = View.GONE
                    loadWeekGraph()
                }
                R.id.rbMonth -> {
                    lineChart.visibility = View.VISIBLE
                    sixMonthGraph.visibility = View.GONE
                    loadMonthGraph()
                }
                R.id.rbSixMonths -> {
                    lineChart.visibility = View.GONE
                    sixMonthGraph.visibility = View.VISIBLE
                    loadSixMonthGraph()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")

            }
        })

        loadWeekGraph()
    }

    private fun loadSixMonthGraph() {
        val sleepSummaries = listOf(
            SleepSummary("Jan", 8.13f, 5.52f, 7.9f to 8.4f, 5.3f to 5.7f, -0.96f),
            SleepSummary("Feb", 7.21f, 5.03f, 7.0f to 7.4f, 4.8f to 5.2f, 0.11f),
            SleepSummary("March", 8.11f, 4.57f, 7.9f to 8.3f, 4.3f to 4.7f, -0.05f),
            SleepSummary("April", 7.88f, 4.54f, 7.6f to 8.0f, 4.3f to 4.7f, -0.03f),
            SleepSummary("May", 8.20f, 5.52f, 8.1f to 8.3f, 5.3f to 5.7f, 1.27f),
            SleepSummary("June", 8.20f, 5.52f, 8.1f to 8.3f, 5.3f to 5.7f, 1.27f)
        )
        sixMonthGraph.data = sleepSummaries
        sixMonthGraph.invalidate()
    }



    private fun loadMonthGraph() {

        val monthData = listOf(
            SleepGraphData("1–7 Jan", 8.0f, 6.0f),
            SleepGraphData("8–14 Jan", 9.0f, 7.2f),
            SleepGraphData("15–21 Jan", 10.0f, 8.0f),
            SleepGraphData("22–28 Jan", 8.5f, 6.5f),
            SleepGraphData("29–31 Jan", 8.2f, 6.3f)
        )

        setGraphDataFromSleepList(monthData)
    }

    private fun loadWeekGraph() {

        val weekData = listOf(
            SleepGraphData("3 Feb", 8.0f, 6.0f),
            SleepGraphData("4 Feb", 9.0f, 7.0f),
            SleepGraphData("5 Feb", 8.0f, 7.0f),
            SleepGraphData("6 Feb", 8.5f, 6.5f),
            SleepGraphData("7 Feb", 7.5f, 6.0f),
            SleepGraphData("8 Feb", 9.0f, 6.5f),
            SleepGraphData("9 Feb", 8.2f, 6.3f)
        )

        setGraphDataFromSleepList(weekData)
    }

    private fun setGraphDataFromSleepList(sleepData: List<SleepGraphData>) {
        val idealEntries = ArrayList<Entry>()
        val actualEntries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        sleepData.forEachIndexed { index, data ->
            idealEntries.add(Entry(index.toFloat(), data.idealSleep))
            actualEntries.add(Entry(index.toFloat(), data.actualSleep))
            labels.add(data.date)
        }

        val idealSet = LineDataSet(idealEntries, "Ideal").apply {
            color = Color.parseColor("#00C853") // green
            circleRadius = 5f
            setCircleColor(color)
            valueTextSize = 10f
        }

        val actualSet = LineDataSet(actualEntries, "Actual").apply {
            color = Color.parseColor("#2979FF") // blue
            circleRadius = 5f
            setCircleColor(color)
            valueTextSize = 10f
        }

        lineChart.data = LineData(idealSet, actualSet)

        lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 12f
        }

        lineChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 14f
            granularity = 2f
            textSize = 12f
        }

        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.legend.textSize = 12f

        lineChart.invalidate()
    }

        private fun fetchSleepData() {
            progressDialog.show()
            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
            val userId = "user_test_1"
            val period = "daily"
            val source = "apple"
            val call = ApiClient.apiServiceFastApi.fetchSleepIdealActual(userId, source, period)
            call.enqueue(object : Callback<SleepIdealActualResponse> {
                override fun onResponse(call: Call<SleepIdealActualResponse>, response: Response<SleepIdealActualResponse>) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        idealActualResponse = response.body()!!
                       // setSleepRightLandingData(idealActualResponse)
                        val sleepDataList: List<SleepGraphData>? = idealActualResponse.data?.sleepTimeDetail?.map { detail ->
                            val formattedDate = detail.sleepDuration.first().startDatetime?.let {
                                formatDate(
                                    it
                                )
                            }
                            return@map formattedDate?.let {
                                detail.sleepTimeData?.idealSleepData?.toFloat()?.let { it1 ->
                                    detail.sleepTimeData!!.actualSleepData?.toFloat()?.let { it2 ->
                                        SleepGraphData(
                                            date = it,
                                            idealSleep = it1,
                                            actualSleep = it2
                                        )
                                    }
                                }
                            }!!
                        }

                        if (sleepDataList != null) {
                            setupChart(lineChart, sleepDataList)
                        }
                    } else {
                        Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<SleepIdealActualResponse>, t: Throwable) {
                    Log.e("Error", "API call failed: ${t.message}")
                    Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            })
        }

    private fun setupChart(chart: LineChart, newData: List<SleepGraphData>) {
        val idealEntries = ArrayList<Entry>()
        val actualEntries = ArrayList<Entry>()

        for ((index, item) in newData.withIndex()) {
            idealEntries.add(Entry(index.toFloat(), item.idealSleep))
            actualEntries.add(Entry(index.toFloat(), item.actualSleep))
        }

        val idealSet = LineDataSet(idealEntries, "Ideal Sleep").apply {
            color = Color.GREEN
            setCircleColor(Color.GREEN)
            valueTextSize = 10f
        }

        val actualSet = LineDataSet(actualEntries, "Actual Sleep").apply {
            color = Color.BLUE
            setCircleColor(Color.BLUE)
            valueTextSize = 10f
        }

        val lineData = LineData(idealSet, actualSet) // ✅ Convert to LineData

        chart.apply {
            data = lineData // ✅ Assign LineData to Chart
            description.isEnabled = false
            xAxis.setDrawGridLines(false)
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            invalidate() // Refresh chart
        }
    }

    private fun formatDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
        return outputFormat.format(inputFormat.parse(dateTime)!!)
    }

    private fun setSleepRightLandingData(sleepLandingResponse: SleepIdealActualResponse) {

    }


    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}

class SixMonthGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var data: List<SleepSummary> = listOf()

    private val axisPaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f
    }

    private val labelPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    private val boldLabelPaint = Paint(labelPaint).apply {
        isFakeBoldText = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val paddingLeft = 100f
        val paddingRight = 50f
        val paddingTop = 50f
        val paddingBottom = 100f

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        val maxY = 14f
        val minY = 2f
        val scaleY = contentHeight / (maxY - minY)

        val barSpacing = contentWidth / data.size
        val barWidth = barSpacing * 0.5f

        // Draw horizontal grid and Y-axis labels
        for (i in 2..14 step 2) {
            val y = height - paddingBottom - (i - minY) * scaleY
            canvas.drawLine(paddingLeft, y, width - paddingRight, y, axisPaint)
            canvas.drawText("$i", paddingLeft - 30f, y + 10f, labelPaint)
        }

        // Draw bars and data
        data.forEachIndexed { index, item ->
            val centerX = paddingLeft + index * barSpacing + barSpacing / 2

            // Ideal shaded range
            val idealTop = height - paddingBottom - (item.idealRange.second - minY) * scaleY
            val idealBottom = height - paddingBottom - (item.idealRange.first - minY) * scaleY
            val idealRect = RectF(centerX - barWidth / 2, idealTop, centerX + barWidth / 2, idealBottom)
            canvas.drawRect(idealRect, Paint().apply {
                color = Color.parseColor("#AA00C853") // green
                style = Paint.Style.FILL
            })

            // Actual shaded range
            val actualTop = height - paddingBottom - (item.actualRange.second - minY) * scaleY
            val actualBottom = height - paddingBottom - (item.actualRange.first - minY) * scaleY
            val actualRect = RectF(centerX - barWidth / 2, actualTop, centerX + barWidth / 2, actualBottom)
            canvas.drawRect(actualRect, Paint().apply {
                color = Color.parseColor("#AA2962FF") // blue
                style = Paint.Style.FILL
            })

            // Ideal average text
            canvas.drawText(
                "%.2f".format(item.idealAverage),
                centerX,
                idealTop - 10,
                boldLabelPaint
            )

            // Actual average text
            canvas.drawText(
                "%.2f".format(item.actualAverage),
                centerX,
                actualBottom + 30,
                boldLabelPaint
            )

            // Percentage change
            val pctColor = if (item.percentageChange >= 0) Color.parseColor("#4CAF50") else Color.RED
            canvas.drawText(
                "%+.2f%%".format(item.percentageChange),
                centerX,
                actualBottom + 60,
                Paint(labelPaint).apply { color = pctColor }
            )

            // X-axis label (week)
            canvas.drawText(
                item.weekLabel,
                centerX,
                height - 30f,
                labelPaint
            )
        }

        val idealPath = Path()
        val actualPath = Path()

        data.forEachIndexed { index, item ->
            val centerX = paddingLeft + index * barSpacing + barSpacing / 2
            val idealY = height - paddingBottom - (item.idealAverage - minY) * scaleY
            val actualY = height - paddingBottom - (item.actualAverage - minY) * scaleY

            if (index == 0) {
                idealPath.moveTo(centerX, idealY)
                actualPath.moveTo(centerX, actualY)
            } else {
                idealPath.lineTo(centerX, idealY)
                actualPath.lineTo(centerX, actualY)
            }
        }

// Draw ideal average line (green)
        val idealLinePaint = Paint().apply {
            color = Color.parseColor("#00C853")
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawPath(idealPath, idealLinePaint)

// Draw actual average line (blue)
        val actualLinePaint = Paint().apply {
            color = Color.parseColor("#2962FF")
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawPath(actualPath, actualLinePaint)

        // Draw Y axis
        canvas.drawLine(paddingLeft, paddingTop, paddingLeft, height - paddingBottom, axisPaint)
    }
}

data class SleepGraphData(val date: String, val idealSleep: Float, val actualSleep: Float)
data class SleepSummary(
    val weekLabel: String,
    val idealAverage: Float,
    val actualAverage: Float,
    val idealRange: Pair<Float, Float>,  // min to max for shaded area
    val actualRange: Pair<Float, Float>,
    val percentageChange: Float          // vs previous week
)