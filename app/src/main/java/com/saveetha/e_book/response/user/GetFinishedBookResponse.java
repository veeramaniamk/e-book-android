package com.saveetha.e_book.response.user;

import java.util.List;

public class GetFinishedBookResponse {
    private int status;
    private String message;
    private List<GetFinishedBookData> data;

    public String getMessage() {
        return message;
    }

    public List<GetFinishedBookData> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
