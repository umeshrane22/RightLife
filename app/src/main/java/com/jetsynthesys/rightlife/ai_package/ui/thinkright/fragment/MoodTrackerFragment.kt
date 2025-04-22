package com.jetsynthesys.rightlife.ai_package.ui.thinkright.fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.databinding.FragmentMoodTrackingBinding
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.MoodTrackerMonthData
import com.jetsynthesys.rightlife.ai_package.model.MoodTrackerMonthlyResponse
import com.jetsynthesys.rightlife.ai_package.model.MoodTrackerPercent
import com.jetsynthesys.rightlife.ai_package.model.MoodTrackerWeeklyResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MoodTrackerFragment(journalAnswer: String,emojis:Int, fromFragment: String) : BaseFragment<FragmentMoodTrackingBinding>(),RecordEmotionDialogFragment.BottomSheetListener{

    private lateinit var calendarGrid: GridLayout
    private lateinit var textMonth: TextView
    private lateinit var btnPrev: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var imgBack:ImageView
    private lateinit var container :RelativeLayout
    private lateinit var progressDialog: ProgressDialog
    private lateinit var moodTrackerResponse: MoodTrackerWeeklyResponse
    private lateinit var moodTrackerMonthlyResponse: MoodTrackerMonthlyResponse
    private var moodsMonthlyList: ArrayList<MoodTrackerMonthData> = arrayListOf()
    private val emojiImageViews = mutableMapOf<Int, ImageView>()
    val weekStartDate = "2025-03-9"
    val weekEndDate = "2025-03-16"
    val fromFragments = fromFragment
    val journalAnswers = journalAnswer
    var emojiSelected = emojis

    val moodEmojiMap = mapOf(
        "Happy" to R.drawable.happy_icon,
        "Relaxed" to R.drawable.relaxed_icon,
        "Unsure" to R.drawable.unsure_icon,
        "Stressed" to R.drawable.stressed_icon,
        "Sad" to R.drawable.sad_icon
    )

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("d", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMoodTrackingBinding
        get() = FragmentMoodTrackingBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = view.findViewById(R.id.circleContainer)
        calendarGrid = view.findViewById(R.id.calendarGrid)
        textMonth = view.findViewById(R.id.textMonth)
        btnPrev = view.findViewById(R.id.btnPrev)
        btnNext = view.findViewById(R.id.btnNext)
        imgBack = view.findViewById(R.id.img_back)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

        /*val emotions = listOf(
            EmotionStat("Relaxed", 10, Color.parseColor("#D6F24B")),
            EmotionStat("Happy", 20, Color.parseColor("#04E17C"))
        )*/

       // renderEmotionCircles(emotions)

        btnPrev.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            renderCalendar(moodsMonthlyList)
        }

        btnNext.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            renderCalendar(moodsMonthlyList)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(ThinkRightReportFragment(), "MoodTrackerFragment")

            }
        })

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(ThinkRightReportFragment(), "ThinkRightReportFragment")
        }

        if (fromFragments == "JournalFragment"){
            val bottomSheet = RecordEmotionDialogFragment(emojiSelected,journalAnswers)
            bottomSheet.show(parentFragmentManager, "WakeUpTimeDialog")
        }

        //renderCalendar(moodsMonthlyList)
        setupWeekNavigation(view)
        fetchMoodPercentage()
        fetchMoodMonthly()
    }

    private fun fetchMoodPercentage() {
        progressDialog.show()
        val token = SharedPreferenceManager.getInstance(requireActivity()).accessToken
        val type = "weekly"
        val call = ApiClient.apiService.fetchMoodTrackerPercentage(token,type, weekStartDate,weekEndDate)
        call.enqueue(object : Callback<MoodTrackerWeeklyResponse> {
            override fun onResponse(call: Call<MoodTrackerWeeklyResponse>, response: Response<MoodTrackerWeeklyResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                       moodTrackerResponse = response.body()!!
                    renderEmotionCircles(getEmotionsForWeek(moodTrackerResponse.data.getOrNull(0)))
                        //  setMoodPercentage(moodTrackerResponse.data)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<MoodTrackerWeeklyResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun setMoodPercentage(moodPercentData: ArrayList<MoodTrackerPercent>) {

    }

    private fun fetchMoodMonthly() {
        progressDialog.show()
        val token = SharedPreferenceManager.getInstance(requireActivity()).accessToken
        val type = "monthly"
        val startDate = "2025-03-01"
        val endDate = "2025-03-31"
        val call = ApiClient.apiService.fetchMoodTrackerMonthly(token,type, startDate,endDate)
        call.enqueue(object : Callback<MoodTrackerMonthlyResponse> {
            override fun onResponse(call: Call<MoodTrackerMonthlyResponse>, response: Response<MoodTrackerMonthlyResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    if (response.body()!=null) {
                        moodTrackerMonthlyResponse = response.body()!!
                        moodsMonthlyList =moodTrackerMonthlyResponse.data
                        renderCalendar(moodTrackerMonthlyResponse.data)
                    }
                    //      setSleepRightStageData(sleepStageResponse)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<MoodTrackerMonthlyResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun applyEmojisToCalendar(moodDataList: List<MoodTrackerMonthData>) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        for (moodData in moodDataList) {
            val dateStr = moodData.createdAt ?: continue

            try {
                val date = formatter.parse(dateStr) ?: continue

                val calendar = Calendar.getInstance()
                calendar.time = date
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val iconRes = moodEmojiMap[moodData.emotion] ?: continue
                emojiImageViews[day]?.setImageResource(iconRes)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var currentWeekStart = LocalDate.of(2025, 2, 3)

    private fun setupWeekNavigation(view: View) {
        val weekRangeText = view.findViewById<TextView>(R.id.weekRangeText)
        val prevBtn = view.findViewById<ImageView>(R.id.prevWeekBtn)
        val nextBtn = view.findViewById<ImageView>(R.id.nextWeekBtn)

        fun updateWeekLabel() {
            val formatter = DateTimeFormatter.ofPattern("d")
            val formatterMonth = DateTimeFormatter.ofPattern("MMM, yyyy")
            val weekEnd = currentWeekStart.plusDays(6)

            val startText = currentWeekStart.format(formatter)
            val endText = weekEnd.format(formatter)
            val monthYear = weekEnd.format(formatterMonth)

            weekRangeText.text = "$startText–$endText $monthYear"

            // Update data for that week here
           // renderEmotionCircles(getEmotionsForWeek(currentWeekStart))
        }

        prevBtn.setOnClickListener {
            currentWeekStart = currentWeekStart.minusWeeks(1)
            updateWeekLabel()
        }

        nextBtn.setOnClickListener {
            currentWeekStart = currentWeekStart.plusWeeks(1)
            updateWeekLabel()
        }

        updateWeekLabel()
    }

    private fun getEmotionsForWeek(moodList: MoodTrackerPercent?): List<EmotionStat> {
        val result = mutableListOf<EmotionStat>()

        moodList?.happy?.let { if (it > 0.0) result.add(EmotionStat("Happy",moodList.happy!!, Color.parseColor("#D6F24B"))) }
        moodList?.relaxed?.let { if (it > 0.0) result.add(EmotionStat("Relaxed",moodList.relaxed!!, Color.parseColor("#04E17C"))) }
        moodList?.unsure?.let { if (it > 0.0) result.add(EmotionStat("Unsure",moodList.unsure!!, Color.parseColor("#04E17C"))) }
        moodList?.stressed?.let { if (it > 0.0) result.add(EmotionStat("Stressed",moodList.stressed!!, Color.parseColor("#D6F24B"))) }
        moodList?.sad?.let { if (it > 0.0) result.add(EmotionStat("Sad",moodList.sad!!, Color.parseColor("#04E17C"))) }

        return result
    }

    private fun renderEmotionCircles(stats: List<EmotionStat>) {
        container.removeAllViews()

        val maxPercentage = stats.maxOf { it.percentage }.coerceAtLeast(1.0)
        val baseSize = 200 // max circle size in dp

        stats.forEachIndexed { index, stat ->
            val sizeDp = (baseSize * (stat.percentage / maxPercentage.toFloat())).coerceAtLeast(60.0)
            val sizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeDp.toFloat(), resources.displayMetrics).toInt()

            // Vertical container inside the circle
            val verticalLayout = LinearLayout(requireContext(),).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }

            val percentText = TextView(requireContext()).apply {
                text = "${stat.percentage}%"
                setTextColor(Color.BLACK)
                textSize = 14f
                setTypeface(null, Typeface.BOLD)
                gravity = Gravity.CENTER
            }

            val labelText = TextView(requireContext()).apply {
                text = stat.emotion
                setTextColor(Color.BLACK)
                textSize = 12f
                gravity = Gravity.CENTER
            }

            verticalLayout.addView(percentText)
            verticalLayout.addView(labelText)

            val circleView = FrameLayout(requireContext()).apply {
                layoutParams = RelativeLayout.LayoutParams(sizePx, sizePx).apply {
                    addRule(RelativeLayout.CENTER_VERTICAL)
                    if (index == 0) addRule(RelativeLayout.ALIGN_PARENT_START)
                    else addRule(RelativeLayout.ALIGN_PARENT_END)
                    marginStart = 32
                    marginEnd = 32
                }
                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(stat.color)
                }
                addView(verticalLayout)
            }

            container.addView(circleView)
        }
    }

    private fun renderCalendar(moodDataList: List<MoodTrackerMonthData>) {
        val targetMonth = getYearMonthFromData(moodDataList)
        if (targetMonth != null) {
            calendar.set(Calendar.YEAR, targetMonth.first)
            calendar.set(Calendar.MONTH, targetMonth.second)
        }

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Adjusted for Monday start

        val startDate = 1
        val endDate = daysInMonth

        textMonth.text = "${startDate}–$endDate ${monthFormat.format(calendar.time)}"
        calendarGrid.removeAllViews()
        emojiImageViews.clear()

        for (i in 0 until firstDayOfWeek) {
            calendarGrid.addView(createEmptyCell())
        }

        for (day in 1..daysInMonth) {
            calendarGrid.addView(createDayCellWithCheckbox(day))
        }

        applyEmojisToCalendar(moodDataList)
    }
    private fun getYearMonthFromData(dataList: List<MoodTrackerMonthData>): Pair<Int, Int>? {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        dataList.firstOrNull()?.createdAt?.let { createdAt ->
            try {
                val date = formatter.parse(createdAt)
                val cal = Calendar.getInstance()
                cal.time = date!!
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH) // 0-based (Jan = 0)
                return Pair(year, month)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun createEmptyCell(): LinearLayout {
        return LinearLayout(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
        }
    }

    private fun createDayCellWithCheckbox(day: Int): LinearLayout {
        val emojiContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            setPadding(8, 8, 8, 8)
        }

        val dayText = TextView(requireContext()).apply {
            text = day.toString()
            gravity = Gravity.CENTER
            setTextColor(Color.BLACK)
            textSize = 10f
        }

        val emojiView = ImageView(requireContext()).apply {
            val size = resources.getDimensionPixelSize(R.dimen.emoji_size)
            layoutParams = LinearLayout.LayoutParams(size, size)
            setImageResource(R.drawable.date_checkbox_bg) // default image
            setOnClickListener {
                showEmotionSelector { selectedEmojiRes ->
                    setImageResource(selectedEmojiRes)
                }
            }
        }

        emojiContainer.addView(dayText)
        emojiContainer.addView(emojiView)

        emojiImageViews[day] = emojiView // store reference

        return emojiContainer
    }

    private fun showEmotionSelector(onEmotionSelected: (Int) -> Unit) {
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_emotion_dialog, null)

        // make background rounded
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottomsheet_bg)

        dialog.setContentView(view)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }

        val emotionContainer = view.findViewById<LinearLayout>(R.id.emotionContainer)

        val emotions = listOf(
            R.drawable.happy_icon,
            R.drawable.relaxed_icon,
            R.drawable.unsure_icon,
            R.drawable.stressed_icon,
            R.drawable.sad_icon
        )

        emotions.forEach { emojiRes ->
            val emojiView = ImageView(requireContext()).apply {
                setImageResource(emojiRes)
                val size = resources.getDimensionPixelSize(R.dimen.emoji_big_size)
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    marginStart = 12
                    marginEnd = 12
                }
                setOnClickListener {
                    onEmotionSelected(emojiRes)
                    val bottomSheet = RecordEmotionDialogFragment(emojiRes,"")
                    bottomSheet.show(parentFragmentManager, "WakeUpTimeDialog")
                    dialog.dismiss()
                }
            }
            emotionContainer.addView(emojiView)
        }

        dialog.show()
    }

    override fun onDataReceived(data: Int) {

    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}

data class EmotionStat(val emotion: String, val percentage: Double, val color: Int)