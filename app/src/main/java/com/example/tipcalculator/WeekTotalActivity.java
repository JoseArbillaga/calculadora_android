package com.example.tipcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class WeekTotalActivity extends AppCompatActivity {
    private int numEmployees;
    private ArrayList<String> employeeNames;
    private ArrayList<Integer> employeeDays;
    private TextInputEditText etWeekTotal, etFullWeekDays;
    private MaterialButton btnNext, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_total);

        // Get data from previous activity
        numEmployees = getIntent().getIntExtra("numEmployees", 0);
        employeeNames = getIntent().getStringArrayListExtra("employeeNames");
        employeeDays = getIntent().getIntegerArrayListExtra("employeeDays");
        
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etWeekTotal = findViewById(R.id.etWeekTotal);
        etFullWeekDays = findViewById(R.id.etFullWeekDays);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (validateInput()) {
                int weekTotal = Integer.parseInt(etWeekTotal.getText().toString().trim());
                int fullWeekDays = Integer.parseInt(etFullWeekDays.getText().toString().trim());
                
                Intent intent = new Intent(this, AdvancesActivity.class);
                intent.putExtra("numEmployees", numEmployees);
                intent.putStringArrayListExtra("employeeNames", employeeNames);
                intent.putIntegerArrayListExtra("employeeDays", employeeDays);
                intent.putExtra("weekTotal", weekTotal);
                intent.putExtra("fullWeekDays", fullWeekDays);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean validateInput() {
        String weekTotalText = etWeekTotal.getText().toString().trim();
        String fullWeekDaysText = etFullWeekDays.getText().toString().trim();
        
        if (weekTotalText.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el total de días de la semana", 
                Toast.LENGTH_SHORT).show();
            etWeekTotal.requestFocus();
            return false;
        }
        
        if (fullWeekDaysText.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese los días de semana laboral completa", 
                Toast.LENGTH_SHORT).show();
            etFullWeekDays.requestFocus();
            return false;
        }
        
        try {
            int weekTotal = Integer.parseInt(weekTotalText);
            int fullWeekDays = Integer.parseInt(fullWeekDaysText);
            
            if (weekTotal < 1 || weekTotal > 7) {
                Toast.makeText(this, "El total de días debe estar entre 1 y 7", 
                    Toast.LENGTH_SHORT).show();
                etWeekTotal.requestFocus();
                return false;
            }
            
            if (fullWeekDays < 1 || fullWeekDays > weekTotal) {
                Toast.makeText(this, "Los días de semana completa deben estar entre 1 y " + weekTotal, 
                    Toast.LENGTH_SHORT).show();
                etFullWeekDays.requestFocus();
                return false;
            }
            
            // Check if any employee worked more days than the total week days
            for (int i = 0; i < employeeDays.size(); i++) {
                if (employeeDays.get(i) > weekTotal) {
                    Toast.makeText(this, employeeNames.get(i) + " no puede trabajar más días que el total de la semana", 
                        Toast.LENGTH_LONG).show();
                    etWeekTotal.requestFocus();
                    return false;
                }
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingrese números válidos", 
                Toast.LENGTH_SHORT).show();
            etWeekTotal.requestFocus();
            return false;
        }
        
        return true;
    }
}