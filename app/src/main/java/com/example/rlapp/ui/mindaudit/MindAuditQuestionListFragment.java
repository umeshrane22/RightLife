package com.example.rlapp.ui.mindaudit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.ui.mindaudit.questions.Question;
import com.example.rlapp.ui.mindaudit.questions.ScoringPattern;

import java.util.ArrayList;

public class MindAuditQuestionListFragment extends Fragment {
    private static final String ARG_QUESTION = "QUESTION";
    private Question question;
    private TextView txt_question;
    private RecyclerView recyclerView;
    //private OnNextFragmentClickListener onNextFragmentClickListener;
    private MindAuditOptionsAdapter adapter;

    public static MindAuditQuestionListFragment newInstance(Question question) {
        MindAuditQuestionListFragment fragment = new MindAuditQuestionListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //onNextFragmentClickListener = (OnNextFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mind_audit_question_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        txt_question = view.findViewById(R.id.txt_question);

        txt_question.setText(question.getQuestion());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new MindAuditOptionsAdapter(requireContext(), (ArrayList<ScoringPattern>) question.getScoringPattern(), scoringPattern -> {
            if (question.isContinueFurtherIfTrue()) {
                boolean b = (boolean) scoringPattern.getScore();
                if (!b) {
                    ((MAAssessmentQuestionaireActivity) requireActivity()).submitButton.setVisibility(View.VISIBLE);
                }else {
                    ((MAAssessmentQuestionaireActivity) requireActivity()).navigateToNextPage();
                }
            } else {
                ((MAAssessmentQuestionaireActivity) requireActivity()).navigateToNextPage();
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}