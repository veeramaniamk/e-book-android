package com.saveetha.e_book.response.admin;

import java.util.List;

public class GetPublisherResponse {
    private int status;
    private String message;
    private List<GetPublisherData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GetPublisherData> getData() {
        return data;
    }
}
