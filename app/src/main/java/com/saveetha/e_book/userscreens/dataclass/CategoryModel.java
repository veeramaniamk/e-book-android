package com.saveetha.e_book.userscreens.dataclass;

public class CategoryModel {
    String name,imageUrl;

    public CategoryModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
