package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private MessageAdapter messageAdapter;
    private ArrayList<MessageModel> messageList = new ArrayList<>();

    private EditText etMessage;
    private ImageButton btnSend, btnBack;
    private TextView tvThreadName;

    private String threadId, threadName;

    private static final String GET_MESSAGES_URL = "https://firebot.ucc-bsit.org/api/get_messages.php?thread_id=";
    private static final String SEND_MESSAGE_URL = "https://firebot.ucc-bsit.org/api/send_message.php";

    private final Handler handler = new Handler();
    private final int REFRESH_INTERVAL = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerMessages = findViewById(R.id.recyclerMessages);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList);
        recyclerMessages.setAdapter(messageAdapter);

        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        tvThreadName = findViewById(R.id.tvThreadName);

        // Get data from intent
        threadId = getIntent().getStringExtra("thread_id");
        threadName = getIntent().getStringExtra("thread_name");
        tvThreadName.setText(threadName);

        btnBack.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> sendMessage());

        // Load initial messages and start auto-refresh
        loadMessages();
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMessages();
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Stop refresh when leaving activity
    }

    private void loadMessages() {
        String url = GET_MESSAGES_URL + threadId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            messageList.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                messageList.add(new MessageModel(
                                        obj.getString("sender"),
                                        obj.getString("message"),
                                        obj.getString("sent_at")
                                ));
                            }

                            messageAdapter.notifyDataSetChanged();
                            recyclerMessages.scrollToPosition(messageList.size() - 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void sendMessage() {
        String msg = etMessage.getText().toString().trim();
        if (msg.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, SEND_MESSAGE_URL,
                response -> {
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.getString("status").equals("success")) {
                            etMessage.setText("");
                            loadMessages(); // instantly refresh messages
                        } else {
                            Toast.makeText(this, res.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("thread_id", threadId);
                params.put("sender", "User"); // ✅ sending as user (not admin)
                params.put("message", msg);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
