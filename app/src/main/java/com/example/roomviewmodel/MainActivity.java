package com.example.roomviewmodel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.roomviewmodel.adapter.NoteAdapter;
import com.example.roomviewmodel.model.Note;
import com.example.roomviewmodel.voewmodel.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton floating_button_add;
    RecyclerView recyclerView;
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private final static int ADD_REQUEST_ADD = 1;
    private final static int EDIT_REQUEST_ADD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        floating_button_add = findViewById(R.id.floating_button_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNote().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        floating_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddOrEditActivity.class);
                startActivityForResult(intent, ADD_REQUEST_ADD);
            }
        });

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddOrEditActivity.class);
                intent.putExtra(AddOrEditActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddOrEditActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddOrEditActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddOrEditActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_REQUEST_ADD);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST_ADD && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddOrEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddOrEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddOrEditActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
            Toast.makeText(getApplicationContext(), "Save SuccessFull", Toast.LENGTH_LONG).show();

        } else if (requestCode == EDIT_REQUEST_ADD && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddOrEditActivity.EXTRA_ID, -1);
            System.out.println("id1=====" + id);
            if (id == -1) {
                Toast.makeText(getApplicationContext(), "No Update", Toast.LENGTH_LONG).show();
            }else {
                String title = data.getStringExtra(AddOrEditActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddOrEditActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddOrEditActivity.EXTRA_PRIORITY, 1);

                Note note = new Note(title, description, priority);
                note.setId(id);
                noteViewModel.update(note);
                Toast.makeText(getApplicationContext(), "Update SuccessFull", Toast.LENGTH_LONG).show();
                System.out.println("id2=====" + note.getId());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Save Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                noteViewModel.deleteAllNotes();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
