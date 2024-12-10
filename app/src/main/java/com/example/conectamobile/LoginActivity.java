package com.example.conectamobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton, registerButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Iniciar FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Vincular vistas de la interfaz con el código
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Boton para iniciar sesión
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Botonn para registrarse
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Ingreso exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Bienvenido, " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        // Redirigir a la actividad de contactos
                        Intent intent = new Intent(LoginActivity.this, ContactsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error en el inicio de sesión
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}