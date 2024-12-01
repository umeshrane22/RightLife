package com.example.rlapp.ui.healthaudit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rlapp.ui.healthaudit.questionlist.Question;
import com.example.rlapp.ui.healthaudit.questionlist.QuestionData;

public class FormPagerAdapter extends FragmentStateAdapter {

    private QuestionData questionData;

    public FormPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Question question = questionData.getQuestionList().get(position);
        String questionType = question.getQuestion();

        switch (questionType) {
            case "dob":
                return FormPageFragment.newInstance(position, question);
            case "height":
                return HAFormHeighFragment.newInstance(position, question);
            case "weight":
                return HAFromWeightFragment.newInstance(position, question);
            case "waist":
                return HAFromWaistFragment.newInstance(position, question);
            case "bp_systolic":
                return HAFromBPFragment.newInstance(position, question);
            case "audio":
                return HAAudioURLFragment.newInstance(question);
            default:
                return FruitListFragment.newInstance(position, question);
        }
    }

    public void setData(QuestionData data) {
        this.questionData = data;
    }

    @Override
    public int getItemCount() {
        if (questionData != null)
            return questionData.getQuestionList().size(); // Number of pages in the form
        else
            return 0;
    }
}

