package com.programmers.team6.comic_rental.service;

import com.programmers.team6.comic_rental.entity.Comic;
import com.programmers.team6.comic_rental.repository.ComicRepository;

import java.util.List;

public class ComicService {

    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public long addComic(String title, String author) {
        validateTitle(title);
        validateAuthor(author);
        return comicRepository.save(title.trim(), author.trim());
    }

    public List<Comic> getAllComics() {
        return comicRepository.findAll();
    }

    public Comic getComicById(long comicId) {
        return comicRepository.findById(comicId);
    }

    public boolean updateComic(long comicId, String title, String author) {
        if (!comicRepository.existsById(comicId)) {
            return false;
        }

        validateTitle(title);
        validateAuthor(author);

        return comicRepository.update(comicId, title.trim(), author.trim());
    }

    public boolean deleteComic(long comicId) {
        if (!comicRepository.existsById(comicId)) {
            return false;
        }

        if (comicRepository.isRented(comicId)) {
            throw new IllegalStateException("대여 중인 만화책은 삭제할 수 없습니다.");
        }

        return comicRepository.delete(comicId);
    }

    public boolean updateRentalStatus(long comicId, boolean rented) {
        return comicRepository.updateRentalStatus(comicId, rented);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
    }

    private void validateAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("작가는 비어 있을 수 없습니다.");
        }
    }
}