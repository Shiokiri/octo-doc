package com.werun.notebook.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.werun.notebook.Bean.ResultLogin;
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

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);
        SharedPreferences sp = getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        String cookie1 = sp.getString("cookie1",null);
        Log.e("cookie1:",cookie1);
        String cookie2 = sp.getString("cookie2",null);
        Log.e("cookie2:",cookie2);
        Button toChange = findViewById(R.id.toChange);
        Button exit = findViewById(R.id.exit);
        Button back = findViewById(R.id.toFragmentThree);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    //退出登录
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                        .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                        .build();
                                FormBody mFormBody = new FormBody.Builder()
                                        .build();
                                Request request = new Request.Builder()
                                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                                        .addHeader("cookie",cookie1)
                                        .addHeader("cookie",cookie2)
                                        .url(Constant.post_url_logout)
                                        .post(mFormBody)
                                        .build();

                                okHttpClient.newCall(request).enqueue(new Callback(){

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e("TwoFragment","1111");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String jsonStr = response.body().string();
                                        Gson gson = new Gson();
                                        ResultLogin logoutResult = gson.fromJson(jsonStr, ResultLogin.class);
                                        if (logoutResult.isSuccess()){
                                            Looper.prepare();
                                            Toast.makeText(UserActivity.this, "退出登陆成功", Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor editor = getSharedPreferences("token", 0).edit();
                                            editor.remove("token");
                                            editor.remove("userid");
                                            editor.remove("cookie1");
                                            editor.remove("cookie2");
                                            editor.apply();
                                            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            Looper.loop();

                                        }else{
                                            Looper.prepare();
                                            Toast.makeText(UserActivity.this, "退出登陆失败", Toast.LENGTH_SHORT).show();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("flag", 3);
                startActivity(intent);
            }
        });

        toChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, ChangeActivity.class);
                startActivity(intent);
            }
        });

    }
}
