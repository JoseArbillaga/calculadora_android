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

public class DailyAmountsActivity extends AppCompatActivity {
    private int numEmployees;
    private ArrayList<String> employeeNames;
    private ArrayList<Integer> employeeDays;
    private int weekTotal;
    private int fullWeekDays;
    private double[] employeeAdvances;
    private LinearLayout dailyAmountsContainer;
    private ArrayList<TextInputEditText> amountInputs;
    private MaterialButton btnCalculate, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_amounts);

        // Get data from previous activity
        numEmployees = getIntent().getIntExtra("numEmployees", 0);
        employeeNames = getIntent().getStringArrayListExtra("employeeNames");
        employeeDays = getIntent().getIntegerArrayListExtra("employeeDays");
        weekTotal = getIntent().getIntExtra("weekTotal", 0);
        fullWeekDays = getIntent().getIntExtra("fullWeekDays", 0);
        employeeAdvances = getIntent().getDoubleArrayExtra("employeeAdvances");
        
        initializeViews();
        createAmountInputs();
        setupListeners();
    }

    private void initializeViews() {
        dailyAmountsContainer = findViewById(R.id.dailyAmountsContainer);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnBack = findViewById(R.id.btnBack);
        amountInputs = new ArrayList<>();

        MaterialTextView tvSubtitle = findViewById(R.id.tvSubtitle);
        tvSubtitle.setText("Ingrese el monto de propinas por cada día de la semana:");
    }

    private void createAmountInputs() {
        String[] dayNames = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        
        for (int i = 0; i < weekTotal; i++) {
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
            editText.setHint("Propinas del " + dayNames[i]);
            editText.setId(3000 + i); // Unique ID for each input
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | 
                                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            
            textInputLayout.addView(editText);
            dailyAmountsContainer.addView(textInputLayout);
            amountInputs.add(editText);
        }
    }

    private void setupListeners() {
        btnCalculate.setOnClickListener(v -> {
            if (validateInputs()) {
                ArrayList<Double> dailyAmounts = getDailyAmounts();
                
                Intent intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("numEmployees", numEmployees);
                intent.putStringArrayListExtra("employeeNames", employeeNames);
                intent.putIntegerArrayListExtra("employeeDays", employeeDays);
                intent.putExtra("weekTotal", weekTotal);
                intent.putExtra("fullWeekDays", fullWeekDays);
                intent.putExtra("employeeAdvances", employeeAdvances);
                
                // Convert ArrayList<Double> to double array for Intent
                double[] amounts = new double[dailyAmounts.size()];
                for (int i = 0; i < dailyAmounts.size(); i++) {
                    amounts[i] = dailyAmounts.get(i);
                }
                intent.putExtra("dailyAmounts", amounts);
                
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean validateInputs() {
        for (int i = 0; i < amountInputs.size(); i++) {
            String amountText = amountInputs.get(i).getText().toString().trim();
            if (amountText.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese el monto para el día " + (i + 1), 
                    Toast.LENGTH_SHORT).show();
                amountInputs.get(i).requestFocus();
                return false;
            }
            
            try {
                double amount = Double.parseDouble(amountText);
                if (amount < 0) {
                    Toast.makeText(this, "El monto no puede ser negativo para el día " + (i + 1), 
                        Toast.LENGTH_SHORT).show();
                    amountInputs.get(i).requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor ingrese un número válido para el día " + (i + 1), 
                    Toast.LENGTH_SHORT).show();
                amountInputs.get(i).requestFocus();
                return false;
            }
        }
        return true;
    }

    private ArrayList<Double> getDailyAmounts() {
        ArrayList<Double> amounts = new ArrayList<>();
        for (TextInputEditText input : amountInputs) {
            amounts.add(Double.parseDouble(input.getText().toString().trim()));
        }
        return amounts;
    }
}