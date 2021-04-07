package com.example.room;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.example.room.Adapters.Adapter_NoteList;
import com.example.room.Database.DataSource;
import com.example.room.Models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditActivity extends AppCompatActivity {

    DataSource dataSource;
    Executor executor;

    FloatingActionButton fabInsert;
    TextInputEditText NoteText;

    //***************************************************
    // OnCreate
    //***************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // connect code to view
        fabInsert = findViewById(R.id.fabInsert);
        NoteText = findViewById(R.id.noteText);

        //initialize to use with DB and Threads
        dataSource = DataSource.getInstance(this);
        executor = Executors.newSingleThreadExecutor();

/*
        //code for clicking on Fab Insert
        fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = NoteText.getText().toString();
                //Check Edit text is Empty or not
                if (NoteText.getText().toString().equals("")){
                    Toast.makeText(EditActivity.this, "Text field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    //if EditText is not Empty after clicking FabInsert inert a record to DB
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            dataSource.noteDao().insertNote(new Note(noteText, 0));
                        }
                    });
                }
                Toast.makeText(EditActivity.this, "یادداشت با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show();
                NoteText.getText().clear();

            }
        });

*/
        //code for clicking on Fab Insert
        fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check Edit text is Empty or not
                if (NoteText.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Text field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //if item on RecyclerView selected for Update / info:reference is ViewHolder click Listener
                if ((getIntent().getLongExtra("noteId", 0)) != 0) {
                    String StrNote = NoteText.getText().toString();
                 executor.execute(new Runnable() {
                     @Override
                     public void run() {
                         Note note = dataSource.noteDao().getNoteById(getIntent().getLongExtra("noteId", 0));
                         note.noteText = StrNote;
                         dataSource.noteDao().updateNote(note);
                     }
                 });
                } else {
                    //if EditText is not Empty and it is new data, after clicking FabInsert inert a record to DB
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            dataSource.noteDao().insertNote(new Note(NoteText.getText().toString(), 0));
                        }
                    });
                }


                Toast.makeText(EditActivity.this, "یادداشت با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show();
                NoteText.getText().clear();
                finish();
            }
            });






        //get selected item from viewHolder to Edit
        long noteId = getIntent().getLongExtra("noteId", 0);
        if(noteId!=0){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    long noteId = getIntent().getLongExtra("noteId", 0);
                    Note note = dataSource.noteDao().getNoteById(noteId);

                    //info : always use "runOnUiThread" to work On UI views like EditText
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NoteText.setText(note.noteText);
                        }
                    });
                }
            });//end executor
        }

    }
}