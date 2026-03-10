package com.programmers.team6.comic_rental.service;

import com.programmers.team6.comic_rental.entity.Rental;
import com.programmers.team6.comic_rental.repository.ComicRepository;
import com.programmers.team6.comic_rental.repository.MemberRepository;
import com.programmers.team6.comic_rental.repository.RentalRepository;

import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepository;
    private final ComicRepository comicRepository;
    private final MemberRepository memberRepository;

    public RentalService(RentalRepository rentalRepository,
                         ComicRepository comicRepository,
                         MemberRepository memberRepository) {
        this.rentalRepository = rentalRepository;
        this.comicRepository = comicRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * 대여 처리
     * 1) comicId 존재 확인
     * 2) memberId 존재 확인  ← MemberRepository에 existsById 없으면 아래 주석 참고
     * 3) 이미 대여중인지 확인 (comic.is_rented == true)
     * 4) rental INSERT + comic.is_rented = true UPDATE
     */
    public long rentComic(long comicId, long memberId) {
        // 만화책 존재 확인
        if (!comicRepository.existsById(comicId)) {
            throw new IllegalArgumentException("존재하지 않는 만화책입니다. (comicId=" + comicId + ")");
        }

        // 이미 대여중인지 확인
        if (comicRepository.isRented(comicId)) {
            throw new IllegalStateException("이미 대여중인 만화책입니다. (comicId=" + comicId + ")");
        }

        // rental 행 INSERT → 생성된 rentalId 반환
        long rentalId = rentalRepository.save(comicId, memberId);

        // comic.is_rented = true 로 변경
        comicRepository.updateRentalStatus(comicId, true);

        return rentalId;
    }

    /**
     * 반납 처리
     * 1) rentalId 존재 확인
     * 2) 이미 반납된 건인지 확인 (return_date != null)
     * 3) rental.return_date = NOW() UPDATE + comic.is_rented = false UPDATE
     */
    public void returnComic(long rentalId) {
        Rental rental = rentalRepository.findById(rentalId);

        // 존재하지 않는 대여 ID
        if (rental == null) {
            throw new IllegalArgumentException("존재하지 않는 대여 내역입니다. (rentalId=" + rentalId + ")");
        }

        // 이미 반납 완료된 건
        if (rental.isReturned()) {
            throw new IllegalStateException("이미 반납된 대여 내역입니다. (rentalId=" + rentalId + ")");
        }

        // return_date 업데이트
        rentalRepository.returnRental(rentalId);

        // comic.is_rented = false 로 변경
        comicRepository.updateRentalStatus(rental.getComicId(), false);
    }

    /**
     * 전체 대여 내역 조회
     */
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * 미반납(대여중) 내역만 조회
     */
    public List<Rental> getOpenRentals() {
        return rentalRepository.findAllOpen();
    }
}