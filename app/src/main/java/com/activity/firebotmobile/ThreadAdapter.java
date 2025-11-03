package com.activity.firebotmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    public interface OnThreadClickListener {
        void onThreadClick(ThreadModel thread);
    }

    private final ArrayList<ThreadModel> threadList;
    private final OnThreadClickListener listener;

    public ThreadAdapter(ArrayList<ThreadModel> threadList, OnThreadClickListener listener) {
        this.threadList = threadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thread, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThreadModel thread = threadList.get(position);
        holder.threadName.setText(thread.getName());
        holder.lastMessage.setText(thread.getLastMessage());
        holder.updatedAt.setText(thread.getUpdatedAt());

        holder.itemView.setOnClickListener(v -> listener.onThreadClick(thread));
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView threadName, lastMessage, updatedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            threadName = itemView.findViewById(R.id.textThreadName);
            lastMessage = itemView.findViewById(R.id.textLastMessage);
            updatedAt = itemView.findViewById(R.id.textUpdatedAt);
        }
    }
}
