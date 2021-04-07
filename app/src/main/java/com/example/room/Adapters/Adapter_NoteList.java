package com.example.room.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.example.room.Database.DataSource;
import com.example.room.EditActivity;
import com.example.room.Models.Note;
import com.example.room.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;

public class Adapter_NoteList extends RecyclerView.Adapter<Adapter_NoteList.NoteViewHolder> {


    List<Note> noteList;
    Context context;
    Executor executor;
    DataSource dataSource;

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }




    public Adapter_NoteList(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.dataSource = DataSource.getInstance(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textView_title.setText(note.noteText);
        if(note.isDone == 0){
            holder.imageView_Status.setImageResource(R.drawable.ic_baseline_not_done);
        }
        else {
            holder.imageView_Status.setImageResource(R.drawable.ic_baseline_done);

        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    //**************************************************
    //ViewHolder Inner Class
    //**************************************************

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView textView_title;
        ImageView imageView_Status;
        ImageView imageViewswipLR;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            imageView_Status = itemView.findViewById(R.id.imageView_Status);
            imageViewswipLR = itemView.findViewById(R.id.imageViewswipLR);

            //change viewHolder to clickable to handle operation by clicking on item in recyclerview
            //Ex. click item to edit
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call EditActivity
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("noteId", noteList.get(getAbsoluteAdapterPosition()).id);//get id of selected item of position x to Edit
                    context.startActivity(intent); // info:startActivity is method in parent class Context
                }
            });


            //Handle Image of each Item
            imageView_Status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note note = noteList.get(getAbsoluteAdapterPosition());
                    if(note.isDone == 0){
                        note.isDone = 1;
                    }
                    else {
                        note.isDone = 0;
                    }
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            dataSource.noteDao().updateNote(note);
                            noteList = dataSource.noteDao().getAllNotes();

                        }
                    });

                    notifyItemChanged(getAbsoluteAdapterPosition());

                }
            });
        }
    }


    // this method use for updating RecyclerView
    public void UpdateRecyclerView(List<Note> noteList) {
        this.noteList = noteList;   //new note list will pass and overwrite to this class noteList
        notifyDataSetChanged();
    }
}
