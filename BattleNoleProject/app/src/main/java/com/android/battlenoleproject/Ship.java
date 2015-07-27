package com.android.battlenoleproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by srandall on 7/12/15.
 */
public class Ship implements Parcelable {

    private static final String KEY_LENGTH = "length";
    private static final String KEY_DIRECTION = "direction";
    private static final String KEY_COORDINATES = "coordinates";

    private static final int SHIP_COUNT = 4;


    private int length;
    private int direction;  // we will use 0 = horizontal and 1 = vertical
    private ArrayList<Integer> coordinates;




    public Ship (int length) {
        this.length = length;
        this.coordinates = new ArrayList<Integer>(length);
    }

    public Ship (Ship modelShip) {
        this.length = modelShip.getLength();
        this.coordinates = modelShip.getCoordinates();
        this.direction = modelShip.getDirection();
    }

    public void writeToParcel ( Parcel out, int flags) {
        out.writeInt(length);
        out.writeInt(direction);
        out.writeList(coordinates);
    }

    public Ship (Parcel in) {
        length = in.readInt();
        direction = in.readInt();
        coordinates = new ArrayList<Integer>();
        in.readList(coordinates, Integer.class.getClassLoader());

    }

    public int describeContents() {
        return this.hashCode();
    }


    public static final Parcelable.Creator<Ship> CREATOR =
            new Parcelable.Creator<Ship>() {
                public Ship createFromParcel(Parcel in) {
                    return new Ship(in);
                }

                public Ship[] newArray(int size) {
                    return new Ship[size];
                }
            };



    public int getLength() {
        return this.length;
    }


    public ArrayList<Integer> getCoordinates() {
        return this.coordinates;
    }


    public void clearCoordinates() {
        this.coordinates.clear();
    }

    public void setCoordinates( ArrayList<Integer> coordinates) {
        this.coordinates.clear();
        this.coordinates.addAll(coordinates);
    }

    public void setDirection( int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return this.direction;
    }

    public int getStartPosition() {
        return this.coordinates.get(0);
    }

    public static int getShipLength(int shipNumber) {
        int shipLength = 0;
        shipLength = shipNumber + 2;
        return shipLength;
    }


}
