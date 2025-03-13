package com.saveetha.e_book;

public class ReviewModule {

    private String profile,name,review,dTime;

    public ReviewModule(String profile, String name, String review, String dTime) {
        this.profile = profile;
        this.name = name;
        this.review = review;
        this.dTime = dTime;
    }

    public String getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

    public String getReview() {
        return review;
    }

    public String getdTime() {
        return dTime;
    }
}
