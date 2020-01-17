package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button testBtn, count_numBtn, composeBtn, divisionBtn, omissionBtn, additionBtn, confrontationBtn, distinctionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_frag);

        testBtn = findViewById(R.id.learn_test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        count_numBtn = findViewById(R.id.learn_count_number);
        count_numBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CountNumActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        composeBtn = findViewById(R.id.learn_compose);
        composeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ComposeActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        divisionBtn = findViewById(R.id.learn_division);
        divisionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DivisionActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        omissionBtn = findViewById(R.id.learn_omission);
        omissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OmissionActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        additionBtn = findViewById(R.id.learn_addition);
        additionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdditionActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        confrontationBtn = findViewById(R.id.learn_confrontation);
        confrontationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfrontationActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        distinctionBtn = findViewById(R.id.learn_distinction);
        distinctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DistinctionActivity.class);
                startActivityForResult(intent,5000);
            }
        });

    }
}
