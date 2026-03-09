package com.programmers.team6.comic_rental.entity;

import java.time.LocalDateTime;

public class Rental {
    private Long rentalId;          // ✅ PK 추가 (팀원 코드에 빠져있던 것)
    private Long comicId;
    private Long memberId;
    private LocalDateTime rentDate;
    private LocalDateTime returnDate;   // 대여 시엔 null, 반납 시 날짜 들어감
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // ✅ 대여 시 사용 (returnDate = null)
    public Rental(Long comicId, Long memberId, LocalDateTime rentDate,
                  LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.comicId = comicId;
        this.memberId = memberId;
        this.rentDate = rentDate;
        this.returnDate = null;  // 대여 시점엔 반납일 없음
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // ✅ DB 조회 시 사용 (전체 필드)
    public Rental(Long rentalId, Long comicId, Long memberId,
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

    // Getter
    public Long getRentalId() { return rentalId; }
    public Long getComicId() { return comicId; }
    public Long getMemberId() { return memberId; }
    public LocalDateTime getRentDate() { return rentDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }

    // Setter
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", comicId=" + comicId +
                ", memberId=" + memberId +
                ", rentDate=" + rentDate +
                ", returnDate=" + (returnDate != null ? returnDate : "미반납") +
                '}';
    }
}