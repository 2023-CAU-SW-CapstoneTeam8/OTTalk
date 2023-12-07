package com.example.ottalk.Domain;

import java.util.List;

public class Contents {
    private String id;
    private List<String> actor;
    private String ageRating;
    private List<String> genre;
    private String name;
    private String image;
    private String ratePercent;
    private String rateStar;
    private String synopsis;
    private String time;

    // 기본 생성자는 Firestore에서 데이터를 가져올 때 필요합니다.
    public Contents() {}

    // 여러분의 데이터 구조에 맞게 생성자를 조정하세요.
    public Contents(String id, List<String> actor, String ageRating, List<String> genre, String name, String image, String ratePercent, String rateStar, String synopsis, String time) {
        this.id = id;
        this.actor = actor;
        this.ageRating = ageRating;
        this.genre = genre;
        this.name = name;
        this.image = image;
        this.ratePercent = ratePercent;
        this.rateStar = rateStar;
        this.synopsis = synopsis;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getActor() {
        return actor;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getName(){
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getRatePercent() {
        return ratePercent;
    }

    public String getRateStar() {
        return rateStar;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getTime() {
        return time;
    }
}
