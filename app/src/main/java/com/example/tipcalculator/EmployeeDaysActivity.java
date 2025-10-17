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

public class EmployeeDaysActivity extends AppCompatActivity {
    private int numEmployees;
    private ArrayList<String> employeeNames;
    private LinearLayout employeeDaysContainer;
    private ArrayList<TextInputEditText> daysInputs;
    private MaterialButton btnNext, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_days);

        // Get data from previous activity
        numEmployees = getIntent().getIntExtra("numEmployees", 0);
        employeeNames = getIntent().getStringArrayListExtra("employeeNames");
        
        initializeViews();
        createDaysInputs();
        setupListeners();
    }

    private void initializeViews() {
        employeeDaysContainer = findViewById(R.id.employeeDaysContainer);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        daysInputs = new ArrayList<>();

        MaterialTextView tvSubtitle = findViewById(R.id.tvSubtitle);
        tvSubtitle.setText("Ingrese los días trabajados por cada empleado:");
    }

    private void createDaysInputs() {
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
            editText.setHint("Días trabajados - " + employeeNames.get(i));
            editText.setId(2000 + i); // Unique ID for each input
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            
            textInputLayout.addView(editText);
            employeeDaysContainer.addView(textInputLayout);
            daysInputs.add(editText);
        }
    }

    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (validateInputs()) {
                ArrayList<Integer> employeeDays = getEmployeeDays();
                
                Intent intent = new Intent(this, WeekTotalActivity.class);
                intent.putExtra("numEmployees", numEmployees);
                intent.putStringArrayListExtra("employeeNames", employeeNames);
                intent.putIntegerArrayListExtra("employeeDays", employeeDays);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean validateInputs() {
        for (int i = 0; i < daysInputs.size(); i++) {
            String daysText = daysInputs.get(i).getText().toString().trim();
            if (daysText.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese los días trabajados por " + employeeNames.get(i), 
                    Toast.LENGTH_SHORT).show();
                daysInputs.get(i).requestFocus();
                return false;
            }
            
            try {
                int days = Integer.parseInt(daysText);
                if (days < 0 || days > 7) {
                    Toast.makeText(this, "Los días deben estar entre 0 y 7 para " + employeeNames.get(i), 
                        Toast.LENGTH_SHORT).show();
                    daysInputs.get(i).requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor ingrese un número válido para " + employeeNames.get(i), 
                    Toast.LENGTH_SHORT).show();
                daysInputs.get(i).requestFocus();
                return false;
            }
        }
        return true;
    }

    private ArrayList<Integer> getEmployeeDays() {
        ArrayList<Integer> days = new ArrayList<>();
        for (TextInputEditText input : daysInputs) {
            days.add(Integer.parseInt(input.getText().toString().trim()));
        }
        return days;
    }
}