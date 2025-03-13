package com.saveetha.e_book.response.admin;

import java.util.List;

public class GetPurchesedBooksResponse {
    private int status;
    private String message;
    private List<GetPurchasedBooksData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GetPurchasedBooksData> getData() {
        return data;
    }
}
