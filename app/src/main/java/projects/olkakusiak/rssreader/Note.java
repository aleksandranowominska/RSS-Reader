package projects.olkakusiak.rssreader;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aleksandra Kusiak on 25.11.2017.
 */

public class Note {
    private String title;
    private String content;
    private String description;
    private String url;
    private String imgURL;
    private Date publishDate = new Date();

    public Note() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;

        if (content.length() >= 50) {
            this.description = content.substring(0, 50);
        } else {
            this.description = content;
        }

    }

    // TODO: Remove it later
//    public static ArrayList<Note> createFakeNoteList(int numNotes) {
//        Date currentTime = Calendar.getInstance().getTime();
//        ArrayList<Note> notes = new ArrayList<Note>();
//
//        for (int i = 1; i <= numNotes; i++) {
//            notes.add(new Note("Some title", "Some description", "https://html.com/wp-content/uploads/flamingo.jpg", currentTime));
//
//        }
//        return notes;
//    }
}
