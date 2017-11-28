package projects.olkakusiak.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;



/**
 * Created by Aleksandra Kusiak on 27.11.2017.
 */

public class NoteDetailsActivity extends AppCompatActivity {

    public static final String NOTE_TITLE = "NOTE_TITLE";
    public static final String NOTE_DATE = "NOTE_DATE";
    public static final String NOTE_CONTENT  = "NOTE_CONTENT";

    TextView noteTitleTextView;
    TextView noteDateTextView;
    TextView noteContentTextView;

    String noteTitle;
    String noteDate;
    String noteContent;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_details);
        noteTitleTextView = findViewById(R.id.note_title_details);
        noteDateTextView = findViewById(R.id.note_publish_date_details);
        noteContentTextView = findViewById(R.id.note_content_details);

        Intent intent = getIntent();
        noteTitle = intent.getStringExtra(NOTE_TITLE);
        noteDate = intent.getStringExtra(NOTE_DATE);
        noteContent = intent.getStringExtra(NOTE_CONTENT);

        updateUI();
    }

    public void updateUI(){
        noteTitleTextView.setText(noteTitle);
        noteDateTextView.setText(noteDate);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            noteContentTextView.setText(Html.fromHtml("<small>" + noteContent + "</small>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            noteContentTextView.setText(Html.fromHtml("<small>" + noteContent + "</small>"));
        }
    }
}
