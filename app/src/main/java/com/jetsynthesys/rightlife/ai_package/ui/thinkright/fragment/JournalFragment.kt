package com.jetsynthesys.rightlife.ai_package.ui.thinkright.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.databinding.FragmentJournalBinding

class JournalFragment(emojiRes:Int) : BaseFragment<FragmentJournalBinding>() {

    var journalAnswer = ""
    var emojis = emojiRes

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentJournalBinding
        get() = FragmentJournalBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<LinearLayout>(R.id.btn_save).setOnClickListener {
            journalAnswer = view.findViewById<EditText>(R.id.journalEditText).text.toString()
            navigateToFragment(MoodTrackerFragment(journalAnswer,emojis,"JournalFragment"),"MoodTrackerFragment")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(MoodTrackerFragment(journalAnswer,emojis, "JournalFragment"),"MoodTrackerFragment")
            }
        })

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(MoodTrackerFragment(journalAnswer, emojis,"JournalFragment"),"MoodTrackerFragment")
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}
