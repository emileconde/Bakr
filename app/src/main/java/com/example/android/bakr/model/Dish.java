package com.example.android.bakr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by conde on 5/10/2018.
 */

public class Dish implements Parcelable{
    private String name;
    private int id;
    private String image;

    public Dish(){}

    public Dish(String name, int id, String image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    public Dish(String name) {
        this.name = name;
    }

    protected Dish(Parcel in) {
        name = in.readString();
        id = in.readInt();
        image = in.readString();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeString(image);
    }
}
