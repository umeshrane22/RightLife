package com.example.rlapp.ui.healthaudit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rlapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HAFromBPFragment extends Fragment {

    TextView dateText;

    private FormViewModel formViewModel;
    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private EditText edtSystolicBP, edtDiastolicBP;
    private Button btnOK;
    private OnNextFragmentClickListener onNextFragmentClickListener;

    public static HAFromBPFragment newInstance(int pageIndex) {
        HAFromBPFragment fragment = new HAFromBPFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
        }

        formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onNextFragmentClickListener = (OnNextFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_health_bp, container, false);

        formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);

        ImageView button1 = view.findViewById(R.id.calendarButton);
        dateText = view.findViewById(R.id.dateText);

        // Set up listeners and interactions for views specific to this page
        button1.setOnClickListener(v -> handleButtonClick());

        getViews(view);
        return view;
    }

    private void getViews(View view) {
        edtDiastolicBP = view.findViewById(R.id.edt_diastolic_bp);
        edtSystolicBP = view.findViewById(R.id.edt_systolic_bp);
        btnOK = view.findViewById(R.id.btn_ok);

        btnOK.setOnClickListener(view1 -> {
            if (edtDiastolicBP.getText().toString().isEmpty() || edtSystolicBP.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), requireActivity().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            } else {
                int diastolicBP = Integer.parseInt(edtDiastolicBP.getText().toString());
                int systolicBP = Integer.parseInt(edtSystolicBP.getText().toString());
                if (systolicBP < 40 || systolicBP > 250) {
                    Toast.makeText(getContext(), requireActivity().getString(R.string.systolic_range_error), Toast.LENGTH_SHORT).show();
                } else if (diastolicBP < 30 || diastolicBP > 300) {
                    Toast.makeText(getContext(), requireActivity().getString(R.string.diastolic_range_error), Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> data = new ArrayList<>();
                    data.add(edtSystolicBP.getText().toString());
                    onNextFragmentClickListener.getDataFromFragment("bp_systolic", data);
                    data.clear();
                    data.add(edtDiastolicBP.getText().toString());
                    onNextFragmentClickListener.getDataFromFragment("bp_diastolic", data);
                    ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
                }
            }

        });
    }

    private void handleButtonClick() {
        // Handle button click logic for Page 1
        Toast.makeText(getContext(), "Button on Page 1 clicked", Toast.LENGTH_SHORT).show();
        showDatePicker();
    }

    private void showDatePicker() {
        if (getContext() == null) {
            return; // Exit if context is null
        }
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateText.setText(selectedDate);
                        saveData();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Method to collect data from this fragment
    public Map<String, ArrayList> collectData() {
        ArrayList datalist = new ArrayList();
        Map<String, ArrayList> data = new HashMap<>();
        // Add values from views in this fragment to the data map
        datalist.add(dateText.getText().toString());
        data.put("fruit", datalist);
        return data;
    }

    private void saveData() {
        FormData data = new FormData();
        data.setAnswer("User answer");
        data.setSelected(true);

        // Save data in ViewModel with page identifier (e.g., 0 for first page)
        formViewModel.saveFormData(pageIndex, data);
    }
}



