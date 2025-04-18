package com.jetsynthesys.rightlife.ai_package.ui.thinkright.fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.AddEmojiRequest
import com.jetsynthesys.rightlife.ai_package.model.AddToolRequest
import com.jetsynthesys.rightlife.ai_package.model.BaseResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordEmotionDialogFragment : BottomSheetDialogFragment() {

    val activities = listOf("Driving", "Eating", "Fitness", "Resting", "Hobbies")
    val withWhom = listOf("By Myself", "Pets", "Co–workers", "Family", "Friends")
    val locations = listOf("Commuting", "Home", "Outside", "School")
    private var tagsList: ArrayList<String> = arrayListOf()

    private lateinit var activityChips: FlexboxLayout
    private lateinit var withChips: FlexboxLayout
    private lateinit var locationChips: FlexboxLayout
    private lateinit var editEmotion: ImageView
    private lateinit var emotionLabel: TextView
    private lateinit var baseResponse: BaseResponse
    var emojiSelected = 0
    private lateinit var progressDialog: ProgressDialog

    interface BottomSheetListener {
        fun onDataReceived(data: Int)
    }

    private var listener: BottomSheetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.record_emotion_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        activityChips = view.findViewById(R.id.activityFlexbox)
        withChips = view.findViewById(R.id.withFlexbox)
        locationChips = view.findViewById(R.id.locationFlexbox)
        editEmotion = view.findViewById(R.id.emotionIcon)
        emotionLabel = view.findViewById(R.id.emotionLabel)
        val addJournalEntry: TextView = view.findViewById(R.id.addJournalEntry)
        val journalEditText: EditText = view.findViewById(R.id.journalEditText)

        addJournalEntry.setOnClickListener {
            addJournalEntry.visibility = View.GONE
            if (journalEditText.visibility == View.GONE) {
                journalEditText.visibility = View.VISIBLE
                journalEditText.requestFocus()
            } else {
                addJournalEntry.visibility = View.GONE
               // journalEditText.visibility = View.GONE
            }
        }
        editEmotion.setOnClickListener {
            showEmotionSelector { selectedEmoji ->
                emojiSelected = selectedEmoji
                editEmotion.setImageResource(selectedEmoji)
                if (selectedEmoji == R.drawable.happy_icon){
                    emotionLabel.setText("Happy")
                }else if (selectedEmoji == R.drawable.relaxed_icon){
                    emotionLabel.setText("Relaxed")
                }else if (selectedEmoji == R.drawable.unsure_icon){
                    emotionLabel.setText("Unsure")
                }else if (selectedEmoji == R.drawable.stressed_icon){
                    emotionLabel.setText("Stressed")
                }else if (selectedEmoji == R.drawable.sad_icon){
                    emotionLabel.setText("Sad")
                }
            }
        }
        view.findViewById<ImageView>(R.id.closeButton).setOnClickListener {
            dismiss()
        }

        setupChips(activityChips, activities, "Add Activity")
        setupChips(withChips, withWhom, "With Whom")
        setupChips(locationChips, locations, "Add Location")

        view.findViewById<LinearLayout>(R.id.btn_save).setOnClickListener {
            listener?.onDataReceived(emojiSelected)
            sendEmotionData()
            Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun sendEmotionData() {
            progressDialog.show()
            val token = SharedPreferenceManager.getInstance(requireActivity()).accessToken
            // val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdlM2ZiMjdiMzNlZGZkNzRlMDY5OWFjIiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImRldmljZUlkIjoiVEUxQS4yNDAyMTMuMDA5IiwibWF4RGV2aWNlUmVhY2hlZCI6ZmFsc2UsInR5cGUiOiJhY2Nlc3MtdG9rZW4ifSwiaWF0IjoxNzQzMDU2OTEwLCJleHAiOjE3NTg3ODE3MTB9.gYLi895fpb4HGitALoGDRwHw3MIDCjYXTyqAKDNjS0A"
            val call = ApiClient.apiService.addThinkJournalEmoji(token,
                AddEmojiRequest(title = "", questionId ="", answer = "", emotion = emotionLabel.text.toString(), tags = tagsList )
            )
            call.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        if (response.body()!=null) {
                            baseResponse = response.body()!!
                            Toast.makeText(requireContext(), "${baseResponse.successMessage}", Toast.LENGTH_SHORT).show()
                            Log.e("Success", "Response is successful: ${response.isSuccessful}")
                        }
                    } else {
                        Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }
                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    Log.e("Error", "API call failed: ${t.message}")
                    Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            })
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
                    dialog.dismiss()
                }
            }
            emotionContainer.addView(emojiView)
        }

        dialog.show()
    }

    private fun setupChips(flexbox: FlexboxLayout, items: List<String>, s: String) {
        flexbox.removeAllViews()

        items.forEach { text ->
            flexbox.addView(createChip(text, flexbox,s))
        }

        // Add "+" chip at the end
        val addChip = createChip("+", flexbox,s, isAddButton = true)
        flexbox.addView(addChip)
    }

    private fun createChip(text: String, flexbox: FlexboxLayout,s:String, isAddButton: Boolean = false): TextView {
        val chip = TextView(requireContext()).apply {
            this.text = text
            setPadding(40, 20, 40, 20)
            setTextColor(ContextCompat.getColor(context, R.color.brown))
            background = ContextCompat.getDrawable(context, R.drawable.chip_selector_bg)
            isSelected = false
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                rightMargin = 24
                bottomMargin = 24
            }
            setOnClickListener {
                if (isAddButton) {
                    showAddChipDialog(flexbox,s)
                } else {
                    isSelected = !isSelected
                    background = ContextCompat.getDrawable(context, R.drawable.chip_selector_bg)
                    setTextColor(
                        ContextCompat.getColor(context, if (isSelected) android.R.color.white else R.color.brown)
                    )
                }
            }
        }
        return chip
    }

    private fun showAddChipDialog(flexbox: FlexboxLayout, s: String) {
        val input = EditText(requireContext())
        input.hint = "Enter activity"

        AlertDialog.Builder(requireContext())
            .setTitle(s)
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val newChipText = input.text.toString().trim()
                if (newChipText.isNotEmpty()) {
                    val chip = createChip(newChipText, flexbox,s)
                    // Insert before the "+" chip
                    tagsList.add(newChipText)
                    flexbox.addView(chip, flexbox.childCount - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}