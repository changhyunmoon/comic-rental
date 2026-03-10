package com.programmers.team6.comic_rental.controller;

import com.programmers.team6.comic_rental.entity.Rental;
import com.programmers.team6.comic_rental.service.RentalService;

import java.util.List;

public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * 대여 처리
     * App.java에서 rent [comicId] [memberId] 파싱 후 호출
     */
    public void rentComic(long comicId, long memberId) {
        try {
            long rentalId = rentalService.rentComic(comicId, memberId);
            System.out.printf("=> 대여 완료: [대여id=%d] 만화(%d) → 회원(%d)%n",
                    rentalId, comicId, memberId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 존재하지 않는 ID, 이미 대여중 등 예상된 오류
            System.out.println("대여 실패: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("대여 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 반납 처리
     * App.java에서 return [rentalId] 파싱 후 호출
     */
    public void returnComic(long rentalId) {
        try {
            rentalService.returnComic(rentalId);
            System.out.println("=> 반납 완료: 대여id=" + rentalId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 존재하지 않는 ID, 이미 반납됨 등 예상된 오류
            System.out.println("반납 실패: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("반납 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 대여 내역 목록 출력
     * onlyOpen=true 면 미반납만, false 면 전체 출력
     */
    public void listRentals(boolean onlyOpen) {
        try {
            List<Rental> rentals = onlyOpen
                    ? rentalService.getOpenRentals()
                    : rentalService.getAllRentals();

            if (rentals.isEmpty()) {
                System.out.println("대여 내역이 없습니다.");
                return;
            }

            System.out.println("대여id | 만화id | 회원id | 대여일              | 반납일");
            System.out.println("------------------------------------------------------------------");

            for (Rental rental : rentals) {
                String returnDateStr = rental.isReturned()
                        ? rental.getReturnDate().toString()
                        : "-";  // 미반납은 "-" 표시

                System.out.printf("%-6d | %-6d | %-6d | %-19s | %s%n",
                        rental.getRentalId(),
                        rental.getComicId(),
                        rental.getMemberId(),
                        rental.getRentDate(),
                        returnDateStr);
            }
        } catch (Exception e) {
            System.out.println("목록 조회 실패: " + e.getMessage());
        }
    }
}