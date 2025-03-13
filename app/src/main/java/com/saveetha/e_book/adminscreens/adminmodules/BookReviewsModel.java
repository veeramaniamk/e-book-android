package com.saveetha.e_book.adminscreens.adminmodules;

public class BookReviewsModel {
    private int bookId;
    private String userName;
    private String reviewText;
    private int userId;
    private String userProfile;

    public String getUserProfile() {
        return userProfile;
    }

    public int getBookId() {
        return bookId;
    }

    public String getUserName() {
        return userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getUserId() {
        return userId;
    }

    public BookReviewsModel(int bookId, String userName,String userProfile,String reviewText, int userId) {
        this.bookId = bookId;
        this.userProfile = userProfile;
        this.userName = userName;
        this.reviewText = reviewText;
        this.userId = userId;
    }
}
