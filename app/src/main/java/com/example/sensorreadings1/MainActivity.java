package com.example.sensorreadings1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    int spanCount=1;
    OkHttpClient client = new OkHttpClient();
    MaterialCardView mCardView;
    public static List<String> names=new ArrayList<>();
    public static List<Integer> values= new ArrayList<>();
    public HashMap<String,String> map=new HashMap<>();
    RecyclerView dataList;
    Adapter adapter;
    TextView textViewError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList=findViewById(R.id.dataList);
        textViewError=findViewById(R.id.Error);
        mCardView=findViewById(R.id.cardView);
        textViewError.setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        fetchData();
    }

    public void fetchData() {
                String url = "http://192.168.1.10/";
                Request request = new Request.Builder()
                        .url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        textViewError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            parseData(response.body().string());
                        }
                    }
                });
        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_LONG).show();
    }

    public void parseData(String string){
        JSONObject object;
        try {
            object = new JSONObject(string);
            Iterator<String> keys = object.keys();
           // while(keys.hasNext()){
             for(int i=0;i<2;i++){
                String key = keys.next();
                String value = object.getString(key);
                names.add(key);
                values.add(Integer.parseInt(value));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter= new Adapter(MainActivity.this,names,values);
                Log.v("object", String.valueOf(values));
                if(names.size()<=2){
                    spanCount=1;
                    LinearLayoutManager linearLayoutManager= new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false);
                    dataList.setLayoutManager(linearLayoutManager);
                    dataList.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                            if (view instanceof MaterialCardView) {
                                int totalWidth = parent.getWidth();
                                int sidePadding = ((totalWidth)/10);
                                sidePadding = Math.max(0, sidePadding);
                                outRect.set(sidePadding, 0, sidePadding, 0);
                            }
                        }
                    });
                }else {
                    spanCount=2;
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this, spanCount,GridLayoutManager.VERTICAL,false);
                    dataList.setLayoutManager(gridLayoutManager);
                }
                dataList.setAdapter(adapter);
            }
        });
    }

}