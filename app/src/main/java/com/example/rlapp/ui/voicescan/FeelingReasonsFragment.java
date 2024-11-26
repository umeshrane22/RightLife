package com.example.rlapp.ui.voicescan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.HealthAuditFormActivity;
import com.example.rlapp.ui.healthaudit.questionlist.Question;

import java.util.ArrayList;

public class FeelingReasonsFragment extends Fragment {

    private static final String ARG_PAGE_INDEX = "page_index";
    private static final String ARG_FEELINGS = "FEELINGS";
    private int pageIndex;
    private String feelings;

    private TextView txtFeelingsTitle, txtFeelingReason;
    private ImageView imgFeelings;
    private RelativeLayout relativeLayoutFeeling;
    private Question question;
    private RecyclerView recyclerView;
    private FeelingReasonsListAdapter adapter;
    private ArrayList<String> selectedOptionString = new ArrayList<>();
    private OnNextVoiceScanFragmentClickListener onNextVoiceScanFragmentClickListener;


    public static FeelingReasonsFragment newInstance(int pageIndex, String feelings, Question question) {
        FeelingReasonsFragment fragment = new FeelingReasonsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putString(ARG_FEELINGS, feelings);
        args.putSerializable(HealthAuditFormActivity.ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
            feelings = getArguments().getString(ARG_FEELINGS);
            question = (Question) getArguments().getSerializable(HealthAuditFormActivity.ARG_QUESTION);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onNextVoiceScanFragmentClickListener = (OnNextVoiceScanFragmentClickListener) context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feelings_reasons, container, false);

        txtFeelingsTitle = view.findViewById(R.id.txt_title_feeling);
        txtFeelingReason = view.findViewById(R.id.txt_title_reason);
        imgFeelings = view.findViewById(R.id.icon_feelings);
        txtFeelingsTitle.setText(feelings);
        txtFeelingReason.setText(question.getQuestionTxt());
        relativeLayoutFeeling = view.findViewById(R.id.rl_main_feeling_1);

        recyclerView = view.findViewById(R.id.recycler_view_reason);

        ((VoiceScanFromActivity) requireActivity()).nextButton.setVisibility(View.VISIBLE);

        ((VoiceScanFromActivity) requireActivity()).nextButton.setOnClickListener(view1 -> {
            onNextVoiceScanFragmentClickListener.onNextFragmentClick(question.getQuestion(), selectedOptionString);
            ((VoiceScanFromActivity) requireActivity()).navigateToNextPage("");
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new FeelingReasonsListAdapter(requireContext(), question.getOptions(), option -> {
            if (option.isSelected()) {
                selectedOptionString.add(option.getOptionText());
            } else {
                selectedOptionString.remove(option.getOptionText());
            }
        });

        recyclerView.setAdapter(adapter);

        switch (feelings) {
            case "Sad":
                imgFeelings.setImageResource(R.drawable.vociesscan_sad);
                relativeLayoutFeeling.setBackground(requireContext().getDrawable(R.drawable.toproundedvoicescan_sad));
                break;
            case "Stressed":
                imgFeelings.setImageResource(R.drawable.voicescan_stressed);
                relativeLayoutFeeling.setBackground(requireContext().getDrawable(R.drawable.toproundedvoicescan_stressed));
                break;
            case "Unsure":
                imgFeelings.setImageResource(R.drawable.voicescan_unsure);
                relativeLayoutFeeling.setBackground(requireContext().getDrawable(R.drawable.toproundedvoicescan_unsure));
                break;
            case "Relaxed":
                imgFeelings.setImageResource(R.drawable.voicescan_relaxed);
                relativeLayoutFeeling.setBackground(requireContext().getDrawable(R.drawable.toproundedvoicescan_relaxed));
                break;
            case "Happy":
                imgFeelings.setImageResource(R.drawable.voicescan_happy);
                relativeLayoutFeeling.setBackground(requireContext().getDrawable(R.drawable.toproundedvoicescan_happy));
                break;
        }

        return view;
    }
}
