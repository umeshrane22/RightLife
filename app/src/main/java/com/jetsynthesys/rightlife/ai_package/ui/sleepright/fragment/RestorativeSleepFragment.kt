package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
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
import com.jetsynthesys.rightlife.databinding.FragmentRestorativeSleepBinding
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepDetail
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepStageResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepStages
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalTime

class RestorativeSleepFragment: BaseFragment<FragmentRestorativeSleepBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRestorativeSleepBinding
        get() = FragmentRestorativeSleepBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var barChart: BarChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var progressDialog: ProgressDialog

    private val stageColorMap = mapOf(
        "Light" to Color.parseColor("#AEE2FF"),
        "Deep" to Color.parseColor("#BDB2FF"),
        "REM" to Color.parseColor("#8ECAE6"),
        "Awake" to Color.parseColor("#6A4C93")
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundResource(R.drawable.sleep_stages_bg)

        barChart = view.findViewById(R.id.restorativeBarChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        // Show Week data by default
      //  updateChart(getWeekData(), getWeekLabels())
        fetchSleepData()

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)
        val weekData = getDummyWeekSleepData()
        val monthData = getDummyMonthSleepData()
        renderStackedChart(weekData)

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek ->{
                  renderStackedChart(weekData)
                }
                R.id.rbMonth ->{
                 renderMonthChart(barChart,monthData)
                }
                R.id.rbSixMonths ->{
                    renderMonthChart(barChart,monthData)
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")

            }
        })

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")
        }

        /*barChart.renderer = RoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )*/


    }

    fun renderMonthChart(chart: BarChart, data: List<Pair<String, RestorativeSleepDetail>>) {
        val entries = ArrayList<BarEntry>()
        val xLabels = ArrayList<String>()

        data.forEachIndexed { index, (dateLabel, detail) ->
            val stageDurations = mutableMapOf(
                "Light" to 0f,
                "Deep" to 0f,
                "REM" to 0f,
                "Awake" to 0f
            )

            detail.sleepStages.forEach {
                val start = LocalTime.parse(it.startDatetime)
                val end = LocalTime.parse(it.endDatetime)
                val duration = Duration.between(start, end).toMinutes().toFloat() / 60f
                stageDurations[it.stage] = stageDurations.getOrDefault(it.stage, 0f) + duration
            }

            val stackedValues = arrayOf(
                stageDurations["Light"] ?: 0f,
                stageDurations["Deep"] ?: 0f,
                stageDurations["REM"] ?: 0f,
                stageDurations["Awake"] ?: 0f
            )

            entries.add(BarEntry(index.toFloat(), stackedValues.toFloatArray()))
            xLabels.add(dateLabel) // Use "1", "2", ..., or full dates if needed
        }

        val dataSet = BarDataSet(entries, "Sleep Stages").apply {
            setDrawValues(false)
            colors = listOf(
                Color.parseColor("#B2E0FE"), // Light
                Color.parseColor("#A3B4F8"), // Deep
                Color.parseColor("#9287F9"), // REM
                Color.parseColor("#4D3DF8")  // Awake
            )
            stackLabels = arrayOf("Light", "Deep", "REM", "Awake")
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.4f // ✅ Thin
            chart.setVisibleXRangeMaximum(31f)
        }

        chart.apply {
            this.data = barData
            description.isEnabled = false
            legend.isEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawGridBackground(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(xLabels)
                granularity = 1f
                labelCount = 6
                setDrawGridLines(false)
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 5f
                granularity = 1f
            }

            axisRight.isEnabled = false

            invalidate()
        }
    }

    private fun fetchSleepData() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "67f6698fa213d14e22a47c2a"
        val date = "2025-03-18"
        val source = "apple"
        val period = "weekly"
        val call = ApiClient.apiServiceFastApi.fetchSleepRestorativeDetail(userId, source,period, date)
        call.enqueue(object : Callback<RestorativeSleepResponse> {
            override fun onResponse(call: Call<RestorativeSleepResponse>, response: Response<RestorativeSleepResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                 //   sleepStageResponse = response.body()!!
              //      setSleepRightStageData(sleepStageResponse)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<RestorativeSleepResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun renderStackedChart(weekData: List<Pair<String, RestorativeSleepDetail>>) {
        val entries = mutableListOf<BarEntry>()
        val xLabels = mutableListOf<String>()

        weekData.forEachIndexed { index, (label, detail) ->
            val stageDurations = mutableMapOf(
                "Light" to 0f,
                "Deep" to 0f,
                "REM" to 0f,
                "Awake" to 0f
            )

            detail.sleepStages.forEach {
                val start = LocalTime.parse(it.startDatetime)
                val end = LocalTime.parse(it.endDatetime)
                val duration = Duration.between(start, end).toMinutes().toFloat() / 60f
                stageDurations[it.stage] = stageDurations.getOrDefault(it.stage, 0f) + duration
            }

            val stackedValues = arrayOf(
                stageDurations["Light"] ?: 0f,
                stageDurations["Deep"] ?: 0f,
                stageDurations["REM"] ?: 0f,
                stageDurations["Awake"] ?: 0f
            )

            entries.add(BarEntry(index.toFloat(), stackedValues.toFloatArray()))
            xLabels.add(label)
        }

        val dataSet = BarDataSet(entries, "Sleep Stages").apply {
            setColors(
                stageColorMap["Light"]!!,
                stageColorMap["Deep"]!!,
                stageColorMap["REM"]!!,
                stageColorMap["Awake"]!!
            )
            stackLabels = arrayOf("Light", "Deep", "REM", "Awake")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        barChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xLabels)
                granularity = 1f
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                labelRotationAngle = -25f
            }

            legend.isEnabled = false
            animateY(1000)
            invalidate()
        }

    }

    private fun getDummyWeekSleepData(): List<Pair<String, RestorativeSleepDetail>> {
        return listOf(
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f)
        )
    }
    private fun getDummyMonthSleepData(): List<Pair<String, RestorativeSleepDetail>> {
        return listOf(
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
        )
    }

    private fun mockDetail(light: Float, deep: Float, rem: Float, awake: Float): RestorativeSleepDetail {
        fun genStage(stage: String, hours: Float, offset: Float): SleepStages {
            val start = LocalTime.of(22, 0).plusMinutes((offset * 60).toLong())
            val end = start.plusMinutes((hours * 60).toLong())
            return SleepStages(stage, start.toString(), end.toString(),45)
        }

        return RestorativeSleepDetail(
            totalSleepDurationMinutes = ((light + deep + rem + awake) * 60).toDouble(),
            sleepStartTime = "22:00",
            sleepEndTime = "06:00",
            sleepStages = arrayListOf(
                genStage("Light", light, 0f),
                genStage("Deep", deep, light),
                genStage("REM", rem, light + deep),
                genStage("Awake", awake, light + deep + rem)
            ),
            calculatedRestorativeSleep = null
        )
    }


    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    class RoundedBarChartRenderer(
        chart: BarDataProvider,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : BarChartRenderer(chart, animator, viewPortHandler) {

        private val radius = 20f // corner radius in pixels

        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val trans = mChart.getTransformer(dataSet.axisDependency)
            mBarBorderPaint.color = dataSet.barBorderColor
            val drawBorder = dataSet.barBorderWidth > 0f

            mShadowPaint.color = dataSet.barShadowColor

            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

            val barData = mChart.barData
            val barWidth = barData.barWidth

            val buffer = mBarBuffers[index]
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
            buffer.setBarWidth(barData.barWidth)
            buffer.feed(dataSet)

            trans.pointValuesToPixel(buffer.buffer)

            for (j in 0 until buffer.size() step 4) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) continue
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

                val left = buffer.buffer[j]
                val top = buffer.buffer[j + 1]
                val right = buffer.buffer[j + 2]
                val bottom = buffer.buffer[j + 3]

                val entryIndex = j / 4
                if (entryIndex >= dataSet.entryCount) continue

                val isTopSegment = isTopBarSegment(dataSet, entryIndex)

                if (isTopSegment) {
                    val rectF = RectF(left, top, right, bottom)
                    c.drawRoundRect(rectF, radius, radius, mRenderPaint)
                } else {
                    c.drawRect(left, top, right, bottom, mRenderPaint)
                }
            }
        }

        private fun isTopBarSegment(dataSet: IBarDataSet, index: Int): Boolean {
            val entry = dataSet.getEntryForIndex(index) as? BarEntry ?: return false
            val stackedValues = entry.yVals
            return stackedValues == null // Only draw rounded if it's not part of a stack
        }
    }
}


