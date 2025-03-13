package com.saveetha.e_book.response.admin;

import java.util.List;

public class GetAllReviewResponse {
    private int status;
    private String message;
    private List<GetReviewData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GetReviewData> getData() {
        return data;
    }
}
