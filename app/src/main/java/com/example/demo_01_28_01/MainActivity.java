package com.example.demo_01_28_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText edtNumber;
    Button btnNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumber = findViewById(R.id.edtNumber);
        btnNumber = findViewById(R.id.btnNumber);

        btnNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ListActivity.class);

                String number = edtNumber.getText().toString();

                intent.putExtra("listNumber", number);

                startActivity(intent);
            }
        });
    }
}