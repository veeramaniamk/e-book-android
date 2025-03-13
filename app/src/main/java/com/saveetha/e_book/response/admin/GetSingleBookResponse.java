package com.saveetha.e_book.response.admin;

public class GetSingleBookResponse {
    private int status;
    private String message;
    private GetSingleBookData data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public GetSingleBookData getData() {
        return data;
    }
}
