package com.jetsynthesys.rightlife.ai_package.ui.thinkright.fragment

import android.app.Dialog
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
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.jetsynthesys.rightlife.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MoodTrackerFragment : BaseFragment<FragmentMoodTrackingBinding>(),RecordEmotionDialogFragment.BottomSheetListener{

    private lateinit var calendarGrid: GridLayout
    private lateinit var textMonth: TextView
    private lateinit var btnPrev: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var imgBack:ImageView
    private lateinit var container :RelativeLayout

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

        val emotions = listOf(
            EmotionStat("Relaxed", 10, Color.parseColor("#D6F24B")),
            EmotionStat("Happy", 20, Color.parseColor("#04E17C"))
        )

        renderEmotionCircles(emotions)

        btnPrev.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            renderCalendar()
        }

        btnNext.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            renderCalendar()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(ThinkRightReportFragment(), "MoodTrackerFragment")

            }
        })

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(ThinkRightReportFragment(), "MoodTrackerFragment")
        }

        renderCalendar()
        setupWeekNavigation(view)
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
            renderEmotionCircles(getEmotionsForWeek(currentWeekStart))
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

    private fun getEmotionsForWeek(startDate: LocalDate): List<EmotionStat> {
        return listOf(
            EmotionStat("Relaxed", (5..20).random(), Color.parseColor("#D6F24B")),
            EmotionStat("Happy", (10..30).random(), Color.parseColor("#04E17C"))
        )
    }

    private fun renderEmotionCircles(stats: List<EmotionStat>) {
        container.removeAllViews()

        container.removeAllViews()

        val maxPercentage = stats.maxOf { it.percentage }.coerceAtLeast(1)
        val baseSize = 200 // max circle size in dp

        stats.forEachIndexed { index, stat ->
            val sizeDp = (baseSize * (stat.percentage / maxPercentage.toFloat())).coerceAtLeast(60f)
            val sizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeDp, resources.displayMetrics).toInt()

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

    private fun renderCalendar() {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Adjusted for Monday start

        val startDate = 1
        val endDate = daysInMonth

        textMonth.text = "${startDate}–$endDate ${monthFormat.format(calendar.time)}"

        calendarGrid.removeAllViews()

        for (i in 0 until firstDayOfWeek) {
            calendarGrid.addView(createEmptyCell())
        }

        for (day in 1..daysInMonth) {
            calendarGrid.addView(createDayCellWithCheckbox(day))
        }
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
        val container = LinearLayout(requireContext()).apply {
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
            setImageResource(R.drawable.date_checkbox_bg) // default state
            setOnClickListener {
                showEmotionSelector { selectedEmoji ->
                    setImageResource(selectedEmoji)
                }
            }
        }

        container.addView(dayText)
        container.addView(emojiView)
        return container
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
                    val bottomSheet = RecordEmotionDialogFragment()
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

data class EmotionStat(val emotion: String, val percentage: Int, val color: Int)