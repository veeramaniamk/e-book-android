package com.saveetha.e_book.adminscreens.adminmodules;

public class AdminBooksModule {

    private String bookName,coverImage,description,author,publisher,category;
    private String pages,price,status;


    private int bookId;

    public int getBookId() {
        return bookId;
    }
    public AdminBooksModule(int bookId,String bookName, String coverImage, String description,String status) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.coverImage = coverImage;
        this.description = description;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public AdminBooksModule(String bookName, String coverImage, String description) {
        this.bookName = bookName;
        this.coverImage = coverImage;
        this.description = description;
    }

    public AdminBooksModule(String bookName, String coverImage, String description, String price) {
        this.bookName = bookName;
        this.coverImage = coverImage;
        this.description = description;
        this.price = price;
    }

    public String getPages() {
        return pages;
    }

    public String getPrice() {
        return price;
    }

    public String getBookName() {
        return bookName;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCategory() {
        return category;
    }
}
