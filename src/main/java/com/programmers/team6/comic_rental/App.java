package com.programmers.team6.comic_rental;

import java.util.Scanner;

/*
    명령어	        switch case
    ==========================================
    comic-add	    case "comic-add"
    comic-list	    case "comic-list"
    comic-detail	case "comic-detail"
    comic-update	case "comic-update"
    comic-delete	case "comic-delete"
    member-add	    case "member-add"
    member-list	    case "member-list"
    rent	        case "rent"
    return	        case "return"
    rental-list	    case "rental-list"
    exit	        case "exit"
 */

public class App {
    Scanner sc = new Scanner(System.in);

    public void run() {   // run 루프
        while (true) {
            System.out.print("명령어: ");
            String cmd = sc.nextLine();

            Rq rq = new Rq(cmd);        // 명령어 파싱 객체

            switch (rq.getActionPath()) {

                // 만화책
                case "comic-add":

                    break;

                case "comic-list":

                    break;

                case "comic-detail":

                    break;

                case "comic-update":

                    break;

                case "comic-delete":

                    break;

                // 회원
                case "member-add":

                    break;

                case "member-list":

                    break;

                // 대여
                case "rent":

                    break;

                case "return":

                    break;

                case "rental-list":

                    break;

                // 종료
                case "exit":
                    System.out.println("프로그램을 종료합니다.");
                    return;

                default:
                    System.out.println("존재하지 않는 명령어입니다.");
            }
        }
    }
}
