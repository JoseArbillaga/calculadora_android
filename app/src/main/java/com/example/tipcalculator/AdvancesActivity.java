package com.example.tipcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

public class AdvancesActivity extends AppCompatActivity {
    private int numEmployees;
    private ArrayList<String> employeeNames;
    private ArrayList<Integer> employeeDays;
    private int weekTotal;
    private int fullWeekDays;
    private LinearLayout advancesContainer;
    private ArrayList<TextInputEditText> advanceInputs;
    private MaterialButton btnNext, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advances);

        // Get data from previous activity
        numEmployees = getIntent().getIntExtra("numEmployees", 0);
        employeeNames = getIntent().getStringArrayListExtra("employeeNames");
        employeeDays = getIntent().getIntegerArrayListExtra("employeeDays");
        weekTotal = getIntent().getIntExtra("weekTotal", 0);
        fullWeekDays = getIntent().getIntExtra("fullWeekDays", 0);
        
        initializeViews();
        createAdvanceInputs();
        setupListeners();
    }

    private void initializeViews() {
        advancesContainer = findViewById(R.id.advancesContainer);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        advanceInputs = new ArrayList<>();

        MaterialTextView tvSubtitle = findViewById(R.id.tvSubtitle);
        tvSubtitle.setText("Ingrese los adelantos de cada empleado (ingrese 0 si no tuvo adelantos):");
    }

    private void createAdvanceInputs() {
        for (int i = 0; i < numEmployees; i++) {
            // Create TextInputLayout
            TextInputLayout textInputLayout = new TextInputLayout(this, null, com.google.android.material.R.attr.textInputStyle);
            
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 32);
            textInputLayout.setLayoutParams(layoutParams);

            // Create TextInputEditText
            TextInputEditText editText = new TextInputEditText(this);
            editText.setHint("Adelanto - " + employeeNames.get(i));
            editText.setId(4000 + i); // Unique ID for each input
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | 
                                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setText("0"); // Default value
            
            textInputLayout.addView(editText);
            advancesContainer.addView(textInputLayout);
            advanceInputs.add(editText);
        }
    }

    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (validateInputs()) {
                ArrayList<Double> employeeAdvances = getEmployeeAdvances();
                
                Intent intent = new Intent(this, DailyAmountsActivity.class);
                intent.putExtra("numEmployees", numEmployees);
                intent.putStringArrayListExtra("employeeNames", employeeNames);
                intent.putIntegerArrayListExtra("employeeDays", employeeDays);
                intent.putExtra("weekTotal", weekTotal);
                intent.putExtra("fullWeekDays", fullWeekDays);
                
                // Convert ArrayList<Double> to double array for Intent
                double[] advances = new double[employeeAdvances.size()];
                for (int i = 0; i < employeeAdvances.size(); i++) {
                    advances[i] = employeeAdvances.get(i);
                }
                intent.putExtra("employeeAdvances", advances);
                
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean validateInputs() {
        for (int i = 0; i < advanceInputs.size(); i++) {
            String advanceText = advanceInputs.get(i).getText().toString().trim();
            if (advanceText.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese el adelanto para " + employeeNames.get(i) + " (ingrese 0 si no tuvo)", 
                    Toast.LENGTH_SHORT).show();
                advanceInputs.get(i).requestFocus();
                return false;
            }
            
            try {
                double advance = Double.parseDouble(advanceText);
                if (advance < 0) {
                    Toast.makeText(this, "El adelanto no puede ser negativo para " + employeeNames.get(i), 
                        Toast.LENGTH_SHORT).show();
                    advanceInputs.get(i).requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor ingrese un número válido para " + employeeNames.get(i), 
                    Toast.LENGTH_SHORT).show();
                advanceInputs.get(i).requestFocus();
                return false;
            }
        }
        return true;
    }

    private ArrayList<Double> getEmployeeAdvances() {
        ArrayList<Double> advances = new ArrayList<>();
        for (TextInputEditText input : advanceInputs) {
            advances.add(Double.parseDouble(input.getText().toString().trim()));
        }
        return advances;
    }
}