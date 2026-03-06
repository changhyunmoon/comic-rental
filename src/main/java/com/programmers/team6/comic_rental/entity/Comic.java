package main.java.com.programmers.team6.comic_rental.entity;

import java.time.LocalDateTime;

public class Comic {
    private long comicId;
    private String title;
    private String author;
    private boolean rented;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Comic() {
    }

    public Comic(long comicId, String title, String author, boolean rented,
                 LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.comicId = comicId;
        this.title = title;
        this.author = author;
        this.rented = rented;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public long getComicId() {
        return comicId;
    }

    public void setComicId(long comicId) {
        this.comicId = comicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}