package com.werun.notebook.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.werun.notebook.Activity.AddNoteActivity;
import com.werun.notebook.Adapter.NoteAdapter;
import com.werun.notebook.Bean.Classification;
import com.werun.notebook.Bean.Note;
import com.werun.notebook.Bean.ResultNote;
import com.werun.notebook.Bean.ResultUser;
import com.werun.notebook.Bean.User;
import com.werun.notebook.Constant;
import com.werun.notebook.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OneFragment extends Fragment {
    String cookie1;
    String cookie2;
    View view;
    Spinner sp;
    List<String> starArray = new ArrayList<String>();
    final Handler handler = new Handler();
    RecyclerView recyclerView;
    ResultNote noteresult;
    List<Classification> classificationList;
    List<Note> noteList;
    List<Note> dataList =new ArrayList<Note>();
    int flag=0;
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.onefragment_layout, container,false);
        SharedPreferences sp = getContext().getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        cookie1 = sp.getString("cookie1",null);
        cookie2 = sp.getString("cookie2",null);
        recyclerView = view.findViewById(R.id.note_recycle_view);
        //获得类别信息
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
                                        initSpinner(view);
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
        Button add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });
        //开始布置recyclerview


        return view;
    }
//2022-04-26T14:56:00.946+00:00  这是日期的格式
    private void initSpinner(View view){
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(this.getContext(),R.layout.item_select,starArray);
        //设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_dropdown);
        //从布局文件中获取名叫sp_dialog的下拉框
        sp = view.findViewById(R.id.spinner);
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
            Log.e("监听器","更新为"+i);
            flag=i;
            //获得笔记信息
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
                                .url(Constant.get_url_getNote)
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
                                noteresult = gson.fromJson(jsonStr, ResultNote.class);
                                noteList = noteresult.getNoteBeanList();
                                if (noteresult.isSuccess()){


                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                            recyclerView.setLayoutManager(layoutManager);
                                            if(dataList!=null)
                                            {
                                                for(int j=dataList.size()-1;j>=0;j--)
                                                {
                                                    dataList.remove(j);
                                                }
                                            }

                                            if(noteList!=null)
                                            {
                                                for(Note note:noteList)
                                                {
                                                    if(note.getCname().equals(classificationList.get(flag).getCategoryName()))
                                                    {
                                                        dataList.add(note);
                                                        Log.e(classificationList.get(flag).getCategoryName(),"添加了一个"+note.getNoteTitle());
                                                    }
                                                }
                                            }
                                            NoteAdapter adapter = new NoteAdapter(dataList);
                                            recyclerView.setAdapter(adapter);
                                            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                                        }
                                    });
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

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
