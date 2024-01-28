package com.example.demo_01_28_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    String[] questions;
    ListView lv;
    Button submit;
    TextView display;

    String quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();

        String number = intent.getStringExtra("listNumber");

        questions = new String[Integer.parseInt(number)];

        for (int i = 0; i < questions.length; i++) {
            if (i<9) {
                questions[i] = "0" + String.valueOf((i+1));
                continue;
            }
            questions[i] = String.valueOf((i+1));
        }

        // Lấy tham chiếu của ListView và Button
        display = findViewById(R.id.display);
        lv = findViewById(R.id.lv);
        submit = findViewById(R.id.submit);

        // Đặt bộ điều hợp để điền dữ liệu vào ListView
        CustomAdapter myArrayAdapter = new CustomAdapter(getApplicationContext(), questions);
        lv.setAdapter(myArrayAdapter);

        // Thực hiện sự kiện setOnClickListener trên Nút
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ans_key = new String[questions.length];
                for (int i = 0; i < questions.length; i++) {
                    ans_key[i] = "A";
                }
                int count = 0;
                float mark = 0;
                for (int i = 0; i < questions.length; i++) {
                    if (ans_key[i] == CustomAdapter.selectedAnswers.get(i)) {
                        count++;
                    }
                }
                mark = (float) (count * 10) / questions.length;
                display.setText("Bạn được " + String.format("%.2f", mark) + " điểm");
            }
        });
    }
}