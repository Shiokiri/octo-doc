package com.cym.notebook.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cym.notebook.Bean.Classification;
import com.cym.notebook.Bean.ResultClassification;
import com.cym.notebook.Bean.ResultUser;
import com.cym.notebook.Bean.User;
import com.cym.notebook.Constant;
import com.cym.notebook.Fragment.OneFragment;
import com.cym.notebook.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddNoteActivity extends AppCompatActivity {
    String cookie1;
    String cookie2;
    List<String> starArray = new ArrayList<String>();
    final Handler handler = new Handler();
    RecyclerView recyclerView;
    List<Classification> classificationList;
    String cid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnote_layout);
        SharedPreferences sp = getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        cookie1 = sp.getString("cookie1",null);
        cookie2 = sp.getString("cookie2",null);
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
                    Log.e("xxxxggggxx11","hh");

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
                            Log.e("xxxxggggxx1","hh");
                            classificationList =  userResult.getCategoryBeanList();
                            Log.e("cidcidcid",classificationList.get(0).getCid());
                            if(!starArray.isEmpty())
                            {
                                starArray = new ArrayList<String>();
                            }
                            for(int i=0;i<classificationList.size();i++)
                            {
                                starArray.add(classificationList.get(i).getCategoryName());
                            }
                            if (userResult.isSuccess()){
                                Looper.prepare();
                                Log.e("xxxxggggxx2","sss");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        initSpinner();
                                    }
                                });


                                Looper.loop();


                            }else{

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

        EditText getTitle = findViewById(R.id.addTitle);
        EditText getContent = findViewById(R.id.addContent);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button add = findViewById(R.id.addNote);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getTitle.getText().toString();
                String content = getContent.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                    .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                    .build();
                            FormBody mFormBody = new FormBody.Builder()
                                    .add("noteTitle",title)
                                    .add("cid",cid)
                                    .add("noteContent",content)
                                    .add("noteCompletedState","true")
                                    .add("description","")
                                    .build();
                            Request request = new Request.Builder()
                                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                                    .addHeader("cookie",cookie1)
                                    .addHeader("cookie",cookie2)
                                    .url(Constant.post_url_addNote)
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
                                        Toast.makeText(AddNoteActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Looper.loop();

                                    }else{
                                        Looper.prepare();
                                        Toast.makeText(AddNoteActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
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
    private void initSpinner(){
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(AddNoteActivity.this,R.layout.item_select,starArray);
        //设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_dropdown);
        //从布局文件中获取名叫sp_dialog的下拉框
        Spinner sp = findViewById(R.id.spinner);
        //设置下拉框的标题，不设置就没有难看的标题了
        sp.setPrompt("请选择分类");
        //设置下拉框的数组适配器
        sp.setAdapter(starAdapter);
        //设置下拉框默认的显示第一项
        sp.setSelection(0);
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        sp.setOnItemSelectedListener(new MySelectedListener());
    }

    class MySelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            cid = classificationList.get(i).getCid();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
