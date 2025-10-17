package com.example.tipcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ResultsActivity extends AppCompatActivity {
    private int numEmployees;
    private ArrayList<String> employeeNames;
    private ArrayList<Integer> employeeDays;
    private int weekTotal;
    private int fullWeekDays;
    private double[] dailyAmounts;
    private double[] employeeAdvances;
    
    private LinearLayout resultsContainer;
    private MaterialTextView tvTotalAmount, tvDailyAverage, tvRemainder;
    private MaterialButton btnNewCalculation, btnBack;
    private DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Get data from previous activity
        numEmployees = getIntent().getIntExtra("numEmployees", 0);
        employeeNames = getIntent().getStringArrayListExtra("employeeNames");
        employeeDays = getIntent().getIntegerArrayListExtra("employeeDays");
        weekTotal = getIntent().getIntExtra("weekTotal", 0);
        fullWeekDays = getIntent().getIntExtra("fullWeekDays", 0);
        dailyAmounts = getIntent().getDoubleArrayExtra("dailyAmounts");
        employeeAdvances = getIntent().getDoubleArrayExtra("employeeAdvances");
        
        initializeViews();
        calculateAndDisplayResults();
        setupListeners();
    }

    private void initializeViews() {
        resultsContainer = findViewById(R.id.resultsContainer);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvDailyAverage = findViewById(R.id.tvDailyAverage);
        tvRemainder = findViewById(R.id.tvRemainder);
        btnNewCalculation = findViewById(R.id.btnNewCalculation);
        btnBack = findViewById(R.id.btnBack);
    }

    private void calculateAndDisplayResults() {
        // 1. Calculate total amount (suma de todos los días)
        double totalAmount = 0;
        for (double amount : dailyAmounts) {
            totalAmount += amount;
        }
        
        // 2. Calculate daily average (total semanal / días de la semana)
        double dailyAverage = totalAmount / weekTotal;
        
        // 3. Calculate daily average per person (total diario / cantidad de empleados)
        double dailyAveragePerPerson = dailyAverage / numEmployees;
        
        // 4. Calculate each employee's base share (total diario por persona * días trabajados)
        ArrayList<Double> employeeShares = new ArrayList<>();
        double totalDistributed = 0;
        
        for (int i = 0; i < numEmployees; i++) {
            double share = dailyAveragePerPerson * employeeDays.get(i);
            employeeShares.add(share);
            totalDistributed += share;
        }
        
        // 5. Calculate remainder (resto = total semanal - suma de propinas brutas)
        double remainder = totalAmount - totalDistributed;
        
        // Find employees who worked the "full week" days or more (semana completa + extras)
        ArrayList<Integer> fullWeekEmployees = new ArrayList<>();
        for (int i = 0; i < numEmployees; i++) {
            if (employeeDays.get(i) >= fullWeekDays) {
                fullWeekEmployees.add(i);
            }
        }
        
        // Distribute remainder among full-week employees
        double remainderDistributed = 0;
        if (fullWeekEmployees.size() > 0 && remainder > 0) {
            double remainderPerEmployee = remainder / fullWeekEmployees.size();
            remainderDistributed = remainder;
            for (int employeeIndex : fullWeekEmployees) {
                employeeShares.set(employeeIndex, 
                    employeeShares.get(employeeIndex) + remainderPerEmployee);
            }
        }
        
        // Subtract advances from each employee's final amount
        ArrayList<Double> finalAmounts = new ArrayList<>();
        double totalAdvances = 0;
        for (int i = 0; i < numEmployees; i++) {
            double advance = (employeeAdvances != null && i < employeeAdvances.length) ? employeeAdvances[i] : 0;
            double finalAmount = employeeShares.get(i) - advance;
            finalAmounts.add(finalAmount);
            totalAdvances += advance;
        }
        
        // Display summary
        tvTotalAmount.setText("Total de propinas: $" + df.format(totalAmount));
        tvDailyAverage.setText("Promedio diario: $" + df.format(dailyAverage) + 
                              " | Por persona: $" + df.format(dailyAveragePerPerson));
        tvRemainder.setText("Sobrante distribuido: $" + df.format(remainderDistributed) + 
                           " | Adelantos totales: $" + df.format(totalAdvances));
        
        // Display individual results
        displayEmployeeResults(employeeShares, finalAmounts, fullWeekEmployees, remainderDistributed);
    }

    private void displayEmployeeResults(ArrayList<Double> employeeShares, 
                                      ArrayList<Double> finalAmounts, 
                                      ArrayList<Integer> fullWeekEmployees, 
                                      double remainderDistributed) {
        for (int i = 0; i < numEmployees; i++) {
            // Create employee card
            com.google.android.material.card.MaterialCardView employeeCard = 
                new com.google.android.material.card.MaterialCardView(this);
            
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 24);
            employeeCard.setLayoutParams(cardParams);
            employeeCard.setCardElevation(4f);
            employeeCard.setRadius(12f);
            
            // Create inner layout
            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setOrientation(LinearLayout.VERTICAL);
            innerLayout.setPadding(24, 24, 24, 24);
            
            // Employee name
            MaterialTextView nameView = new MaterialTextView(this);
            nameView.setText(employeeNames.get(i));
            nameView.setTextSize(18f);
            nameView.setTextColor(getColor(com.google.android.material.R.color.material_on_surface_emphasis_high_type));
            nameView.setTypeface(null, android.graphics.Typeface.BOLD);
            
            // Days worked
            MaterialTextView daysView = new MaterialTextView(this);
            daysView.setText("Días trabajados: " + employeeDays.get(i) + "/" + weekTotal + 
                           " (Semana completa: " + fullWeekDays + "+ días)");
            daysView.setTextSize(14f);
            
            // Base amount
            MaterialTextView baseAmountView = new MaterialTextView(this);
            double baseAmount = employeeShares.get(i);
            if (fullWeekEmployees.contains(i) && remainderDistributed > 0) {
                baseAmount -= (remainderDistributed / fullWeekEmployees.size());
            }
            baseAmountView.setText("Propina base: $" + df.format(baseAmount));
            baseAmountView.setTextSize(14f);
            
            // Add bonus indicator if applicable
            if (fullWeekEmployees.contains(i) && remainderDistributed > 0) {
                MaterialTextView bonusView = new MaterialTextView(this);
                bonusView.setText("+ Bono semana completa (" + fullWeekDays + "+ días): $" + df.format(remainderDistributed / fullWeekEmployees.size()));
                bonusView.setTextSize(14f);
                bonusView.setTextColor(getColor(com.google.android.material.R.color.design_default_color_secondary));
                innerLayout.addView(bonusView);
            }
            
            // Show advance if any
            double advance = (employeeAdvances != null && i < employeeAdvances.length) ? employeeAdvances[i] : 0;
            if (advance > 0) {
                MaterialTextView advanceView = new MaterialTextView(this);
                advanceView.setText("- Adelanto: $" + df.format(advance));
                advanceView.setTextSize(14f);
                advanceView.setTextColor(getColor(android.R.color.holo_red_dark));
                innerLayout.addView(advanceView);
            }
            
            // Final amount
            MaterialTextView finalAmountView = new MaterialTextView(this);
            finalAmountView.setText("TOTAL A RECIBIR: $" + df.format(finalAmounts.get(i)));
            finalAmountView.setTextSize(16f);
            finalAmountView.setTextColor(getColor(com.google.android.material.R.color.design_default_color_primary));
            finalAmountView.setTypeface(null, android.graphics.Typeface.BOLD);
            
            innerLayout.addView(nameView);
            innerLayout.addView(daysView);
            innerLayout.addView(baseAmountView);
            innerLayout.addView(finalAmountView);
            
            employeeCard.addView(innerLayout);
            resultsContainer.addView(employeeCard);
        }
    }

    private void setupListeners() {
        btnNewCalculation.setOnClickListener(v -> {
            // Clear the activity stack and start fresh
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnBack.setOnClickListener(v -> finish());
    }
}