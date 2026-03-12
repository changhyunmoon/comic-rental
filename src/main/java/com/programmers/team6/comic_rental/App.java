package com.programmers.team6.comic_rental;

import java.util.Scanner;

import com.programmers.team6.comic_rental.controller.ComicController;
import com.programmers.team6.comic_rental.controller.MemberController;
import com.programmers.team6.comic_rental.controller.RentalController;
import com.programmers.team6.comic_rental.repository.ComicRepository;
import com.programmers.team6.comic_rental.repository.MemberRepository;
import com.programmers.team6.comic_rental.repository.RentalRepository;
import com.programmers.team6.comic_rental.service.ComicService;
import com.programmers.team6.comic_rental.controller.RentalController;
import com.programmers.team6.comic_rental.service.RentalService;
import com.programmers.team6.comic_rental.repository.RentalRepository;

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
    RentalRepository rentalRepository = new RentalRepository();
    RentalService rentalService = new RentalService(new RentalRepository(), new ComicRepository());
    RentalController rentalController = new RentalController(rentalService);

    // 대여 관련 객체 추가
    RentalRepository rentalRepository = new RentalRepository();
    RentalService rentalService = new RentalService(rentalRepository, comicRepository, new MemberRepository());
    RentalController rentalController = new RentalController(rentalService);

    public void run() {   // run 루프
        printBanner();
        while (true) {
            try {
                System.out.print("명령어: ");
                String cmd = sc.nextLine();

                if (cmd.isEmpty()) {
                    System.out.println("명령어를 입력해주세요.(help 입력)");
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
                        comicController.detailComic(rq.getComicId());
                        break;

                    case "comic-update":
                        comicController.updateComic(rq.getComicId());
                        break;

                    case "comic-delete":
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
                        rentalController.rentComic(rq.getComicId(), rq.getMemberId());
                        break;

                    case "return":
                        rentalController.returnComic(rq.getRentalId());
                        break;

                    case "rental-list":
                        rentalController.listRentals(false);
                        break;

                    case "rental-list open":
                        rentalController.listRentals(true);
                        break;

                    case "rental-overdue":
                        rentalController.listOverdueRentals();
                        break;

                    case "help":
                        printHelp();
                        break;

                    // 종료
                    case "exit":
                        System.out.println("\uD83D\uDCDA 만화책 대여점 시스템을 종료합니다.\n" +
                                "이용해주셔서 감사합니다.");
                        return;

                    default:
                        System.out.println("존재하지 않는 명령어입니다. (help 입력)");
                }
            } catch (Exception e) {
                System.out.println("명령 처리 중 오류가 발생했습니다.");
                System.out.println("입력값을 확인해주세요.");
            }
        }
    }

    //유틸
    private void printBanner() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      📚 만화책 대여점 시스템            ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("  'help' 를 입력하면 명령어 목록을 볼 수 있습니다.");
    }

    private void printHelp() {
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────────────────┐");
        System.out.println("                     명령어 목록                         ");
        System.out.println("  ├──────────────────────────┬─────────────────────────┤");
        System.out.println("    comic-add                │ 만화책 등록                ");
        System.out.println("    comic-list               │ 만화책 목록                ");
        System.out.println("    comic-detail [id]        │ 만화책 상세                ");
        System.out.println("    comic-update [id]        │ 만화책 수정                ");
        System.out.println("    comic-delete [id]        │ 만화책 삭제                ");
        System.out.println("  ├──────────────────────────┼──────────────────────────┤");
        System.out.println("    member-add               │ 회원 등록                  ");
        System.out.println("    member-list              │ 회원 목록                  ");
        System.out.println("  ├──────────────────────────┼──────────────────────────┤");
        System.out.println("    rent [comicId] [memberId]│ 대여                       ");
        System.out.println("    return [rentalId]        │ 반납                       ");
        System.out.println("    rental-list              │ 전체 대여 목록             ");
        System.out.println("    rental-list open         │ 미반납 목록                ");
        System.out.println("    rental-overdue           │ 연체 목록 (7일 초과)       ");
        System.out.println("  ├──────────────────────────┼──────────────────────────┤");
        System.out.println("    help                     │ 도움말                     ");
        System.out.println("    exit                     │ 종료                       ");
        System.out.println("  └──────────────────────────┴──────────────────────────┘");
    }
}
