package com.cym.notebook.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cym.notebook.Activity.ChangeActivity;
import com.cym.notebook.Activity.ClassificationNameChangeActivity;
import com.cym.notebook.Activity.MainActivity;
import com.cym.notebook.Bean.Classification;
import com.cym.notebook.Bean.ResultClassification;
import com.cym.notebook.Bean.ResultUser;
import com.cym.notebook.Constant;
import com.cym.notebook.R;
import com.google.gson.Gson;

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

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.ViewHolder>{
    private List<Classification> mClassificationList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classificationView;
        TextView classificationName;
        public ViewHolder(View view) {
            super(view);
            classificationView = view;
            classificationName = (TextView) view.findViewById(R.id.classification_name);
        }
    }
    public ClassificationAdapter(List<Classification> classificationList) {
        mClassificationList = classificationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classification_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        SharedPreferences sp = parent.getContext().getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        String cookie1 = sp.getString("cookie1",null);
        String cookie2 = sp.getString("cookie2",null);
        final Handler handler = new Handler();
        holder.classificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Classification classification = mClassificationList.get(position);
                Intent intent = new Intent( parent.getContext(), ClassificationNameChangeActivity.class);
                intent.putExtra("cid", classification.getCid());
                parent.getContext().startActivity(intent);
            }
        });

        holder.classificationView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //做删除操作
                int position = holder.getAdapterPosition();
                Classification classification = mClassificationList.get(position);
                //出现dialogue
                AlertDialog.Builder dialog = new AlertDialog.Builder (parent.getContext());
                dialog.setTitle("确认信息");
                dialog.setMessage("确定要删除吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //做数据的删除操作
                        String cid = classification.getCid();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(8000, TimeUnit.MILLISECONDS)
                                            .build();
                                    FormBody mFormBody = new FormBody.Builder()
                                            .add("cid",cid)
                                            .build();
                                    Request request = new Request.Builder()
                                            .addHeader("Content-Type","application/x-www-form-urlencoded")
                                            .addHeader("cookie",cookie1)
                                            .addHeader("cookie",cookie2)
                                            .url(Constant.delete_url_classification)
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
                                                        mClassificationList.remove(position);
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



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificationAdapter.ViewHolder holder, int position) {
        Classification classification = mClassificationList.get(position);
        holder.classificationName.setText(classification.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mClassificationList.size();
    }
}
