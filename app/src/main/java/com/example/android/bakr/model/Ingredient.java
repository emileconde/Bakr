package com.example.android.bakr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by conde on 5/10/2018.
 */

public class Ingredient implements Parcelable{
    private String name;
    private int quantity;
    private String measure;

    public Ingredient(String name, int quantity, String measure) {
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    protected Ingredient(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        measure = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(quantity);
        parcel.writeString(measure);
    }
}
