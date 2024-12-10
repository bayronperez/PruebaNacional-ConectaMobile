package com.example.conectamobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<User> userList;
    private OnContactClickListener listener;

    public ContactsAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void setOnContactClickListener(OnContactClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());

        // Manejar clics en el contacto
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<User> newList) {
        userList = newList;
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }
    }

    // Interfaz para manejar clics en los contactos
    public interface OnContactClickListener {
        void onContactClick(User user);
    }
}



