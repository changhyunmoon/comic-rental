package com.programmers.team6.comic_rental.service;

import com.programmers.team6.comic_rental.entity.Rental;
import com.programmers.team6.comic_rental.repository.RentalRepository;
import com.programmers.team6.comic_rental.repository.ComicRepository;

import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepository;
    private final ComicRepository comicRepository;

    public RentalService(RentalRepository rentalRepository,
                         ComicRepository comicRepository) {
        this.rentalRepository = rentalRepository;
        this.comicRepository = comicRepository;
    }

    public long rentComic(long comicId, long memberId) {

        boolean updated = comicRepository.updateRentalStatus(comicId, true);

        if(!updated){
            throw new IllegalStateException("이미 대여중인 만화입니다.");
        }

        try{
            return rentalRepository.save(comicId, memberId);
        }catch(RuntimeException e){

            // 실패하면 comic 상태 복구
            comicRepository.updateRentalStatus(comicId, false);

            throw e;
        }
    }

    public void returnComic(long rentalId){

        Rental rental = rentalRepository.findById(rentalId);

        // 존재하지 않는 대여
        if(rental == null){
            throw new IllegalArgumentException("존재하지 않는 대여입니다. (rentalId=" + rentalId + ")");
        }

        // 이미 반납된 경우
        if(rental.getReturnDate() != null){
            throw new IllegalStateException("이미 반납된 대여입니다. (rentalId=" + rentalId + ")");
        }

        boolean result = rentalRepository.returnRental(rentalId);

        if(!result){
            throw new IllegalStateException("반납 처리에 실패했습니다.");
        }

        comicRepository.updateRentalStatus(rental.getComicId(), false);
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