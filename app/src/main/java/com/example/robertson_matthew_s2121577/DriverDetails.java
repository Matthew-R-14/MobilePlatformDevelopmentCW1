//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverDetails implements Parcelable {
    private String position;
    private String points;
    private String wins;
    private String givenName;
    private String familyName;
    private String dateOfBirth;
    private String nationality;
    private String constructorName;
    private String constructorNationality;

    public DriverDetails(String position, String points, String wins, String givenName, String familyName,
                         String dateOfBirth, String nationality, String constructorName, String constructorNationality) {
        this.position = position;
        this.points = points;
        this.wins = wins;
        this.givenName = givenName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.constructorName = constructorName;
        this.constructorNationality = constructorNationality;
    }

    public String getPosition() {
        return position;
    }

    public String getPoints() {
        return points;
    }

    public String getWins() {
        return wins;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public String getConstructorNationality() {
        return constructorNationality;
    }
    protected DriverDetails(Parcel in) {
        position = in.readString();
        points = in.readString();
        wins = in.readString();
        givenName = in.readString();
        familyName = in.readString();
        dateOfBirth = in.readString();
        nationality = in.readString();
        constructorName = in.readString();
        constructorNationality = in.readString();
    }

    public static final Creator<DriverDetails> CREATOR = new Creator<DriverDetails>() {
        @Override
        public DriverDetails createFromParcel(Parcel in) {
            return new DriverDetails(in);
        }

        @Override
        public DriverDetails[] newArray(int size) {
            return new DriverDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(position);
        dest.writeString(points);
        dest.writeString(wins);
        dest.writeString(givenName);
        dest.writeString(familyName);
        dest.writeString(dateOfBirth);
        dest.writeString(nationality);
        dest.writeString(constructorName);
        dest.writeString(constructorNationality);
    }
}
