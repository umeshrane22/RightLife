package com.jetsynthesys.rightlife.ui.voicescan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Question;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.QuestionData;


public class VoiceScanFormPagerAdapter extends FragmentStateAdapter {

    private String feelings = "Sad";
    private QuestionData questionData;

    public VoiceScanFormPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Question question = questionData.getQuestionList().get(position);
        String questionType = question.getQuestion();
        switch (questionType) {
            case "feeling":
                return FeelingListFragment.newInstance(position,question);
            case "reason":
                return FeelingReasonsFragment.newInstance(position, feelings,question);
            case "audio":
                return VoiceRecordFragment.newInstance(position,question);
            // Continue for each page up to

            default:
                return FeelingListFragment.newInstance(position, question);
            //throw new IllegalArgumentException("Invalid page position");
        }
    }

    public void setData(QuestionData data) {
        this.questionData = data;
    }

    public void setFeelings(String feelings) {
        this.feelings = feelings;
    }

    @Override
    public int getItemCount() {
        if (questionData != null)
            return questionData.getQuestionList().size(); // Number of pages in the form
        else
            return 0;
    }
}



