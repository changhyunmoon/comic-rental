package com.programmers.team6.comic_rental;

import com.programmers.team6.comic_rental.controller.RentalController;

import java.util.Scanner;

public class Runner {

    RentalController rentalController = new RentalController();
    Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean continueProcess = true;

        while (continueProcess) {

            rentalController.processRental();

            // 추가 진행 여부 확인
            System.out.print("\n계속하시겠습니까? (y/n): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("n")) {
                continueProcess = false;
                System.out.println("프로그램을 종료합니다.");
            }
        }
    }
}