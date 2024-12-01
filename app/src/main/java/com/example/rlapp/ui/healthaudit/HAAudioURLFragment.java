package com.example.rlapp.ui.healthaudit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.questionlist.Question;

import static com.example.rlapp.ui.healthaudit.HealthAuditFormActivity.ARG_QUESTION;

public class HAAudioURLFragment extends Fragment {
    private TextView tvQuestion, tvComingSoon;
    private Question question;
    private static final String ARGS_QUESTION = "Question";

    public static Fragment newInstance(Question question) {
        HAAudioURLFragment fragment = new HAAudioURLFragment();
        Bundle args = new Bundle();
        ;
        args.putSerializable(ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(HealthAuditFormActivity.ARG_QUESTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ha_audio_url, container, false);
        tvQuestion = view.findViewById(R.id.dobPrompt);
        tvComingSoon = view.findViewById(R.id.comingSoon);

        tvQuestion.setText(question.getQuestionTxt());
        tvComingSoon.setText(question.getQuestion() +  " feature cooming soon");

        return view;
    }
}
