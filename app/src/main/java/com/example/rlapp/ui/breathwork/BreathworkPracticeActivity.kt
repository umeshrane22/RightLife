package com.example.rlapp.ui.breathwork


import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R
import com.example.rlapp.databinding.ActivityBreathworkPracticeBinding
import com.example.rlapp.databinding.BottomsheetBreathworkCompleteBinding
import com.example.rlapp.databinding.BottomsheetDeleteTagBinding
import com.example.rlapp.ui.jounal.new_journal.JournalEntry
import com.google.android.material.bottomsheet.BottomSheetDialog

class BreathworkPracticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreathworkPracticeBinding

    private var totalSets = 3
    private var currentSet = 1
    private var sessionDurationSeconds = 135 // Default session duration
    private var countDownTimer: CountDownTimer? = null

    private lateinit var breathingPractice: BreathingPractice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathworkPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the selected breathing practice from the intent
        val practiceType = intent.getStringExtra("PRACTICE_TYPE") ?: "ALTERNATE_NOSTRIL"
        breathingPractice = BreathingPractice.valueOf(practiceType)
        val sessionCount = intent.getIntExtra("sessionCount", 3)
        totalSets = sessionCount

        // Calculate session duration based on the selected practice
        val cycleDuration = breathingPractice.inhaleDuration +
                breathingPractice.holdDuration +
                breathingPractice.exhaleDuration +
                (if (breathingPractice == BreathingPractice.BOX_BREATHING) breathingPractice.holdDuration else 0L)
        sessionDurationSeconds = (totalSets * cycleDuration / 1000).toInt()

        // Set initial values
        binding.setIndicator.text = "Set $currentSet/$totalSets"
        updateSessionTimer(sessionDurationSeconds * 1000L)

        // Set click listeners
        binding.backButton.setOnClickListener { onBackPressed() }
        binding.finishEarlyButton.setOnClickListener {
            showDeleteBottomSheet();
        }

        // Start the breathing session
        startSessionTimer()
        startBreathingCycle()
    }

    private fun startSessionTimer() {
        object : CountDownTimer(sessionDurationSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateSessionTimer(millisUntilFinished)
            }

            override fun onFinish() {
                binding.sessionTimer.text = "00:00"
            }
        }.start()
    }

    private fun updateSessionTimer(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60
        binding.sessionTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun startBreathingCycle() {
        if (currentSet <= totalSets) {
            binding.setIndicator.text = "Set $currentSet/$totalSets"
            startBreathIn()
        } else {
            //finish()
            showCompletedBottomSheet()
        }
    }

    private fun startBreathIn() {
        binding.breathingPhase.text = "Breath In"
        animateCircle(1f, 1.5f, breathingPractice.inhaleDuration)
        startCountdown(breathingPractice.inhaleDuration) {
            if (breathingPractice.holdDuration > 0) startHold() else startBreathOut()
        }
    }

    private fun startHold() {
        binding.breathingPhase.text = "Hold"
        animateCircle(1.5f, 1.5f, breathingPractice.holdDuration)
        startCountdown(breathingPractice.holdDuration) { startBreathOut() }
    }

    private fun startBreathOut() {
        binding.breathingPhase.text = "Breath Out"
        animateCircle(1.5f, 1f, breathingPractice.exhaleDuration)
        startCountdown(breathingPractice.exhaleDuration) {
            if (breathingPractice == BreathingPractice.BOX_BREATHING) {
                startFinalHold()
            } else {
                currentSet++
                startBreathingCycle()
            }
        }
    }

    private fun startFinalHold() {
        binding.breathingPhase.text = "Hold"
        animateCircle(1f, 1f, breathingPractice.holdDuration)
        startCountdown(breathingPractice.holdDuration) {
            currentSet++
            startBreathingCycle()
        }
    }

    private fun startCountdown(duration: Long, onFinish: () -> Unit) {
        var secondsLeft = (duration / 1000).toInt()
        binding.circleTimer.text = secondsLeft.toString()
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsLeft--
                binding.circleTimer.text = secondsLeft.toString()
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    private fun animateCircle(fromScale: Float, toScale: Float, duration: Long) {
        val scaleAnimation = ScaleAnimation(
            fromScale, toScale, fromScale, toScale,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = duration
        scaleAnimation.fillAfter = true
        binding.circleView.startAnimation(scaleAnimation)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    /*show erly finish bottomsheet*/
    private fun showDeleteBottomSheet() {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val dialogBinding = BottomsheetDeleteTagBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root

        bottomSheetDialog.setContentView(bottomSheetView)


        bottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.tvTitle.text = "Leaving early?"
        dialogBinding.tvDescription.text = "A few more minutes of breathing practise will make a world of difference."

        dialogBinding.btnCancel.text = "Continue Practise"
        dialogBinding.btnYes.text = "Leave"

        dialogBinding.ivDialogClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnYes.setOnClickListener {
            //deleteJournal(journalEntry)
            bottomSheetDialog.dismiss()
            finish()
        }
        bottomSheetDialog.show()
    }


    private fun showCompletedBottomSheet() {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val dialogBinding = BottomsheetBreathworkCompleteBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root

        bottomSheetDialog.setContentView(bottomSheetView)


        bottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.tvTitle.text = "How do you feel after the exercise?"
        dialogBinding.tvDescription.text = "A few more minutes of breathing practise will make a world of difference."

        dialogBinding.btnBetter.text = "better than before"
        dialogBinding.btnSame.text = "same as before"
        dialogBinding.btnWorse.text = "worse than before"

        dialogBinding.ivDialogClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnBetter.setOnClickListener {
            bottomSheetDialog.dismiss()
            finish()
        }

        dialogBinding.btnSame.setOnClickListener {
            //deleteJournal(journalEntry)
            bottomSheetDialog.dismiss()
            finish()
        }
        dialogBinding.btnWorse.setOnClickListener {
            //deleteJournal(journalEntry)
            bottomSheetDialog.dismiss()
            finish()
        }
        bottomSheetDialog.show()
    }
}
