package com.example.recipeeer.domain;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recipeeer.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private final LayoutInflater inflater;
    private List<User> users;

    public UserListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.recyclerview_item,viewGroup,false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserViewHolder userViewHolder, int i) {
        if (users != null) {
            User current = users.get(i);
            userViewHolder.userItemView.setText(current.getEmail());
        }
        else {
            userViewHolder.userItemView.setText("No user");
        }
    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        }
        else return 0;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView userItemView;

        private UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userItemView = itemView.findViewById(R.id.recyclerViewItemText);
        }
    }
}
