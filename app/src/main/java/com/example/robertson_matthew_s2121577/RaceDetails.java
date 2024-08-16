//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.os.Parcel;
import android.os.Parcelable;

public class RaceDetails implements Parcelable {
    private String raceName;
    private String date;
    private String time;
    private String circuitName;
    private String round;
    // Constructor
    public RaceDetails(String round, String raceName, String date, String time, String circuitName) {
        this.round = round;
        this.raceName = raceName;
        this.date = date;
        this.time = time;
        this.circuitName = circuitName;
    }
    public String getRaceName() {
        return raceName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public String getRound() {
        return round;
    }

    protected RaceDetails(Parcel in) {
        round = in.readString();
        raceName = in.readString();
        date = in.readString();
        time = in.readString();
        circuitName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(round);
        dest.writeString(raceName);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(circuitName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RaceDetails> CREATOR = new Creator<RaceDetails>() {
        @Override
        public RaceDetails createFromParcel(Parcel in) {
            return new RaceDetails(in);
        }

        @Override
        public RaceDetails[] newArray(int size) {
            return new RaceDetails[size];
        }
    };
}

