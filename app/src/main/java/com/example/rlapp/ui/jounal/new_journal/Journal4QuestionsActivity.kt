package com.example.rlapp.ui.jounal.new_journal

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.databinding.ActivityJournalAnswerBinding
import com.example.rlapp.databinding.BottomsheetAddTagBinding
import com.example.rlapp.databinding.BottomsheetDeleteTagBinding
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Journal4QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalAnswerBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var tagsList1: ArrayList<String> = ArrayList()
    private var isExpanded1 = false
    private var tagsList2: ArrayList<String> = ArrayList()
    private var isExpanded2 = false
    private var tagsList3: ArrayList<String> = ArrayList()
    private var isExpanded3 = false
    private val visibleCount = 3
    private lateinit var journalItem: JournalItem
    private lateinit var answer: String
    private val journalQuestionCreateRequest = JournalQuestionCreateRequest()
    private var selectedTag1: String = ""
    private var selectedTag2: String = ""
    private var selectedTag3: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        journalItem = intent.getSerializableExtra("Section") as JournalItem
        answer = intent.getStringExtra("Answer").toString()
        val questionId = intent.getStringExtra("QuestionId")


        binding.tvEntryText.text = answer
        journalQuestionCreateRequest.title = journalItem.title
        journalQuestionCreateRequest.questionId = questionId
        journalQuestionCreateRequest.answer = answer


        setUpMoodList()
        getJournalTags()

        binding.imageEdit.setOnClickListener {
            finish()
        }

        binding.moreButton1.setOnClickListener {
            binding.chipGroup1.removeAllViews()
            isExpanded1 = !isExpanded1

            addPlusChip(binding.chipGroup1, 1)
            val itemsToShow = if (isExpanded1) tagsList1 else tagsList1.take(visibleCount)
            for (label in itemsToShow) {
                addChip(label, binding.chipGroup1, false, 1)
            }

            binding.moreButton1.text = if (isExpanded1) "Less ▲" else "More ⌄"
        }

        binding.moreButton2.setOnClickListener {
            binding.chipGroup2.removeAllViews()
            isExpanded2 = !isExpanded2

            addPlusChip(binding.chipGroup2, 2)
            val itemsToShow = if (isExpanded2) tagsList2 else tagsList2.take(visibleCount)
            for (label in itemsToShow) {
                addChip(label, binding.chipGroup2, false, 2)
            }

            binding.moreButton2.text = if (isExpanded2) "Less ▲" else "More ⌄"
        }

        binding.moreButton3.setOnClickListener {
            binding.chipGroup3.removeAllViews()
            isExpanded3 = !isExpanded3

            addPlusChip(binding.chipGroup3, 3)
            val itemsToShow = if (isExpanded3) tagsList3 else tagsList3.take(visibleCount)
            for (label in itemsToShow) {
                addChip(label, binding.chipGroup3, false, 3)
            }

            binding.moreButton3.text = if (isExpanded3) "Less ▲" else "More ⌄"
        }

        binding.btnSave.setOnClickListener {
            if (journalQuestionCreateRequest.emotion.isNullOrEmpty()){
                Toast.makeText(this,"Please select emotion",Toast.LENGTH_SHORT).show()
            }else if (selectedTag1.isNullOrEmpty() || selectedTag2.isNullOrEmpty() || selectedTag3.isNullOrEmpty()){
                Toast.makeText(this,"Please select tag",Toast.LENGTH_SHORT).show()
            }else {
                journalQuestionCreateRequest.tags?.add(selectedTag1)
                journalQuestionCreateRequest.tags?.add(selectedTag2)
                journalQuestionCreateRequest.tags?.add(selectedTag3)
                createJournal()
            }
        }

    }

    private fun showAddEditBottomSheet(
        isFromEdit: Boolean,
        chipGroup: ChipGroup,
        type: Int,
        chip: Chip,
        position: Int
    ) {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val dialogBinding = BottomsheetAddTagBinding.inflate(layoutInflater)
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

        if (isFromEdit) {
            dialogBinding.btnAdd.text = "Update"
            dialogBinding.edtTag.setText(chip.text.toString())
        }

        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnAdd.setOnClickListener {
            if (dialogBinding.edtTag.text.isNullOrEmpty()) {
                Toast.makeText(this, "Tag should not be empty", Toast.LENGTH_SHORT).show()
            } else {
                if (isFromEdit) {
                    val journalUpdateTagsRequest = JournalUpdateTagsRequest()
                    journalUpdateTagsRequest.tag = dialogBinding.edtTag.text.toString()
                    journalUpdateTagsRequest.outerIndex = type - 1
                    journalUpdateTagsRequest.innerIndex = position
                    updateJournalTag(
                        journalUpdateTagsRequest,
                        dialogBinding.edtTag.text.toString(),
                        chip,
                        type,
                        position
                    )
                } else {
                    val journalAddTagsRequest = JournalAddTagsRequest()
                    journalAddTagsRequest.tag = dialogBinding.edtTag.text.toString()
                    journalAddTagsRequest.outerIndex = type - 1
                    addJournalTag(
                        journalAddTagsRequest,
                        dialogBinding.edtTag.text.toString(),
                        chipGroup,
                        type
                    )
                }
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
    }

    private fun showDeleteBottomSheet(
        chipGroup: ChipGroup,
        type: Int,
        chip: Chip,
        position: Int
    ) {
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


        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnYes.setOnClickListener {
            val journalDeleteTagRequest = JournalDeleteTagRequest()
            journalDeleteTagRequest.outerIndex = type - 1
            journalDeleteTagRequest.innerIndex = position
            deleteTag(journalDeleteTagRequest, chip, chipGroup, type, position)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun setUpMoodList() {
        val moodList = listOf(
            Mood("Happy", R.drawable.ic_happy),
            Mood("Relaxed", R.drawable.ic_relaxed),
            Mood("Unsure", R.drawable.ic_unsure),
            Mood("Stressed", R.drawable.ic_stressed),
            Mood("Sad", R.drawable.ic_sad)
        )

        val adapter = JournalMoodAdapter(moodList) { selectedMood ->
            journalQuestionCreateRequest.emotion = selectedMood.name
        }

        binding.moodRecyclerView.adapter = adapter
        binding.moodRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun addChip(name: String, chipGroup: ChipGroup, isFromAddTag: Boolean, type: Int) {
        val chip = Chip(this)
        chip.id = View.generateViewId() // Generate unique ID
        chip.text = name
        chip.isCheckable = true
        chip.isChecked = false
        chip.chipCornerRadius = 50f
        chip.chipStrokeColor = ContextCompat.getColorStateList(
            this,
            R.color.color_think_right
        )

        chip.textSize = 13f

        val heightInDp = 60 // or whatever height you want
        val heightInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            heightInPx
        )

        chip.layoutParams = layoutParams

        // Set different colors for selected state
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.color_think_right),
                ContextCompat.getColor(this, R.color.white)
            )
        )

        val textColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.color_think_right)
            )
        )
        chip.chipBackgroundColor = colorStateList

        chip.setOnLongClickListener { view ->
            val position = chipGroup.indexOfChild(view) - 1
            showCustomPopupMenu(view, chipGroup, type, chip, position)
            true
        }

        chip.setOnClickListener { view ->
            val position = chipGroup.indexOfChild(view) -1
            when (type) {
                1 -> selectedTag1 = tagsList1[position]
                2 -> selectedTag2 = tagsList2[position]
                3 -> selectedTag3 = tagsList3[position]
            }
        }

        // Text color for selected state
        chip.setTextColor(textColorStateList)
        if (isFromAddTag)
            chipGroup.addView(chip, 1)
        else
            chipGroup.addView(chip)
    }

    private fun addPlusChip(chipGroup: ChipGroup, type: Int) {
        val addChip = Chip(this).apply {
            text = "+"
            isClickable = true
            isCheckable = false
            textSize = 22f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            chipCornerRadius = 50f
            chipStrokeColor = ContextCompat.getColorStateList(
                this@Journal4QuestionsActivity,
                R.color.color_think_right
            )
            setTextColor(getColor(R.color.color_think_right))
            setOnClickListener {
                showAddEditBottomSheet(false, chipGroup, type, this, 0)
            }
        }

        val heightInDp = 60 // or whatever height you want
        val heightInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val layoutParams = ViewGroup.MarginLayoutParams(
            heightInPx,
            heightInPx
        )

        addChip.layoutParams = layoutParams

        chipGroup.addView(addChip)
    }

    private fun showCustomPopupMenu(
        view: View,
        chipGroup: ChipGroup,
        type: Int,
        chip: Chip,
        position: Int
    ) {
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
                showAddEditBottomSheet(true, chipGroup, type, chip, position)
            } else {
                showDeleteBottomSheet(chipGroup, type, chip, position)
            }
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(view, -100, 0)
    }


    private fun getJournalTags() {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getJournalTags(sharedPreferenceManager.accessToken)

        call.enqueue(object : Callback<JournalTagsResponse> {
            override fun onResponse(
                call: Call<JournalTagsResponse>,
                response: Response<JournalTagsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    addPlusChip(binding.chipGroup1, 1)
                    addPlusChip(binding.chipGroup2, 2)
                    addPlusChip(binding.chipGroup3, 3)
                    response.body()?.data?.tags?.let {
                        tagsList1.addAll(it[0])
                        tagsList2.addAll(it[1])
                        tagsList3.addAll(it[2])
                    }

                    if (tagsList1.size == visibleCount)
                        binding.moreButton1.visibility = View.GONE
                    if (tagsList2.size == visibleCount)
                        binding.moreButton2.visibility = View.GONE
                    if (tagsList3.size == visibleCount)
                        binding.moreButton3.visibility = View.GONE

                    val itemsToShow1 = tagsList1.take(visibleCount)
                    for (label in itemsToShow1) {
                        addChip(label, binding.chipGroup1, false, 1)
                    }

                    val itemsToShow2 = tagsList2.take(visibleCount)
                    for (label in itemsToShow2) {
                        addChip(label, binding.chipGroup2, false, 2)
                    }

                    val itemsToShow3 = tagsList3.take(visibleCount)
                    for (label in itemsToShow3) {
                        addChip(label, binding.chipGroup3, false, 3)
                    }

                } else {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JournalTagsResponse>, t: Throwable) {
                Toast.makeText(
                    this@Journal4QuestionsActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun addJournalTag(
        journalAddTagsRequest: JournalAddTagsRequest,
        name: String,
        chipGroup: ChipGroup,
        type: Int
    ) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call =
            apiService.addJournalTag(sharedPreferenceManager.accessToken, journalAddTagsRequest)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    addChip(name, chipGroup, true, type)
                    when (type) {
                        1 -> tagsList1.add(0, name)
                        2 -> tagsList2.add(0, name)
                        3 -> tagsList3.add(0, name)
                    }
                } else {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@Journal4QuestionsActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateJournalTag(
        journalUpdateTagsRequest: JournalUpdateTagsRequest,
        name: String,
        chip: Chip,
        type: Int,
        position: Int
    ) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call =
            apiService.updateJournalTag(
                sharedPreferenceManager.accessToken,
                journalUpdateTagsRequest
            )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    when (type) {
                        1 -> tagsList1[position] = name
                        2 -> tagsList2[position] = name
                        3 -> tagsList3[position] = name
                    }
                    chip.text = name
                } else {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@Journal4QuestionsActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun deleteTag(
        journalDeleteTagRequest: JournalDeleteTagRequest,
        chip: Chip,
        chipGroup: ChipGroup,
        type: Int,
        position: Int
    ) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call =
            apiService.deleteJournalTag(
                sharedPreferenceManager.accessToken,
                journalDeleteTagRequest
            )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    when (type) {
                        1 -> tagsList1.removeAt(position)
                        2 -> tagsList2.removeAt(position)
                        3 -> tagsList3.removeAt(position)
                    }
                    chipGroup.removeView(chip)
                } else {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@Journal4QuestionsActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun createJournal() {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call =
            apiService.createJournal(
                sharedPreferenceManager.accessToken,
                journalQuestionCreateRequest
            )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this@Journal4QuestionsActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@Journal4QuestionsActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}