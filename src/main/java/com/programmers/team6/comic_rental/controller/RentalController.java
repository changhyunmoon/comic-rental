package com.programmers.team6.comic_rental.controller;

import com.programmers.team6.comic_rental.entity.Rental; // ✅ 새로 추가: 대여 목록 출력할 때 Rental 객체 필요해서 추가
import com.programmers.team6.comic_rental.service.RentalService;

import java.time.LocalDateTime;
import java.util.List; // ✅ 새로 추가: 대여 목록을 List로 받아오기 위해 추가
import java.util.Scanner;

public class RentalController {

    // 코드 그대로
    RentalService rentalService = new RentalService();
    Scanner scanner = new Scanner(System.in);

    // ==============================
    // 코드 그대로 (processRental)
    // ==============================
    public void processRental() {
        System.out.println("\n======= 만화책 대여 서비스 =======");

        try {
            System.out.print("회원 ID를 입력하세요: ");
            Long memberId = Long.parseLong(scanner.nextLine());

            System.out.print("만화책 ID를 입력하세요: ");
            Long comicId = Long.parseLong(scanner.nextLine());

            // 대여 요청
            rentalService.rentBook(memberId, comicId);

            System.out.println(">>> 대여 처리가 정상적으로 완료되었습니다.");
            System.out.println(">>> 반납 예정일: " + LocalDateTime.now().plusDays(7));

        } catch (NumberFormatException e) {
            System.out.println(" 오류: ID는 숫자만 입력 가능합니다.");
        } catch (Exception e) {
            System.out.println(" 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==============================
    // ✅ 새로 추가: 반납 처리
    // 이유:  코드에 반납 기능이 없었음
    // 흐름: 사용자에게 대여ID 입력받음
    //       → RentalService.returnBook() 호출
    //       → 이미 반납됐거나 없는 ID면 오류 메시지 출력
    // ==============================
    public void processReturn() {
        System.out.println("\n======= 만화책 반납 =======");

        try {
            System.out.print("반납할 대여 ID를 입력하세요: ");
            Long rentalId = Long.parseLong(scanner.nextLine());

            rentalService.returnBook(rentalId);

            System.out.println(">>> 반납 처리가 정상적으로 완료되었습니다. (대여ID=" + rentalId + ")");

        } catch (NumberFormatException e) {
            System.out.println(" 오류: ID는 숫자만 입력 가능합니다.");
        } catch (IllegalStateException e) {
            // 이미 반납된 경우 RentalService에서 던진 오류
            System.out.println(" 오류: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==============================
    // ✅ 새로 추가: 대여 목록 출력
    // 이유: 코드에 목록 조회 기능이 없었음
    // 흐름: RentalService.getRentalList() 호출
    //       → 전체 대여 목록을 표 형식으로 출력
    //       → returnDate가 null이면 "미반납", 값 있으면 반납일 출력
    // ==============================
    public void showRentalList() {
        System.out.println("\n======= 대여 목록 =======");

        try {
            List<Rental> list = rentalService.getRentalList();

            if (list.isEmpty()) {
                System.out.println("대여 내역이 없습니다.");
                return;
            }

            System.out.printf("%-10s %-10s %-10s %-22s %-22s%n",
                    "대여ID", "만화ID", "회원ID", "대여일", "반납일");
            System.out.println("-".repeat(75));

            for (Rental r : list) {
                System.out.printf("%-10d %-10d %-10d %-22s %-22s%n",
                        r.getRentalId(),
                        r.getComicId(),
                        r.getMemberId(),
                        r.getRentDate(),
                        r.getReturnDate() != null ? r.getReturnDate() : "미반납"
                );
            }
        } catch (Exception e) {
            System.out.println(" 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}