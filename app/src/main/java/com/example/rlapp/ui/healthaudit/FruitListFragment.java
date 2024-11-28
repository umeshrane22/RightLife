package com.example.rlapp.ui.healthaudit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.questionlist.Option;
import com.example.rlapp.ui.healthaudit.questionlist.Question;

import java.util.ArrayList;

public class FruitListFragment extends Fragment {

    private TextView txt_question;
    private TextView txt_question_desc;
    private RecyclerView recyclerView;
    private OptionsAdapter optionsAdapter;
    private ArrayList<Option> selectedOptions = new ArrayList<>();
    private ArrayList<String> selectedOptionsString = new ArrayList<>();
    private static final String ARG_PAGE_INDEX = "page_index";
    private static final String ARG_QUESTION = "QUESTION";
    private int pageIndex;

    private Question question;
    private ArrayList<Option> optionsList = new ArrayList<>();
    boolean isMultipleSelection = true;
    private OnNextFragmentClickListener onNextFragmentClickListener;
    private Button btnOK;


    public static FruitListFragment newInstance(int pageIndex, Question question) {
        FruitListFragment fragment = new FruitListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putSerializable(ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onNextFragmentClickListener = (OnNextFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fruit_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        txt_question = view.findViewById(R.id.txt_question);
        txt_question_desc = view.findViewById(R.id.txt_question_desc);
        btnOK = view.findViewById(R.id.btn_ok);


        txt_question.setText(question.getQuestionTxt());

        optionsList.addAll(question.getOptions());


        int spanCount = 1;
        if (optionsList.size() >= 4)
            spanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (question.getInputType().equals("RADIO")) {
            isMultipleSelection = false;
        } else if (question.getInputType().equals("SELECT") && !question.getMultiple()) {
            isMultipleSelection = false;
        }

        if (isMultipleSelection) {
            txt_question_desc.setText("Choose all that apply to you");
            btnOK.setVisibility(View.VISIBLE);
        } else {
            txt_question_desc.setText("Choose any one option");
            btnOK.setVisibility(View.GONE);
        }

        optionsAdapter = new OptionsAdapter(optionsList, option -> {

            if (isMultipleSelection) {
                if (option.isSelected()) {
                    selectedOptions.add(option);
                    selectedOptionsString.add(option.getOptionPosition());
                } else {
                    selectedOptions.remove(option);
                    selectedOptionsString.remove(option.getOptionPosition());
                }

                btnOK.setEnabled(!selectedOptions.isEmpty());

            } else {
                if (!option.isSelected()) {
                    selectedOptions.add(option);
                    selectedOptionsString.add(option.getOptionPosition());
                } else {
                    selectedOptions.remove(option);
                    selectedOptionsString.remove(option.getOptionPosition());
                }
                navigateNext();
            }
        }, isMultipleSelection);

        if (isMultipleSelection)
            btnOK.setVisibility(View.VISIBLE);
        else btnOK.setVisibility(View.GONE);

        btnOK.setOnClickListener(view1 -> navigateNext());

        recyclerView.setAdapter(optionsAdapter);

        return view;
    }

    private void navigateNext() {
        onNextFragmentClickListener.getDataFromFragment(question.getQuestion(), selectedOptionsString);
        ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
    }

}