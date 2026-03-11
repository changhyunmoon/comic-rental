package com.programmers.team6.comic_rental.entity;


import java.time.LocalDateTime;

public class Rental {
    private long rentalId;
    private long comicId;
    private long memberId;
    private LocalDateTime rentDate;
    private LocalDateTime returnDate;   // null이면 아직 반납 안 된 상태
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Rental() {}

    public Rental(long rentalId, long comicId, long memberId,
                  LocalDateTime rentDate, LocalDateTime returnDate,
                  LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.rentalId = rentalId;
        this.comicId = comicId;
        this.memberId = memberId;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
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

    // 반납 여부 편의 메서드 (returnDate가 null이면 미반납)
    public boolean isReturned() { return returnDate != null; }
}