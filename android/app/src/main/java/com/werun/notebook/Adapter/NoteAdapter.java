package com.werun.notebook.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.werun.notebook.Activity.NoteActivity;
import com.werun.notebook.Bean.Note;
import com.werun.notebook.Bean.ResultClassification;
import com.werun.notebook.Constant;
import com.werun.notebook.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private List<Note> mNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View noteView;
        TextView noteTitle;
        TextView noteDate;

        public ViewHolder(View view) {
            super(view);
            noteView = view;
            noteTitle = view.findViewById(R.id.note_name);
            noteDate = view.findViewById(R.id.date);
        }
    }
    public NoteAdapter(List<Note> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        final NoteAdapter.ViewHolder holder = new NoteAdapter.ViewHolder(view);
        SharedPreferences sp = parent.getContext().getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        String cookie1 = sp.getString("cookie1",null);
        String cookie2 = sp.getString("cookie2",null);
        final Handler handler = new Handler();

        //长按删除
        holder.noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //做删除操作
                int position = holder.getAdapterPosition();
                Note note = mNoteList.get(position);
                //出现dialogue
                AlertDialog.Builder dialog = new AlertDialog.Builder (parent.getContext());
                dialog.setTitle("确认信息");
                dialog.setMessage("确定要删除吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //做数据的删除操作
                        String nid = note.getNid();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                            .build();
                                    FormBody mFormBody = new FormBody.Builder()
                                            .add("nid",nid)
                                            .build();
                                    Request request = new Request.Builder()
                                            .addHeader("Content-Type","application/x-www-form-urlencoded")
                                            .addHeader("cookie",cookie1)
                                            .addHeader("cookie",cookie2)
                                            .url(Constant.delete_url_note)
                                            .delete(mFormBody)
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
                                            ResultClassification classificationResult = gson.fromJson(jsonStr, ResultClassification.class);
                                            if (classificationResult.isSuccess()){
                                                Looper.prepare();
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mNoteList.remove(position);
                                                        notifyItemRemoved(position);
                                                    }
                                                });

                                                Toast.makeText(parent.getContext(), "已删除", Toast.LENGTH_SHORT).show();
                                                Looper.loop();

                                            }else{
                                                Looper.prepare();
                                                Toast.makeText(parent.getContext(), "删除失败", Toast.LENGTH_SHORT).show();
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
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(parent.getContext(), "已取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return false;
            }
        });

        holder.noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Note note = mNoteList.get(position);
                Intent intent = new Intent( parent.getContext(), NoteActivity.class);
                intent.putExtra("extra_note", (Serializable) note);
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull NoteAdapter.ViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        //2022-04-26T14:56:00.946+00:00  这是日期的格式
        String day = note.getNoteCreateTime().substring(8,10);
        String month = note.getNoteCreateTime().substring(5,7);
        holder.noteDate.setText(day+"\n"+month+"月");
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

}
