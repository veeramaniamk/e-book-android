package com.saveetha.e_book.response.admin;


import java.util.List;

public class GetPublisherBookResponse {
    private int status;
    private String message;
    private List<GetPublisherBookData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GetPublisherBookData> getData() {
        return data;
    }
}
