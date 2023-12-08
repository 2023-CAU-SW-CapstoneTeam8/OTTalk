package com.example.ottalk.Domain;

import java.util.List;

public class User {
    private String age;
    private List<String> favoriteMovies;
    private String gender;
    private String mbti;

    public String getAge() {
        return age;
    }

    public List<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public String getGender() {
        return gender;
    }

    public String getMbti() {
        return mbti;
    }
}
