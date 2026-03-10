package com.programmers.team6.comic_rental.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Rental {

    // ✅ 새로 추가: rentalId (PK)
    // 이유:  코드에 PK가 빠져있었음
    //       반납할 때 "몇 번 대여건을 반납할게요" 하려면 rentalId가 꼭 필요함
    //       DB rental 테이블의 rental_id 컬럼과 매핑됨
    private Long rentalId;

    //  코드 그대로 (필드)
    private LocalDateTime rentDate;     // 대여일
    private LocalDateTime returnDate;   // 반납일 (반납 전엔 null)
    private LocalDateTime createdDate;  // 데이터 생성일
    private LocalDateTime updatedDate;  // 데이터 수정일
    private Long comicId;               // 대여된 만화책 ID (FK 역할)
    private Long memberId;              // 대여한 회원 ID (FK 역할)

    // ✅ 새로 추가: 대여 시 사용하는 생성자
    // 이유: 창현님 생성자는 returnDate를 무조건 받도록 되어있었음
    //       근데 대여 시점엔 returnDate가 null이어야 함
    //       null인 상태 = "아직 반납 안 됨" 을 표현하는 것
    //       그래서 returnDate 없이 만드는 생성자를 새로 추가함
    public Rental(Long comicId, Long memberId, LocalDateTime rentDate,
                  LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.comicId = comicId;
        this.memberId = memberId;
        this.rentDate = rentDate;
        this.returnDate = null;  // 대여 시점엔 반납일 없음
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // ✅ 새로 추가: DB 조회 시 사용하는 생성자
    // 이유: DB에서 대여 목록을 가져올 때는 rentalId 포함한 모든 필드가 필요함
    //       위 생성자는 rentalId가 없어서 DB 조회용으로 쓸 수가 없음
    //       그래서 rentalId 포함한 전체 필드 생성자를 따로 만든 것
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

    // 코드 그대로 (toString)
    // returnDate null 체크 추가: null이면 "미반납" 출력
    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", rentDate=" + rentDate +
                ", returnDate=" + (returnDate != null ? returnDate : "미반납") +
                ", comicId=" + comicId +
                ", memberId=" + memberId +
                '}';
    }
}