package com.saveetha.e_book.request;

public class SignUpRequest {
    private String name;
    private String phone;
    private String email;
    private String user_type;
    private String gender;
    private String password;

    public SignUpRequest(String name, String phone, String email, String user_type, String gender, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.user_type = user_type;
        this.gender = gender;
        this.password = password;
    }
}
