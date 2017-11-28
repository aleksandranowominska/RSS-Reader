package projects.olkakusiak.rssreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Aleksandra Kusiak on 25.11.2017.
 */

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteNameTextView;
        TextView noteDescriptionTextView;
        TextView notePublishDate;
        ImageView noteMainImgImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            noteNameTextView = itemView.findViewById(R.id.note_title);
            noteDescriptionTextView = itemView.findViewById(R.id.note_description);
            notePublishDate = itemView.findViewById(R.id.note_publish_date);
            noteMainImgImageView = itemView.findViewById(R.id.note_main_image);
        }
    }

    private List<Note> listOfNotes;
    private Context context;

    public NotesRecyclerViewAdapter(Context context, List<Note> listOfNotes) {
        this.context = context;
        this.listOfNotes = listOfNotes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View noteView = inflater.inflate(R.layout.note_single_cell, parent, false);

        ViewHolder viewHolder = new ViewHolder(noteView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = listOfNotes.get(position);
        holder.noteNameTextView.setText(note.getTitle());
        holder.noteDescriptionTextView.setText(note.getDescription());


        if (note.getPublishDate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dateAsString = simpleDateFormat.format(note.getPublishDate());
            holder.notePublishDate.setText(dateAsString);
        } else {
            holder.notePublishDate.setText("");
        }

        if (note.getImgURL() != null && note.getImgURL().isEmpty()) {
            holder.noteMainImgImageView.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.with(this.context)
                    .load(note.getImgURL())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.no_image_avaiable)
                    .into(holder.noteMainImgImageView);
        }
    }

    @Override
    public int getItemCount() {
        return listOfNotes.size();
    }


}
