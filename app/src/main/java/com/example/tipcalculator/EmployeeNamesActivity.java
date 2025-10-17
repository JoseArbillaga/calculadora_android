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

public class EmployeeNamesActivity extends AppCompatActivity {
    private int numEmployees;
    private LinearLayout employeeNamesContainer;
    private ArrayList<TextInputEditText> nameInputs;
    private MaterialButton btnNext, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_names);

        // Get number of employees from previous activity
        numEmployees = getIntent().getIntExtra("numEmployees", 0);
        
        // Validate that we received a valid number of employees
        if (numEmployees <= 0) {
            Toast.makeText(this, "Error: Número de empleados inválido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        initializeViews();
        createNameInputs();
        setupListeners();
    }

    private void initializeViews() {
        employeeNamesContainer = findViewById(R.id.employeeNamesContainer);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        nameInputs = new ArrayList<>();

        MaterialTextView tvSubtitle = findViewById(R.id.tvSubtitle);
        tvSubtitle.setText("Ingrese los nombres de los " + numEmployees + " empleados:");
    }

    private void createNameInputs() {
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
            editText.setHint("Empleado " + (i + 1));
            editText.setId(1000 + i); // Unique ID for each input
            
            textInputLayout.addView(editText);
            employeeNamesContainer.addView(textInputLayout);
            nameInputs.add(editText);
        }
    }

    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (validateInputs()) {
                ArrayList<String> employeeNames = getEmployeeNames();
                
                Intent intent = new Intent(this, EmployeeDaysActivity.class);
                intent.putExtra("numEmployees", numEmployees);
                intent.putStringArrayListExtra("employeeNames", employeeNames);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean validateInputs() {
        for (int i = 0; i < nameInputs.size(); i++) {
            String name = nameInputs.get(i).getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese el nombre del empleado " + (i + 1), 
                    Toast.LENGTH_SHORT).show();
                nameInputs.get(i).requestFocus();
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> getEmployeeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (TextInputEditText input : nameInputs) {
            names.add(input.getText().toString().trim());
        }
        return names;
    }
}