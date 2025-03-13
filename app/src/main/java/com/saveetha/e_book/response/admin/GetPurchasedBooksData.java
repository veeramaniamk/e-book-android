package com.saveetha.e_book.response.admin;

public class GetPurchasedBooksData {
    private int purches_id;
    private String purches_date;
    private String purchased_transaction_id;
    private int user_id;
    private String book_description;
    private int book_id;
    private int publisher_id;
    private int user_payed_amount;
    private String book_publisher_name;
    private String book_titile;
    private String book_cover_image;
    private String book_pdf;
    private String demo_book;
    private String category_name;
    private String book_auther_name;
    private String year;
    private double book_price;
    private String user_name;
    private long user_phone;
    private int user_type;
    private String email;
    private String user_profile_photo;

    public String getBook_description() {
        return book_description;
    }

    public int getPurches_id() {
        return purches_id;
    }

    public String getPurches_date() {
        return purches_date;
    }

    public String getPurchased_transaction_id() {
        return purchased_transaction_id;
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

    public int getUser_payed_amount() {
        return user_payed_amount;
    }

    public String getBook_publisher_name() {
        return book_publisher_name;
    }

    public String getBook_titile() {
        return book_titile;
    }

    public String getBook_cover_image() {
        return book_cover_image;
    }

    public String getBook_pdf() {
        return book_pdf;
    }

    public String getDemo_book() {
        return demo_book;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getBook_auther_name() {
        return book_auther_name;
    }

    public String getYear() {
        return year;
    }

    public double getBook_price() {
        return book_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public long getUser_phone() {
        return user_phone;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_profile_photo() {
        return user_profile_photo;
    }
}
