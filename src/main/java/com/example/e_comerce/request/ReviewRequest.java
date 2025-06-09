package com.example.e_comerce.request;

public class ReviewRequest {
    private Long prdouctId;
    private String review;

    public Long getPrdouctId() {
        return prdouctId;
    }

    public void setPrdouctId(Long prdouctId) {
        this.prdouctId = prdouctId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
