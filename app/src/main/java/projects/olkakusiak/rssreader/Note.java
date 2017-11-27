package projects.olkakusiak.rssreader;


import android.text.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Date publishDate;

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

    public void setPublishDate(String publishDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        try {
            this.publishDate = dateFormat.parse(publishDate);
        } catch (Exception ignored) {
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        String contentWithoutTags = stripHtml(content);
        if (content.length() >= 150) {
            this.description = contentWithoutTags.substring(0, 150);
        } else {
            this.description = contentWithoutTags;
        }

    }

    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

}
