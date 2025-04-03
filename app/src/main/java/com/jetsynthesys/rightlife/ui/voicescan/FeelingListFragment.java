package com.jetsynthesys.rightlife.ui.voicescan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.healthaudit.HealthAuditFormActivity;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Question;
import com.jetsynthesys.rightlife.ui.utility.DateTimeUtils;

import java.util.ArrayList;

public class FeelingListFragment extends Fragment {

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private Question question;
    private OnNextVoiceScanFragmentClickListener onNextVoiceScanFragmentClickListener;
    private FeelingListAdapter adapter;

    public static FeelingListFragment newInstance(int pageIndex, Question question) {
        FeelingListFragment fragment = new FeelingListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putSerializable(HealthAuditFormActivity.ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
            question = (Question) getArguments().getSerializable(HealthAuditFormActivity.ARG_QUESTION);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onNextVoiceScanFragmentClickListener = (OnNextVoiceScanFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feelings_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_feelings);

        TextView tvDateTime = view.findViewById(R.id.txt_title_date);
        tvDateTime.setText(DateTimeUtils.getDateTime());

        adapter = new FeelingListAdapter(requireContext(), question.getOptions(), option -> {
            ArrayList<String> data = new ArrayList<>();
            data.add(option.getOptionText());
            onNextVoiceScanFragmentClickListener.onNextFragmentClick(question.getQuestion(), data);
            ((VoiceScanFromActivity) requireActivity()).navigateToNextPage(option.getOptionText());
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((VoiceScanFromActivity) requireActivity()).nextButton.setVisibility(View.GONE);

        return view;
    }
}
