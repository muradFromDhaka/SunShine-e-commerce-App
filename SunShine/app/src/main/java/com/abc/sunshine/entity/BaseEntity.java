package com.abc.sunshine.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class BaseEntity {

    protected long id;
    protected boolean deleted = false;
    protected String createdAt;
    protected String updatedAt;

    public BaseEntity() {
        String now = currentTime();
        this.createdAt = now;
        this.updatedAt = now;
    }

    protected String currentTime() {
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
        ).format(new Date());
    }

    // 🔄 Update হলে call করবে
    public void touch() {
        this.updatedAt = currentTime();
    }

    // getters & setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}

