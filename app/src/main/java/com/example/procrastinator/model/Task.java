package com.example.procrastinator.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class Task implements Parcelable {

    private String id;
    private String author;
    private boolean shared;
    private String title;
    private String content;
    private String remindWhen;

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemindWhen() {
        return remindWhen;
    }

    public void setRemindWhen(String remindWhen) {
        this.remindWhen = remindWhen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Task() {
    }

    public Task(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.shared = in.readInt() == 1;
        this.title = in.readString();
        this.content = in.readString();
        this.remindWhen = in.readString();
    }

    public Task(QueryDocumentSnapshot document) {
        this.id = document.getId();
        Map<String, Object> data = document.getData();
        this.author = (String) data.get("author");
        this.shared = (boolean) data.get("shared");
        this.title = (String) data.get("title");
        this.content = (String) data.get("content");
        this.remindWhen = (String) data.get("remindWhen");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeInt(this.shared ? 1 : 0);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.remindWhen);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", shared=" + shared +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", remindWhen=" + remindWhen +
                '}';
    }
}
