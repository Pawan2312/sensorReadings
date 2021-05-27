package com.example.sensorreadings1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

public class MainActivity extends AppCompatActivity {
    int spanCount = 1;
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.MILLISECONDS).build();
    MaterialCardView mCardView;
    public static List<String> names = new ArrayList<>();
    public static List<Integer> values = new ArrayList<>();
    public HashMap<String, String> map = new HashMap<>();
    RecyclerView dataList;
    Adapter adapter;
    TextView textViewError;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean first=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList = findViewById(R.id.dataList);
        textViewError = findViewById(R.id.Error);
        mCardView = findViewById(R.id.cardView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        textViewError.setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        fetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                fetchData();
            }
        });

    }

    public void fetchData() {

        String url = "http://192.168.1.10/";
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        textViewError.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    parseData(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    public void parseData(String string) {
        JSONObject object;
        try {
            object = new JSONObject(string);
            Iterator<String> keys = object.keys();
            names = new ArrayList<>();
            values = new ArrayList<>();

             while(keys.hasNext()){
//            for (int i = 0; i < 2; i++) {
                String key = keys.next();
                String value = object.getString(key);
                names.add(key);
                values.add(Integer.parseInt(value));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!first)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                first=true;
                adapter = new Adapter(MainActivity.this, names, values);
                Log.v("object", String.valueOf(values));
                if (names.size() <= 2) {
                    spanCount = 1;
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                    dataList.setLayoutManager(linearLayoutManager);
                    dataList.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                            int sidePadding = 0;
                            int totalWidth = 0;
                            if (view instanceof MaterialCardView) {
                                totalWidth = parent.getWidth();
                                sidePadding = ((totalWidth) / 10);
                                sidePadding = Math.max(0, sidePadding);
                                outRect.set(sidePadding, 0, sidePadding, 0);
                                Log.v("object", String.valueOf(sidePadding));
                            }
                        }
                    });
                } else {
                    spanCount = 2;
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount, GridLayoutManager.VERTICAL, false);
                    dataList.setLayoutManager(gridLayoutManager);
                }
                dataList.setAdapter(adapter);
            }
        });
    }

}
