package com.example.procrastinator.model;

import java.time.LocalDateTime;

public class Task {

    private String author;
    private boolean shared;
    private String subject;
    private LocalDateTime remindWhen;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getRemindWhen() {
        return remindWhen;
    }

    public void setRemindWhen(LocalDateTime remindWhen) {
        this.remindWhen = remindWhen;
    }
}
