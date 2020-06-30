package com.laioffer.tinnews.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Article implements Serializable {
//public class Article {
    @Ignore
    public Source source; // (need converter https://developer.android.com/training/data-storage/room/referencing-data#java)
    public String author;
    public String content;
    public String description;
    public String publishedAt;
    public String title;
    @NonNull
    @PrimaryKey
    public String url;
    public String urlToImage;
    public boolean favorite;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(source, article.source) &&
                Objects.equals(author, article.author) &&
                Objects.equals(content, article.content) &&
                Objects.equals(description, article.description) &&
                Objects.equals(publishedAt, article.publishedAt) &&
                Objects.equals(title, article.title) &&
                Objects.equals(url, article.url) &&
                Objects.equals(urlToImage, article.urlToImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, author, content, description, publishedAt, title, url, urlToImage);
    }

    @Override
    public String toString() {
        return "Article{" +
                "source=" + source +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }
}
