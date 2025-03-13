package com.saveetha.e_book.adminscreens.adminmodules;

public class AdminAuthorsModule {
    String name,email,phone,imageUrl;

    public AdminAuthorsModule(String name, String email, String phone, String imageUrl) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
