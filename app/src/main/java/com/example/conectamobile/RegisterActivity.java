package com.example.conectamobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameField, emailField, passwordField;
    private Button registerButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Vincular vistas
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        registerButton = findViewById(R.id.registerButton);


        // Iniciar Firebase Auth y Database
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Boton para registrarse
        registerButton.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(name, email, password);
        });


    }

    private void registerUser(String name, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Guardar información adicional en la base de datos
                        String userId = auth.getCurrentUser().getUid();
                        User newUser = new User(name, email);
                        databaseRef.child(userId).setValue(newUser)
                                .addOnCompleteListener(databaseTask -> {
                                    if (databaseTask.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {

                        // Error en el registro
                        Toast.makeText(RegisterActivity.this, "Registro fallido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
