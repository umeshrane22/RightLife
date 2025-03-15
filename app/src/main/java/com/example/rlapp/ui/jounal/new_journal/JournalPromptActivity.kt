package com.example.rlapp.ui.jounal.new_journal

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.databinding.ActivityJournalPromptBinding
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JournalPromptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalPromptBinding
    private lateinit var adapter: PromptAdapter
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var questionsList: ArrayList<Question> = ArrayList()
    private var sectionList: ArrayList<Section> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalPromptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        val journalItem: JournalItem = intent.getSerializableExtra("Section") as JournalItem

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnInfo.setOnClickListener {
            Toast.makeText(this, "Info clicked", Toast.LENGTH_SHORT).show()
        }

        getSections(journalItem.id!!)

        setSelectedChipListener()

        // Setup RecyclerView
        adapter = PromptAdapter(questionsList, object :PromptAdapter.OnItemClickListener{
            override fun onItemClick(question: Question) {
                val intent =
                    Intent(this@JournalPromptActivity, Journal4QuestionsActivity::class.java).apply {
                        putExtra("Section", journalItem)
                        putExtra("Answer", question.question)
                    }
                startActivity(intent)
            }

            override fun onSwapClick(question: Question, position: Int) {

            }

        })
        binding.recyclerViewPrompts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPrompts.adapter = adapter
    }

    private fun addChip(name: String, isSelected: Boolean) {
        val chip = Chip(this)
        chip.id = View.generateViewId() // Generate unique ID
        chip.text = name
        chip.isCheckable = true
        chip.isChecked = false
        chip.chipCornerRadius = 50f
        chip.chipStrokeColor = ContextCompat.getColorStateList(
            this,
            R.color.menuselected
        )

        if (isSelected) {
            chip.isChecked = true
        }

        chip.textSize = 11f

        val heightInDp = 50 // or whatever height you want
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
                ContextCompat.getColor(this, R.color.menuselected),
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
                ContextCompat.getColor(this, R.color.chip_text_color)
            )
        )
        chip.chipBackgroundColor = colorStateList

        // Text color for selected state
        chip.setTextColor(textColorStateList)
        binding.chipGroup.addView(chip)
    }

    private fun setSelectedChipListener() {
        binding.chipGroup.setOnCheckedChangeListener { group: ChipGroup, checkedId: Int ->
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null) {
                var position = -1
                for (i in 0 until group.childCount) {
                    val child = group.getChildAt(i)
                    if (child.id == checkedId) {
                        position = i
                        break
                    }
                }

                // Use the position as needed
                if (position != -1) {
                    sectionList[position].id?.let { getQuestions(it) }
                }
            }
        }
    }


    private fun getSections(id: String, type: String = "section") {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getJournalSections(sharedPreferenceManager.accessToken, type, id)

        call.enqueue(object : Callback<JournalSectionResponse> {
            override fun onResponse(
                call: Call<JournalSectionResponse>,
                response: Response<JournalSectionResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.let { sectionList.addAll(it) }
                    for (i in 0 until sectionList.size) {
                        if (i == 0) {
                            sectionList[0].sectionName?.let { addChip(it, true) }
                            sectionList[0].id?.let { getQuestions(it) }
                        }
                        else
                            sectionList[i].sectionName?.let { addChip(it, false) }
                    }
                } else {
                    Toast.makeText(
                        this@JournalPromptActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JournalSectionResponse>, t: Throwable) {
                Toast.makeText(
                    this@JournalPromptActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun getQuestions(id: String, type: String = "question") {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getJournalQuestions(sharedPreferenceManager.accessToken, type, id)

        call.enqueue(object : Callback<JournalQuestionsResponse> {
            override fun onResponse(
                call: Call<JournalQuestionsResponse>,
                response: Response<JournalQuestionsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    questionsList.clear()
                    response.body()?.data?.let { questionsList.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@JournalPromptActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JournalQuestionsResponse>, t: Throwable) {
                Toast.makeText(
                    this@JournalPromptActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}
