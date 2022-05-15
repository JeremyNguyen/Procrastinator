package com.example.procrastinator.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Objects;

public class Task implements Parcelable {

    private String id;
    private String author;
    private boolean shared;
    private String title;
    private String content;
    private Timestamp remindWhen;
    private String category;

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

    public Timestamp getRemindWhen() {
        return remindWhen;
    }

    public void setRemindWhen(Timestamp remindWhen) {
        this.remindWhen = remindWhen;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        this.remindWhen = in.readParcelable(Timestamp.class.getClassLoader());
        this.category = in.readString();
    }

    public Task(DocumentSnapshot document) {
        this.id = document.getId();
        Map<String, Object> data = document.getData();
        this.author = (String) data.get("author");
        this.shared = (boolean) data.get("shared");
        this.title = (String) data.get("title");
        this.content = (String) data.get("content");
        this.remindWhen = (Timestamp) data.get("remindWhen");
        this.category = (String) data.get("category");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeInt(this.shared ? 1 : 0);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeParcelable(this.remindWhen, 0);
        dest.writeString(this.category);
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
