package com.example.rlapp.ui.drawermenu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;

import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FAQAdapter faqAdapter;
    private ArrayList<FAQData> faqDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
        recyclerView = findViewById(R.id.rv_faq_list);

        ArrayList<QuestionAns> questionAnsGeneral = new ArrayList<>();
        ArrayList<QuestionAns> questionAnsUsage = new ArrayList<>();
        ArrayList<QuestionAns> questionAnsSubscription = new ArrayList<>();
        ArrayList<QuestionAns> questionAnsTechnical = new ArrayList<>();
        ArrayList<QuestionAns> questionAnsPrivacy = new ArrayList<>();
        ArrayList<QuestionAns> questionAnsContent = new ArrayList<>();
        ArrayList<QuestionAns> questionAnsUser = new ArrayList<>();

        questionAnsGeneral.add(new QuestionAns("Question : How is RightLife different from other apps?",
                "RightLife stands out by blending technology and content, offering unique features like diagnostic "));
        questionAnsUsage.add(new QuestionAns("Question : 111","Answer 1111"));
        questionAnsUsage.add(new QuestionAns("Question : 222","Answer 222"));
        questionAnsSubscription.add(new QuestionAns("Question : How is RightLife different from other apps?",
                "RightLife stands out by blending technology and content, offering unique features like diagnostic "));
        questionAnsSubscription.add(new QuestionAns("Question : ","Answer"));
        questionAnsPrivacy.add(new QuestionAns("Question : ","Answer"));
        questionAnsContent.add(new QuestionAns("Question : ","Answer"));
        questionAnsUser.add(new QuestionAns("Question : ","Answer"));
        questionAnsUser.add(new QuestionAns("Question : ","Answer"));

        faqDataArrayList.add(new FAQData("General", questionAnsGeneral));
        faqDataArrayList.add(new FAQData("Usage", questionAnsUsage));
        faqDataArrayList.add(new FAQData("Suscription & Pricing", questionAnsSubscription));
        faqDataArrayList.add(new FAQData("Technical & Compactibility", questionAnsTechnical));
        faqDataArrayList.add(new FAQData("Privacy & Security", questionAnsPrivacy));
        faqDataArrayList.add(new FAQData("Content & Learning", questionAnsContent));
        faqDataArrayList.add(new FAQData("User Account & Personalization", questionAnsUser));

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        faqAdapter = new FAQAdapter(this, faqDataArrayList);

        recyclerView.setAdapter(faqAdapter);

    }
}
