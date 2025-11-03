package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerThreads;
    private ThreadAdapter threadAdapter;
    private ArrayList<ThreadModel> threadList = new ArrayList<>();

    private static final String THREADS_URL = "https://firebot.ucc-bsit.org/api/get_threads.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerThreads = findViewById(R.id.recyclerThreads);
        recyclerThreads.setLayoutManager(new LinearLayoutManager(this));

        threadAdapter = new ThreadAdapter(threadList, thread -> {
            // open ChatActivity when a thread is clicked
            Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
            intent.putExtra("thread_id", thread.getId());
            intent.putExtra("thread_name", thread.getName());
            startActivity(intent);
        });

        recyclerThreads.setAdapter(threadAdapter);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadThreads();
    }

    private void loadThreads() {
        com.android.volley.toolbox.JsonObjectRequest request =
                new com.android.volley.toolbox.JsonObjectRequest(Request.Method.GET, THREADS_URL, null,
                        response -> {
                            try {
                                if (response.has("data")) {
                                    JSONArray dataArray = response.getJSONArray("data");
                                    parseThreads(dataArray);
                                } else {
                                    Toast.makeText(this, "No data field in response", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            Toast.makeText(this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                );

        Volley.newRequestQueue(this).add(request);
    }

    private void parseThreads(JSONArray response) {
        try {
            threadList.clear();
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                threadList.add(new ThreadModel(
                        obj.getString("id"),
                        obj.getString("name"),
                        obj.optString("last_message", ""),
                        obj.optString("updated_at", "")
                ));
            }
            threadAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
