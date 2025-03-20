package com.example.rlapp.ui.jounal.new_journal

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.databinding.ActivityJournalListBinding
import com.example.rlapp.databinding.BottomsheetDeleteTagBinding
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class JournalListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalListBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var adapter: JournalListAdapter
    private val journalList = mutableListOf<JournalEntry>()

    private val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
    private var calendarDays = mutableListOf<CalendarDay>()
    private lateinit var calendarAdapter: CalendarAdapter
    private val calendar = Calendar.getInstance()
    private var selectedDate: CalendarDay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        binding.addEntryButton.setOnClickListener {
            startActivity(Intent(this, JournalNewActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        adapter = JournalListAdapter(journalList, object : JournalListAdapter.OnItemClickListener {
            override fun onMenuClick(journalEntry: JournalEntry, view: View) {
                showCustomPopupMenu(view, journalEntry)
            }

            override fun onItemClick(journalEntry: JournalEntry) {

            }

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.rvCalendar.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing)))
        calendarAdapter = CalendarAdapter(calendarDays) { selectedDay ->
            // Toggle selection
            calendarDays.forEach { it.isSelected = false }
            selectedDay.isSelected = true

            selectedDay.isChecked = !selectedDay.isChecked

            calendarAdapter.updateData(calendarDays)
            selectedDate = selectedDay
            getJournalList(selectedDay.dateString)
        }

        binding.rvCalendar.adapter = calendarAdapter

        updateWeekView()

        binding.ivPrev.setOnClickListener {
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekView()
        }

        binding.ivNext.setOnClickListener {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            updateWeekView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedDate == null)
            getJournalList(
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(calendar.time)
            )
        else
            selectedDate?.dateString?.let { getJournalList(it) }
    }

    private fun showDeleteBottomSheet(journalEntry: JournalEntry) {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val dialogBinding = BottomsheetDeleteTagBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root

        bottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.tvTitle.text = "Delete Journal?"
        dialogBinding.tvDescription.text = "Are you sure you want to delete? This cannot be undone."

        dialogBinding.ivDialogClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnYes.setOnClickListener {
            deleteJournal(journalEntry)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun initSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            private val deleteIcon = getDrawable(R.drawable.delete_journal) // your trash icon
            private val background = ColorDrawable(Color.RED)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.deleteItem(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float,
                actionState: Int, isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                if (dX < 0) { // Swiping left
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)

                    deleteIcon?.let {
                        val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                        val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
                        val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        val iconBottom = iconTop + it.intrinsicHeight

                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)
                    }
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun showCustomPopupMenu(view: View, journalEntry: JournalEntry) {
        val menuItems = listOf(
            MenuItemData("Edit", R.drawable.edit_journal, R.color.black),
            MenuItemData("Delete", R.drawable.delete_journal, R.color.delete_color_journal)
        )

        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_custom_menu, null)
        val listView = popupView.findViewById<ListView>(R.id.listView)

        val popupWindow = PopupWindow(popupView, 400, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        listView.adapter = PopupMenuAdapter(this, menuItems) { item ->
            if (item.title == "Edit") {
                val intent: Intent =
                    when (journalEntry.title) {
                        "Free Form" -> Intent(this, FreeFormJournalActivity::class.java)
                        "Bullet" -> Intent(this, BulletJournalActivity::class.java)
                        else -> {
                            Intent(this, GriefJournalActivity::class.java)
                        }
                    }
                startActivity(intent.apply {
                    putExtra("JournalEntry", journalEntry)
                })
            } else {
                showDeleteBottomSheet(journalEntry)
            }
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(view, -100, 0)
    }

    private fun updateWeekView() {
        val currentWeek = getWeekDates()
        calendarDays.clear()

        for (i in daysOfWeek.indices) {
            val isToday = currentWeek[i] == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, currentWeek[i])
            cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            calendarDays.add(
                CalendarDay(
                    daysOfWeek[i], currentWeek[i], isSelected = isToday,
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                )
            )
        }

        binding.tvSelectedDate.text =
            SimpleDateFormat("EEE . dd MMM yyyy", Locale.getDefault()).format(calendar.time)
        calendarAdapter.updateData(calendarDays)
    }

    private fun getWeekDates(): List<Int> {
        val calendarCopy = calendar.clone() as Calendar
        calendarCopy.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return List(7) {
            calendarCopy.get(Calendar.DAY_OF_MONTH)
                .also { calendarCopy.add(Calendar.DAY_OF_MONTH, 1) }
        }
    }

    private fun getJournalList(date: String) {
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getJournalList(sharedPreferenceManager.accessToken, date)
        call.enqueue(object : Callback<JournalListResponse> {
            override fun onResponse(
                call: Call<JournalListResponse>,
                response: Response<JournalListResponse>
            ) {
                Utils.dismissLoader(this@JournalListActivity)
                if (response.isSuccessful && response.body() != null) {
                    journalList.clear()
                    response.body()?.data?.let { journalList.addAll(it) }
                    adapter.notifyDataSetChanged()
                    //initSwipeToDelete()
                } else {
                    Toast.makeText(
                        this@JournalListActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JournalListResponse>, t: Throwable) {
                Utils.dismissLoader(this@JournalListActivity)
                Toast.makeText(
                    this@JournalListActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun deleteJournal(journalEntry: JournalEntry) {
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call =
            apiService.deleteJournalEntry(sharedPreferenceManager.accessToken, journalEntry.id)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Utils.dismissLoader(this@JournalListActivity)
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@JournalListActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    journalList.remove(journalEntry)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@JournalListActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Utils.dismissLoader(this@JournalListActivity)
                Toast.makeText(
                    this@JournalListActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

}