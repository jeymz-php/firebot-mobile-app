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

        // Align messages: right for Admin, left for others
        boolean isAdmin = msg.getSender().equalsIgnoreCase("Admin");

        // Set gravity for the message bubble
        holder.container.setGravity(isAdmin ? Gravity.END : Gravity.START);

        // Set background based on sender
        holder.tvMessage.setBackgroundResource(isAdmin ?
                R.drawable.bg_message_admin : R.drawable.bg_message_user);

        // Align text inside message bubble
        holder.tvMessage.setTextAlignment(isAdmin ?
                View.TEXT_ALIGNMENT_VIEW_END : View.TEXT_ALIGNMENT_VIEW_START);

        // Optional: change sender text alignment too
        holder.tvSender.setTextAlignment(isAdmin ?
                View.TEXT_ALIGNMENT_VIEW_END : View.TEXT_ALIGNMENT_VIEW_START);
        holder.tvTime.setTextAlignment(isAdmin ?
                View.TEXT_ALIGNMENT_VIEW_END : View.TEXT_ALIGNMENT_VIEW_START);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvSender;
        LinearLayout container;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSender = itemView.findViewById(R.id.tvSender);
            container = itemView.findViewById(R.id.messageContainer);
        }
    }
}
