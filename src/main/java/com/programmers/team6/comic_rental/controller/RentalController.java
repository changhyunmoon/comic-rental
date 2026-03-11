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
     * rent [comicId] [memberId] 명령어 처리
     */
    public void rentComic(long comicId, long memberId) {
        try {
            long rentalId = rentalService.rentComic(comicId, memberId);
            System.out.printf("=> 대여 완료: [대여id=%d] 만화(%d) → 회원(%d)%n",
                    rentalId, comicId, memberId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("대여 실패: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("대여 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 반납 처리
     * return [rentalId] 명령어 처리
     */
    public void returnComic(long rentalId) {
        try {
            rentalService.returnComic(rentalId);
            System.out.println("=> 반납 완료: 대여id=" + rentalId);
        } catch (IllegalArgumentException | IllegalStateException e) {
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

            System.out.println("대여id | 만화id | 회원id | 대여일              | 반납일     | 연체여부");
            System.out.println("--------------------------------------------------------------------------");

            for (Rental rental : rentals) {
                String returnDateStr = rental.isReturned()
                        ? rental.getReturnDate().toString()
                        : "-";

                // 연체 여부: 미반납 + 7일 초과면 연체
                String overdueStr = "-";
                if (!rental.isReturned()) {
                    boolean isOverdue = rental.getRentDate()
                            .plusDays(7)
                            .isBefore(java.time.LocalDateTime.now());
                    overdueStr = isOverdue ? "⚠️연체" : "정상";
                }

                System.out.printf("%-6d | %-6d | %-6d | %-19s | %-10s | %s%n",
                        rental.getRentalId(),
                        rental.getComicId(),
                        rental.getMemberId(),
                        rental.getRentDate(),
                        returnDateStr,
                        overdueStr);
            }
        } catch (Exception e) {
            System.out.println("목록 조회 실패: " + e.getMessage());
        }
    }

    /**
     * 연체 목록만 출력
     * rental-list overdue 명령어 처리
     */
    public void listOverdueRentals() {
        try {
            List<Rental> rentals = rentalService.getOverdueRentals();

            if (rentals.isEmpty()) {
                System.out.println("연체 중인 대여가 없습니다.");
                return;
            }

            System.out.println("대여id | 만화id | 회원id | 대여일              | 연체일수");
            System.out.println("------------------------------------------------------------------");

            for (Rental rental : rentals) {
                // 연체 일수 계산: 오늘 - 대여일 - 7일
                long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                        rental.getRentDate().plusDays(7),
                        java.time.LocalDateTime.now()
                );

                System.out.printf("%-6d | %-6d | %-6d | %-19s | %d일 연체%n",
                        rental.getRentalId(),
                        rental.getComicId(),
                        rental.getMemberId(),
                        rental.getRentDate(),
                        overdueDays);
            }
        } catch (Exception e) {
            System.out.println("연체 목록 조회 실패: " + e.getMessage());
        }
    }
}