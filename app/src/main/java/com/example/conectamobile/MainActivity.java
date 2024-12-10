package com.example.conectamobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Verificar si el usuario está autenticado
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Usuario autenticado, redirigir a la lista de contactos
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Usuario no autenticado, redirigir a la pantalla de login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
