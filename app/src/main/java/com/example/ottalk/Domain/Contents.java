package com.example.ottalk.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Contents implements Parcelable {
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

    public Contents() {}

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

    protected Contents(Parcel in) {
        id = in.readString();
        actor = in.createStringArrayList();
        ageRating = in.readString();
        genre = in.createStringArrayList();
        name = in.readString();
        image = in.readString();
        ratePercent = in.readString();
        rateStar = in.readString();
        synopsis = in.readString();
        time = in.readString();
    }

    public static final Creator<Contents> CREATOR = new Creator<Contents>() {
        @Override
        public Contents createFromParcel(Parcel in) {
            return new Contents(in);
        }

        @Override
        public Contents[] newArray(int size) {
            return new Contents[size];
        }
    };

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

    public String getName() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringList(actor);
        dest.writeString(ageRating);
        dest.writeStringList(genre);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(ratePercent);
        dest.writeString(rateStar);
        dest.writeString(synopsis);
        dest.writeString(time);
    }
}
