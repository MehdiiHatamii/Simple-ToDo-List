package com.example.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.room.Adapters.Adapter_NoteList;
import com.example.room.Database.DataSource;
import com.example.room.Models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infinum.dbinspector.DbInspector;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView_note_list;

    // 1-To use Room add thread executor and dataSource
    Executor executor;
    DataSource dataSource;

    //Adapters
    Adapter_NoteList adapter_noteList;

    //************************************************************************
    // onResume
    //After back from an activity to source activity OnResume Method will call
    //************************************************************************

    @Override
    protected void onResume() {
        super.onResume();
        UpdateRecyclerView();
    }

    //This Method Contain Refresh RecyclerView Method after inserting Data
    public void UpdateRecyclerView() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> noteList = dataSource.noteDao().getAllNotes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter_noteList.UpdateRecyclerView(noteList);
                    }
                });
            }
        });
    }


    //************************************************************************
    // onCreate
    //************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView_note_list = findViewById(R.id.recyclerView_note_list);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);


        //2-create an executor that use a single work thread operation on queue
        //2-prepare executor to use
        executor = Executors.newSingleThreadExecutor();

        //3-instance of DataSource
        dataSource = DataSource.getInstance(MainActivity.this);

        //4-execute executor
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //5-call insert method
                //dataSource.noteDao().insertNote(note);
            }
        });


        // Get All Data to pass to recyclerview


        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> noteList = dataSource.noteDao().getAllNotes();
                adapter_noteList = new Adapter_NoteList(noteList, MainActivity.this);

                // pass every View from every thread to Main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView_note_list.setAdapter(adapter_noteList);
                        recyclerView_note_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    }
                });
            }

        });


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });


        // this Class use for swiping on recyclerview items and do a job by swiping
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        //get witch item(note) is selected
                        Note note = adapter_noteList.getNoteList().get(viewHolder.getAbsoluteAdapterPosition());

                        //after select note on recyclerview call delete method from dataSource
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                dataSource.noteDao().deleteNote(note);
                                UpdateRecyclerView();
                            }
                        });

                        //get current position
                        viewHolder.getAbsoluteAdapterPosition();
                    }
                });

        //attach touchHelper to Recyclerview
        itemTouchHelper.attachToRecyclerView(recyclerView_note_list);

        //This block of code is for appear and disappear floating action button
        //when Recyclerview scrolling to display last list item
        recyclerView_note_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //first we determine what LayoutManager is used by recyclerView
                //find last item position of recyclerview
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView_note_list.getLayoutManager();
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition(); //Ex.20

                //second get item count & Item Range
                int itemCount = adapter_noteList.getItemCount(); //EX. 20
                int itemRange = linearLayoutManager.findFirstCompletelyVisibleItemPosition() +
                        linearLayoutManager.findLastCompletelyVisibleItemPosition(); //Ex. 4

                //check if range of item less than count all item visible FabAdd button
                //else check is the scroll over
                if (itemRange <= itemCount-1){
                    fabAdd.setVisibility(View.VISIBLE);
                }
                else {
                    //check is the scroll over
                    if (lastItemPosition >= itemCount - 1) {
                        fabAdd.setVisibility(View.INVISIBLE);
                    } else {
                        fabAdd.setVisibility(View.VISIBLE);
                    }
                }




            }
        });


















//        DbInspector.show();


    }
}