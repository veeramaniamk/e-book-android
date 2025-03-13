package com.saveetha.e_book.response;

import java.util.ArrayList;

public class NewCategoryResponse {

    private int status;
    private  String message;
    private ArrayList<GetCategoryResponse.data> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<GetCategoryResponse.data> getData() {
        return data;
    }

    public class data{
        private String category_id,category_name,category_image;

        public String getCategory_id() {
            return category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public String getCategory_image() {
            return category_image;
        }
    }
}
