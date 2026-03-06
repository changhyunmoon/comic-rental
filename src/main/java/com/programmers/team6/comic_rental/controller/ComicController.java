package main.java.com.programmers.team6.comic_rental.controller;

import main.java.com.programmers.team6.comic_rental.entity.Comic;
import main.java.com.programmers.team6.comic_rental.service.ComicService;

import java.util.List;
import java.util.Scanner;

public class ComicController {

    private final Scanner scanner;
    private final ComicService comicService;

    public ComicController(Scanner scanner, ComicService comicService) {
        this.scanner = scanner;
        this.comicService = comicService;
    }

    public void addComic() {
        try {
            System.out.print("제목(예: 원피스1): ");
            String title = scanner.nextLine();

            System.out.print("작가: ");
            String author = scanner.nextLine();

            long id = comicService.addComic(title, author);
            System.out.println("=> 만화책이 등록되었습니다. (id=" + id + ")");
        } catch (Exception e) {
            System.out.println("등록 실패: " + e.getMessage());
        }
    }

    public void listComics() {
        try {
            List<Comic> comics = comicService.getAllComics();

            if (comics.isEmpty()) {
                System.out.println("등록된 만화책이 없습니다.");
                return;
            }

            System.out.println("번호 | 제목 | 권수 | 작가 | 상태");
            System.out.println("------------------------------------------------");

            for (Comic comic : comics) {
                String[] parsed = splitTitleAndVolume(comic.getTitle());

                System.out.printf("%d | %s | %s | %s | %s%n",
                        comic.getComicId(),
                        parsed[0],
                        parsed[1].isEmpty() ? "-" : parsed[1],
                        comic.getAuthor(),
                        comic.isRented() ? "대여중" : "대여가능");
            }
        } catch (Exception e) {
            System.out.println("목록 조회 실패: " + e.getMessage());
        }
    }

    public void detailComic(long comicId) {
        try {
            Comic comic = comicService.getComicById(comicId);

            if (comic == null) {
                System.out.println("해당 만화책이 존재하지 않습니다.");
                return;
            }

            String[] parsed = splitTitleAndVolume(comic.getTitle());

            System.out.println("번호: " + comic.getComicId());
            System.out.println("제목: " + parsed[0]);
            System.out.println("권수: " + (parsed[1].isEmpty() ? "-" : parsed[1]));
            System.out.println("작가: " + comic.getAuthor());
            System.out.println("상태: " + (comic.isRented() ? "대여중" : "대여가능"));
            System.out.println("등록일: " + comic.getCreatedDate());
            System.out.println("수정일: " + comic.getUpdatedDate());
        } catch (Exception e) {
            System.out.println("상세 조회 실패: " + e.getMessage());
        }
    }

    public void updateComic(long comicId) {
        try {
            Comic comic = comicService.getComicById(comicId);

            if (comic == null) {
                System.out.println("해당 만화책이 존재하지 않습니다.");
                return;
            }

            System.out.println("현재 제목: " + comic.getTitle());
            System.out.print("새 제목(예: 원피스2): ");
            String newTitle = scanner.nextLine();

            System.out.println("현재 작가: " + comic.getAuthor());
            System.out.print("새 작가: ");
            String newAuthor = scanner.nextLine();

            boolean result = comicService.updateComic(comicId, newTitle, newAuthor);

            if (result) {
                System.out.println("=> 만화책이 수정되었습니다.");
            } else {
                System.out.println("=> 수정 실패");
            }
        } catch (Exception e) {
            System.out.println("수정 실패: " + e.getMessage());
        }
    }

    public void deleteComic(long comicId) {
        try {
            boolean result = comicService.deleteComic(comicId);

            if (result) {
                System.out.println("=> 만화책이 삭제되었습니다.");
            } else {
                System.out.println("=> 해당 만화책이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            System.out.println("삭제 실패: " + e.getMessage());
        }
    }

    private String[] splitTitleAndVolume(String rawTitle) {
        if (rawTitle == null || rawTitle.isBlank()) {
            return new String[]{"", ""};
        }

        String title = rawTitle.trim();
        int idx = title.length() - 1;

        while (idx >= 0 && Character.isDigit(title.charAt(idx))) {
            idx--;
        }

        String namePart = title.substring(0, idx + 1).trim();
        String volumePart = title.substring(idx + 1).trim();

        return new String[]{namePart, volumePart};
    }
}