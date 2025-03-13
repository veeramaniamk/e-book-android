package com.saveetha.e_book.response.admin;

import java.util.List;

public class GetBooksResponse {
    private int status;
    private String message;
    private List<GetBooksData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GetBooksData> getData() {
        return data;
    }
}
