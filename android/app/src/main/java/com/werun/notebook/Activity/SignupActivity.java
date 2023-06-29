package com.cym.notebook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cym.notebook.Bean.ResultSignup;
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

public class SignupActivity extends AppCompatActivity {
    Button toLoginButton;
    Button signupButton;
    EditText passwordEdit;
    EditText phoneNumberEdit;
    EditText nameEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        toLoginButton = findViewById(R.id.toLogin);
        signupButton = findViewById(R.id.signup);
        passwordEdit = findViewById(R.id.getPassword_signup);
        phoneNumberEdit = findViewById(R.id.getPhoneNumber_signup);
        nameEdit = findViewById(R.id.getName_signup);
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //注册功能
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = phoneNumberEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String name = nameEdit.getText().toString();
                if (userid.equals("")||password.equals("")||name.equals("")){
                    Toast.makeText(SignupActivity.this, "未输入有效信息", Toast.LENGTH_SHORT).show();
                }else {
                    //开始注册
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                        .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                        .build();
                                FormBody mFormBody = new FormBody.Builder()
                                        .add("userName",name)
                                        .add("userAccount",userid)
                                        .add("userPassword",password)
                                        .build();
                                Request request = new Request.Builder()
                                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                                        .url(Constant.post_url_signup)
                                        .post(mFormBody)
                                        .build();
                                okHttpClient.newCall(request).enqueue(new Callback(){

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e("wrong request","SignupActivity");

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String jsonStr = response.body().string();
                                        Gson gson = new Gson();
                                        ResultSignup signupResult = gson.fromJson(jsonStr, ResultSignup.class);
                                        Log.e("result:","SignupActivity");
                                        Log.e("message:"+signupResult.getMessage(),"SignupActivity");
                                        Log.e("success:"+signupResult.isSuccess(),"SignupActivity");
                                        Log.e("token:"+signupResult.getToken(),"SignupActivity");
                                        if (signupResult.isSuccess()){
                                            Looper.prepare();
                                            Toast.makeText(SignupActivity.this, "注册成功，请登陆账号", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            Looper.loop();
                                            finish();
                                        }else{
                                            Looper.prepare();
                                            Toast.makeText(SignupActivity.this, "注册失败，请重新来过", Toast.LENGTH_SHORT).show();
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
            }
        });

    }
}