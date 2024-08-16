//Matthew Robertson S2121577
package com.example.robertson_matthew_s2121577;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import androidx.lifecycle.MutableLiveData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {

    private final MutableLiveData<List<DriverDetails>> driverDetails;

    public DriverRepository(Application application) {
        driverDetails = new MutableLiveData<>();
        loadDriverDetails(application.getApplicationContext());
    }

    public MutableLiveData<List<DriverDetails>> getDriverDetails() {
        return driverDetails;
    }

    private void loadDriverDetails(Context context) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("f1_current_driverStandings.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(inputStream, null);
            parser.nextTag();

            List<DriverDetails> drivers = readDriverStandings(parser);
            driverDetails.postValue(drivers);

        } catch (IOException | XmlPullParserException e) {
            Log.e("DriverRepository", "Error loading driver data", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<DriverDetails> readDriverStandings(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<DriverDetails> driverStandings = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "MRData");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("StandingsTable")) {
                driverStandings = readStandingsTable(parser);
            } else {
                skip(parser);
            }
        }
        return driverStandings;
    }

    private List<DriverDetails> readStandingsTable(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<DriverDetails> driverStandings = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "StandingsTable");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("StandingsList")) {
                driverStandings = readStandingsList(parser);
            } else {
                skip(parser);
            }
        }
        return driverStandings;
    }

    private List<DriverDetails> readStandingsList(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<DriverDetails> driverStandings = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "StandingsList");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("DriverStanding")) {
                driverStandings.add(readDriverStanding(parser));
            } else {
                skip(parser);
            }
        }
        return driverStandings;
    }

    private DriverDetails readDriverStanding(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "http://ergast.com/mrd/1.5", "DriverStanding");

        String position = parser.getAttributeValue(null, "position");
        String points = parser.getAttributeValue(null, "points");
        String wins = parser.getAttributeValue(null, "wins");

        String givenName = "";
        String familyName = "";
        String dateOfBirth = "";
        String nationality = "";
        String constructorName = "";
        String constructorNationality = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String name = parser.getName();
            if (name.equals("Driver")) {
                while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals("Driver")) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) continue;
                    String driverTag = parser.getName();
                    if (driverTag.equals("GivenName")) {
                        givenName = readText(parser);
                    } else if (driverTag.equals("FamilyName")) {
                        familyName = readText(parser);
                    } else if (driverTag.equals("DateOfBirth")) {
                        dateOfBirth = readText(parser);
                    } else if (driverTag.equals("Nationality")) {
                        nationality = readText(parser);
                    } else {
                        skip(parser);
                    }
                }
            } else if (name.equals("Constructor")) {
                while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals("Constructor")) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) continue;
                    String constructorTag = parser.getName();
                    if (constructorTag.equals("Name")) {
                        constructorName = readText(parser);
                    } else if (constructorTag.equals("Nationality")) {
                        constructorNationality = readText(parser);
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }
        }

        return new DriverDetails(position, points, wins, givenName, familyName, dateOfBirth, nationality, constructorName, constructorNationality);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
