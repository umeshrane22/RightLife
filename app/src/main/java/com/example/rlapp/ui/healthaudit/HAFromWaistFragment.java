package com.example.rlapp.ui.healthaudit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.questionlist.Question;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HAFromWaistFragment extends Fragment {

    TextView dateText;

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;

    private EditText edtSpinnerWaist, edtWaist;
    private Button btnOK;
    private OnNextFragmentClickListener onNextFragmentClickListener;
    private Question question;

    public static HAFromWaistFragment newInstance(int pageIndex, Question question) {
        HAFromWaistFragment fragment = new HAFromWaistFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putSerializable(HealthAuditFormActivity.ARG_QUESTION,question);
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
        View view = inflater.inflate(R.layout.page_health_waist, container, false);

        dateText = view.findViewById(R.id.dateText);

        getViews(view);
        return view;
    }

    private void getViews(View view) {
        edtWaist = view.findViewById(R.id.edt_waist);
        edtSpinnerWaist = view.findViewById(R.id.edt_spinner_waist);
        btnOK = view.findViewById(R.id.btn_ok);

        edtSpinnerWaist.setOnClickListener(view1 -> {
            openPopupForWaist();
        });

        btnOK.setOnClickListener(view12 -> {
            if (edtWaist.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), requireActivity().getString(R.string.waist_error), Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<String> data = new ArrayList<>();
                data.add(edtWaist.getText().toString());
                onNextFragmentClickListener.getDataFromFragment(question.getQuestion(), data);
                ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
            }
        });
    }

    private void openPopupForWaist() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), edtSpinnerWaist);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_waist, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtSpinnerWaist.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }
}



