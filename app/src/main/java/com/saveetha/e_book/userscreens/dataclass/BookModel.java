package com.saveetha.e_book.userscreens.dataclass;

public class BookModel {
    String name,imageUrl;
    private int book_id;
    private int publisher_id;
    private String publisher_name;
    private String book_cover_image;
    private String auther_name;
    private String year_of_the_book;
    private String book_submit_date;
    private String book_description;
    private String book_approval_status;
    private String book_approval_date;
    private String book_cancelled_msg;
    private String book_title;
    private String book_pdf;
    private String demo_book;
    private String price;

    public String getPrice() {
        return price;
    }

    public BookModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public BookModel(String name, String imageUrl, int book_id, int publisher_id, String publisher_name, String book_cover_image, String auther_name, String year_of_the_book, String book_submit_date, String book_description, String book_approval_status, String book_approval_date, String book_cancelled_msg, String book_title, String book_pdf, String demo_book, String price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.book_id = book_id;
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.book_cover_image = book_cover_image;
        this.auther_name = auther_name;
        this.year_of_the_book = year_of_the_book;
        this.book_submit_date = book_submit_date;
        this.book_description = book_description;
        this.book_approval_status = book_approval_status;
        this.book_approval_date = book_approval_date;
        this.book_cancelled_msg = book_cancelled_msg;
        this.book_title = book_title;
        this.book_pdf = book_pdf;
        this.demo_book = demo_book;
        this.price = price;
    }

    public int getBook_id() {
        return book_id;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public String getBook_cover_image() {
        return book_cover_image;
    }

    public String getAuther_name() {
        return auther_name;
    }

    public String getYear_of_the_book() {
        return year_of_the_book;
    }

    public String getBook_submit_date() {
        return book_submit_date;
    }

    public String getBook_description() {
        return book_description;
    }

    public String getBook_approval_status() {
        return book_approval_status;
    }

    public String getBook_approval_date() {
        return book_approval_date;
    }

    public String getBook_cancelled_msg() {
        return book_cancelled_msg;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getBook_pdf() {
        return book_pdf;
    }

    public String getDemo_book() {
        return demo_book;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
