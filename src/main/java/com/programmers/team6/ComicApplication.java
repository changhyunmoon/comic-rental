package com.programmers.team6;

import com.programmers.team6.comic_rental.Runner;
import com.programmers.team6.comic_rental.controller.RentalController;
import com.programmers.team6.comic_rental.repository.RentalRepository;
import com.programmers.team6.comic_rental.service.RentalService;


public class ComicApplication {

	public static void main(String[] args) {
        //실행
        Runner runner = new Runner();
        runner.run();
    }

}
