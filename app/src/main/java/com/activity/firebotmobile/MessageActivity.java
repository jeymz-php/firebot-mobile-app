package com.activity.firebotmobile;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    EditText messageInput;
    ImageButton sendButton;
    LinearLayout chatContainer;
    ScrollView scrollView;

    private static final String SEND_URL = "http://192.168.1.32/firebot/api/send_message.php";
    private static final String GET_URL = "http://192.168.1.32/firebot/api/get_messages.php";

    private final Handler handler = new Handler();
    private final int REFRESH_INTERVAL = 4000; // 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.btnSend);
        chatContainer = findViewById(R.id.chatContainer);
        scrollView = findViewById(R.id.scrollViewChat);

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();

            if (message.isEmpty()) {
                Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
                return;
            }

            addMessage(message, true);
            sendMessageToServer(message);
            messageInput.setText("");
        });

        // Start fetching messages periodically
        handler.post(fetchMessagesRunnable);
    }

    private final Runnable fetchMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            fetchMessagesFromServer();
            handler.postDelayed(this, REFRESH_INTERVAL);
        }
    };

    private void addMessage(String text, boolean isSentByUser) {
        TextView messageBubble = new TextView(this);
        messageBubble.setText(text);
        messageBubble.setTextSize(16);
        messageBubble.setPadding(20, 12, 20, 12);
        messageBubble.setMaxWidth(700);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 10);

        if (isSentByUser) {
            params.gravity = Gravity.END;
            messageBubble.setBackgroundResource(R.drawable.bg_message_sent);
            messageBubble.setTextColor(Color.WHITE);
        } else {
            params.gravity = Gravity.START;
            messageBubble.setBackgroundResource(R.drawable.bg_message_received);
            messageBubble.setTextColor(Color.BLACK);
        }

        messageBubble.setLayoutParams(params);
        chatContainer.addView(messageBubble);

        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void sendMessageToServer(String message) {
        StringRequest request = new StringRequest(Request.Method.POST, SEND_URL,
                response -> Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sender", "mobile");    // ✅ mobile user identifier
                params.put("receiver", "admin");   // ✅ or whichever receiver name
                params.put("message", message);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void fetchMessagesFromServer() {
        String sender = "mobile";   // you can replace with logged-in user
        String receiver = "admin";  // or the web-side user
        String url = GET_URL + "?sender=" + sender + "&receiver=" + receiver;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray data = jsonResponse.getJSONArray("data");
                            chatContainer.removeAllViews();
                            parseMessages(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // optional: show error for debugging
                    Toast.makeText(this, "Failed to fetch messages", Toast.LENGTH_SHORT).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseMessages(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                String sender = obj.getString("sender");
                String msg = obj.getString("message");

                boolean isSentByUser = sender.equalsIgnoreCase("mobile");
                addMessage(msg, isSentByUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(fetchMessagesRunnable);
    }
}
