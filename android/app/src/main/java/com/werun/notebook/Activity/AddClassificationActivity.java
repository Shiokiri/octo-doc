package com.werun.notebook.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.werun.notebook.Bean.ResultClassification;
import com.werun.notebook.Constant;
import com.werun.notebook.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddClassificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classification_add_layout);
        SharedPreferences sp = getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        String cookie1 = sp.getString("cookie1",null);
        String cookie2 = sp.getString("cookie2",null);
        Button add = findViewById(R.id.add_add);
        EditText getname = findViewById(R.id.getClassificationName);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getname.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                    .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                    .build();
                            FormBody mFormBody = new FormBody.Builder()
                                    .add("categoryName",name)
                                    .build();
                            Request request = new Request.Builder()
                                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                                    .addHeader("cookie",cookie1)
                                    .addHeader("cookie",cookie2)
                                    .url(Constant.post_url_classification)
                                    .post(mFormBody)
                                    .build();

                            okHttpClient.newCall(request).enqueue(new Callback(){

                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("ChangeActivity","1111");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String jsonStr = response.body().string();
                                    Gson gson = new Gson();
                                    ResultClassification result = gson.fromJson(jsonStr, ResultClassification.class);
                                    if (result.isSuccess()){
                                        Looper.prepare();
                                        Toast.makeText(AddClassificationActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddClassificationActivity.this, ClassificationActivity.class);
                                        startActivity(intent);
                                        Looper.loop();

                                    }else{
                                        Looper.prepare();
                                        Toast.makeText(AddClassificationActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
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
            }
        });
    }
}
