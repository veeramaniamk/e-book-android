package com.saveetha.e_book.response;

import java.util.List;

public class ReviewResponse {



    private String status;
    private String message;
    private List<Review> data;

    public class Review {
        private int review_id;
        private int publisher_id;
        private String review_text;
        private String user_name;
        private String user_phone;
        private String user_email;
        private String user_type;
        private String gender;
        private String date;
        private String profile;

        public int getReview_id() {
            return review_id;
        }

        public int getPublisher_id() {
            return publisher_id;
        }

        public String getReview_text() {
            return review_text;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public String getUser_email() {
            return user_email;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getGender() {
            return gender;
        }

        public String getDate() {
            return date;
        }

        public String getProfile() {
            return profile;
        }
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Review> getData() {
        return data;
    }
}
