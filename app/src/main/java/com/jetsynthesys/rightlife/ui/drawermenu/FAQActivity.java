package com.jetsynthesys.rightlife.ui.drawermenu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class FAQActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FAQAdapter faqAdapter;
    private FAQData faqData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
        recyclerView = findViewById(R.id.rv_faq_list);

        Gson gson = new Gson();
        faqData = gson.fromJson(loadJSONFromAsset(), FAQData.class);


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        faqAdapter = new FAQAdapter(this, faqData);

        recyclerView.setAdapter(faqAdapter);

    }


    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.faq_model);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
