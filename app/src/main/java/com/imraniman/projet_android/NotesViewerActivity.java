package com.imraniman.projet_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class NotesViewerActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT_NAME = "viewer_student_name";
    public static final String EXTRA_NOTES_LIST = "viewer_notes_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);

        TextView tvTitle = findViewById(R.id.tvNotesTitle);
        TextView tvEmpty = findViewById(R.id.tvEmptyNotes);
        ListView listView = findViewById(R.id.listViewNotesViewer);
        MaterialButton btnClose = findViewById(R.id.buttonCloseNotes);

        String name = getIntent().getStringExtra(EXTRA_STUDENT_NAME);
        if (name != null && !name.trim().isEmpty()) {
            tvTitle.setText("Notes de " + name);
        }

        @SuppressWarnings("unchecked")
        ArrayList<Note> notes =
                (ArrayList<Note>) getIntent().getSerializableExtra(EXTRA_NOTES_LIST);

        if (notes == null || notes.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            NoteAdapter adapter = new NoteAdapter(this, notes);
            listView.setAdapter(adapter);
        }

        btnClose.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
