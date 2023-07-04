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

import com.werun.notebook.Bean.ResultLogin;
import com.werun.notebook.Constant;
import com.werun.notebook.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    Button toSignupButton;
    Button loginButton;
    EditText phoneNumberEdit;
    EditText passwordEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        loginButton = findViewById(R.id.login);
        toSignupButton = findViewById(R.id.toSignup);
        phoneNumberEdit = findViewById(R.id.getPhoneNumber_login);
        passwordEdit = findViewById(R.id.getPassword_login);
        toSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        //登录功能
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = phoneNumberEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (userid.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this, "未输入有效信息", Toast.LENGTH_SHORT).show();
                }else {
                    //开始登录
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                        .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                        .build();
                                FormBody mFormBody = new FormBody.Builder()
                                        .add("userAccount",userid)
                                        .add("userPassword",password)
                                        .build();
                                Request request = new Request.Builder()
                                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                                        .url(Constant.post_url_login)
                                        .post(mFormBody)
                                        .build();
                                okHttpClient.newCall(request).enqueue(new Callback(){

                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String jsonStr = response.body().string();
                                        Gson gson = new Gson();
                                        ResultLogin loginResult = gson.fromJson(jsonStr, ResultLogin.class);
                                        Log.e("result:","SignupActivity");
                                        Log.e("message:"+loginResult.getMessage(),"LoginActivity");
                                        Log.e("success:"+loginResult.isSuccess(),"LoginActivity");
                                        Log.e("token:"+loginResult.getToken(),"LoginActivity");

                                        Looper.prepare();
                                        if (loginResult.isSuccess()){
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            String token = loginResult.getToken().toString();
                                            SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
                                            editor.putString("token", token);
                                            editor.putString("username",userid);
                                            Headers headers = response.headers();
                                            int flag=1;
                                            for (int i = 0; i < headers.size(); i++) {
                                                Log.e("登录", headers.name(i) + ":" + headers.value(i));
                                                if (headers.name(i).contains("Set-Cookie")) {
                                                    String cookie = headers.value(i);
                                                    if(flag==1){
                                                        editor.putString("cookie1",cookie);
                                                        flag=2;
                                                    }else if(flag==2){
                                                        editor.putString("cookie2",cookie);
                                                    }

                                                }
                                            }

                                            editor.apply();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);

                                        }else{
                                            Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                                        }
                                        Looper.loop();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}
