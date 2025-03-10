package com.example.rlapp.ui.affirmation

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.databinding.ActivityPratciseAffirmationPlaylistBinding
import com.example.rlapp.databinding.BottmsheetReminderSelectionBinding
import com.example.rlapp.databinding.DialogPraticeTimeAffirmationBinding
import com.example.rlapp.ui.affirmation.adapter.AffirmationCardPagerAdapter
import com.example.rlapp.ui.affirmation.adapter.WeekDayAdapter
import com.example.rlapp.ui.affirmation.pojo.AffirmationSelectedCategoryData
import com.example.rlapp.ui.affirmation.pojo.GetAffirmationPlaylistResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Calendar


class PractiseAffirmationPlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPratciseAffirmationPlaylistBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private val affirmationList: ArrayList<AffirmationSelectedCategoryData> = ArrayList()
    private lateinit var affirmationCardPagerAdapter: AffirmationCardPagerAdapter
    private var seconds: Int = 0
    private var timerJob: Job? = null
    private val DELAY_TIME: Long = 1000
    private lateinit var reminderBottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPratciseAffirmationPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        getAffirmationPlaylist()
        setupReminderBottomSheet()

        setCardPlaylistAdapter(affirmationList)

        binding.ivDownload.setOnClickListener {
            val savedImageUri = saveLinearLayoutToGallery(binding.cardViewPager)
            savedImageUri?.let {
                Toast.makeText(this, "Image saved to gallery at: $it", Toast.LENGTH_LONG).show()
            } ?: run {
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivClose.setOnClickListener {
            stopTimer()
            showPracticeCompleteDialog()
        }
    }

    private fun setCardPlaylistAdapter(affirmationList: ArrayList<AffirmationSelectedCategoryData>) {
        affirmationCardPagerAdapter =
            AffirmationCardPagerAdapter(affirmationList, this, binding.cardViewPager)
        binding.cardViewPager.setPageTransformer(true, AffirmationPageTransformer())
        binding.cardViewPager.adapter = affirmationCardPagerAdapter
    }

    private fun getAffirmationPlaylist() {
        Utils.showLoader(this)
        val authToken = sharedPreferenceManager.accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getAffirmationPlaylist(authToken)

        call.enqueue(object : Callback<GetAffirmationPlaylistResponse> {
            override fun onResponse(
                call: Call<GetAffirmationPlaylistResponse>,
                response: Response<GetAffirmationPlaylistResponse>
            ) {
                Utils.dismissLoader(this@PractiseAffirmationPlaylistActivity)
                if (response.isSuccessful && response.body() != null) {
                    affirmationList.clear()
                    response.body()?.data?.let { affirmationList.addAll(it) }
                    setCardPlaylistAdapter(affirmationList)
                    startTimer()
                } else {
                    Toast.makeText(
                        this@PractiseAffirmationPlaylistActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetAffirmationPlaylistResponse>, t: Throwable) {
                Utils.dismissLoader(this@PractiseAffirmationPlaylistActivity)
                Toast.makeText(
                    this@PractiseAffirmationPlaylistActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveLinearLayoutToGallery(view: View): String? {
        // Create a bitmap with the same dimensions as the LinearLayout
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        // Create a Canvas object to draw on the bitmap
        val canvas = Canvas(bitmap)

        // Draw the LinearLayout onto the canvas
        view.draw(canvas)

        // Check if the device is running Android Q (API level 29) or higher
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore to save the image to the gallery
            saveImageToMediaStore(bitmap, this)
        } else {
            // Fallback for older Android versions, using traditional file saving method
            saveImageToOldStorage(bitmap, this)
        }
    }

    // Save the bitmap using MediaStore (for Android 10 and above)
    private fun saveImageToMediaStore(bitmap: Bitmap, context: Context): String? {
        val contentResolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Card${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp") // Save to Pictures folder
        }

        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        return try {
            imageUri?.let { uri ->
                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                outputStream?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                uri.toString() // Return the URI of the saved image
            } ?: run {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Fallback for devices below Android Q (before scoped storage)
    private fun saveImageToOldStorage(bitmap: Bitmap, context: Context): String? {
        val directory = context.getExternalFilesDir(null)
        val file = File(directory, "card.png")
        return try {
            FileOutputStream(file).use { outStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            }
            file.absolutePath // Return the path of the saved image
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(DELAY_TIME)
                seconds++
                updateTimerDisplay()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    override fun onStop() {
        super.onStop()
        timerJob?.cancel()
    }

    private fun updateTimerDisplay() {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        val formattedTime = String.format("%02d:%02d", minutes, remainingSeconds)
        binding.tvTimer.text = formattedTime
    }

    private fun showPracticeCompleteDialog() {
        // Create the dialog
        val dialog = Dialog(this)
        val dialogBinding = DialogPraticeTimeAffirmationBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val window = dialog.window

        // Set the dim amount
        val layoutParams = window?.attributes
        layoutParams?.dimAmount = 0.7f // Adjust the dim amount (0.0 - 1.0)
        window?.attributes = layoutParams

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        layoutParams?.width = width

        dialogBinding.ivDialogClose.setOnClickListener {
            reminderBottomSheetDialog.show()
        }

        val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")
        val currentDayIndex = 0 // e.g., highlight Monday

        val adapter = WeekDayAdapter(weekDays, currentDayIndex)

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.weekRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            this@PractiseAffirmationPlaylistActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter

        dialogBinding.tvDialogAffirmations.text = "8"
        dialogBinding.tvDialogDuration.text = binding.tvTimer.text
        dialogBinding.tvDialogTotalSessions.text = "2"

        dialog.show()
    }

    private fun setupReminderBottomSheet() {
        // Create and configure BottomSheetDialog
        reminderBottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val dialogBinding = BottmsheetReminderSelectionBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root

        reminderBottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.ivDialogClose.setOnClickListener {
            finish()
        }

        dialogBinding.llMorningTime.isClickable = false
        dialogBinding.llAfternoonTime.isClickable = false
        dialogBinding.llEveningTime.isClickable = false

        dialogBinding.llMorningRadio.setOnClickListener {
            dialogBinding.radioMorning.isChecked = !dialogBinding.radioMorning.isChecked
            dialogBinding.llMorningTime.isClickable = dialogBinding.radioMorning.isChecked
        }

        dialogBinding.llAfternoonRadio.setOnClickListener {
            dialogBinding.radioAfternoon.isChecked = !dialogBinding.radioAfternoon.isChecked
            dialogBinding.llAfternoonTime.isClickable = dialogBinding.radioAfternoon.isChecked
        }

        dialogBinding.llEveningRadio.setOnClickListener {
            dialogBinding.radioAEvening.isChecked = !dialogBinding.radioAEvening.isChecked
            dialogBinding.llEveningTime.isClickable = dialogBinding.radioAEvening.isChecked
        }

        dialogBinding.llMorningTime.setOnClickListener {
            showTimePickerDialog(dialogBinding.tvTimeMorning, 1)
        }

        dialogBinding.llAfternoonTime.setOnClickListener {
            showTimePickerDialog(dialogBinding.tvTimeAfternoon, 2)
        }

        dialogBinding.llEveningTime.setOnClickListener {
            showTimePickerDialog(dialogBinding.tvTimeEvening, 3)
        }

        dialogBinding.btnSetReminder.setOnClickListener{

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val triggerTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    private fun showTimePickerDialog(textView: TextView, type: Int) {
        val currentTime: Calendar = Calendar.getInstance()
        val hour: Int = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute: Int = currentTime.get(Calendar.MINUTE)
        val mTimePicker = TimePickerDialog(
            this@PractiseAffirmationPlaylistActivity,
            { timePicker, selectedHour, selectedMinute ->
                run {

                    val amPm = if (selectedHour >= 12) "PM" else "AM"
                    val hourFormatted = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                    val minuteFormatted = String.format("%02d", selectedMinute)
                    textView.text = "$hourFormatted:$minuteFormatted $amPm"
                    /*//Morning
                    if (selectedHour < 12) {
                        val amPm = "AM"
                        val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                        val minuteFormatted = String.format("%02d", selectedMinute)
                        textView.text = "$hourIn12Format:$minuteFormatted $amPm"
                    } else {
                        Toast.makeText(
                            this@PractiseAffirmationPlaylistActivity,
                            "Please select a morning time (AM only)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }*/

                    //Afternoon
                    /*if (selectedHour < 12) {
                        Toast.makeText(this, "Please select a time in the afternoon.", Toast.LENGTH_SHORT).show()
                    } else {
                        val amPm = if (selectedHour >= 12) "PM" else "AM"
                        val hourFormatted = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                        val minuteFormatted = String.format("%02d", selectedMinute)
                        textView.text = "$hourFormatted:$minuteFormatted $amPm"
                    }*/

                    //Evening
                    /*if (selectedHour < 18) { // before 6 PM
                        Toast.makeText(this, "Please select a time in the evening (6:00 PM to 11:59 PM).", Toast.LENGTH_SHORT).show()
                    } else {
                        val amPm = if (selectedHour >= 12) "PM" else "AM"
                        val hourFormatted = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                        val minuteFormatted = String.format("%02d", selectedMinute)
                        textView.text = "$hourFormatted:$minuteFormatted $amPm"
                    }*/

                }
            },
            hour,
            minute,
            false
        ) // 12 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }
}