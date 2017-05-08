package com.shoestore.objects;

/**
 * Created by Rangel on 07/05/2017.
 */

public class ReviewsVo {

    private String key_product;
    private String id_user;
    private String name;
    private String photo;
    private String title;
    private String review;
    private float rating;

    public ReviewsVo() {
    }

    public String getKey_product() {
        return key_product;
    }

    public void setKey_product(String key_product) {
        this.key_product = key_product;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
