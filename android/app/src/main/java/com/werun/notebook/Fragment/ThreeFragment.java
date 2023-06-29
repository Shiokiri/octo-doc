package com.cym.notebook.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cym.notebook.Activity.ClassificationActivity;
import com.cym.notebook.Activity.LoginActivity;
import com.cym.notebook.Activity.SignupActivity;
import com.cym.notebook.Activity.UserActivity;
import com.cym.notebook.Bean.Classification;
import com.cym.notebook.Bean.ResultLogin;
import com.cym.notebook.Bean.ResultUser;
import com.cym.notebook.Bean.User;
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

public class ThreeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.threefragment_layout, container,false);
        SharedPreferences sp = getContext().getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        String cookie1 = sp.getString("cookie1",null);
        Log.e("cookie1:",cookie1);
        String cookie2 = sp.getString("cookie2",null);
        Log.e("cookie2:",cookie2);
        Button touser = view.findViewById(R.id.toUser);
        final Handler handler = new Handler();
        TextView username = view.findViewById(R.id.username);
        Button toClassification= view.findViewById(R.id.Toclassification);
        touser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                startActivity(intent);
            }
        });
        toClassification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ClassificationActivity.class);
                startActivity(intent);
            }
        });
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
                            if (userResult.isSuccess()){
                                Looper.prepare();
                                Log.e("ThreeFragment",user.getUserName());

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        username.setText(user.getUserName());
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

        return view;
    }
}
