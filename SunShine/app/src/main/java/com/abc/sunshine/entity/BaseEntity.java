package com.abc.sunshine.entity;
public abstract class BaseEntity {

    protected long id;
    protected boolean deleted = false;

    public BaseEntity() {
    }

    public BaseEntity(boolean deleted) {
        this.deleted = deleted;
    }

    // getters & setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

}

