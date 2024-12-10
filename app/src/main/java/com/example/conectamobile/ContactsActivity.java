package com.example.conectamobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private EditText searchField;
    private Button addUserButton;

    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Iniciar vistas
        recyclerView = findViewById(R.id.recyclerView);
        searchField = findViewById(R.id.searchField);
        addUserButton = findViewById(R.id.addUserButton);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsAdapter(userList);
        recyclerView.setAdapter(adapter);

        // Configurar listener para clics en contactos
        adapter.setOnContactClickListener(user -> {
            String currentUserId = auth.getCurrentUser().getEmail().replace(".", "_");
            String selectedUserId = user.getEmail().replace(".", "_");

            // Iniciar ChatActivity con los datos necesarios
            Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
            intent.putExtra("senderId", currentUserId);
            intent.putExtra("receiverId", selectedUserId);
            intent.putExtra("receiverName", user.getName());
            startActivity(intent);
        });

        // Cargar contactos desde Firebase
        loadContacts();

        // Busca contactos
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        // boton para redirigir al registro
        addUserButton.setOnClickListener(v -> {
            startActivity(new Intent(ContactsActivity.this, RegisterActivity.class));
        });
    }

    private void loadContacts() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null && !user.getEmail().equals(auth.getCurrentUser().getEmail())) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactsActivity.this, "Error al cargar contactos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterContacts(String query) {
        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        adapter.updateList(filteredList);
    }
}

