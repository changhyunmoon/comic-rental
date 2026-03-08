/* package com.programmers.team6.comic_rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


public class ComicApplication {

	public static void main(String[] args) {

	}

} */

package com.programmers.team6.comic_rental;

import com.programmers.team6.comic_rental.controller.ComicController;
import com.programmers.team6.comic_rental.repository.ComicRepository;
import com.programmers.team6.comic_rental.service.ComicService;

import java.util.Scanner;

public class ComicApplication {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		ComicRepository comicRepository = new ComicRepository();
		ComicService comicService = new ComicService(comicRepository);
		ComicController comicController = new ComicController(scanner, comicService);

		while (true) {
			System.out.print("명령어: ");
			String command = scanner.nextLine().trim();

			if (command.equals("exit")) {
				System.out.println("프로그램을 종료합니다.");
				break;
			} else if (command.equals("comic-add")) {
				comicController.addComic();
			} else if (command.equals("comic-list")) {
				comicController.listComics();
			} else if (command.startsWith("comic-detail ")) {
				String[] parts = command.split(" ");
				if (parts.length != 2) {
					System.out.println("사용법: comic-detail [id]");
					continue;
				}
				long comicId = Long.parseLong(parts[1]);
				comicController.detailComic(comicId);
			} else if (command.startsWith("comic-update ")) {
				String[] parts = command.split(" ");
				if (parts.length != 2) {
					System.out.println("사용법: comic-update [id]");
					continue;
				}
				long comicId = Long.parseLong(parts[1]);
				comicController.updateComic(comicId);
			} else if (command.startsWith("comic-delete ")) {
				String[] parts = command.split(" ");
				if (parts.length != 2) {
					System.out.println("사용법: comic-delete [id]");
					continue;
				}
				long comicId = Long.parseLong(parts[1]);
				comicController.deleteComic(comicId);
			} else {
				System.out.println("알 수 없는 명령어입니다.");
			}
		}

		scanner.close();
	}
}
