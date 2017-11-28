package projects.olkakusiak.rssreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static projects.olkakusiak.rssreader.NoteDetailsActivity.NOTE_CONTENT;
import static projects.olkakusiak.rssreader.NoteDetailsActivity.NOTE_DATE;
import static projects.olkakusiak.rssreader.NoteDetailsActivity.NOTE_TITLE;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Olka";

    ArrayList<Note> arrayListOfNotes = new ArrayList<Note>();
    RecyclerView recyclerView;
    NotesRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    String fetchURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.notesRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        adapter = new NotesRecyclerViewAdapter(this, arrayListOfNotes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pullNotes();

        recyclerView.addOnItemTouchListener(new RecyclerNoteClickListener(this, recyclerView, new RecyclerNoteClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Note note = arrayListOfNotes.get(position);
                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                intent.putExtra(NOTE_TITLE, note.getTitle());
                intent.putExtra(NOTE_DATE, note.getPublishDate());
                intent.putExtra(NOTE_CONTENT, note.getContent());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
// TODO: Set as red
            }
        }));
    }

    public void pullNotes() {
        new FetchFeedTask().execute((Void) null);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });
    }


    class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            fetchURL = "https://www.thatcatblog.com/home?format=RSS";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL(fetchURL);
                InputStream inputStream = url.openConnection().getInputStream();
                List<Note> newNotes = parseFeed(inputStream);
                arrayListOfNotes.clear();
                arrayListOfNotes.addAll(newNotes);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            swipeRefreshLayout.setRefreshing(false);

            if (success) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<Note> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        String date = null;
        String imgURL = null;
        boolean isItem = false;
        List<Note> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                if (name.equals("media:content")) {
                    for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                        if (Objects.equals(xmlPullParser.getAttributeName(i), "url")) {
                            imgURL = xmlPullParser.getAttributeValue(1).toString();
                        }
                    }

                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                } else if (name.equals("pubDate")) {
                    date = result;
                }

                if (title != null && link != null && description != null && imgURL != null && date != null) {
                    if (isItem) {
                        Note note = new Note();
                        note.setTitle(title);
                        note.setUrl(link);
                        note.setContent(description);
                        note.setImgURL(imgURL);
                        note.setPublishDate(date);
                        items.add(note);
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                    imgURL = null;
                    date = null;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }


    }
}