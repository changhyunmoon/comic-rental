package com.programmers.team6.comic_rental;

import java.util.Scanner;

import com.programmers.team6.comic_rental.controller.ComicController;
import com.programmers.team6.comic_rental.controller.MemberController;
import com.programmers.team6.comic_rental.controller.RentalController;
import com.programmers.team6.comic_rental.repository.ComicRepository;
import com.programmers.team6.comic_rental.repository.MemberRepository;
import com.programmers.team6.comic_rental.repository.RentalRepository;
import com.programmers.team6.comic_rental.service.ComicService;
import com.programmers.team6.comic_rental.service.RentalService;

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

    ComicRepository comicRepository = new ComicRepository();
    ComicService comicService = new ComicService(comicRepository);
    ComicController comicController = new ComicController(sc, comicService);
    MemberController memberController = new MemberController(sc);

    // 대여 관련 객체 추가
    RentalRepository rentalRepository = new RentalRepository();
    RentalService rentalService = new RentalService(rentalRepository, comicRepository, new MemberRepository());
    RentalController rentalController = new RentalController(rentalService);

    public void run() {   // run 루프
        while (true) {
            System.out.print("명령어: ");
            String cmd = sc.nextLine();

            if (cmd.isEmpty()) {
                System.out.println("명령어를 입력해주세요.");
                continue;
            }

            Rq rq = new Rq(cmd);        // 명령어 파싱 객체

            switch (rq.getActionPath()) {

                // 만화책
                case "comic-add":
                    comicController.addComic();
                    break;

                case "comic-list":
                    comicController.listComics();
                    break;

                case "comic-detail":
                    if (rq.getComicId() == 0) {
                        System.out.println("사용법: comic-detail [id]");
                        break;
                    }
                    comicController.detailComic(rq.getComicId());
                    break;

                case "comic-update":
                    if (rq.getComicId() == 0) {
                        System.out.println("사용법: comic-update [id]");
                        break;
                    }
                    comicController.updateComic(rq.getComicId());
                    break;

                case "comic-delete":
                    if (rq.getComicId() == 0) {
                        System.out.println("사용법: comic-delete [id]");
                        break;
                    }
                    comicController.deleteComic(rq.getComicId());
                    break;

                // 회원
                case "member-add":
                    memberController.addMember();
                    break;

                case "member-list":
                    memberController.findAllMembers();
                    break;

                // 대여
                case "rent":
                    if (rq.getComicId() == 0 || rq.getMemberId() == 0) {
                        System.out.println("사용법: rent [comicId] [memberId]");
                        break;
                    }
                    rentalController.rentComic(rq.getComicId(), rq.getMemberId());
                    break;

                case "return":
                    if (rq.getRentalId() == 0) {
                        System.out.println("사용법: return [rentalId]");
                        break;
                    }
                    rentalController.returnComic(rq.getRentalId());
                    break;

                case "rental-list":
                    if (cmd.trim().equals("rental-list open")) {
                        // 미반납만 출력
                        rentalController.listRentals(true);
                    } else if (cmd.trim().equals("rental-list overdue")) {
                        // 연체 목록만 출력
                        rentalController.listOverdueRentals();
                    } else {
                        // 전체 출력
                        rentalController.listRentals(false);
                    }
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
