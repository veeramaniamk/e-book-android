package com.saveetha.e_book.reviewerscrees.reviewermodules;

public class ReviewerBooksModule {

    private final String bookName;
    private final String coverImage;
    private final String description;
    private final int bookId;
    private final String status;

    public ReviewerBooksModule(int bookId, String bookName, String coverImage, String description,String status) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.coverImage = coverImage;
        this.description = description;
        this.status=status;
    }
    public String getStatus() {
        return status;

    }
    public int getBookId() {
        return bookId;
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


}
