package com.programmers.team6.comic_rental.controller;

import com.programmers.team6.comic_rental.entity.Rental;
import com.programmers.team6.comic_rental.service.RentalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalController {

    private final RentalService rentalService;

    // 날짜 포맷 (콘솔 출력용)
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * 대여 처리
     */
    public void rentComic(long comicId, long memberId) {
        try {
            long rentalId = rentalService.rentComic(comicId, memberId);
            System.out.printf("=> 대여 완료: [대여id=%d] 만화(%d) → 회원(%d)%n",
                    rentalId, comicId, memberId);

        } catch (IllegalArgumentException e) {
            System.out.println("대여 실패: 존재하지 않는 만화 또는 회원입니다.");

        } catch (IllegalStateException e) {
            System.out.println("대여 실패: 이미 대여 중인 만화입니다.");

        } catch (Exception e) {
            System.out.println("대여 중 오류 발생: 존재하지 않는 만화 또는 회원입니다.");
        }
    }

    /**
     * 반납 처리
     */
    public void returnComic(long rentalId) {
        try {
            rentalService.returnComic(rentalId);
            System.out.println("=> 반납 완료: 대여id=" + rentalId);

        } catch (IllegalArgumentException e) {
            System.out.println("반납 실패: 존재하지 않는 대여 정보입니다.");

        } catch (IllegalStateException e) {
            System.out.println("반납 실패: 이미 반납된 대여입니다.");

        } catch (Exception e) {
            System.out.println("반납 중 오류 발생: 존재하지 않는 대여 정보입니다.");
        }
    }

    /**
     * 대여 내역 목록 출력
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

            System.out.printf("%-5s | %-5s | %-4s | %-15s | %-14s | %-6s%n",
                    "대여ID", "만화ID", "회원ID", "대여일", "반납일", "연체");
            System.out.println("-----------------------------------------------------------------------");

            for (Rental rental : rentals) {

                String rentDateStr = rental.getRentDate().format(formatter);

                String returnDateStr = rental.isReturned()
                        ? rental.getReturnDate().format(formatter)
                        : "-";

                // 연체 여부
                String overdueStr = "-";
                if (!rental.isReturned()) {
                    boolean isOverdue = rental.getRentDate()
                            .plusDays(7)
                            .isBefore(LocalDateTime.now());

                    overdueStr = isOverdue ? "⚠️연체" : "정상";
                }

                System.out.printf("%-6d | %-6d | %-6d | %-16s | %-16s | %-6s%n",
                        rental.getRentalId(),
                        rental.getComicId(),
                        rental.getMemberId(),
                        rentDateStr,
                        returnDateStr,
                        overdueStr);
            }

        } catch (Exception e) {
            System.out.println("대여 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 연체 목록 출력
     */
    public void listOverdueRentals() {
        try {
            List<Rental> rentals = rentalService.getOverdueRentals();

            if (rentals.isEmpty()) {
                System.out.println("연체 중인 대여가 없습니다.");
                return;
            }

            System.out.printf("%-5s | %-5s | %-4s | %-15s | %-6s%n",
                    "대여ID", "만화ID", "회원ID", "대여일", "연체일수");
            System.out.println("------------------------------------------------------------");

            for (Rental rental : rentals) {

                long overdueDays = ChronoUnit.DAYS.between(
                        rental.getRentDate().plusDays(7),
                        LocalDateTime.now()
                );

                String rentDateStr = rental.getRentDate().format(formatter);

                System.out.printf("%-6d | %-6d | %-6d | %-16s | %d일%n",
                        rental.getRentalId(),
                        rental.getComicId(),
                        rental.getMemberId(),
                        rentDateStr,
                        overdueDays);
            }

        } catch (Exception e) {
            System.out.println("연체 목록 조회 중 오류가 발생했습니다.");
        }
    }
}