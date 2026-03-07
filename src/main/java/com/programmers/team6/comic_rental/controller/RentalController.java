package com.programmers.team6.comic_rental.controller;

import com.programmers.team6.comic_rental.service.RentalService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class RentalController {

    RentalService rentalService = new RentalService();
    Scanner scanner = new Scanner(System.in);

    public void processRental() {
        System.out.println("\n======= 만화책 대여 서비스 =======");

        try {
            System.out.print("회원 ID를 입력하세요: ");
            Long memberId = Long.parseLong(scanner.nextLine());

            System.out.print("만화책 ID를 입력하세요: ");
            Long comicId = Long.parseLong(scanner.nextLine());

            //대여 요청
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
}