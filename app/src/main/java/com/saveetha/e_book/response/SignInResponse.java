package com.saveetha.e_book.response;

public class SignInResponse {
    private int status;
    private String message;
    private SignInData data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public SignInData getData() {
        return data;
    }
}
