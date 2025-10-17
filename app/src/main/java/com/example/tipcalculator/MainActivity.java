package com.example.tipcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

/**
 * Pantalla 1: Solicitar número total de empleados
 */
public class MainActivity extends AppCompatActivity {
    
    private TextInputEditText etNumEmployees;
    private MaterialButton btnNext;
    private MaterialTextView tvTitle, tvInstructions;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupEventListeners();
    }
    
    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvInstructions = findViewById(R.id.tvInstructions);
        etNumEmployees = findViewById(R.id.etNumEmployees);
        btnNext = findViewById(R.id.btnNext);
        
        // Set texts
        tvTitle.setText("Calculadora de Propinas");
        tvInstructions.setText("Ingrese el número total de empleados:");
    }
    
    private void setupEventListeners() {
        btnNext.setOnClickListener(v -> validateAndContinue());
    }
    
    private void validateAndContinue() {
        String numEmployeesStr = etNumEmployees.getText().toString().trim();
        
        // Validation
        if (TextUtils.isEmpty(numEmployeesStr)) {
            Toast.makeText(this, "Por favor ingrese el número de empleados", Toast.LENGTH_SHORT).show();
            etNumEmployees.requestFocus();
            return;
        }
        
        try {
            int numEmployees = Integer.parseInt(numEmployeesStr);
            
            if (numEmployees <= 0) {
                Toast.makeText(this, "El número de empleados debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                etNumEmployees.requestFocus();
                return;
            }
            
            if (numEmployees > 50) {
                Toast.makeText(this, "El número de empleados no puede ser mayor a 50", Toast.LENGTH_SHORT).show();
                etNumEmployees.requestFocus();
                return;
            }
            
            // Continue to next screen
            Intent intent = new Intent(this, EmployeeNamesActivity.class);
            intent.putExtra("numEmployees", numEmployees);
            startActivity(intent);
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingrese solo números", Toast.LENGTH_SHORT).show();
            etNumEmployees.requestFocus();
        }
    }
}