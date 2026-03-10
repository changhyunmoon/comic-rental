package com.programmers.team6.comic_rental.service;

import com.programmers.team6.comic_rental.entity.Rental;
import com.programmers.team6.comic_rental.repository.RentalRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class RentalService {

    // 코드 그대로
    RentalRepository rentalRepository = new RentalRepository();

    // ==============================
    // 창현님 코드 기반 (일부 수정)
    // ==============================
    public Rental rentBook(Long memberId, Long comicId) throws SQLException {

        // 코드 그대로

        // ex) comic 테이블에서 해당 만화책이 빌릴 수 있는지
        // ex) 빌리려는 사람이 빌려도 되는지

        // ✅ 새로 추가: 이미 대여중인지 체크
        // 이유: 같은 만화책을 두 명이 동시에 빌리는 걸 막기 위해
        // RentalRepository.isRented()로 DB에서 현재 대여중인지 확인
        if (rentalRepository.isRented(comicId)) {
            throw new IllegalStateException("이미 대여중인 만화책입니다. (comicId=" + comicId + ")");
        }

        LocalDateTime now = LocalDateTime.now();

        // ✅ 수정: Rental 생성자 호출 방식 변경
        //  코드: new Rental(now, returnDate, now, now, comicId, memberId)
        // → 문제: returnDate를 now+7일로 미리 설정해서 DB에 넣어버림
        //         대여 시점엔 returnDate가 null이어야 함
        //         null인 상태여야 "아직 반납 안 됨"을 표현할 수 있음
        // 수정 코드: new Rental(comicId, memberId, now, now, now)
        // → Rental.java에 새로 만든 생성자 사용 (returnDate 자동으로 null)
        Rental rental = new Rental(comicId, memberId, now, now, now);

        //  코드 그대로
        rentalRepository.save(rental);
        System.out.println("[Service] 대여 정보 저장 완료: 회원 " + memberId + ", 도서 " + comicId);

        return rental;
    }

    // ==============================
    // ✅ 새로 추가: 반납 처리
    // 이유: 코드에 반납 기능이 없었음
    // 흐름: 대여ID로 DB조회 → 이미 반납됐는지 확인 → 반납일 업데이트
    // ==============================
    public void returnBook(Long rentalId) throws SQLException {
        // 대여ID로 DB에서 해당 대여 정보 가져옴
        // 존재하지 않는 ID면 RentalRepository.findById()에서 오류 던져줌
        Rental rental = rentalRepository.findById(rentalId);

        // 이미 반납됐는지 체크
        // returnDate가 null이 아니면 = 이미 반납된 것
        if (rental.getReturnDate() != null) {
            throw new IllegalStateException("이미 반납된 대여입니다. (rentalId=" + rentalId + ")");
        }

        // 반납일을 지금 시간으로 업데이트
        rentalRepository.returnRental(rentalId, LocalDateTime.now());
        System.out.println("[Service] 반납 완료 - 대여ID: " + rentalId);
    }

    // ==============================
    // ✅ 새로 추가: 전체 대여 목록 조회
    // 이유: 코드에 목록 조회 기능이 없었음
    // 흐름: RentalRepository.findAll()로 DB에서 전체 목록 가져와서 반환
    // ==============================
    public List<Rental> getRentalList() throws SQLException {
        return rentalRepository.findAll();
    }
}