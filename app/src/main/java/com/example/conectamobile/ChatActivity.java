package com.example.conectamobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private TextView chatLog;
    private EditText messageField;
    private Button sendButton;
    private ScrollView chatScrollView;

    private String senderId;
    private String receiverId;
    private String chatId;

    private DatabaseReference chatRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Iniciar Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        senderId = currentUser.getEmail().replace(".", "_");

        // Iniciar vistas
        chatLog = findViewById(R.id.chatLog);
        messageField = findViewById(R.id.messageField);
        sendButton = findViewById(R.id.sendButton);
        chatScrollView = findViewById(R.id.chatScrollView);

        // Obtener datos del intent
        receiverId = getIntent().getStringExtra("receiverId");
        String receiverName = getIntent().getStringExtra("receiverName");

        if (receiverId == null || receiverName == null) {
            Toast.makeText(this, "Error: Datos de usuario no recibidos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle("Chat con " + receiverName);

        // Generar ID único
        chatId = generateChatId(senderId, receiverId);

        // Referencia a la base de datos
        chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);

        // Cargar mensajes existentes desde Firebase
        loadMessages();

        // boton de envío de mensajes
        sendButton.setOnClickListener(v -> {
            String message = messageField.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageField.setText("");
            } else {
                Toast.makeText(ChatActivity.this, "No se puede enviar un mensaje vacío", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateChatId(String sender, String receiver) {
        return sender.compareTo(receiver) < 0 ? sender + "_" + receiver : receiver + "_" + sender;
    }

    private void sendMessage(String content) {
        String messageId = chatRef.child("messages").push().getKey();

        if (messageId == null) {
            Toast.makeText(ChatActivity.this, "Error al generar ID del mensaje", Toast.LENGTH_SHORT).show();
            Log.e("ChatActivity", "Error: messageId es null");
            return;
        }

        Map<String, Object> message = new HashMap<>();
        message.put("sender", senderId);
        message.put("receiver", receiverId);
        message.put("content", content);
        message.put("timestamp", System.currentTimeMillis());

        chatRef.child("messages").child(messageId).setValue(message)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ChatActivity", "Mensaje enviado correctamente");
                        scrollToBottom();
                    } else {
                        Toast.makeText(ChatActivity.this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
                        Log.e("ChatActivity", "Error al enviar mensaje: " + task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChatActivity.this, "Fallo al enviar mensaje: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ChatActivity", "Fallo en Firebase: " + e.getMessage());
                });
    }

    private void loadMessages() {
        chatRef.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder chatContent = new StringBuilder();

                if (!snapshot.exists()) {
                    chatLog.setText("No hay mensajes en este chat.\n");
                    return;
                }

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Map<String, Object> messageData = (Map<String, Object>) messageSnapshot.getValue();

                    if (messageData != null) {
                        String sender = (String) messageData.get("sender");
                        String content = (String) messageData.get("content");

                        if (sender != null && content != null) {
                            if (senderId.equals(sender)) {
                                chatContent.append("Yo: ").append(content).append("\n");
                            } else {
                                chatContent.append("Amigo: ").append(content).append("\n");
                            }
                        } else {
                            Log.e("ChatActivity", "Mensaje mal formado: " + messageData);
                        }
                    }
                }

                chatLog.setText(chatContent.toString());
                scrollToBottom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error al cargar mensajes: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ChatActivity", "Error de Firebase: " + error.getMessage());
            }
        });
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
}





