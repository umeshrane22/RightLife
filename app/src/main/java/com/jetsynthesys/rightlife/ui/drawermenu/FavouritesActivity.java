package com.jetsynthesys.rightlife.ui.drawermenu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> favTypes = new ArrayList<>();
    private FavouritesButtonAdapter favouritesButtonAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.rv_favourites);

        favTypes.add("All");
        favTypes.add("Think Right");
        favTypes.add("Eat Right");
        favTypes.add("Move Right");
        favTypes.add("Sleep Right");
        favTypes.add("My Health");

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        favouritesButtonAdapter = new FavouritesButtonAdapter(this, favTypes, selectedValue -> {
            /***
             * Here code for selected button
             */
        });
        recyclerView.setAdapter(favouritesButtonAdapter);

    }
}
