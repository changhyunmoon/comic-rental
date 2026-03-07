package com.programmers.team6.comic_rental.service;

import com.programmers.team6.comic_rental.entity.Rental;
import com.programmers.team6.comic_rental.repository.RentalRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class RentalService {

    RentalRepository rentalRepository = new RentalRepository();

    // 만화책 대여
    public Rental rentBook(Long memberId, Long comicId) throws SQLException {

        //검증 로직 (아직 member, comic 구현이 안되어 있어 나중에 추가)
        //ex) comic 테이블에서 해당 만화택이 빌릴 수 있는지
        //ex) 빌리려는 사람이 빌려도 되는지 (연체해서 못빌리는 경우, 이미 여러권 빌린 경우) <- 이 기능은 넣고싶으면 추가.

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime returnDate = now.plusDays(7);

        // 엔티티 객체 생성
        Rental rental = new Rental(now, returnDate, now, now, comicId, memberId);

        // 저장
        rentalRepository.save(rental);

        System.out.println("[Service] 대여 정보 저장 완료: 회원 " + memberId + ", 도서 " + comicId);

        // [수정] 저장된(확정된 데이터가 담긴) 엔티티 객체 반환
        return rental;
    }
}