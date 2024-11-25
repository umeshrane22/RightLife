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
import androidx.lifecycle.ViewModelProvider;

import com.example.rlapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HAFormHeighFragment extends Fragment {

    TextView dateText;
    private TextView txtFt, txtInch;
    private EditText edtFt, edtInch, edtCms, edtSpinner;
    private Button btnOK;

    private FormViewModel formViewModel;
    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private OnNextFragmentClickListener onNextFragmentClickListener;

    public static HAFormHeighFragment newInstance(int pageIndex) {
        HAFormHeighFragment fragment = new HAFormHeighFragment();
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
        View view = inflater.inflate(R.layout.page_health_height, container, false);

        getViews(view);

        return view;
    }

    private void getViews(View view) {
        txtFt = view.findViewById(R.id.txt_ft);
        txtInch = view.findViewById(R.id.txt_inch);

        edtCms = view.findViewById(R.id.edt_cms);
        edtFt = view.findViewById(R.id.edt_ft);
        edtInch = view.findViewById(R.id.edt_inch);
        edtSpinner = view.findViewById(R.id.edt_spinner);
        btnOK = view.findViewById(R.id.btn_ok);

        edtSpinner.setOnClickListener(view1 -> {
            openPopUp();
        });

        btnOK.setOnClickListener(view12 -> {
            ArrayList<String> data = new ArrayList<>();
            if (requireActivity().getString(R.string.str_cms).equals(edtSpinner.getText().toString())) {
                if (edtCms.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), requireActivity().getString(R.string.height_error), Toast.LENGTH_SHORT).show();
                } else {
                    data.add(edtCms.getText().toString());
                    navigateNext(data);
                }
            } else {
                if (edtInch.getText().toString().isEmpty() && edtFt.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), requireActivity().getString(R.string.height_error), Toast.LENGTH_SHORT).show();
                } else {
                    data.add(edtFt.getText().toString() + "." + edtInch.getText().toString());
                    navigateNext(data);
                }
            }
        });
    }

    private void navigateNext(ArrayList<String> data) {
        onNextFragmentClickListener.getDataFromFragment("height", data);
        ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
    }

    private void openPopUp() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), edtSpinner);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_ft_inch_cms, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (requireActivity().getString(R.string.str_cms).equals(menuItem.toString())) {
                edtCms.setVisibility(View.VISIBLE);
                edtInch.setVisibility(View.GONE);
                edtFt.setVisibility(View.GONE);
                txtInch.setVisibility(View.GONE);
                txtFt.setVisibility(View.GONE);
            } else {
                edtCms.setVisibility(View.GONE);
                edtInch.setVisibility(View.VISIBLE);
                edtFt.setVisibility(View.VISIBLE);
                txtInch.setVisibility(View.VISIBLE);
                txtFt.setVisibility(View.VISIBLE);
            }
            edtSpinner.setText(menuItem.toString());
            return true;
        });

        popupMenu.show();
    }

    /*private void handleButtonClick() {
        // Handle button click logic for Page 1
        Toast.makeText(getContext(), "Button on Page 1 clicked", Toast.LENGTH_SHORT).show();
        showDatePicker();
    }*/

    /*private void showDatePicker() {
        if (getContext() == null) {
            return; // Exit if context is null
        }
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    dateText.setText(selectedDate);
                    saveData();
                }, year, month, day);
        datePickerDialog.show();
    }*/

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



