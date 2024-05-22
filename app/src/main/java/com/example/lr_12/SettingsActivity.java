package com.example.lr_12;

// В SettingsActivity.java

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button backButton;
    private RadioGroup colorRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.backButton);
        colorRadioGroup = findViewById(R.id.colorRadioGroup);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получить выбранный цвет из RadioGroup
                int selectedColorId = colorRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedColorId);
                String selectedColor = selectedRadioButton.getText().toString();

                // Сохранить выбранный цвет в SharedPreferences
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selectedColor", selectedColor);
                editor.apply();

                finish(); // Закрыть активность
            }
        });
    }
}
