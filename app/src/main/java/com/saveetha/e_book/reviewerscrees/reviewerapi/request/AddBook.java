package com.saveetha.e_book.reviewerscrees.reviewerapi.request;

import java.io.File;

public class AddBook {

    private String publisher_id, publisher_name, book_titile, book_description, auther_name, year_of_the_book, category_name, book_price;

    private File cover_image,book,demo_file;

    public AddBook(String publisher_id, String publisher_name, String book_titile, String book_description, String auther_name, String year_of_the_book, String category_name, String book_price, File cover_image, File book, File demo_file) {
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.book_titile = book_titile;
        this.book_description = book_description;
        this.auther_name = auther_name;
        this.year_of_the_book = year_of_the_book;
        this.category_name = category_name;
        this.book_price = book_price;
        this.cover_image = cover_image;
        this.book = book;
        this.demo_file = demo_file;
    }
}
