package com.example.rlapp.newdashboard



import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.databinding.ItemHeartRateCardBinding
import com.example.rlapp.newdashboard.model.FacialScan
import com.example.rlapp.newdashboard.model.ScanData
import com.example.rlapp.ui.utility.DateConverter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HeartRateAdapter(
    private val heartRateList: ArrayList<FacialScan>?,
    private val context: Context
) : RecyclerView.Adapter<HeartRateAdapter.HeartRateViewHolder>() {

    inner class HeartRateViewHolder(val binding: ItemHeartRateCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartRateViewHolder {
        val binding = ItemHeartRateCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HeartRateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeartRateViewHolder, position: Int) {
        val item = heartRateList?.get(position)
        val binding = holder.binding

        if (item != null) {
            binding.tvValue.text = item.avgValue.toString()
        }
        if (item != null) {
            binding.tvDate.text = DateConverter.convertToDate(item.data?.get(0)?.createdAt)
        }
        if (item != null) {
            binding.tvUnit.text = item.avgUnit
        }
        if (item != null) {
            binding.tvHeartRate.text = item.avgParameter
        }
        // show date
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        //binding.tvDate.text = currentDate


        if (item != null) {
            binding.tvWarning.text = item.avgIndicator
        }
        // Show warning if heart rate is above normal
      /*  if (item.heartRate > 100) {
            binding.ivWarning.visibility = android.view.View.VISIBLE
            //binding.tvWarning.visibility = android.view.View.VISIBLE
            //binding.tvWarning.text = "Above Normal"
        } else {
            binding.ivWarning.visibility = android.view.View.VISIBLE
            //binding.tvWarning.visibility = android.view.View.GONE
        }*/
        val iconRes = getWarningIconByType("normal")
        binding.ivWarning.setImageResource(iconRes)

        val iconResReport = getReportIconByType(item?.key.toString())
        binding.ivHeartIcon.setImageResource(iconResReport)


        //if (holder.bindingAdapterPosition != 2) {}

            binding.heartRateChart.visibility = android.view.View.VISIBLE
            setupChart(binding.heartRateChart, item?.data)
            binding.barChart.visibility = android.view.View.GONE
         /*else {
            binding.barChart.visibility = android.view.View.VISIBLE
            binding.heartRateChart.visibility = android.view.View.GONE
            setupStackedBarChart(
                binding.barChart,
                listOf(120f, 122f, 118f, 125f, 130f, 128f, 124f),
                listOf(80f, 82f, 78f, 85f, 88f, 86f, 83f)
            )
        }*/

        // open report detail: click listener
        binding.cardHeartRate.setOnClickListener {
            val intent = Intent(context, FacialScanReportDetailsActivity::class.java).apply {
                /*putExtra("HEART_RATE", item.heartRate)
                putExtra("DATE", item.date)
                putStringArrayListExtra("TREND_DATA", item.trendData)*/
            }
            context.startActivity(intent)
        }
    }
    private fun getWarningIconByType(type: String): Int {
        return when (type) {
            "normal" -> R.drawable.breathing_green_tick
            "Borderline" -> R.drawable.ic_alert_report_page
            "Productive" -> R.drawable.breathing_green_tick
            "Optimal" -> R.drawable.breathing_green_tick
            else -> R.drawable.breathing_green_tick
        }
    }private fun getReportIconByType(type: String): Int {
        return when (type) {
            "BMI_CALC" -> R.drawable.ic_db_report_bmi
            "BP_RPP" -> R.drawable.ic_db_report_cardiak_workload
            "BP_SYSTOLIC" -> R.drawable.ic_db_report_bloodpressure
            "BP_CVD" -> R.drawable.ic_db_report_cvdrisk
            "MSI" -> R.drawable.ic_db_report_stresslevel
            "BR_BPM" -> R.drawable.ic_db_report_respiratory_rate
            "HRV_SDNN" -> R.drawable.ic_db_report_heart_variability
            else -> R.drawable.ic_db_report_heart_rate
        }
    }


    private fun setupStackedBarChart(
        barChart: BarChart,
        systolicValues: List<Float>,
        diastolicValues: List<Float>
    ) {
        val entries = systolicValues.mapIndexed { index, systolic ->
            val diastolic = diastolicValues[index]
            BarEntry(index.toFloat(), floatArrayOf(diastolic, systolic - diastolic))
        }

        val dataSet = BarDataSet(entries, "Blood Pressure").apply {
            colors = listOf(Color.GRAY, Color.BLUE)
            setDrawValues(false)
            stackLabels = arrayOf("Diastolic", "Systolic")
        }

        val barData = BarData(dataSet)
        barData.barWidth = 1f

        barChart.apply {
            data = barData
            description.isEnabled = false
            legend.isEnabled = false
            setDrawGridBackground(false)
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            invalidate()
        }
    }

    private fun setupChart(chart: LineChart, trendData: ArrayList<ScanData>?) {
        val entries = mutableListOf<Entry>()
        if (trendData != null) {
            for ((index, data) in trendData.withIndex()) {
                entries.add(Entry(index.toFloat(), data.value?.toFloat() ?: 1f))
            }
        }

        val dataSet = LineDataSet(entries, "Heart Rate").apply {
            color = Color.RED
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setDrawCircles(true)
            setCircleColor(Color.RED)
            setDrawValues(false)
        }

        chart.apply {
            this.data = LineData(dataSet)
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
            xAxis.setDrawLabels(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(false)
            setPinchZoom(false)
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            setNoDataText("")
            invalidate()
        }
    }

    override fun getItemCount() = heartRateList?.let { it.size }?:0
}

