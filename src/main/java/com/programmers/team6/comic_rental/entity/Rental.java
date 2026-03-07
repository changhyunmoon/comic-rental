package com.programmers.team6.comic_rental.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Rental {
    private LocalDateTime rentDate;     // 대여일
    private LocalDateTime returnDate;   // 반납일 (또는 반납 예정일)
    private LocalDateTime createdDate;  // 데이터 생성일
    private LocalDateTime updatedDate;  // 데이터 수정일
    private Long comicId;               // 대여된 만화책 ID (FK 역할)
    private Long memberId;              // 대여한 회원 ID (FK 역할)

    // 모든 필드를 포함 생성자
    public Rental(LocalDateTime rentDate, LocalDateTime returnDate,
                  LocalDateTime createdDate, LocalDateTime updatedDate,
                  Long comicId, Long memberId) {
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.comicId = comicId;
        this.memberId = memberId;
    }

//    // Getter & Setter
//    public Long getRentalId() { return rentalId; }
//    public void setRentalId(Long rentalId) { this.rentalId = rentalId; }
//
//    public LocalDateTime getRentDate() { return rentDate; }
//    public void setRentDate(LocalDateTime rentDate) { this.rentDate = rentDate; }
//
//    public LocalDateTime getReturnDate() { return returnDate; }
//    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
//
//    public LocalDateTime getCreatedDate() { return createdDate; }
//    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
//
//    public LocalDateTime getUpdatedDate() { return updatedDate; }
//    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
//
//    public Long getComicId() { return comicId; }
//    public void setComicId(Long comicId) { this.comicId = comicId; }
//
//    public Long getMemberId() { return memberId; }
//    public void setMemberId(Long memberId) { this.memberId = memberId; }

    @Override
    public String toString() {
        return "Rental{" +
                ", rentDate=" + rentDate +
                ", returnDate=" + returnDate +
                ", comicId=" + comicId +
                ", memberId=" + memberId +
                '}';
    }
}