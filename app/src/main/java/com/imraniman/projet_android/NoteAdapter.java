package com.imraniman.projet_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    static class ViewHolder {
        ImageView imageViewIcon;
        TextView textViewMatiere;
        TextView textViewNote;
    }

    public NoteAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);

            holder = new ViewHolder();
            holder.imageViewIcon = convertView.findViewById(R.id.imageViewNoteIcon);
            holder.textViewMatiere = convertView.findViewById(R.id.textViewMatiere);
            holder.textViewNote = convertView.findViewById(R.id.textViewNote);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        Note note = getItem(position);
        holder.textViewMatiere.setText(note.getMatiere());
        holder.textViewNote.setText(note.getNote() + "/20");

        if (note.getNote() >= 10) {
            holder.imageViewIcon.setImageResource(R.drawable.ic_like);
        } else {
            holder.imageViewIcon.setImageResource(R.drawable.ic_dislike);
        }

        return convertView;
    }
}
