package com.werun.notebook.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.werun.notebook.Bean.Note;
import com.werun.notebook.R;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class NoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_layout);
        SharedPreferences sp = getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        String cookie1 = sp.getString("cookie1",null);
        String cookie2 = sp.getString("cookie2",null);
        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra("extra_note");
        Button back = findViewById(R.id.back);
        Button edit = findViewById(R.id.edit);
        TextView title = findViewById(R.id.title);
        TextView content = findViewById(R.id.content);
        title.setText(note.getNoteTitle());
        content.setText(note.getNoteContent());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this,EditNoteActivity.class);
                intent.putExtra("extra_note", (Serializable) note);
                startActivity(intent);
            }
        });

    }
}
