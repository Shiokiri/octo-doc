package com.werun.notebook.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.werun.notebook.Adapter.NoteAdapter;
import com.werun.notebook.Bean.Note;
import com.werun.notebook.Bean.ResultNote;
import com.werun.notebook.Constant;
import com.werun.notebook.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TwoFragment extends Fragment {

    String cookie1;
    String cookie2;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twofragment_layout, container,false);
        SharedPreferences sp = getContext().getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        cookie1 = sp.getString("cookie1",null);
        cookie2 = sp.getString("cookie2",null);
        recyclerView = view.findViewById(R.id.note_recycle_view);
        EditText gettitle = view.findViewById(R.id.title);
        Button search = view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = gettitle.getText().toString();
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
                                    .url(Constant.get_url_search+"?noteTitle="+title)
                                    .get()
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
                                    ResultNote noteresult = gson.fromJson(jsonStr, ResultNote.class);
                                    List<Note> noteList = noteresult.getNoteBeanList();
                                    if (noteresult.isSuccess()){
                                        Log.e("查找","成功");
                                        recyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                                recyclerView.setLayoutManager(layoutManager);
                                                NoteAdapter adapter = new NoteAdapter(noteList);
                                                recyclerView.setAdapter(adapter);
                                                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                                            }
                                        });





                                    }else{
                                        Toast.makeText(getContext(), "查找失败", Toast.LENGTH_SHORT).show();
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
        return view;
    }

}


//根据题目搜索
