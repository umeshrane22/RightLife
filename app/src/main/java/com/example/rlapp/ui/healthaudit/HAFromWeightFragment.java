package com.example.rlapp.ui.healthaudit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.questionlist.Question;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.rlapp.ui.utility.ConversionUtils.convertKgToLbs;
import static com.example.rlapp.ui.utility.ConversionUtils.convertLbsToKgs;

public class HAFromWeightFragment extends Fragment {

    private TextView dateText, edtSpinnerKgsLbs;

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;

    private EditText edtWeight;
    private Button btnOk;
    private OnNextFragmentClickListener onNextFragmentClickListener;
    private Question question;

    public static HAFromWeightFragment newInstance(int pageIndex, Question question) {
        HAFromWeightFragment fragment = new HAFromWeightFragment();
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
        onNextFragmentClickListener = (OnNextFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_health_weight, container, false);

        dateText = view.findViewById(R.id.dateText);

        getViews(view);

        return view;
    }

    private void getViews(View view) {
        edtWeight = view.findViewById(R.id.edt_weight);
        edtSpinnerKgsLbs = view.findViewById(R.id.edt_spinner_kgs_lbs);
        btnOk = view.findViewById(R.id.btn_ok);

        TextView txtQuestionText = view.findViewById(R.id.dobPrompt);
        txtQuestionText.setText(question.getQuestionTxt());

        edtSpinnerKgsLbs.setOnClickListener(view1 -> openPopupForWeight());

        btnOk.setOnClickListener(view12 -> {

            if (edtWeight.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), requireActivity().getString(R.string.weight_error), Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<String> data = new ArrayList<>();
                data.add(edtWeight.getText().toString());
                onNextFragmentClickListener.getDataFromFragment(question.getQuestion(), data);
                ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
            }
        });
    }

    private void openPopupForWeight() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), edtSpinnerKgsLbs);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_weight, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (!edtSpinnerKgsLbs.getText().toString().equals(menuItem.toString())) {
                String weight;
                if (requireActivity().getString(R.string.str_kgs).equals(menuItem.toString())) {
                    weight = convertKgToLbs(edtWeight.getText().toString());
                } else {
                    weight = convertLbsToKgs(edtWeight.getText().toString());
                }
                edtWeight.setText(weight);
            }
            edtSpinnerKgsLbs.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

}



