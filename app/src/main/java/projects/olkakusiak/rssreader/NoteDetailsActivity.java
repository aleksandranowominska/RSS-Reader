package projects.olkakusiak.rssreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


/**
 * Created by Aleksandra Kusiak on 27.11.2017.
 */

public class NoteDetailsActivity extends AppCompatActivity {

    public static final String NOTE_TITLE = "NOTE_TITLE";
    public static final String NOTE_DATE = "NOTE_DATE";
    public static final String NOTE_CONTENT = "NOTE_CONTENT";

    TextView noteTitleTextView;
    TextView noteDateTextView;
    TextView noteContentTextView;

    String noteTitle;
    String noteDate;
    String noteContent;

    Context context;
    private int screenW;
    private int screenH;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getBaseContext();

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

    public void updateUI() {
        noteTitleTextView.setText(noteTitle);
        noteDateTextView.setText(noteDate);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        float dpHeight = displayMetrics.heightPixels;
        screenW = (int) dpWidth;
        screenH = (int) dpHeight;

        try {
            noteContentTextView.setText(Html.fromHtml("<small>" + noteContent + "</small>", Images, null));
        } catch(Exception ignored){
        }


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            noteContentTextView.setText(Html.fromHtml("<small>" + noteContent + "</small>", Html.FROM_HTML_MODE_LEGACY));
//        } else {
//            noteContentTextView.setText(Html.fromHtml("<small>" + noteContent + "</small>"));
//        }
    }

    private Html.ImageGetter Images = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String s) {
            Drawable drawable;

            FetchImageUrl fetchImageUrl = new FetchImageUrl(context, s);
            try {
                fetchImageUrl.execute().get();
                drawable = fetchImageUrl.GetImage();
            } catch (Exception e){
                drawable = getResources().getDrawable(R.drawable.placeholder);
            }
            // to display image,center of screen
            int imgH = drawable.getIntrinsicHeight();
            int imgW = drawable.getIntrinsicWidth();
            int padding =20;
            int realWidth = screenW-(2*padding);
            int realHeight = imgH * realWidth/imgW;
            drawable.setBounds(padding,0,realWidth ,realHeight);
            return drawable;
        }
    };
}
