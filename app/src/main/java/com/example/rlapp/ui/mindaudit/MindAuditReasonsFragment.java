package com.example.rlapp.ui.mindaudit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.FormData;
import com.example.rlapp.ui.healthaudit.FormViewModel;
import com.example.rlapp.ui.healthaudit.Fruit;
import com.example.rlapp.ui.healthaudit.FruitAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MindAuditReasonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MindAuditReasonslistAdapter adapter;
    ArrayList<Fruit> selectedfruitList = new ArrayList<>();
    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;

    private FormViewModel formViewModel;


    public static MindAuditReasonsFragment newInstance(int pageIndex) {
        MindAuditReasonsFragment fragment = new MindAuditReasonsFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mindaudit_reason_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);


        ArrayList<Fruit> fruitList = new ArrayList<>();
        // Add fruits to the list

        fruitList.add(new Fruit("Fluctuating Mood", false));
        fruitList.add(new Fruit("Crying Spells", false));
        fruitList.add(new Fruit("Excessive Tiredness", false));
        fruitList.add(new Fruit("Loss of Zeal", false));
        fruitList.add(new Fruit("Low Confidence", false));


        int spanCount = fruitList.size() > 6 ? 2 : 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);


        adapter = new MindAuditReasonslistAdapter(fruitList, fruit -> {
            // Handle fruit selection here
            Log.d("FruitSelection", "Fruit clicked: " + fruit.getName());

            if (fruit.isSelected()) {
                // Add to favorites
                Log.d("FruitSelection", "Added to favorites: " + fruit.getName());
                selectedfruitList.add(fruit);
            } else {
                // Remove from favorites
                Log.d("FruitSelection", "Removed from favorites: " + fruit.getName());
                selectedfruitList.remove(fruit);
            }
            saveData();
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    public Map<String, ArrayList> collectData() {
        Map<String, ArrayList> data = new HashMap<>();
        // Add values from views in this fragment to the data map
        data.put("fruit", selectedfruitList);
        return data;
    }

    private void saveData() {
        FormData data = new FormData();
        data.setAnswer("User Fruit");
        data.setSelected(true);
        formViewModel.saveFormData(pageIndex, data); // Replace `getPageIndex()` with the actual page number
    }
}