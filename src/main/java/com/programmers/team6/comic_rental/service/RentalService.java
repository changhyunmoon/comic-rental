package com.programmers.team6.comic_rental.service;

import com.programmers.team6.comic_rental.entity.Rental;
import com.programmers.team6.comic_rental.repository.RentalRepository;

import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public long rentComic(long comicId, long memberId) {

        if(rentalRepository.isComicRented(comicId)){
            throw new IllegalStateException("이미 대여중인 만화입니다.");
        }

        return rentalRepository.save(comicId, memberId);
    }

    public void returnComic(long rentalId){

        boolean result = rentalRepository.returnRental(rentalId);

        if(!result){
            throw new IllegalArgumentException("존재하지 않는 대여입니다.");
        }
    }

    public List<Rental> getAllRentals(){
        return rentalRepository.findAll();
    }

    public List<Rental> getOpenRentals(){
        return rentalRepository.findAllOpen();
    }

    public List<Rental> getOverdueRentals(){
        return rentalRepository.findAllOverdue();
    }
}