package com.activity.firebotmobile;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final ArrayList<MessageModel> messages;

    public MessageAdapter(ArrayList<MessageModel> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel msg = messages.get(position);

        holder.tvMessage.setText(msg.getMessage());
        holder.tvTime.setText(msg.getTime());
        holder.tvSender.setText(msg.getSender());

        // Check if it's Admin or Semi
        boolean isAdmin = msg.getSender().equalsIgnoreCase("Admin") ||
                msg.getSender().equalsIgnoreCase("Semi");

        // Set gravity for the message container
        holder.container.setGravity(isAdmin ? Gravity.START : Gravity.END);

        // Set background based on sender
        if (isAdmin) {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_admin);
            holder.tvHelp.setVisibility(View.VISIBLE);

            // For Admin messages, show help text (bold responses)
            if (msg.getMessage().contains("**")) {
                String[] parts = msg.getMessage().split("\\*\\*");
                if (parts.length >= 2) {
                    holder.tvMessage.setText(parts[0].trim());
                    holder.tvHelp.setText(parts[1].trim());
                }
            } else {
                holder.tvHelp.setVisibility(View.GONE);
            }
        } else {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_user);
            holder.tvHelp.setVisibility(View.GONE);
        }

        // Align text inside message bubble
        holder.tvMessage.setTextAlignment(isAdmin ?
                View.TEXT_ALIGNMENT_VIEW_START : View.TEXT_ALIGNMENT_VIEW_END);
        holder.tvSender.setTextAlignment(isAdmin ?
                View.TEXT_ALIGNMENT_VIEW_START : View.TEXT_ALIGNMENT_VIEW_END);
        holder.tvTime.setTextAlignment(isAdmin ?
                View.TEXT_ALIGNMENT_VIEW_START : View.TEXT_ALIGNMENT_VIEW_END);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvSender, tvHelp;
        LinearLayout container;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvHelp = itemView.findViewById(R.id.tvHelp);
            container = itemView.findViewById(R.id.messageContainer);
        }
    }
}