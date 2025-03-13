package com.saveetha.e_book.response.admin;

public class GetReviewData {
    private int review_id;
    private int user_id;
    private int book_id;
    private int publisher_id;
    private String name;
    private long phone;
    private String email;
    private int user_type;
    private String gender;
    private String review_text;
    private String date;
    private String profile;

    public int getReview_id() {
        return review_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public String getName() {
        return name;
    }

    public long getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getGender() {
        return gender;
    }

    public String getReview_text() {
        return review_text;
    }

    public String getDate() {
        return date;
    }

    public String getProfile() {
        return profile;
    }
}
