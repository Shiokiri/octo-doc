package com.werun.notebook.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.werun.notebook.Adapter.ClassificationAdapter;
import com.werun.notebook.Bean.Classification;
import com.werun.notebook.Bean.ResultUser;
import com.werun.notebook.Bean.User;
import com.werun.notebook.Constant;
import com.werun.notebook.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClassificationActivity extends AppCompatActivity {
    Button toAdd;
    Button toFragment3;
    String cookie1;
    String cookie2;
    RecyclerView recyclerView;
    List<Classification> classificationList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classification_layout);
        SharedPreferences sp = getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        cookie1 = sp.getString("cookie1",null);
        cookie2 = sp.getString("cookie2",null);
        toFragment3 = findViewById(R.id.toFragmentThreeFromClassification);
        toAdd = findViewById(R.id.toAdd);

        final Handler handler = new Handler();
//获得分类的列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(8000, TimeUnit.MILLISECONDS)
                            .build();
                    Request request = new Request.Builder()
                            .addHeader("cookie",cookie1)
                            .addHeader("cookie",cookie2)
                            .url(Constant.get_url_getuser)
                            .build();

                    okHttpClient.newCall(request).enqueue(new Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("ThreeFragment","1111");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String jsonStr = response.body().string();
                            Gson gson = new Gson();
                            ResultUser userResult = gson.fromJson(jsonStr, ResultUser.class);
                            User user = gson.fromJson(userResult.getUserBean().toString(),User.class);

                            classificationList = (List<Classification>) userResult.getCategoryBeanList();

                            if (userResult.isSuccess()){
                                Looper.prepare();
                                Log.e("xxxxxxxxxxx",user.getUserName());
                                recyclerView =findViewById(R.id.classification_recycle_view);
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(ClassificationActivity.this);
                                        recyclerView.setLayoutManager(layoutManager);
                                        ClassificationAdapter adapter = new ClassificationAdapter(classificationList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.addItemDecoration(new DividerItemDecoration(ClassificationActivity.this, LinearLayoutManager.VERTICAL));
                                    }
                                });

                                Looper.loop();


                            }else{
                                Looper.prepare();
                                Looper.loop();
                            }
                        }
                    });
                    //String responseData = response.body().string();
                    //showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        toFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassificationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("flag", 3);
                startActivity(intent);
            }
        });
        toAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassificationActivity.this, AddClassificationActivity.class);
                startActivity(intent);
            }
        });





    }

   
}
