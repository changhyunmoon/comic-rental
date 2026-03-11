package com.programmers.team6.comic_rental.entity;

import java.time.LocalDateTime;

public class Rental {

    private long rentalId;
    private long comicId;
    private long memberId;
    private LocalDateTime rentDate;
    private LocalDateTime returnDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Rental() {}

    // 대여 생성용 생성자
    public Rental(long comicId, long memberId) {
        this.comicId = comicId;
        this.memberId = memberId;
        this.rentDate = LocalDateTime.now();
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public long getRentalId() { return rentalId; }
    public void setRentalId(long rentalId) { this.rentalId = rentalId; }

    public long getComicId() { return comicId; }
    public void setComicId(long comicId) { this.comicId = comicId; }

    public long getMemberId() { return memberId; }
    public void setMemberId(long memberId) { this.memberId = memberId; }

    public LocalDateTime getRentDate() { return rentDate; }
    public void setRentDate(LocalDateTime rentDate) { this.rentDate = rentDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}