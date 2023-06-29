package com.cym.notebook.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cym.notebook.Bean.ResultLogin;
import com.cym.notebook.Bean.ResultUser;
import com.cym.notebook.Constant;
import com.cym.notebook.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_layout);
        SharedPreferences sp = getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        String cookie1 = sp.getString("cookie1",null);
        Log.e("cookie1:",cookie1);
        String cookie2 = sp.getString("cookie2",null);
        Log.e("cookie2:",cookie2);
        Button toHome = findViewById(R.id.toHome_change);
        EditText getPassword = findViewById(R.id.getPassword_change);
        Button change = findViewById(R.id.changeButton);

        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = getPassword.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                    .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                    .build();
                            FormBody mFormBody = new FormBody.Builder()
                                    .add("changeUserPassword",password)
                                    .build();
                            Request request = new Request.Builder()
                                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                                    .addHeader("cookie",cookie1)
                                    .addHeader("cookie",cookie2)
                                    .url(Constant.put_url_change)
                                    .put(mFormBody)
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
                                    ResultUser changeResult = gson.fromJson(jsonStr, ResultUser.class);
                                    if (changeResult.isSuccess()){
                                        Looper.prepare();
                                        Toast.makeText(ChangeActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = getSharedPreferences("token", 0).edit();
                                        editor.remove("token");
                                        editor.remove("userid");
                                        editor.remove("cookie1");
                                        editor.remove("cookie2");
                                        editor.apply();
                                        Intent intent = new Intent(ChangeActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Looper.loop();

                                    }else{
                                        Looper.prepare();
                                        Toast.makeText(ChangeActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
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
