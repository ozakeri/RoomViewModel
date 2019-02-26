package com.example.roomviewmodel.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roomviewmodel.R;
import com.example.roomviewmodel.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.CustomView> {

    private List<Note> noteList = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView customView, int i) {

        Note note = noteList.get(i);
        customView.textViewTitle.setText(note.getTitle());
        customView.textViewDesc.setText(note.getDescription());
        customView.textViewPriority.setText(String.valueOf(note.getPriority()));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setNotes(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return noteList.get(position);
    }

    public class CustomView extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDesc, textViewPriority;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(noteList.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
