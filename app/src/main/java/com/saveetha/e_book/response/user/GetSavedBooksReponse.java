package com.saveetha.e_book.response.user;

import java.util.List;

public class GetSavedBooksReponse {
    private int status;
    private String message;
    private List<GetSavedBookData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GetSavedBookData> getData() {
        return data;
    }
}
